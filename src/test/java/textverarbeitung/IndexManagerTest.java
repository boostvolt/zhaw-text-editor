package textverarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Tests the {@link IndexManager} and its methods
 */
class IndexManagerTest {

    private IndexManager indexManager;

    /**
     * Setup new {@link IndexManager} for each test
     */
    @BeforeEach
    void setUp() {
        indexManager = new IndexManager();
    }

    /**
     * Testing the calculation of the index using the {@link Paragraph} wordFrequency method.
     * Tests word occurrence in same and multiple paragraphs.
     * For extended wordFrequencyTests see {@link ParagraphTest}
     * <p>
     * We add Paragraph 1 with 3 Lorem and 3 Ipsum
     * We add Paragraph 2 with 1 Ipsum
     * We add Paragraph 3 with 3 Test and 3 case and 3 ipsum
     * <p>
     * 1. We check the Threshold of 4 as well as if multiple Paragraphs are looked at,
     * by asserting that the Index has a size of 2
     * 2. We check if the word Ipsum appears in both the first and second Paragraph
     * 4. We check if the word Test appears in the third paragraph.
     */
    @Test
    void calculateIndex() {
        ArrayList<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph("Lorem Ipsum ".repeat(3)));
        paragraphs.add(new Paragraph("Ipsum test ".repeat(1)));
        paragraphs.add(new Paragraph("Test  case ipsum ".repeat(4)));
        paragraphs.add(new Paragraph(WHITESPACE));

        indexManager.calculateIndices(paragraphs);

        Map<String, Set<Integer>> indices = indexManager.getIndices();
        assertEquals(2, indices.size());
        Set<Integer> paragraphPositions = indices.get("Ipsum");
        assertEquals(2, paragraphPositions.size());
        assertTrue(paragraphPositions.contains(1));
        assertTrue(paragraphPositions.contains(2));
        paragraphPositions = indices.get("Test");
        assertEquals(1, paragraphPositions.size());
        assertTrue(paragraphPositions.contains(3));
    }

    /**
     * Negative test of calculation method when null is given instead of a {@link Paragraph} list
     */
    @Test
    void indexWithNullParagraphs(){
        assertThrows(NullPointerException.class, () -> indexManager.calculateIndices(null));
    }

    /**
     * Testing the output of printIndex Method. It asserts that the correct paragraph positions are mentioned for the
     * words which occur at least four times.
     */
    @Test
    void printIndex() {
        ArrayList<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph("Lorem Ipsum ".repeat(2)));
        paragraphs.add(new Paragraph("Ipsum ".repeat(2)));
        paragraphs.add(new Paragraph("Test Case ".repeat(4)));
        paragraphs.add(new Paragraph(WHITESPACE));

        indexManager.calculateIndices(paragraphs);

        assertEquals("Test 3" + lineSeparator() +
                "Ipsum 1,2" + lineSeparator() +
                "Case 3", indexManager.transformIndicesToPrintFormat());
    }

    /**
     * Testing the output of printing method with an empty {@link Paragraph} list.
     */
    @Test
    void printIndexWithoutParagraphs(){
        indexManager.calculateIndices(new ArrayList<>());

        assertEquals("Not enough words were found to index.", indexManager.transformIndicesToPrintFormat());
    }
}