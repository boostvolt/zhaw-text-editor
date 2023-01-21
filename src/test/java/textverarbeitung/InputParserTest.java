package textverarbeitung;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static textverarbeitung.Command.ADD;
import static textverarbeitung.Command.EXIT;
import static textverarbeitung.Command.FORMAT_FIX;
import static textverarbeitung.Command.UNKNOWN;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Tests the {@link InputParser} and its methods
 */
public class InputParserTest {

    private final InputParser inputParser = new InputParser();

    @Test
    void testGetNoMatchingCommand() {
        final String[] userInput = new String[1];
        userInput[0] = "WrongCommand";
        assertEquals(UNKNOWN, inputParser.getMatchingCommand(userInput));
    }

    @Test
    void testGetMatchingCommand() {
        final String[] userInput = new String[3];
        final String[] commandWords = Command.FORMAT_FIX.getIdentifier().split(WHITESPACE);
        userInput[0] = commandWords[0];
        userInput[1] = commandWords[1];
        userInput[2] = "10";
        assertEquals(FORMAT_FIX, inputParser.getMatchingCommand(userInput));
    }

    @Test
    void testHasCorrectNumberOfParameters_True() {
        final String[] userInput = new String[2];
        userInput[0] = ADD.getIdentifier();
        userInput[1] = "10";
        assertTrue(inputParser.hasCorrectNumberOfParameters(ADD, userInput));
    }

    @Test
    void testHasCorrectNumberOfParameters_False() {
        final String[] userInput = new String[2];
        userInput[0] = EXIT.getIdentifier();
        userInput[1] = "10";
        assertFalse(inputParser.hasCorrectNumberOfParameters(EXIT, userInput));
    }

    @Test
    void testGetSelectedParameterOrNull() {
        final String[] userInput = new String[2];
        userInput[0] = ADD.getIdentifier();
        userInput[1] = "10";
        assertEquals("10", inputParser.getSelectedParameterOrNull(ADD, userInput));
    }

    @Test
    void testGetSelectedParameterOrNull_ParameterOmitted() {
        final String[] userInput = new String[1];
        userInput[0] = ADD.getIdentifier();
        assertNull(inputParser.getSelectedParameterOrNull(ADD, userInput));
    }

}
