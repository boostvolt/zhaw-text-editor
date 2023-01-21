package textverarbeitung;

import java.util.EnumSet;

import static textverarbeitung.Command.UNKNOWN;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Class to validate userInput based on {@link Command} constant.
 */
public class InputParser {

    /**
     * Trims and Splits the given user input at every WHITESPACE.
     *
     * @param rawUserInput the input from the console by the user
     * @return the split user input as a String[]
     */
    public String[] splitUserInput(final String rawUserInput) {
        return rawUserInput.trim().split(WHITESPACE);
    }

    /**
     * Checks if the user input contained a matching {@link Command} and returns it.
     * If no command has been found, the enum constant UNKNOWN will be returned.
     *
     * @param userInput array of {@link String} Objects, representing the user Input.
     * @return Matching {@link Command} constant.
     */
    public Command getMatchingCommand(final String[] userInput) {
        for (Command command : EnumSet.allOf(Command.class)) {
            int numberOfIdentifierWords = command.getNumberOfIdentifierWords();
            if (userInput.length >= numberOfIdentifierWords && command.getIdentifier() != null && command.getIdentifier().equalsIgnoreCase(getConcatenatedUserInput(userInput, numberOfIdentifierWords))) {
                return command;
            }
        }
        return UNKNOWN;
    }

    /**
     * Checks if Input as array of {@link String} objects, has the required amount of parameters.
     *
     * @param userInput array of {@link String} Objects, representing the user Input.
     * @return {@code true} if the input has the correct amount of parameters.
     */
    public boolean hasCorrectNumberOfParameters(final Command selectedCommand, final String[] userInput) {
        final int numberOfGivenParameters = userInput.length - selectedCommand.getNumberOfIdentifierWords();
        return selectedCommand.isParameterRequired() ? numberOfGivenParameters == selectedCommand.getNumberOfParameters() : numberOfGivenParameters <= selectedCommand.getNumberOfParameters();
    }

    /**
     * Checks if userInput contains a parameter or if it is omitted.
     *
     * @param selectedCommand uses {@link Command} constant to validate the amount of parameters expected
     * @param userInput       array of {@link String} Objects, representing the user Input.
     * @return returns the parsed parameter for the {@link Command}
     */
    public String getSelectedParameterOrNull(final Command selectedCommand, final String[] userInput) {
        return isParameterOmitted(selectedCommand, userInput) ? null : userInput[userInput.length - 1];
    }

    /**
     * Checks if parameters were omitted when entering a {@link Command}
     *
     * @param userInput array of {@link String} Objects, representing the user Input.
     * @return {@code true} if parameter was omitted.
     */
    private boolean isParameterOmitted(final Command selectedCommand, final String[] userInput) {
        return userInput.length - selectedCommand.getNumberOfIdentifierWords() < selectedCommand.getNumberOfParameters();
    }

    /**
     * Method to separate {@link Command} within user input from parameters.
     *
     * @param userInput               array of {@link String} Objects, representing the user Input.
     * @param numberOfIdentifierWords number of words a given command contains. (i.e. 2 for 'format fix')
     * @return returns {@link String} of User Input that can be compared with the {@link Command} identifier
     */
    private String getConcatenatedUserInput(String[] userInput, int numberOfIdentifierWords) {
        final StringBuilder concatenatedUserInput = new StringBuilder();
        for (int i = 0; i < numberOfIdentifierWords; i++) {
            concatenatedUserInput.append(userInput[i]).append(WHITESPACE);
        }
        return concatenatedUserInput.toString().trim();
    }

}
