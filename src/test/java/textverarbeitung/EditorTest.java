package textverarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static textverarbeitung.Command.ADD;
import static textverarbeitung.Command.DEL;
import static textverarbeitung.Command.DUMMY;
import static textverarbeitung.Command.EXIT;
import static textverarbeitung.Command.FORMAT_FIX;
import static textverarbeitung.Command.FORMAT_RAW;
import static textverarbeitung.Command.HELP;
import static textverarbeitung.Command.INDEX;
import static textverarbeitung.Command.PRINT;
import static textverarbeitung.Command.REPLACE;

/**
 * Teste the App / Editor class by calling the main and simulating the user input from the console end to end.
 */
class EditorTest {

    private final InputStream stdin = System.in;
    private ByteArrayOutputStream byteArrayOutputStream;

    @BeforeEach
    void setup() {
        System.setIn(stdin);
        byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        System.setOut(ps);
    }

    @Test
    void testHelp() {
        System.setIn(new ByteArrayInputStream((HELP.getIdentifier()
                + lineSeparator()
                + EXIT.getIdentifier()).getBytes()));
        App.main(new String[0]);

        assertTrue(byteArrayOutputStream.toString().contains(Command.getAllHelpTexts()));
    }

    @Test
    void testAdd() {
        final String paragraphToAdd = "Simon";
        System.setIn(new ByteArrayInputStream((addParagraph(paragraphToAdd)
                + PRINT.getIdentifier()
                + lineSeparator()
                + EXIT.getIdentifier()).getBytes()));
        App.main(new String[0]);

        assertTrue(getConsoleOutput().contains("1: " + paragraphToAdd));
    }

    @Test
    void testDel() {
        final String paragraphToAdd = "Simon";
        System.setIn(new ByteArrayInputStream((addParagraph(paragraphToAdd)
                + DEL.getIdentifier()
                + lineSeparator()
                + PRINT.getIdentifier()
                + exitEditor()).getBytes()));
        App.main(new String[0]);

        assertTrue(getConsoleOutput().contains("No paragraphs found to print."));
    }

    @Test
    void testAddWithIndex() {
        final String paragraphToAdd1 = "Simon1";
        final String paragraphToAdd2 = "Simon2";
        System.setIn(new ByteArrayInputStream((addParagraph(paragraphToAdd1, 1)
                + addParagraph(paragraphToAdd2, 1)
                + PRINT.getIdentifier()
                + exitEditor()).getBytes()));
        App.main(new String[0]);

        final String result = getConsoleOutput();
        assertTrue(result.contains("1: " + paragraphToAdd2));
        assertTrue(result.contains("2: " + paragraphToAdd1));
    }

    @Test
    void testDummy() {
        System.setIn(new ByteArrayInputStream((DUMMY.getIdentifier()
                + lineSeparator()
                + PRINT.getIdentifier()
                + exitEditor()).getBytes()));
        App.main(new String[0]);

        assertTrue(getConsoleOutput().contains("1: "));
    }

    @Test
    void testFormatFix() {
        final String paragraphToAdd = "Simon".repeat(5);
        System.setIn(new ByteArrayInputStream((addParagraph(paragraphToAdd)
                + FORMAT_FIX.getIdentifier().concat(" 20")
                + lineSeparator()
                + PRINT.getIdentifier()
                + exitEditor()).getBytes()));
        App.main(new String[0]);

        final String result = getConsoleOutput();
        assertTrue(result.contains("SimonSimonSimonSimo-"));
        assertTrue(result.contains("nSimon"));
    }

    @Test
    void testFormatRaw() {
        final String paragraphToAdd = "Simon".repeat(5);
        System.setIn(new ByteArrayInputStream((addParagraph(paragraphToAdd)
                + FORMAT_FIX.getIdentifier().concat(" 20")
                + lineSeparator()
                + FORMAT_RAW.getIdentifier()
                + lineSeparator()
                + PRINT.getIdentifier()
                + exitEditor()).getBytes()));
        App.main(new String[0]);

        assertTrue(getConsoleOutput().contains("1: SimonSimonSimonSimonSimon"));
    }

    @Test
    void testIndex() {
        final String paragraphToAdd = "Simon ".repeat(2);
        final String paragraphToAdd2 = "Simon ".repeat(2);
        System.setIn(new ByteArrayInputStream((addParagraph(paragraphToAdd)
                + addParagraph(paragraphToAdd2)
                + INDEX.getIdentifier()
                + exitEditor()).getBytes()));
        App.main(new String[0]);

        assertTrue(getConsoleOutput().contains("Simon 1,2"));
    }

    @Test
    void testReplace() {
        final String paragraphToAdd = "Simon";
        System.setIn(new ByteArrayInputStream((addParagraph(paragraphToAdd)
                + REPLACE.getIdentifier()
                + lineSeparator()
                + "S"
                + lineSeparator()
                + "X"
                + lineSeparator()
                + PRINT.getIdentifier()
                + lineSeparator()
                + exitEditor()).getBytes()));
        App.main(new String[0]);

        assertTrue(getConsoleOutput().contains("1: Ximon"));
    }

    private String addParagraph(final String paragraphToAdd) {
        return addParagraph(paragraphToAdd, null);
    }

    private String addParagraph(final String paragraphToAdd, final Integer index) {
        return ADD.getIdentifier() + (index == null ? "" : " " + index) + lineSeparator() + paragraphToAdd + lineSeparator();
    }

    private String exitEditor() {
        return lineSeparator() + EXIT.getIdentifier();
    }

    private String getConsoleOutput() {
        return byteArrayOutputStream.toString();
    }

}
