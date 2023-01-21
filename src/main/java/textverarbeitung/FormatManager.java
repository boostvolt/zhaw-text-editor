package textverarbeitung;

import java.util.List;

import static java.lang.Math.min;
import static java.lang.String.valueOf;
import static java.lang.System.lineSeparator;
import static textverarbeitung.TextConstants.BLANK;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Manages the formatting of the {@link Paragraph}. It handles the setting of the selected {@link Format} and the
 * printing.
 */
public class FormatManager {

    private Format selectedFormat;

    /**
     * Constructs a new FormatManager object. It also sets the default selected {@link Format}.
     */
    public FormatManager() {
        setDefaultFormat();
    }

    /**
     * Sets the default {@link Format} as the selected {@link Format}.
     */
    public void setDefaultFormat() {
        selectedFormat = Format.DEFAULT_FORMAT;
    }

    /**
     * Sets the fixed column width on the selected {@link Format}.
     *
     * @param fixedColumnWidth the fixed column width for this {@link Format}.
     */
    public void setSelectedFormat(final Integer fixedColumnWidth) {
        selectedFormat = new Format(fixedColumnWidth);
    }

    /**
     * Returns the selected {@link Format}.
     *
     * @return the selected {@link Format}
     */
    public Format getSelectedFormat() {
        return selectedFormat;
    }

    /**
     * Prints the given paragraphs in the selected {@link Format}.
     *
     * @param paragraphs the {@link Paragraph} objects to print.
     * @return the printed {@link Paragraph} in the selected {@link Format} as a {@link String}
     */
    public String transformParagraphsToSelectedFormat(final List<Paragraph> paragraphs) {
        if (paragraphs.isEmpty()) {
            return "No paragraphs found to print.";
        }
        return selectedFormat.isRaw()
                ? transformToRawFormat(paragraphs)
                : transformToFixedLengthFormat(paragraphs);
    }

    private String transformToRawFormat(final List<Paragraph> paragraphs) {
        final StringBuilder stringBuilder = new StringBuilder(paragraphs.size());
        for (int i = 0; i < paragraphs.size(); i++) {
            stringBuilder.append(i + 1)
                    .append(": ")
                    .append(paragraphs.get(i).getContent())
                    .append(insertLineBreakIfApplicable(paragraphs.size(), i));
        }
        return stringBuilder.toString();
    }

    private String transformToFixedLengthFormat(final List<Paragraph> paragraphs) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < paragraphs.size(); i++) {
            Paragraph paragraph = paragraphs.get(i);
            stringBuilder.append(transformParagraphToFixedLengthFormat(paragraph.getContent()))
                    .append(insertLineBreakIfApplicable(paragraphs.size(), i));
        }
        return stringBuilder.toString();
    }

    private String transformParagraphToFixedLengthFormat(final String paragraphText) {
        final StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (i < paragraphText.length()) {
            int lineBreakIndex = min(selectedFormat.getFixedColumnWidth() + i, paragraphText.length());
            final String subText = paragraphText.substring(i, lineBreakIndex);
            final boolean isNextCharacterWhitespace = WHITESPACE.equals(
                    valueOf(paragraphText.charAt(min(lineBreakIndex, paragraphText.length() - 1))));
            // If the lineBreakIndex matches with the length of the whole paragraph it can be appended right away
            if (lineBreakIndex == paragraphText.length()) {
                stringBuilder.append(subText);
            // If a whitespace has been found within the subText or as the next character, it needs to find out the
            // location of the rightmost whitespace in order to perform the line break at the correct location.
            } else if (subText.contains(WHITESPACE) || isNextCharacterWhitespace) {
                lineBreakIndex = getUpdatedLineBreakIndex(paragraphText, lineBreakIndex, subText, isNextCharacterWhitespace, i);
                stringBuilder.append(paragraphText, i, lineBreakIndex);
            // If no whitespaces have been found within the fixed column width, a forced line break will be appended.
            } else {
                stringBuilder.append(subText, 0, subText.length() - 1)
                        .append("-");
                lineBreakIndex--;
            }
            stringBuilder.append(lineSeparator());
            i = lineBreakIndex;
        }
        return stringBuilder.toString();
    }

    private int getUpdatedLineBreakIndex(final String paragraphText, int lineBreakIndex, final String subText,
                                         final boolean isNextCharacterWhitespace, final int previousLineBreakIndex) {
        // There could be multiple spaces at the end of the current subText. To avoid their contribution to the next
        // line break index they are being appended to the current line even though the fixed width will be exceeded
        if (subText.endsWith(WHITESPACE) || isNextCharacterWhitespace) {
            while (lineBreakIndex < paragraphText.length()
                    && valueOf(paragraphText.charAt(lineBreakIndex)).equals(WHITESPACE)) {
                lineBreakIndex++;
            }
        // To avoid word wrapping it appends the line break after the rightmost whitespace
        } else {
            lineBreakIndex = previousLineBreakIndex + subText.lastIndexOf(WHITESPACE) + 1;
        }
        return lineBreakIndex;
    }

    private String insertLineBreakIfApplicable(int paragraphSize, int currentIndex) {
        return currentIndex == paragraphSize - 1
                ? BLANK
                : lineSeparator();
    }

}
