package textverarbeitung;

/**
 * Class to hold static {@link String} constants used in other classes.
 */
public final class TextConstants {

    private TextConstants() {
        // No instantiation possible for this class
    }

    public static final String BLANK = "";
    public static final String WHITESPACE = " ";
    public static final String TAB = "\t";
    public static final String AS_ITALIC = "\033[3m%s\033[0m";
    public static final String AS_BOLD = "\033[1m%s\033[0m";
    public static final String AS_RED = "\u001B[31m%s\u001B[0m";

}
