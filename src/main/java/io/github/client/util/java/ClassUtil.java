package io.github.client.util.java;

public class ClassUtil {
    public static String toClassName(String name) {
        String[] parts = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1));
        }
        return sb.toString();
    }
}