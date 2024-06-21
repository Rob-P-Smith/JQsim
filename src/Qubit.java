public class Qubit {
    private final Matrix state;

    public Qubit(double a, double b) {
        this.state = new Matrix(new double[][]{{a}, {b}});
    }

    public Matrix getState() {
        return state;
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
