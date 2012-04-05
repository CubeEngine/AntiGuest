package de.codeinfection.quickwango.Translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;

/**
 *
 * @author CodeInfection
 */
public class Translator
{
    private static final Map<String, String> translations = new HashMap<String, String>();
    private static final String RESOURCE_PATH = "/language/";
    private static final String RESOURCE_EXT = ".ini";

    public static String t(String key, Object... params)
    {
        return translate(key, params);
    }

    public static String translate(String key, Object... params)
    {
        String translation = translations.get(key);
        if (translation == null)
        {
            return "null";
        }
        return String.format(translation, params);
    }

    public static synchronized boolean loadTranslation(String language)
    {
        try
        {
            InputStream is = Translator.class.getResourceAsStream(RESOURCE_PATH + language + RESOURCE_EXT);
            if (is == null)
            {
                System.err.println("InputStream is null -> "  + RESOURCE_PATH + language + RESOURCE_EXT);
                return false;
            }
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[512];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) > 0)
            {
                sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
            }
            is.close();

            translations.clear();
            int equalsOffset;
            char firstChar;
            for (String line : explode("\n", sb.toString().trim()))
            {
                firstChar = line.charAt(0);
                if (firstChar == ';' || firstChar == '[')
                {
                    continue;
                }
                equalsOffset = line.indexOf("=");
                if (equalsOffset < 1)
                {
                    continue;
                }

                translations.put(line.substring(0, equalsOffset).trim(), ChatColor.translateAlternateColorCodes('&', line.substring(equalsOffset + 1).trim()));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }

    private static List<String> explode(String delim, String string)
    {
        int pos, offset = 0, delimLen = delim.length();
        List<String> tokens = new ArrayList<String>();

        while ((pos = string.indexOf(delim, offset)) > -1)
        {
            tokens.add(string.substring(offset, pos));
            offset = pos + delimLen;
        }
        tokens.add(string.substring(offset));

        return tokens;
    }
}
