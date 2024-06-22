import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix {

    private int rows;
    private int cols;
    private double[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double get(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, double value) {
        data[row][col] = value;
    }

    public double[][] getMatrix(){
        return data;
    }

    public Matrix multiply(Matrix other) {
        int commonDim = data[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < commonDim; k++) {
                    result[i][j] += data[i][k] * other.data[k][j];
                }
            }
        }
        return new Matrix(result);
    }

    // Method to perform element-wise multiplication with another matrix
    public Matrix elementWiseMultiply(Matrix other) {
        if (this.rows != other.getRows() || this.cols != other.getCols()) {
            throw new IllegalArgumentException("Matrices must have the same dimensions.");
        }

        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.set(i, j, this.get(i, j) * other.get(i, j));
            }
        }
        return result;
    }

    // Convert matrix to nested list format (for easy manipulation in Java)
    public List<List<Double>> toList() {
        List<List<Double>> list = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(data[i][j]);
            }
            list.add(row);
        }
        return list;
    }

    // Static method to create a Matrix from a nested list
    public static Matrix fromList(List<List<Double>> list) {
        int rows = list.size();
        int cols = list.get(0).size();
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = list.get(i).get(j);
            }
        }
        return new Matrix(data);
    }

    // Static method to perform tensor product of two matrices
    public static Matrix tensorProduct(Matrix m1, Matrix m2) {
        Matrix result = new Matrix(m1.getRows() * m2.getRows(), m1.getCols() * m2.getCols());
        for (int i = 0; i < m1.getRows(); i++) {
            for (int j = 0; j < m1.getCols(); j++) {
                for (int k = 0; k < m2.getRows(); k++) {
                    for (int l = 0; l < m2.getCols(); l++) {
                        result.set(i * m2.getRows() + k, j * m2.getCols() + l,
                                m1.get(i, j) * m2.get(k, l));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(data);
    }
}
