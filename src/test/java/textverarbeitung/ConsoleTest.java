package textverarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the {@link Console} and its methods
 */
class ConsoleTest {

    private Console console;

    @BeforeEach
    void setup() {
        console = new Console();
    }

    @Test
    void testPrint() {
        String output = "Output";
        OutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        console.print(output);

        assertEquals(output, outputStream.toString());
    }

    @Test
    void testPrintLine() {
        String output = "Output";
        OutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        console.printLine(output);

        assertEquals(output + System.lineSeparator(), outputStream.toString());
    }

    @Test
    void testPrintError() {
        String errorOutput = "Error Output";
        OutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        console.printError(errorOutput);

        assertEquals(format(TextConstants.AS_RED, errorOutput) + System.lineSeparator(), outputStream.toString());
    }
}
