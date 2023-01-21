package textverarbeitung;

import static java.lang.Integer.parseInt;

/**
 * Manages any validation of the parameters. It returns a {@link ValidationResult} object which is used as a container.
 */
public class ValidationManager {

    /**
     * Validates the given paragraphIndex parameter and returns the corresponding {@link ValidationResult} container.
     *
     * @param selectedParagraphIndex       the paragraph index parameter typed by the user
     * @param paragraphSize                the current range of the paragraphs
     * @param allowedToExceedParagraphSize boolean to steer the range comparison
     * @return the {@link ValidationResult} container object
     */
    public ValidationResult validateParagraphIndex(final String selectedParagraphIndex, final Integer paragraphSize,
                                                   final boolean allowedToExceedParagraphSize) {
        if (isNullOrBlank(selectedParagraphIndex)) {
            return createValidResultWithoutParameter();
        } else if (!isPositiveNonZeroInteger(selectedParagraphIndex)) {
            return createInvalidResult("Given paragraph position '" + selectedParagraphIndex
                    + "' must be a non-zero positive Integer.");
        } else if (!isIndexWithinRange(parseInt(selectedParagraphIndex), paragraphSize, allowedToExceedParagraphSize)) {
            return createInvalidResult("Given paragraph position '" + selectedParagraphIndex
                    + "' invalid. Choose any paragraph position between 1 and "
                    + (allowedToExceedParagraphSize ? paragraphSize + 1 : paragraphSize));
        } else {
            return createValidResultWithParameter(selectedParagraphIndex);
        }
    }

    /**
     * Validates the given fixed column width and returns the corresponding {@link ValidationResult} container.
     *
     * @param selectedFixedColumnWidth the fixed column width typed by the user
     * @return the {@link ValidationResult} container object
     */
    public ValidationResult validateFixedColumnWidth(final String selectedFixedColumnWidth) {
        if (isNullOrBlank(selectedFixedColumnWidth)) {
            return createInvalidResult("A fixed column width as a positive non-zero Integer greater than 1 must be specified.");
        } else if (!isPositiveMinTwoInteger(selectedFixedColumnWidth)) {
            return createInvalidResult("Given fixed column width '" + selectedFixedColumnWidth
                    + "' must be a non-zero positive Integer greater than 1.");
        } else {
            return createValidResultWithParameter(selectedFixedColumnWidth);
        }
    }

    private boolean isPositiveNonZeroInteger(final String parameter) {
        return parameter.matches("[1-9]\\d*");
    }

    private boolean isPositiveMinTwoInteger(final String parameter) {
        return isPositiveNonZeroInteger(parameter) && !parameter.equals(Integer.toString(1));
    }

    private boolean isIndexWithinRange(final Integer selectedIndex, final Integer paragraphRange,
                                       final boolean allowedToExceedParagraphSize) {
        return selectedIndex <= (allowedToExceedParagraphSize
                ? paragraphRange + 1
                : paragraphRange);
    }

    private boolean isNullOrBlank(final String value) {
        return value == null || value.isBlank();
    }

    private ValidationResult createInvalidResult(final String errorMessage) {
        return new ValidationResult(errorMessage);
    }

    private ValidationResult createValidResultWithoutParameter() {
        return new ValidationResult();
    }

    private ValidationResult createValidResultWithParameter(final String unparsedParameter) {
        return new ValidationResult(parseInt(unparsedParameter));
    }

}