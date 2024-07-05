package supportClasses;
import java.util.Scanner;

/**
 * Helper class for reading and writing to the Java console with input validation specific to type requested.
 *
 * @author Robert Smith
 * @version 0.1
 */
public class Console
{
    private static Scanner console = new Scanner(System.in);

    /**
     * Retrieves a String value from the Java console.
     *
     * @return a String value
     */
    public static String getString()
    {
        return console.nextLine();
    }

    /**
     * Retrieves an int value from the Java console.
     *
     * @return an int value
     */
    public static int getInt()
    {
        while (!console.hasNextInt())
        {
            System.out.print("Please enter a valid integer: ");

            //clear the scanner buffer
            console.nextLine();
        }

        int result = console.nextInt();

        //clear the scanner buffer
        console.nextLine();

        return result;
    }

    /**
     * Retrieves a double value from the Java console.
     *
     * @return a double value
     */
    public static double getDouble()
    {
        while (!console.hasNextDouble())
        {
            System.out.print("Please enter a valid double: ");

            //clear the scanner buffer
            console.nextLine();
        }

        double result = console.nextDouble();

        //clear the scanner buffer
        console.nextLine();

        return result;
    }

    /**
     * Retrieves a boolean value from the Java console.
     *
     * @return a boolean value
     */
    public static boolean getBoolean()
    {
        while (!console.hasNextBoolean())
        {
            System.out.print("Please enter true/false: ");

            //clear the scanner buffer
            console.nextLine();
        }

        boolean result = console.nextBoolean();

        //clear the scanner buffer
        console.nextLine();

        return result;
    }

    /**
     * Retrieves a char value from the Java console.
     *
     * @return a char value
     */
    public static char getCharacter()
    {
        while (true)
        {
            String input = console.nextLine();

            if (input.length() == 1)
            {
                return input.charAt(0);
            }
            else
            {
                System.out.print("Please enter a single character: ");
            }
        }
    }
}
