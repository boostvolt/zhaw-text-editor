package textverarbeitung;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static textverarbeitung.TextConstants.BLANK;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Tests the {@link EditorManager} and its methods
 */
public class EditorManagerTest {

    private final EditorManager editorManager = new EditorManager();

    @Test
    void testAddParagraph() {
        editorManager.addParagraph("asdf1", 1);
        editorManager.addParagraph("asdf2", 1);
        assertEquals(2, editorManager.getParagraphSize());
        final List<Paragraph> paragraphs = editorManager.getParagraphs();
        assertEquals("asdf2", paragraphs.get(0).getContent());
        assertEquals("asdf1", paragraphs.get(1).getContent());
    }

    @Test
    void testDeleteParagraph() {
        editorManager.addParagraph("asdf1", 1);
        editorManager.deleteParagraph(1);
        assertTrue(editorManager.getParagraphs().isEmpty());
    }

    @Test
    void testDummy() {
        editorManager.addDummyText(1);
        assertEquals(1, editorManager.getParagraphSize());
        assertFalse(editorManager.getParagraphs().get(0).getContent().isEmpty());
    }

    @Test
    void testReplace() {
        final String content = "asdf";
        final String toReplaceWith = "x";
        editorManager.addParagraph(content, 1);
        editorManager.addParagraph(content, 2);
        final String resultMessage = editorManager.replace(2, content, toReplaceWith);
        assertEquals("Characters " + content + " have been replaced with " + toReplaceWith + " in paragraph Nr. 2.", resultMessage);
        assertEquals(2, editorManager.getParagraphSize());
        final List<Paragraph> paragraphs = editorManager.getParagraphs();
        assertEquals(content, paragraphs.get(0).getContent());
        assertEquals(toReplaceWith, paragraphs.get(1).getContent());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {BLANK, WHITESPACE})
    void testReplace_InvalidCharacters(final String input) {
        editorManager.addParagraph("asdf", 1);
        final String resultMessage = editorManager.replace(1, "asdf", input);
        assertEquals("You cannot search or replace blanks or spaces.", resultMessage);
    }

}
