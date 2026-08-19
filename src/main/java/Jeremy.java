import java.util.Scanner;

public class Jeremy {

    private static final String LINE =
            "____________________________________________________________";

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(" Hello!, I'm Jeremy");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);

        // Polymorphic storage: every Todo/Deadline/Event is a Task.
        Task[] tasks = new Task[100];
        int taskCount = 0;

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

            } else if (trimmed.equalsIgnoreCase("list")) {
                System.out.println(LINE);
                if (taskCount == 0) {
                    System.out.println(" No items stored yet.");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        // toString() dispatches to the right subclass automatically.
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                }
                System.out.println(LINE);

            } else if (trimmed.toLowerCase().startsWith("mark ")
                    || trimmed.toLowerCase().startsWith("unmark ")) {
                boolean markAsDone = trimmed.toLowerCase().startsWith("mark ");
                String numberPart = markAsDone
                        ? trimmed.substring(5).trim()
                        : trimmed.substring(7).trim();
                int index;
                try {
                    index = Integer.parseInt(numberPart);
                } catch (NumberFormatException e) {
                    index = -1;
                }
                System.out.println(LINE);
                if (index < 1 || index > taskCount) {
                    System.out.println(" That task number doesn't exist.");
                } else {
                    Task task = tasks[index - 1];
                    if (markAsDone) {
                        task.markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                    } else {
                        task.markAsNotDone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                    }
                    System.out.println("   " + task);
                }
                System.out.println(LINE);

            } else if (trimmed.equalsIgnoreCase("todo") || trimmed.toLowerCase().startsWith("todo ")) {
                String description = trimmed.length() > 4 ? trimmed.substring(4).trim() : "";
                System.out.println(LINE);
                if (description.isEmpty()) {
                    System.out.println(" The description of a todo cannot be empty.");
                } else if (taskCount >= tasks.length) {
                    System.out.println(" Storage full, can't add more items.");
                } else {
                    Task newTask = new Todo(description);
                    tasks[taskCount++] = newTask;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + taskCount + " task(s) in the list.");
                }
                System.out.println(LINE);

            } else if (trimmed.equalsIgnoreCase("deadline") || trimmed.toLowerCase().startsWith("deadline ")) {
                String rest = trimmed.length() > 8 ? trimmed.substring(8).trim() : "";
                System.out.println(LINE);
                int byIndex = rest.indexOf("/by");
                String description = byIndex >= 0 ? rest.substring(0, byIndex).trim() : rest.trim();
                String by = byIndex >= 0 ? rest.substring(byIndex + 3).trim() : "";
                if (description.isEmpty() || by.isEmpty()) {
                    System.out.println(" A deadline needs a description and a '/by' date/time,");
                    System.out.println(" e.g. deadline submit report /by 11/10/2019 5pm");
                } else if (taskCount >= tasks.length) {
                    System.out.println(" Storage full, can't add more items.");
                } else {
                    Task newTask = new Deadline(description, by);
                    tasks[taskCount++] = newTask;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + taskCount + " task(s) in the list.");
                }
                System.out.println(LINE);

            } else if (trimmed.equalsIgnoreCase("event") || trimmed.toLowerCase().startsWith("event ")) {
                String rest = trimmed.length() > 5 ? trimmed.substring(5).trim() : "";
                System.out.println(LINE);
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
                if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    System.out.println(" An event needs a description, '/from' and '/to' date/time,");
                    System.out.println(" e.g. event team project meeting /from 2/10/2019 2pm /to 4pm");
                } else if (taskCount >= tasks.length) {
                    System.out.println(" Storage full, can't add more items.");
                } else {
                    Task newTask = new Event(description, from, to);
                    tasks[taskCount++] = newTask;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + taskCount + " task(s) in the list.");
                }
                System.out.println(LINE);

            } else if (trimmed.isEmpty()) {
                System.out.println(LINE);
                System.out.println(LINE);

            } else {
                // Fallback: unrecognized command. (Old behaviour treated any
                // free text as a plain item; now todo/deadline/event are the
                // supported ways to add tasks, so we just flag it.)
                System.out.println(LINE);
                System.out.println(" I'm not sure what that means. Try: todo, deadline, event, list, mark, unmark, bye.");
                System.out.println(LINE);
            }
        }
        scanner.close();
    }
}
