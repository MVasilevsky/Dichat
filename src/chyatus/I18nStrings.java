package chyatus;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * I18n
 *
 * @author mvas
 */
public class I18nStrings {

    private static final ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.getDefault());

    public static String getString(String key) {
        return messages.getString(key);
    }

}
