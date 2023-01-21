package textverarbeitung;

import java.util.EnumSet;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static textverarbeitung.TextConstants.AS_BOLD;
import static textverarbeitung.TextConstants.AS_ITALIC;
import static textverarbeitung.TextConstants.BLANK;
import static textverarbeitung.TextConstants.TAB;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Command Enum Storing all Commands.
 * Contains {@code String} Command Name with spaces, {@code String} Help text, {@code Integer} Numbers of Parameters and {@code boolean} if Parameters are required.
 */
public enum Command {

    /**
     * Adds a {@link Paragraph} at position n or at the end of the list.
     */
    ADD("ADD", "Adds a paragraph at position n or at the end if no parameter given.", 1, false),
    /**
     * Removes a {@link Paragraph} at position n or at the end of the list.
     */
    DEL("DEL", "Removes a paragraph at position n or the last one if no parameter given.", 1, false),
    /**
     * Adds a dummy {@link Paragraph} at the end of the {@link Paragraph}-List.
     */
    DUMMY("DUMMY", "Adds a dummy paragraph at position n or at the end if no parameter given.", 1, false),
    /**
     * Exits the Application.
     */
    EXIT("EXIT", "Exits the program."),
    /**
     * Calls {@link FormatManager} and changes the output format of the {@link Paragraph}s to only be n columns wide.
     */
    FORMAT_FIX("FORMAT FIX", "Changes the output format of the paragraphs to only be n columns wide.", 1, true),
    /**
     * Calls {@link FormatManager} and changes the output {@link Format} to a raw output of the {@link Paragraph}s.
     */
    FORMAT_RAW("FORMAT RAW", "Changes the output format to a raw output of the paragraphs."),
    /**
     * Prints help Text of all {@link Command}s.
     */
    HELP("HELP", "Prints this help text."),
    /**
     * Uses {@link IndexManager} to calculate word frequency in {@link Paragraph}s and prints out frequent words.
     */
    INDEX("INDEX", "Indexes word frequency in paragraphs and prints out frequent words."),
    /**
     * Prints out all {@link Paragraph}s in the current {@link Format}.
     */
    PRINT("PRINT", "Prints out all Paragraphs in the current format."),
    /**
     * Replaces a sequence of characters in {@link Paragraph} n.
     */
    REPLACE("REPLACE", "Replaces a sequence of characters in paragraph n or in the last paragraph if no parameter given.", 1, false),
    /**
     * Used if the user input does not match any command's identifier
     */
    UNKNOWN();

    private static final Integer MIN_WHITESPACES_BETWEEN_COMMAND_AND_HELP_TEXT = 10;

    private final String identifier;
    private final String helpText;
    private final Integer numberOfParameters;
    private final boolean parameterRequired;

    /**
     * Creates a new {@link Command} {@link Enum} constant.
     * This constructor is used for commands with parameters.
     *
     * @param identifier         Command Name as {@code String} with Spaces, to match User Input.
     * @param helpText           Returned in Help Command. To output all Commands for the User.
     * @param numberOfParameters {@code Integer} of expected Parameters for the given {@link Command}.
     * @param parameterRequired  {@code boolean} if parameters are required or not for given {@link Command}.
     */
    Command(final String identifier, final String helpText, final Integer numberOfParameters,
            final boolean parameterRequired) {
        this.identifier = identifier;
        this.helpText = helpText;
        this.numberOfParameters = numberOfParameters;
        this.parameterRequired = parameterRequired;
    }

    /**
     * Creates a new {@link Command} {@link Enum} constant.
     * This constructor is used for commands that have no parameters.
     *
     * @param identifier Command Name as {@code String} with Spaces, to match User Input.
     * @param helpText   Returned in Help Command. To output all Commands for the User.
     */
    Command(final String identifier, final String helpText) {
        this(identifier, helpText, 0, false);
    }

    /**
     * Creates a new {@link Command} {@link Enum} constant.
     * This constructor is used for the unknown command constant.
     */
    Command() {
        this(null, null, 0, false);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getHelpText() {
        return helpText;
    }

    public Integer getNumberOfParameters() {
        return numberOfParameters;
    }

    public boolean isParameterRequired() {
        return parameterRequired;
    }

    /**
     * Gets identifier of {@link Command}, splits it and returns the length.
     *
     * @return number of words a given {@link Command} contains. (i.e. 2 for 'format fix')
     */
    public int getNumberOfIdentifierWords() {
        return this.getIdentifier() == null
                ? 0
                : this.getIdentifier().split(WHITESPACE).length;
    }

    /**
     * Builds a {@link StringBuilder} Object with all Commands and their respective values.
     * This text is used for the Help {@link Command} and an information on how to use the Text-Editor
     *
     * @return Text with all {@link Command}s as {@link String}
     */
    public static String getAllHelpTexts() {
        final StringBuilder stringBuilder = new StringBuilder();
        int longestCommandIdentifierLength = 0;

        for (final Command command : EnumSet.allOf(Command.class)) {
            if (command.getIdentifier() != null
                    && command.getIdentifier().length() > longestCommandIdentifierLength) {
                longestCommandIdentifierLength = command.getIdentifier().length();
            }
        }

        for (Command command : EnumSet.allOf(Command.class)) {
            if (command.getHelpText() != null) {
                final String parameter = command.getNumberOfParameters() == 0 ? BLANK : " [n]";
                stringBuilder.append(TAB)
                        .append(format(AS_BOLD, command.getIdentifier()))
                        .append(format(AS_BOLD, format(AS_ITALIC, parameter)))
                        .append(WHITESPACE.repeat((longestCommandIdentifierLength + MIN_WHITESPACES_BETWEEN_COMMAND_AND_HELP_TEXT)
                                - (command.getIdentifier().length()) - parameter.length()))
                        .append(command.getHelpText());
                if (command.getNumberOfParameters() > 0) {
                    stringBuilder.append(command.isParameterRequired()
                            ? WHITESPACE + format(AS_ITALIC, "(Parameter is mandatory)")
                            : WHITESPACE + format(AS_ITALIC, "(Parameter can be omitted)"));
                }
                stringBuilder.append(lineSeparator());
            }
        }
        return stringBuilder.toString();
    }

}