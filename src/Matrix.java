import java.util.Arrays;

public class Matrix {
    private final double[][] matrix;

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public Matrix multiply(Matrix other) {
        int rows = matrix.length;
        int cols = other.matrix[0].length;
        int commonDim = matrix[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < commonDim; k++) {
                    result[i][j] += matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return new Matrix(result);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(matrix);
    }
}
