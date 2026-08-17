import java.util.Scanner;

public class Jeremy {
    public static void main(String[] args) {

        System.out.println("Hello!, I'm Jeremy\nWhat can I do for you?");
        System.out.println("Hits you with a surprise left");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine();

            if (input.trim().equalsIgnoreCase("bye")) {
                System.out.println("Bye!");
                break;
            }

            System.out.println(input);
        }

        scanner.close();
    }
}
