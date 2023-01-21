package textverarbeitung;

import java.util.Scanner;

import static java.lang.System.lineSeparator;
import static textverarbeitung.TextConstants.AS_RED;

/**
 * Console class to handle console inputs and outputs.
 */
public class Console {

    private final Scanner scanner;

    /**
     * Creates a new Console.
     * It initializes the scanner which is used to read the console input.
     */
    public Console() {
        scanner = new Scanner(System.in);
    }

    /**
     * Prints the given message to the console and inserts a line break afterwards.
     *
     * @param message the message to print
     */
    public void printLine(final String message) {
        System.out.println(message);
    }

    /**
     * Prints an empty line to the console.
     */
    public void printEmptyLine() {
        System.out.println();
    }

    /**
     * Prints the given message to the console without any line break.
     *
     * @param message the message to print
     */
    public void print(final String message) {
        System.out.print(message);
    }

    /**
     * Prints the given error message to the console.
     *
     * @param message the message text to print
     */
    public void printError(final String message) {
        System.out.printf(AS_RED + lineSeparator(), message);
    }

    /**
     * Reads a line from the console.
     *
     * @return the line that was read from the console
     */
    public String readLine() {
        return scanner.nextLine();
    }

}