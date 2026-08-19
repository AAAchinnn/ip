import java.util.Scanner;

public class Jeremy {

    private static final String LINE =
            "____________________________________________________________";
    private static final int CAPACITY = 100;

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(" Hello!, I'm Jeremy");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);

        // Polymorphic storage: every Todo/Deadline/Event is a Task.
        Task[] tasks = new Task[CAPACITY];
        int[] taskCount = new int[] { 0 }; // boxed so helper methods can update it

        while (true) {
            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine();
            String trimmed = input.trim();

            if (trimmed.equalsIgnoreCase("bye")) {
                System.out.println(LINE);
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            if (trimmed.isEmpty()) {
                System.out.println(LINE);
                System.out.println(" I didn't quite catch that — type something, or 'bye' to exit.");
                System.out.println(LINE);
                continue;
            }

            try {
                handleCommand(trimmed, tasks, taskCount);
            } catch (JeremyException e) {
                System.out.println(LINE);
                System.out.println(" " + e.getMessage());
                System.out.println(LINE);
            }
        }
        scanner.close();
    }

    /**
     * Parses and executes a single non-empty, non-"bye" command.
     * Throws JeremyException with a user-facing message for any
     * kind of bad input (unknown command, missing description,
     * bad task number, etc).
     */
    private static void handleCommand(String trimmed, Task[] tasks, int[] taskCount) throws JeremyException {
        String lower = trimmed.toLowerCase();

        if (lower.equals("list")) {
            printList(tasks, taskCount[0]);

        } else if (lower.startsWith("mark ") || lower.equals("mark")
                || lower.startsWith("unmark ") || lower.equals("unmark")) {
            handleMark(trimmed, tasks, taskCount[0]);

        } else if (lower.equals("todo") || lower.startsWith("todo ")) {
            String description = lower.equals("todo") ? "" : trimmed.substring(4).trim();
            addTask(new Todo(requireDescription(description, "todo")), tasks, taskCount);

        } else if (lower.equals("deadline") || lower.startsWith("deadline ")) {
            String rest = lower.equals("deadline") ? "" : trimmed.substring(8).trim();
            addTask(parseDeadline(rest), tasks, taskCount);

        } else if (lower.equals("event") || lower.startsWith("event ")) {
            String rest = lower.equals("event") ? "" : trimmed.substring(5).trim();
            addTask(parseEvent(rest), tasks, taskCount);

        } else {
            throw new JeremyException(
                    "I don't recognize '" + firstWord(trimmed)
                            + "'. Try: todo, deadline, event, list, mark, unmark, bye.");
        }
    }

    private static String firstWord(String s) {
        int spaceIndex = s.indexOf(' ');
        return spaceIndex >= 0 ? s.substring(0, spaceIndex) : s;
    }

    private static void printList(Task[] tasks, int taskCount) {
        System.out.println(LINE);
        if (taskCount == 0) {
            System.out.println(" No items stored yet.");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < taskCount; i++) {
                System.out.println(" " + (i + 1) + "." + tasks[i]);
            }
        }
        System.out.println(LINE);
    }

    private static void handleMark(String trimmed, Task[] tasks, int taskCount) throws JeremyException {
        boolean markAsDone = trimmed.toLowerCase().startsWith("mark");
        // Strip the leading "mark"/"unmark" word to get whatever's left.
        String numberPart = markAsDone
                ? trimmed.substring(Math.min(4, trimmed.length())).trim()
                : trimmed.substring(Math.min(6, trimmed.length())).trim();

        if (numberPart.isEmpty()) {
            throw new JeremyException(
                    "Which task? Use: " + (markAsDone ? "mark" : "unmark") + " <task number>.");
        }

        int index;
        try {
            index = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            throw new JeremyException("'" + numberPart + "' isn't a valid task number.");
        }

        if (taskCount == 0) {
            throw new JeremyException("Your list is empty — nothing to " + (markAsDone ? "mark" : "unmark") + ".");
        }
        if (index < 1 || index > taskCount) {
            throw new JeremyException(
                    "That task number doesn't exist. You have " + taskCount + " task(s) — pick between 1 and " + taskCount + ".");
        }

        Task task = tasks[index - 1];
        System.out.println(LINE);
        if (markAsDone) {
            task.markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            task.markAsNotDone();
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + task);
        System.out.println(LINE);
    }

    private static String requireDescription(String description, String taskType) throws JeremyException {
        if (description.isEmpty()) {
            throw new JeremyException("The description of a " + taskType + " cannot be empty.");
        }
        return description;
    }

    private static Deadline parseDeadline(String rest) throws JeremyException {
        int byIndex = rest.indexOf("/by");
        String description = byIndex >= 0 ? rest.substring(0, byIndex).trim() : rest.trim();
        String by = byIndex >= 0 ? rest.substring(byIndex + 3).trim() : "";

        if (description.isEmpty() && by.isEmpty()) {
            throw new JeremyException(
                    "A deadline needs a description and a '/by' date/time, e.g. "
                            + "deadline submit report /by 11/10/2019 5pm");
        }
        if (description.isEmpty()) {
            throw new JeremyException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new JeremyException(
                    "A deadline needs a '/by' date/time, e.g. deadline " + description + " /by 11/10/2019 5pm");
        }
        return new Deadline(description, by);
    }

    private static Event parseEvent(String rest) throws JeremyException {
        int fromIndex = rest.indexOf("/from");
        int toIndex = rest.indexOf("/to");
        String description = fromIndex >= 0 ? rest.substring(0, fromIndex).trim() : rest.trim();

        String from = "";
        String to = "";
        if (fromIndex >= 0 && toIndex > fromIndex) {
            from = rest.substring(fromIndex + 5, toIndex).trim();
            to = rest.substring(toIndex + 3).trim();
        } else if (fromIndex >= 0) {
            from = rest.substring(fromIndex + 5).trim();
        }

        if (description.isEmpty()) {
            throw new JeremyException("The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new JeremyException(
                    "An event needs both '/from' and '/to' date/time, e.g. "
                            + "event " + description + " /from 2/10/2019 2pm /to 4pm");
        }
        return new Event(description, from, to);
    }

    private static void addTask(Task newTask, Task[] tasks, int[] taskCount) throws JeremyException {
        if (taskCount[0] >= tasks.length) {
            throw new JeremyException("Storage full, can't add more items.");
        }
        tasks[taskCount[0]] = newTask;
        taskCount[0]++;
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + newTask);
        System.out.println(" Now you have " + taskCount[0] + " task(s) in the list.");
        System.out.println(LINE);
    }
}
