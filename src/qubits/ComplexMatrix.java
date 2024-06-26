package qubits;

import java.util.Arrays;

/**
 * This class represents a matrix of complex numbers and supports basic matrix operations.
 *
 * <p>
 * A {@code ComplexMatrix} object can be constructed using its dimensions or from an existing
 * two-dimensional array of {@link ComplexNumber} objects. It supports matrix multiplication
 * and basic getter and setter operations.
 * </p>
 *
 * <p>
 * This class assumes matrices are rectangular (all rows have the same number of columns).
 * </p>
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public class ComplexMatrix {

    private int rows;
    private int cols;
    private ComplexNumber[][] data;

    /**
     * Constructs an empty matrix of given dimensions filled with zero complex numbers.
     *
     * @param rows The number of rows in the matrix.
     * @param cols The number of columns in the matrix.
     */
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

    /**
     * Constructs a matrix from a two-dimensional array of complex numbers.
     *
     * <p>
     * The dimensions of the matrix are determined from the array.
     * </p>
     *
     * @param data The two-dimensional array of complex numbers representing the matrix.
     */
    public ComplexMatrix(ComplexNumber[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    /**
     * Returns the number of rows in the matrix.
     *
     * @return The number of rows in the matrix.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the matrix.
     *
     * @return The number of columns in the matrix.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Retrieves the complex number at the specified row and column in the matrix.
     *
     * @param row The row index of the element (0-based).
     * @param col The column index of the element (0-based).
     * @return The complex number at the specified position.
     */
    public ComplexNumber get(int row, int col) {
        return data[row][col];
    }

    /**
     * Sets the value of the matrix element at the specified row and column.
     *
     * @param row   The row index of the element (0-based).
     * @param col   The column index of the element (0-based).
     * @param value The complex number to set at the specified position.
     */
    public void set(int row, int col, ComplexNumber value) {
        data[row][col] = value;
    }

    /**
     * Retrieves the underlying two-dimensional array representation of the matrix.
     *
     * @return The two-dimensional array of complex numbers representing the matrix.
     */
    public ComplexNumber[][] getMatrix() {
        return data;
    }

    /**
     * Multiplies this matrix with another matrix and returns the result as a new matrix.
     *
     * @param other The matrix to multiply with this matrix.
     * @return A new {@code ComplexMatrix} object that is the result of the matrix multiplication.
     * @throws IllegalArgumentException If the matrices have incompatible dimensions for multiplication.
     */
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

    /**
     * Multiplies two complex numbers.
     *
     * @param aVec The first complex number.
     * @param bVec The second complex number.
     * @return The complex number result of the multiplication.
     */
    private ComplexNumber multiplyComplex(ComplexNumber aVec, ComplexNumber bVec) {
        double real = aVec.getReal() * bVec.getReal() - aVec.getImag() * bVec.getImag();
        double imag = aVec.getReal() * bVec.getImag() + aVec.getImag() * bVec.getReal();
        return new ComplexNumber(real, imag);
    }

    /**
     * Adds two complex numbers.
     *
     * @param aVec The first complex number.
     * @param bVec The second complex number.
     * @return The complex number result of the addition.
     */
    private ComplexNumber addComplex(ComplexNumber aVec, ComplexNumber bVec) {
        double real = aVec.getReal() + bVec.getReal();
        double imag = aVec.getImag() + bVec.getImag();
        return new ComplexNumber(real, imag);
    }

    /**
     * Prints the matrix to the standard output.
     */
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Returns a string representation of the matrix.
     *
     * @return A string representation of the matrix.
     */
    @Override
    public String toString(){
        StringBuilder sBuild = new StringBuilder();
        for(ComplexNumber[] row : data){
            sBuild.append(Arrays.deepToString(row)).append("\n");
        }
        return sBuild.toString();
    }
}
