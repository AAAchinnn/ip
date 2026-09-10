package jeremy.ui;

import java.util.List;
import java.util.Scanner;

import jeremy.task.Task;

/** Deals with all interactions with the user: reading input and printing output. */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        showLine();
        System.out.println(" Hello!, I'm Jeremy");
        System.out.println(" What can I do for you?");
        showLine();
    }

    public void showBye() {
        showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showEmptyInputMessage() {
        showLine();
        System.out.println(" I didn't quite catch that — type something, or 'bye' to exit.");
        showLine();
    }

    public void showError(String message) {
        showLine();
        System.out.println(" " + message);
        showLine();
    }

    public void showLoadingError() {
        System.out.println(" Warning: I couldn't load your saved tasks.");
    }

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

    public void showMatchingTasks(List<Task> tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println(" No matching tasks found.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i));
            }
        }
        showLine();
    }

    public void showTaskMarked(Task task) {
        showLine();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
        showLine();
    }

    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        showLine();
    }

    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " task(s) in the list.");
        showLine();
    }

    public void showScheduleConflict(List<Task> conflicts) {
        showLine();
        System.out.println(" Warning: this task may clash with:");
        for (Task conflict : conflicts) {
            System.out.println("   - " + conflict);
        }
        showLine();
    }

    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " task(s) in the list.");
        showLine();
    }

    public void close() {
        scanner.close();
    }
}
