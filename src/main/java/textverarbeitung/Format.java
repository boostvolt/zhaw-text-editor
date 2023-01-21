package textverarbeitung;

import static java.util.Objects.requireNonNull;

/**
 * Represents the selected Format by the user which is used when the List of {@link Paragraph} are printed out.
 */
public class Format {

    /**
     * The default format which is selected upon start of the {@link App}.
     */
    public static final Format DEFAULT_FORMAT = new Format();

    private final Integer fixedColumnWidth;
    private final boolean raw;

    /**
     * Creates a new Format object. This constructor is used for the raw format.
     */
    private Format() {
        this.fixedColumnWidth = null;
        raw = true;
    }

    /**
     * Creates a new Format object. This constructor is used for formats with a fixed column width.
     *
     * @param fixedColumnWidth the desired fixed column width
     */
    public Format(final Integer fixedColumnWidth) {
        this.fixedColumnWidth = requireNonNull(fixedColumnWidth, "fixedColumnWidth must not be null");
        raw = false;
    }

    /**
     * Returns the fixedColumnWidth of the Format.
     *
     * @return the fixedColumnWidth of the Format
     */
    public Integer getFixedColumnWidth() {
        return fixedColumnWidth;
    }

    /**
     * Returns if the Format is raw or not.
     *
     * @return the raw boolean of the Format
     */
    public boolean isRaw() {
        return raw;
    }

}
