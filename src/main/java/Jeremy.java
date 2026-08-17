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
                    for (int i = 0; i < itemCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + items[i]);
                    }
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
                itemCount++;
                System.out.println(line);
                System.out.println(" added: " + trimmed);
                System.out.println(line);
            }
        }

        scanner.close();
    }
}
