package de.cubeisland.libMinecraft.translation;

import java.io.IOException;
import java.io.InputStream;

import de.cubeisland.libMinecraft.ChatColor;
import de.cubeisland.libMinecraft.StringUtils;
import gnu.trove.map.hash.THashMap;

/**
 * Represents a translation loaded from the jar.
 *
 * @author Phillip Schichtel
 */
public class Translation {
    private String language;
    private final THashMap<String, String> translations;
    private static final String RESOURCE_PATH = "/language/";
    private static final String RESOURCE_EXT = ".ini";

    public Translation(Class clazz, final String language) throws IOException {
        if (clazz == null)
            throw new IllegalArgumentException("The class must not be null!");
        if (language == null)
            throw new IllegalArgumentException("The language must not be null!");

        InputStream is = clazz.getResourceAsStream(RESOURCE_PATH + language + RESOURCE_EXT);
        if (is == null)
            throw new IllegalStateException("Requested language '" + language + "' was not found!");
        translations = new THashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[512];
        int bytesRead;

        while ((bytesRead = is.read(buffer)) > 0)
            sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
        is.close();

        translations.clear();
        int equalsOffset;
        char firstChar;
        String key;
        String message;
        boolean parseColors;
        for (String line : StringUtils.explode("\n", sb.toString().trim())) {
            if (line.length() == 0)
                continue;
            firstChar = line.charAt(0);
            parseColors = true;
            if (firstChar == ';' || firstChar == '[')
                continue;
            equalsOffset = line.indexOf("=");
            if (equalsOffset < 1)
                continue;

            key = line.substring(0, equalsOffset).trim().toLowerCase();
            message = line.substring(equalsOffset + 1).trim();
            if (message.length() > 0 && message.charAt(0) == '@') {
                message = message.substring(1).trim();
                parseColors = false;
            }
            if (message.length() > 2 && message.charAt(0) == '"' && message.charAt(message.length() - 1) == '"')
                message = message.substring(1, message.length() - 1);

            if (parseColors)
                message = ChatColor.translateAlternateColorCodes('&', message);
            translations.put(key, message);
        }

        this.language = language;
    }

    public String translate(String key, Object... params) {
        key = key.toLowerCase();
        String translation = translations.get(key);
        if (translation == null)
            return "[" + key + "]";
        else
            return String.format(translation, params);
    }

    public static Translation get(final Class clazz, final String language) {
        try {
            return new Translation(clazz, language);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * Returns the name of the language
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }
}
