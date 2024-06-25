package qubits;

import java.util.Arrays;

/**
 * This class represents the Matrix
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public class ComplexMatrix {

    private int rows;
    private int cols;
    private ComplexNumber[][] data;

    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = new ComplexNumber();
            }
        }
    }

    public ComplexMatrix(ComplexNumber[][] data) {
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

    public ComplexNumber get(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, ComplexNumber value) {
        data[row][col] = value;
    }

    public ComplexNumber[][] getMatrix() {
        return data;
    }

    public ComplexMatrix multiply(ComplexMatrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication: "
                    + this.rows + "x" + this.cols + " cannot be multiplied with " + other.rows + "x" + other.cols);
        }
        ComplexNumber[][] result = new ComplexNumber[this.rows][other.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                result[i][j] = new ComplexNumber(); // Initialize the result matrix cell
                for (int k = 0; k < this.cols; k++) {
                    result[i][j] = addComplex(result[i][j], multiplyComplex(this.data[i][k], other.data[k][j]));
                }
            }
        }
        return new ComplexMatrix(result);
    }

    private ComplexNumber multiplyComplex(ComplexNumber a, ComplexNumber b) {
        double real = a.getReal() * b.getReal() - a.getImag() * b.getImag();
        double imag = a.getReal() * b.getImag() + a.getImag() * b.getReal();
        return new ComplexNumber(real, imag);
    }

    private ComplexNumber addComplex(ComplexNumber a, ComplexNumber b) {
        double real = a.getReal() + b.getReal();
        double imag = a.getImag() + b.getImag();
        return new ComplexNumber(real, imag);
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }
//
//    @Override
//    public String toString() {
//        return Arrays.deepToString(data);
//    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(ComplexNumber[] row : data){
            sb.append(Arrays.deepToString(row)+"\n");
        }
        return sb.toString();
    }
}
