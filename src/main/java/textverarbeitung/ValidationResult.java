package textverarbeitung;

import static java.util.Objects.requireNonNull;

/**
 * Container for any result returned by {@link ValidationManager}. It provides additional information why a validation
 * was unsuccessful and also the parsed parameter (if any).
 */
public class ValidationResult {

    private final boolean valid;
    private final String errorMessage;
    private final Integer parsedParameter;

    /**
     * Creates a new ValidationResult object. All constructors end up calling this one. It sets all fields.
     *
     * @param valid           boolean to indicate if the validation was successful
     * @param errorMessage    in case the validation is unsuccessful a meaningful error message
     * @param parsedParameter the validated and parsed parameter as an {@link Integer}
     */
    private ValidationResult(final boolean valid, final String errorMessage, final Integer parsedParameter) {
        this.valid = valid;
        this.errorMessage = valid
                ? errorMessage
                : requireNonNull(errorMessage, "errorMessage must not be null for an invalid validation result");
        this.parsedParameter = parsedParameter;
    }

    /**
     * Creates a new ValidationResult object. Only used if the validation was successful and no parameter was passed.
     */
    public ValidationResult() {
        this(true, null, null);
    }

    /**
     * Creates a new ValidationResult object. Only used if the validation was successful and a parameter was passed.
     *
     * @param parsedParameter the validated and parsed parameter as an {@link Integer}
     */
    public ValidationResult(final Integer parsedParameter) {
        this(true, null, parsedParameter);
    }

    /**
     * Creates a new ValidationResult object. Only used if the validation was unsuccessful.
     *
     * @param errorMessage a meaningful error message why the validation was unsuccessful.
     */
    public ValidationResult(final String errorMessage) {
        this(false, errorMessage, null);
    }

    /**
     * Returns if the ValidationResult is successful or unsuccessful.
     *
     * @return the valid boolean of the ValidationResult
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the error message of the ValidationResult.
     *
     * @return the error message of the ValidationResult
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the parsed parameter of the ValidationResult.
     *
     * @return the parsed parameter of the ValidationResult
     */
    public Integer getParsedParameter() {
        return parsedParameter;
    }

}
