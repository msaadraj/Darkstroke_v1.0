package client;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static client.Main.connectToServer;

public class BufferHelpers extends Main {

    private static final String SECRET = "1234567890123456";    // 16-char AES key
    private static final Key AES_KEY = new SecretKeySpec(SECRET.getBytes(), "AES");

    // ---------------- Buffer helpers ----------------

    public static void addToBuffer(String line) {
        if (line == null) return;
        logQueue.add(line);
        int s = queueSize.incrementAndGet();
        if (s >= BATCH_SIZE) {
            scheduler.execute(() -> {
                try { flushBuffer(); } catch (Throwable ignored) {}
            });
        }
    }

    public static synchronized void flushBuffer() {
        if (dos == null) return;
        if (queueSize.get() == 0) return;

        StringBuilder sb = new StringBuilder();
        String item;
        int drained = 0;
        while ((item = logQueue.poll()) != null) {
            if (drained > 0) sb.append('\n');
            sb.append(item);
            drained++;
        }
        queueSize.addAndGet(-drained);
        if (drained > 0) {
            sendLogImmediate(sb.toString());
        }
    }

    private static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, AES_KEY);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return plainText; // fallback
        }
    }

    protected static void sendLogImmediate(String combinedText) {
        try {
            if (dos == null) {
                connectToServer();
            }
            dos.writeUTF("LOG");
            dos.writeUTF(encrypt(combinedText)); // ALWAYS encrypt here
            dos.flush();
        } catch (IOException ex) {
            System.err.println("Failed to send log: " + ex.getMessage());
            connectToServer();
            sendLogImmediate(combinedText); // retry
        }
    }
}
