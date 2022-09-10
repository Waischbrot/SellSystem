package de.waischbrot.messages;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessageUtil {

    public static String getMessageColour(String text) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher match = pattern.matcher(text);

        while (match.find()) {
            String color = text.substring(match.start(), match.end());
            text = text.replace(color, ChatColor.of(color) + "");

            match = pattern.matcher(text);
        }

        text = ChatColor.translateAlternateColorCodes('&', text);

        return text;
    }

    public static boolean isAlpha(String text) {
        boolean is = false;
        for (char c : text.toCharArray()) {

            // a - z
            if (c >= 'a' && c <= 'z') {
                is = true;
            }

            // A - Z
            if (c >= 'A' && c <= 'Z') {
                is = true;
            }

            // Zahlen
            if (c >= '0' && c <= '9') {
                is = true;
            }

            if (!is) {
                return false;
            }
            is = false;
        }
        return true;
    }
}
