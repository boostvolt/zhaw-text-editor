package textverarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Tests the {@link FormatManager} and its methods
 */
class FormatManagerTest {

    private static final String TEXT_X = "x";
    private static final String TEXT_Y = "y";

    private FormatManager formatManager;

    @BeforeEach
    void setup() {
        formatManager = new FormatManager();
    }

    @Test
    void testDefaultFormatSetting() {
        assertEquals(Format.DEFAULT_FORMAT, formatManager.getSelectedFormat());
    }

    @Test
    void testFixedColumnWidthFormatSetting() {
        formatManager.setSelectedFormat(20);
        final Format selectedFormat = formatManager.getSelectedFormat();
        assertEquals(20, selectedFormat.getFixedColumnWidth());
        assertFalse(selectedFormat.isRaw());
    }

    @Test
    void testInvalidFixedColumnWidthFormatSetting() {
        final NullPointerException exception = assertThrows(NullPointerException.class,
                () -> formatManager.setSelectedFormat(null));
        assertEquals("fixedColumnWidth must not be null", exception.getMessage());
    }

    @Test
    void testRawFormatSetting() {
        formatManager.setDefaultFormat();
        final Format selectedFormat = formatManager.getSelectedFormat();
        assertNull(selectedFormat.getFixedColumnWidth());
        assertTrue(selectedFormat.isRaw());
    }

    @Test
    void testTransformRawFormatMultipleParagraphs() {
        final List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(TEXT_X));
        paragraphs.add(new Paragraph(TEXT_Y.repeat(100)));
        final String result = formatManager.transformParagraphsToSelectedFormat(paragraphs);
        assertTrue(result.contains("1: " + TEXT_X + lineSeparator()));
        assertTrue(result.contains("2: " + TEXT_Y.repeat(100)));
    }

    @Test
    void testTransformRawFormatEmptyParagraphs() {
        final List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(WHITESPACE));
        final String result = formatManager.transformParagraphsToSelectedFormat(paragraphs);
        assertEquals("1: " + WHITESPACE, result);
    }

    @Test
    void testTransformRawFormatNoParagraphs() {
        final String result = formatManager.transformParagraphsToSelectedFormat(new ArrayList<>());
        assertEquals("No paragraphs found to print.", result);
    }

    @Test
    void testTransformFixedLengthFormatMultipleParagraphs() {
        final List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(TEXT_X.repeat(9) + WHITESPACE + TEXT_X.repeat(2)));
        paragraphs.add(new Paragraph(TEXT_Y.repeat(10)));
        formatManager.setSelectedFormat(10);
        final String result = formatManager.transformParagraphsToSelectedFormat(paragraphs);
        assertTrue(result.contains(TEXT_X.repeat(9) + WHITESPACE + lineSeparator()));
        assertTrue(result.contains(TEXT_X.repeat(2) + lineSeparator()));
        assertTrue(result.contains(TEXT_Y.repeat(10) + lineSeparator()));
    }

    @Test
    void testTransformFixedLengthFormatParagraphShorterThanColumnWidth() {
        final List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(TEXT_X.repeat(10)));
        formatManager.setSelectedFormat(20);
        final String result = formatManager.transformParagraphsToSelectedFormat(paragraphs);
        assertTrue(result.contains(TEXT_X.repeat(10) + lineSeparator()));
    }

    @Test
    void testTransformFixedLengthFormatTrailingSpaces() {
        final List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(TEXT_X.repeat(9) + WHITESPACE.repeat(5) + TEXT_Y));
        formatManager.setSelectedFormat(10);
        final String result = formatManager.transformParagraphsToSelectedFormat(paragraphs);
        assertTrue(result.contains(TEXT_X.repeat(9) + WHITESPACE.repeat(5) + lineSeparator()));
        assertTrue(result.contains(TEXT_Y + lineSeparator()));
    }

    @Test
    void testTransformFixedLengthFormatWordWrap() {
        final List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(TEXT_X.repeat(15) + WHITESPACE + TEXT_Y.repeat(8)));
        formatManager.setSelectedFormat(20);
        final String result = formatManager.transformParagraphsToSelectedFormat(paragraphs);
        assertTrue(result.contains(TEXT_X.repeat(15) + WHITESPACE + lineSeparator()));
        assertTrue(result.contains(TEXT_Y.repeat(8) + lineSeparator()));
    }

    @Test
    void testTransformFixedLengthFormatEmptyParagraph() {
        final List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(new Paragraph(WHITESPACE));
        formatManager.setSelectedFormat(10);
        final String result = formatManager.transformParagraphsToSelectedFormat(paragraphs);
        assertEquals(WHITESPACE + lineSeparator(), result);
    }

    @Test
    void testTransformFixedLengthFormatNoParagraphs() {
        formatManager.setSelectedFormat(10);
        final String result = formatManager.transformParagraphsToSelectedFormat(new ArrayList<>());
        assertEquals("No paragraphs found to print.", result);
    }

}