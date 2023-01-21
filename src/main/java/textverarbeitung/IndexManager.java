package textverarbeitung;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static textverarbeitung.TextConstants.BLANK;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Holds and calculates the Index {@link Map} of frequent words contained in the {@link Paragraph} objects.
 */
public class IndexManager {

    private static final int INDEX_THRESHOLD = 4;

    private final Map<String, Set<Integer>> indices = new HashMap<>();

    Map<String, Set<Integer>> getIndices() {
        return indices;
    }

    /**
     * Method to calculate the index over all {@link Paragraph} objects.
     * First the index {@code HashMap} is cleared and
     * then filled up with words that are checked against the INDEX_THRESHOLD
     *
     * @param paragraphs {@code List} of {@link Paragraph} Objects of which the words should be indexed.
     */
    public void calculateIndices(final List<Paragraph> paragraphs) {
        indices.clear();
        Map<String, Integer> wordFrequency = new HashMap<>();

        for (Paragraph paragraph : paragraphs) {
            Map<String, Integer> paragraphWordFrequency = paragraph.getParagraphWordFrequencies();
            for (Map.Entry<String, Integer> entry : paragraphWordFrequency.entrySet()) {
                if (wordFrequency.containsKey(entry.getKey())) {
                    wordFrequency.put(entry.getKey(), wordFrequency.get(entry.getKey()) + entry.getValue());
                } else {
                    wordFrequency.put(entry.getKey(), entry.getValue());
                }
            }
        }

        checkAgainstThresholdAndFillIndices(wordFrequency);
        addWordParagraphOccurrenceToIndices(paragraphs);
    }

    /**
     * Takes the calculated wordFrequency {@code Map}
     * and fills the Index {@code HashMap} with words that appear as much as the INDEX_THRESHOLD determines.
     *
     * @param wordFrequency {@code Map} of calculated word frequency.
     */
    private void checkAgainstThresholdAndFillIndices(final Map<String, Integer> wordFrequency) {
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            if (entry.getValue() >= INDEX_THRESHOLD) {
                indices.put(entry.getKey(), new HashSet<>());
            }
        }
    }

    /**
     * Uses all {@link Paragraph} objects to add occurrence of words to Index {@code HashMap}.
     *
     * @param paragraphs {@code List} of {@link Paragraph} objects.
     */
    private void addWordParagraphOccurrenceToIndices(final List<Paragraph> paragraphs) {
        for (Map.Entry<String, Set<Integer>> entry : indices.entrySet()) {
            for (int i = 0; i < paragraphs.size(); i++) {
                if (paragraphs.get(i).getParagraphWordFrequencies().containsKey(entry.getKey())) {
                    entry.getValue().add(i + 1);
                }
            }
        }
    }

    /**
     * Returns Content of the current contents of Indices in {@code String} Format.
     * If the index is empty it will return an information message.
     *
     * @return Index in {@code String} format
     */
    public String transformIndicesToPrintFormat() {
        if (indices.isEmpty()) {
            return "Not enough words were found to index.";
        }

        final StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Set<Integer>> entry : indices.entrySet()) {
            stringBuilder.append(entry.getKey())
                    .append(WHITESPACE);

            for (Integer number : entry.getValue()) {
                stringBuilder.append(number == entry.getValue().toArray()[0] ? BLANK : ",")
                        .append(number);
            }

            if (entry != indices.entrySet().toArray()[indices.size() - 1]) {
                stringBuilder.append(System.lineSeparator());
            }
        }
        return stringBuilder.toString();
    }

}
