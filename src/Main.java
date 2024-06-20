import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        //testing the 50/50 case
//        ComplexNumber a = new ComplexNumber(1/(Math.sqrt(2))); // Example: sqrt(1/2) + 0i
//        ComplexNumber b = new ComplexNumber(1/(Math.sqrt(2))); // Example: 0 + sqrt(1/2)i

        //testing the zero case
//        ComplexNumber a = new ComplexNumber(1.0);
//        ComplexNumber b = new ComplexNumber(0.0);

        //testing the one case
        ComplexNumber a = new ComplexNumber(1.0);
        ComplexNumber b = new ComplexNumber(0.0);
//        ComplexNumber c = new ComplexNumber(0.0);
//        ComplexNumber d = new ComplexNumber(0.0);
        List<ComplexNumber> listOfValues = new ArrayList<ComplexNumber>();
        listOfValues.add(a);
        listOfValues.add(b);
//        listOfValues.add(c);
//        listOfValues.add(d);
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
        if(result.length > 0) {
            Qubit.measureMat(result);
        }
    }
}
