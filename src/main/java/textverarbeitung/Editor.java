package textverarbeitung;

import static textverarbeitung.Command.ADD;
import static textverarbeitung.Command.DEL;
import static textverarbeitung.Command.DUMMY;
import static textverarbeitung.Command.FORMAT_FIX;
import static textverarbeitung.Command.FORMAT_RAW;
import static textverarbeitung.Command.REPLACE;
import static textverarbeitung.TextConstants.WHITESPACE;

/**
 * Main Class of the textverarbeitung Editor.
 * Holds references to all Subclasses and handles the runtime of the program.
 */
public class Editor {

    private final Console console;
    private final InputParser inputParser;
    private final EditorManager editorManager;
    private final IndexManager indexManager;
    private final ValidationManager validationManager;
    private final FormatManager formatManager;

    private boolean editorRunning;

    /**
     * Creates a new Editor.
     * It initializes a new {@link Console}, {@link InputParser}, {@link ValidationManager}, {@link EditorManager}, {@link IndexManager} and {@link FormatManager}
     */
    public Editor() {
        console = new Console();
        inputParser = new InputParser();
        validationManager = new ValidationManager();
        editorManager = new EditorManager();
        indexManager = new IndexManager();
        formatManager = new FormatManager();
    }

    /**
     * Main Program loop
     * Prompts user for input and uses {@link InputParser} to validate the input and get the used {@link Command}.
     * The {@link Command} as well as Parameters are then passed on to processSelectedCommand method.
     */
    public void run() {
        editorRunning = true;
        printIntro();

        while (editorRunning) {
            console.print("> ");
            final String[] splitUserInput = inputParser.splitUserInput(console.readLine());
            final Command selectedCommand = inputParser.getMatchingCommand(splitUserInput);
            if (inputParser.hasCorrectNumberOfParameters(selectedCommand, splitUserInput)) {
                processSelectedCommand(selectedCommand, splitUserInput);
            } else {
                printInvalidInput();
            }
        }
    }

    /**
     * Prints out Intro text and help text using the {@link Console} Class.
     */
    private void printIntro() {
        console.printLine("Welcome to the command line based text editor program!");
        console.printLine("Please enter any available command including parameter (if required) from the list below:");
        console.printLine(Command.getAllHelpTexts());
    }

    /**
     * Processes the command that was input.
     * If it is a valid command it calls for the corresponding method.
     * Otherwise, it returns an error message.
     *
     * @param selectedCommand the {@link Command} that was input.
     * @param userInput array of {@link String} Objects, representing the user Input.
     */
    private void processSelectedCommand(final Command selectedCommand, final String[] userInput) {
        switch (selectedCommand) {
            case ADD -> processAdd(userInput);
            case DEL -> processDelete(userInput);
            case DUMMY -> processDummy(userInput);
            case EXIT -> editorRunning = false;
            case FORMAT_FIX -> processFormatFix(userInput);
            case FORMAT_RAW -> processFormatRaw();
            case HELP -> console.printLine(Command.getAllHelpTexts());
            case INDEX -> processIndex();
            case PRINT -> console.printLine(formatManager.transformParagraphsToSelectedFormat(editorManager.getParagraphs()));
            case REPLACE -> processReplace(userInput);
            default -> printInvalidInput();
        }
    }

    /**
     * Method to add a {@link Paragraph}.
     * Validates the userInput and parameters using {@link ValidationManager}
     * The {@link EditorManager} handles the implementation of the addition.
     *
     * @param userInput array of {@link String} Objects, representing the user Input.
     */
    private void processAdd(final String[] userInput) {
        final ValidationResult validationResult = validationManager.validateParagraphIndex(
                inputParser.getSelectedParameterOrNull(ADD, userInput), editorManager.getParagraphSize(), true);
        if (validationResult.isValid()) {
            console.print("Please enter text to add: ");
            final String paragraphToAdd = console.readLine();
            final String resultMessage = editorManager.addParagraph(paragraphToAdd, validationResult.getParsedParameter());
            console.printLine(resultMessage);
        } else {
            console.printError(validationResult.getErrorMessage());
        }
    }

    /**
     * Method to remove a {@link Paragraph}.
     * Validates the userInput and parameters using {@link ValidationManager}
     * The {@link EditorManager} handles implementation.
     *
     * @param userInput array of {@link String} Objects, representing the user Input.
     */
    private void processDelete(final String[] userInput) {
        final ValidationResult validationResult = validationManager.validateParagraphIndex(
                inputParser.getSelectedParameterOrNull(DEL, userInput), editorManager.getParagraphSize(), false);
        if (validationResult.isValid()) {
            final String resultMessage = editorManager.deleteParagraph(validationResult.getParsedParameter());
            console.printLine(resultMessage);
        } else {
            console.printError(validationResult.getErrorMessage());
        }
    }

    /**
     * Method to add a dummy text {@link Paragraph}.
     * Validates the userInput and parameters using {@link ValidationManager}
     * The {@link EditorManager} handles the implementation of the addition.
     *
     * @param userInput array of {@link String} Objects, representing the user Input.
     */
    private void processDummy(final String[] userInput) {
        final ValidationResult validationResult = validationManager.validateParagraphIndex(
                inputParser.getSelectedParameterOrNull(DUMMY, userInput), editorManager.getParagraphSize(), true);
        if (validationResult.isValid()) {
            final String resultMessage = editorManager.addDummyText(validationResult.getParsedParameter());
            console.printLine(resultMessage);
        } else {
            console.printError(validationResult.getErrorMessage());
        }
    }

    /**
     * Method to process and change the printing style to Format Fix.
     * Validates the userInput and parameters using {@link ValidationManager}
     * The {@link FormatManager} handles implementation
     *
     * @param userInput array of {@link String} Objects, representing the user Input.
     */
    private void processFormatFix(final String[] userInput) {
        final ValidationResult validationResult = validationManager.validateFixedColumnWidth(
                inputParser.getSelectedParameterOrNull(FORMAT_FIX, userInput));
        if (validationResult.isValid()) {
            formatManager.setSelectedFormat(validationResult.getParsedParameter());
            console.printLine("Format has been set to " + FORMAT_FIX.getIdentifier() + WHITESPACE + validationResult.getParsedParameter());
        } else {
            console.printError(validationResult.getErrorMessage());
        }
    }

    /**
     * Method to process and change the printing style to Format Raw.
     * The {@link FormatManager} handles implementation.
     */
    private void processFormatRaw() {
        formatManager.setDefaultFormat();
        console.printLine("Format has been set to " + FORMAT_RAW.getIdentifier());
    }

    /**
     * Method to process and print out Index using {@link IndexManager}
     * The {@link EditorManager} handles implementation.
     */
    private void processIndex() {
        indexManager.calculateIndices(editorManager.getParagraphs());
        final String resultMessage = indexManager.transformIndicesToPrintFormat();
        console.printLine(resultMessage);
    }

    /**
     * Processes the Replace Command.
     * Uses {@link ValidationManager} to validate the {@link Command} and inputs.
     * If the input is valid it receives the search- & replace-text from the user and validates those as well.
     * Then it passes the {@link Paragraph} Index parameter, search- and replace-text to the {@link EditorManager}
     *
     * @param userInput array of {@link String} Objects, representing the user Input.
     */
    private void processReplace(final String[] userInput) {
        final ValidationResult validationResult = validationManager.validateParagraphIndex(
                inputParser.getSelectedParameterOrNull(REPLACE, userInput), editorManager.getParagraphSize(), false);
        if (validationResult.isValid()) {
            console.print("Please enter the character or word you would like to replace: ");
            String searchText = console.readLine();
            console.print("Please enter what you want to replace it with: ");
            String replaceText = console.readLine();
            final String resultMessage = editorManager.replace(validationResult.getParsedParameter(), searchText, replaceText);
            console.printLine(resultMessage);
        } else {
            console.printError(validationResult.getErrorMessage());
        }

    }

    /**
     * Uses {@link Console} class to print out an "Invalid input" error to the user.
     */
    private void printInvalidInput() {
        console.printError("Invalid command entered. Please enter a valid command with the correct number of parameters from the list below:");
        console.printEmptyLine();
        console.printLine(Command.getAllHelpTexts());
    }

}