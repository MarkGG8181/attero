package fag.ware.client.util;

import java.security.MessageDigest;

public class SystemUtil {
    public static void main(String[] args) {
        System.out.println(getHWID());
    }

    public static String getHWID() {
        try {
            String toEncrypt = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(toEncrypt.getBytes());
            byte[] byteData = md.digest();
            return byteArrayToHex(byteData);
        } catch (Exception e) {
            return null;
        }
    }

    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}