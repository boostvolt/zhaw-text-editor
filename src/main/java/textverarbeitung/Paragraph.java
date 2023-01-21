package textverarbeitung;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.isUpperCase;
import static java.util.Objects.requireNonNull;
import static textverarbeitung.TextConstants.BLANK;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Paragraph Class stores the content of each paragraph in a String.
 */
public class Paragraph {

    private static final String SPECIAL_CHARACTERS = ".,:;\\-!?’\s()\"%@+*\\[\\]{}/&#$\\\\";
    private static final String ALLOWED_CHARACTERS = "([^A-Za-z0-9\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC" + SPECIAL_CHARACTERS + "])";
    private static final int WORD_FREQUENCY_INCREMENTER = 1;

    private final Map<String, Integer> paragraphWordFrequencies;

    private String content;

    /**
     * Creates a new Paragraph Object using the passed string.
     *
     * @param content String with the text content of this Paragraph.
     */
    public Paragraph(String content) {
        this.content = requireNonNull(content, "content must not be null")
                .replaceAll(ALLOWED_CHARACTERS, BLANK);
        paragraphWordFrequencies = new HashMap<>();
        calculateWordFrequency(content);
    }

    public String getContent() {
        return content;
    }

    /**
     * Returns a Map filled with a set of words and their frequency.
     *
     * @return returns a Map filled with a set of words and their frequency.
     */
    public Map<String, Integer> getParagraphWordFrequencies() {
        return paragraphWordFrequencies;
    }

    /**
     * Replaces the String toSearch with the String toReplaceWith in the String content. If the replacement was successful
     * it calls up the method calculateWordFrequency.
     *
     * @param toSearch       String that should be replaced.
     * @param toReplaceWith  String that is replacing the searched string.
     * @param paragraphIndex Number of current paragraph.
     * @return Returns a message if the replacement was successful or not.
     */
    public String searchReplaceParagraphContent(String toSearch, String toReplaceWith, Integer paragraphIndex) {
        toSearch = toSearch.replaceAll(ALLOWED_CHARACTERS, BLANK);
        toReplaceWith = toReplaceWith.replaceAll(ALLOWED_CHARACTERS, BLANK);
        if (content.contains(toSearch)) {
            this.content = content.replace(toSearch, toReplaceWith);
            calculateWordFrequency(content);
            return "Characters " + toSearch + " have been replaced with " + toReplaceWith + " in paragraph Nr. "
                    + paragraphIndex + ".";
        } else {
            return "We couldn't find anything to replace.";
        }
    }

    /**
     * Returns a Map with the frequency of every word from the String content.
     *
     * @param content String with the text content of this Paragraph.
     */
    private void calculateWordFrequency(String content) {
        paragraphWordFrequencies.clear();
        for (String currentWord : content.split(WHITESPACE)) {
            currentWord = currentWord.replaceAll("[" + SPECIAL_CHARACTERS + "]", BLANK);
            if (!currentWord.trim().isBlank() && startsWithUppercase(currentWord)) {
                paragraphWordFrequencies.put(currentWord, paragraphWordFrequencies.containsKey(currentWord)
                        ? (paragraphWordFrequencies.get(currentWord) + WORD_FREQUENCY_INCREMENTER)
                        : WORD_FREQUENCY_INCREMENTER);
            }
        }
    }

    private static boolean startsWithUppercase(String currentWord) {
        return isUpperCase(currentWord.charAt(0));
    }

}