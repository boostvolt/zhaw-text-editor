package textverarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static textverarbeitung.TextConstants.BLANK;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Tests the {@link ValidationManager} class and its methods
 */
class ValidationManagerTest {

    private ValidationManager validationManager;

    @BeforeEach
    void setup() {
        validationManager = new ValidationManager();
    }

    @ParameterizedTest
    @ValueSource(strings = {"E", "e", "-0", "-10", "0"})
    void testValidateParagraphIndex_InvalidNumber(final String input) {
        final ValidationResult result = validationManager.validateParagraphIndex(input, 2, false);
        assertFalse(result.isValid());
        assertEquals("Given paragraph position '" + input + "' must be a non-zero positive Integer.", result.getErrorMessage());
    }

    @Test
    void testValidateParagraphIndex_NotWithinRange() {
        final ValidationResult result = validationManager.validateParagraphIndex("3", 2, false);
        assertFalse(result.isValid());
        assertEquals("Given paragraph position '3' invalid. Choose any paragraph position between 1 and 2", result.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "10", BLANK, WHITESPACE})
    @NullSource
    void testValidateParagraphIndex_True(final String input) {
        final ValidationResult result = validationManager.validateParagraphIndex(input, 20, false);
        assertTrue(result.isValid());
    }

    @ParameterizedTest
    @ValueSource(strings = {"E", "e", "-0", "-10", "0", "1"})
    void testValidateFixedColumnWidth_InvalidNumber(final String input) {
        final ValidationResult result = validationManager.validateFixedColumnWidth(input);
        assertFalse(result.isValid());
        assertEquals("Given fixed column width '" + input + "' must be a non-zero positive Integer greater than 1.", result.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {BLANK, WHITESPACE})
    @NullSource
    void testValidateFixedColumnWidth_NullOrBlank(final String input) {
        final ValidationResult result = validationManager.validateFixedColumnWidth(input);
        assertFalse(result.isValid());
        assertEquals("A fixed column width as a positive non-zero Integer greater than 1 must be specified.", result.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"10", "2", "10000"})
    void testValidateFixedColumnWidth_True(final String input) {
        final ValidationResult result = validationManager.validateFixedColumnWidth(input);
        assertTrue(result.isValid());
    }

}