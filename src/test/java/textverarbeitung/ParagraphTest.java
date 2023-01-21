package textverarbeitung;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link Paragraph} and its methods
 */
public class ParagraphTest {

    private Paragraph paragraph;

    /**
     * Negative test that tests that a {@link NullPointerException} is being thrown if it tries to create a Paragraph
     * without any content.
     */
    @Test
    void testParagraphNullContent(){
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new Paragraph(null));

        assertEquals("content must not be null", exception.getMessage());
    }

    /**
     * Test cases for the wordFrequency method.
     * It tests for word occurrences in various scenarios.
     * <p>
     *     We add different variants of the letters asdf which should count to 5 occurrences
     *     - Twice Asdf, which should ignore the comma
     *     - -Asdf which should ignore the hyphen
     *     - Asdf. which should ignore the dot
     *     - Asdf which counts as well
     *     - asdf, and asdf both don't count because they don't start with a capital letter
     *     We also added different variants of fdsa which should give only two occurrence count
     *     - .FDSA which should ignore the dot
     *     - fdsaFDSA which won't count because it does not start with a capital letter
     *     - FDSA which counts as well
     *     1. We create a new Paragraph which internally calculates the word frequencies.
     *     2. We fetch the word frequencies and assert each entry.
     * </p>
     */
    @Test
    void testWordFrequencies() {
        paragraph = new Paragraph("Asdf, Asdf, Asdf asdf, asdf FDSA fdsaFDSA .FDSA’ -’- -Asdf Asdf. =‘•..•’=");
        final Map<String, Integer> wordFrequencies = paragraph.getParagraphWordFrequencies();

        assertEquals(2, wordFrequencies.size());
        assertEquals(5, wordFrequencies.get("Asdf"));
        assertEquals(2, wordFrequencies.get("FDSA"));
    }

    /**
     * Tests that all allowed special characters are not filtered out by the regex.
     */
    @Test
    void testAllowedSpecialCharacters() {
        final String allowedSpecialCharacters = ".,:;-!?’()\"%@+*[]{}/\\&#$";
        paragraph = new Paragraph(allowedSpecialCharacters);
        assertEquals(allowedSpecialCharacters, paragraph.getContent());
    }

    /**
     * Tests that a given word will be replaced. It collects the word frequencies afterwards to assert if the
     * replacement was successful.
     */
    @Test
    void testReplace() {
        paragraph = new Paragraph("Asdf, Asdf, Asdf asdf, FDSA ^~^");
        paragraph.searchReplaceParagraphContent("asdf", "Asdf", 1);
        final Map<String, Integer> wordFrequencies = paragraph.getParagraphWordFrequencies();
        assertEquals("Asdf, Asdf, Asdf Asdf, FDSA ", paragraph.getContent());
        assertEquals(2, wordFrequencies.size());
        assertEquals(4, wordFrequencies.get("Asdf"));
        assertEquals(1, wordFrequencies.get("FDSA"));
    }

}
