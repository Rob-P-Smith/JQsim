import java.util.ArrayList;
import java.util.List;

/**
 * Driver for testing functionality of the simulator, will eventually migrate to a menu driven system
 *
 * @author Robert P Smith
 * @version 0.1
 * @since 20 June 2024
 */
public class Main {

    /**
     * Simple driver for testing
     *
     * @param args none required or provisioned for
     */
    public static void main(String[] args) {

        //testing case, input your arguments as the numbers in the ComplexNumber instantiates
        ComplexNumber[] nums = {
                new ComplexNumber(0.0),
                new ComplexNumber(1 / (Math.sqrt(2))),
                new ComplexNumber(1 / (Math.sqrt(2))),
                new ComplexNumber(0.0)
        };

        List<ComplexNumber> listOfValues = new ArrayList<>(List.of(nums));
        ComplexNumber[][] result = new ComplexNumber[0][];

        try {
            result = Qubit.qubit(listOfValues);
            System.out.println("Qubit created successfully:");
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    ComplexNumber num = result[i][j];
                    System.out.println("[" + num.real + "]");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        if (result.length > 0) {
            Qubit.measureMat(result);
        }
    }
}
