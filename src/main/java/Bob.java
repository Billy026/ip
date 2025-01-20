import java.util.Scanner;

public class Bob {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Printing of logo
        String logo = 
                  "      ____        _        \n"
                + "     |  _ \\      | |      \n"
                + "     | |_| |     | |       \n"
                + "     |    /  ___ | | __    \n"
                + "     |  _ \\ / _ \\| |/_ \\\n"
                + "     | |_| | |_| |  |_| |  \n"
                + "     |____/ \\___/|_|\\__/ \n";
        System.out.println("    Hello from\n" + logo);
        System.out.println("    ___________________________________");

        // Initial greeting
        System.out.println("    Hi, I'm Bob!");
        System.out.println("    Can I do something for you?");
        System.out.println("    ___________________________________");
        System.out.println();

        // Echo commands from user
        while(true) {
            String input = sc.nextLine();
            if (input.equals("bye")) break;

            System.out.println("    ___________________________________");
            System.out.println("    You inputted: " + input);
            System.out.println("    ___________________________________");
            System.out.println();
        }

        System.out.println("    ___________________________________");
        System.out.println("    Bye! See you soon!");
        System.out.println("    ___________________________________");

        sc.close();
    }
}
