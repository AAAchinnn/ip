package jeremy.ui;

import java.util.List;
import java.util.Scanner;

import jeremy.task.Task;

/**
 * Handles user input and displays application output.
 */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a user-interface handler that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the application welcome message.
     */
    public void showWelcome() {
        showLine();
        System.out.println(" Hello!, I'm Jeremy");
        System.out.println(" What can I do for you?");
        showLine();
    }

    /**
     * Displays the application goodbye message.
     */
    public void showBye() {
        showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays the separator line used by the user interface.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Returns whether another command line is available from standard input.
     *
     * @return {@code true} when another line is available; {@code false} otherwise.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and returns the next command line from standard input.
     *
     * @return Next command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the message shown when the user enters an empty command.
     */
    public void showEmptyInputMessage() {
        showLine();
        System.out.println(" I didn't quite catch that — type something, or 'bye' to exit.");
        showLine();
    }

    /**
     * Displays an error message surrounded by separator lines.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        showLine();
        System.out.println(" " + message);
        showLine();
    }

    /**
     * Displays a warning that saved tasks could not be loaded.
     */
    public void showLoadingError() {
        System.out.println(" Warning: I couldn't load your saved tasks.");
    }

    /**
     * Displays all tasks in the task list.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println(" No items stored yet.");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i));
            }
        }
        showLine();
    }

    /**
     * Displays a confirmation that a task was marked as done.
     *
     * @param task Task that was marked as done.
     */
    public void showTaskMarked(Task task) {
        showLine();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
        showLine();
    }

    /**
     * Displays a confirmation that a task was marked as not done.
     *
     * @param task Task that was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        showLine();
    }

    /**
     * Displays a confirmation that a task was added to the list.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks currently in the list.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " task(s) in the list.");
        showLine();
    }

    /**
     * Displays a confirmation that a task was removed from the list.
     *
     * @param task Task that was removed.
     * @param taskCount Number of tasks remaining in the list.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " task(s) in the list.");
        showLine();
    }

    /**
     * Closes the standard-input scanner.
     */
    public void close() {
        scanner.close();
    }
}
