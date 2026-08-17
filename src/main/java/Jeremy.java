import java.util.Scanner;

public class Jeremy {
    public static void main(String[] args) {

        String line = "____________________________________________________________";

        System.out.println(line);
        System.out.println(" Hello!, I'm Jeremy");
        System.out.println(" What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);

        String[] items = new String[100];
        boolean[] done = new boolean[100];
        int itemCount = 0;

        while (true) {
            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine();
            String trimmed = input.trim();

            if (trimmed.equalsIgnoreCase("bye")) {
                System.out.println(line);
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            } else if (trimmed.equalsIgnoreCase("list")) {
                System.out.println(line);
                if (itemCount == 0) {
                    System.out.println(" No items stored yet.");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < itemCount; i++) {
                        String mark = done[i] ? "X" : " ";
                        System.out.println(" " + (i + 1) + ".[" + mark + "] " + items[i]);
                    }
                }
                System.out.println(line);
            } else if (trimmed.toLowerCase().startsWith("mark ") || trimmed.toLowerCase().startsWith("unmark ")) {
                boolean markAsDone = trimmed.toLowerCase().startsWith("mark ");
                String numberPart = markAsDone ? trimmed.substring(5).trim() : trimmed.substring(7).trim();
                int index = -1;
                try {
                    index = Integer.parseInt(numberPart);
                } catch (NumberFormatException e) {
                    index = -1;
                }

                System.out.println(line);
                if (index < 1 || index > itemCount) {
                    System.out.println(" That task number doesn't exist.");
                } else {
                    done[index - 1] = markAsDone;
                    String mark = markAsDone ? "X" : " ";
                    if (markAsDone) {
                        System.out.println(" Nice! I've marked this task as done:");
                    } else {
                        System.out.println(" OK, I've marked this task as not done yet:");
                    }
                    System.out.println("   [" + mark + "] " + items[index - 1]);
                }
                System.out.println(line);
            } else if (trimmed.isEmpty()) {
                System.out.println(line);
                System.out.println(line);
            } else if (itemCount >= items.length) {
                System.out.println(line);
                System.out.println(" Storage full, can't add more items.");
                System.out.println(line);
            } else {
                items[itemCount] = trimmed;
                done[itemCount] = false;
                itemCount++;
                System.out.println(line);
                System.out.println(" added: " + trimmed);
                System.out.println(line);
            }
        }

        scanner.close();
    }
}
