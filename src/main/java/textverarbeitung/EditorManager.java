package textverarbeitung;

import java.util.ArrayList;
import java.util.List;

/**
 * EditorManager Class manages the {@link Paragraph}'s with addition and removal,
 * and indexing using the {@link IndexManager}.
 */
public class EditorManager {

    private static final String DUMMY_TEXT = "Morbi vel leo consequat, volutpat ante nec, mattis nulla. " +
            "Integer placerat fringilla ullamcorper. " +
            "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; " +
            "Nullam accumsan, nunc ac feugiat sagittis, leo nunc rutrum neque, ut luctus lectus purus a magna. " +
            "Sed vel leo aliquet, pulvinar nisi id, molestie odio. " +
            "Morbi magna odio, feugiat vel fringilla nec, posuere at magna. " +
            "Nam in blandit dui. Nullam at ipsum mollis, finibus augue non, mollis eros. " +
            "Pellentesque a viverra diam. Donec euismod mollis faucibus. Fusce in dui metus. " +
            "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; " +
            "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;";

    private final List<Paragraph> paragraphs;

    /**
     * Creates a new EditorManager.
     * It initializes the {@link ArrayList} for {@link Paragraph} Objects
     */
    public EditorManager() {
        paragraphs = new ArrayList<>();
    }

    /**
     * Method to add a {@link Paragraph} to {@link ArrayList}.
     *
     * @param paragraphToAdd    Text of {@link Paragraph} as {@code String}.
     * @param paragraphPosition Validated {@code Integer} of position in paragraphList where {@link Paragraph} should be added.
     * @return Result message of the command as {@code String}
     */
    public String addParagraph(final String paragraphToAdd, Integer paragraphPosition) {
        if (paragraphPosition == null) {
            paragraphs.add(new Paragraph(paragraphToAdd));
            paragraphPosition = paragraphs.size();
        } else {
            paragraphs.add(paragraphPosition - 1, new Paragraph(paragraphToAdd));
        }

        return "Paragraph was added at Position " + paragraphPosition + " of " + paragraphs.size();
    }

    /**
     * Method to add a {@link Paragraph} with dummy text as Content to {@link ArrayList}.
     *
     * @param paragraphPosition Validated {@code Integer} of position in paragraphList where {@link Paragraph} should be added.
     * @return Result Text as {@code String}
     */
    public String addDummyText(final Integer paragraphPosition) {
        return "Dummy " + addParagraph(DUMMY_TEXT, paragraphPosition);
    }

    /**
     * Method to replace a {@link String} with another {@link String} in a {@link Paragraph}.
     * Validates the search- and replace text before passing it on to {@link Paragraph} for execution.
     *
     * @param paragraphPosition Validated {@code Integer} of position in paragraphList where {@code toSearch} should be replaced
     * @param toSearch          {@code String} of Characters that are being replaced
     * @param toReplaceWith     {@code String} of Characters that will be newly inserted
     * @return Result Text as {@code String} of what was done.
     */
    public String replace(Integer paragraphPosition, final String toSearch, final String toReplaceWith) {
        if (paragraphPosition == null) {
            paragraphPosition = paragraphs.size();
        }
        if (toSearch == null || toReplaceWith == null || toSearch.trim().isBlank() || toReplaceWith.trim().isBlank()) {
            return "You cannot search or replace blanks or spaces.";
        } else if (paragraphs.isEmpty()) {
            return "No Paragraphs were found to replace anything in.";
        } else {
            return paragraphs.get(paragraphPosition - 1).searchReplaceParagraphContent(toSearch, toReplaceWith, paragraphPosition);
        }
    }

    /**
     * {@link Paragraph} List used for Validation purposes.
     *
     * @return size of the {@code ArrayList} of all {@link Paragraph} Elements.
     */
    public int getParagraphSize() {
        return paragraphs.size();
    }

    /**
     * Function to delete a {@link Paragraph}. Handles Validation with {@link ValidationManager}
     *
     * @param paragraphPosition as validated int with position of {@link Paragraph} that should be deleted
     * @return {@code String} return Message
     */
    public String deleteParagraph(Integer paragraphPosition) {
        if (paragraphs.isEmpty()) {
            return "No Paragraphs were found to remove.";
        }
        if (paragraphPosition == null) {
            paragraphPosition = paragraphs.size();
        }
        paragraphs.remove(paragraphPosition - 1);
        return "Paragraph Nr. " + paragraphPosition + " was removed";
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

}
