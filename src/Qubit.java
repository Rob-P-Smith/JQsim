/**
 * Qubit class which holds the qubit values as doubles in a Matrix. Provides ability to manpiulate via
 * quantum gates while tracking state.
 * 
 * @author Robert P Smith
 * @version 0.1
 * @since 20 June 2024
 */
public class Qubit {
    private final Matrix state;

    public Qubit(double a, double b) {
        if(Qops.eEqual(((a*a)+(b*b)), 1.0)){
            this.state = new Matrix(new double[][]{{a}, {b}});
        } else{
            throw new IllegalArgumentException("Violates Born's Rule for provided values");
        }
    }

    public Matrix getState() {
        return state;
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
