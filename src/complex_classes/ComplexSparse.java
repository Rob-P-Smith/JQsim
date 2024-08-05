package complex_classes;

import java.util.*;

/**
 * Represents a sparse matrix of complex numbers using Compressed Sparse Column (CSC) format.
 * This class supports basic matrix operations and efficient storage for matrices with many zero elements.
 *
 * <p>A {@code ComplexSparse} object can be constructed using its dimensions or from an existing
 * two-dimensional array of {@link ComplexNumber} objects. It supports matrix operations
 * and basic getter and setter functions.</p>
 *
 * <p>This class assumes matrices are rectangular (all rows have the same number of columns).</p>
 *
 * @author Robert Smith
 * @version 0.1
 * @since 03 August 2024
 * @see ComplexNumber
 */
public final class ComplexSparse {
    // CSC format components
    private List<ComplexNumber> values; // Non-zero values
    private List<Integer> rowIndices;   // Row indices for each non-zero value
    private List<Integer> colPointers;  // Pointers to start of each column

    private int rows;
    private int cols;


    /**
     * Constructs an empty sparse matrix.
     * Initializes the CSC format components with empty lists and zero dimensions.
     */
    public ComplexSparse() {
        values = new ArrayList<>();
        rowIndices = new ArrayList<>();
        colPointers = new ArrayList<>();
        // First column always starts at index 0
        colPointers.add(0);
        rows = 0;
        cols = 0;
    }

    /**
     * Constructs a sparse matrix with specified dimensions.
     * Initializes the CSC format components for the given dimensions.
     *
     * @param height the number of rows in the matrix
     * @param width  the number of columns in the matrix
     */
    public ComplexSparse(int height, int width) {
        values = new ArrayList<>();
        rowIndices = new ArrayList<>();
        colPointers = new ArrayList<>(width + 1);

        // Initialize colPointers with zeros
        for (int i = 0; i <= width; i++) {
            colPointers.add(0);
        }

        rows = height;
        cols = width;
    }

    /**
     * Constructs a sparse matrix from a 2D array of ComplexNumbers.
     * Converts the dense matrix representation to CSC format.
     *
     * @param matrix the 2D array of {@link ComplexNumber} objects representing the matrix
     * @see ComplexNumber
     */
    public ComplexSparse(ComplexNumber[][] matrix) {
        this.rows = matrix.length;
        this.cols = matrix[0].length;

        // Initialize CSC components
        values = new ArrayList<>();
        rowIndices = new ArrayList<>();
        colPointers = new ArrayList<>(cols + 1);

        // Initialize colPointers with zeros
        for (int i = 0; i <= cols; i++) {
            colPointers.add(0);
        }

        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                if (!isZero(matrix[i][j])) {
                    values.add(new ComplexNumber(matrix[i][j].getReal(), matrix[i][j].getImag()));
                    rowIndices.add(i);
                }
            }
            colPointers.set(j + 1, values.size());
        }
    }

    /**
     * Returns a string representation of the sparse matrix in CSC format.
     *
     * @return a string representation of the matrix, including dimensions and CSC components
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("CSC Format " + "(" + rows + "x" + cols + "):\n");
        result.append("Values: ").append(values).append("\n");
        result.append("Row Indices: ").append(rowIndices).append("\n");
        result.append("Column Pointers: ").append(colPointers);
        return result.toString();
    }

    /**
     * Checks if a given complex number is effectively zero.
     * Uses a small epsilon value for floating-point comparison.
     *
     * @param value the {@link ComplexNumber} to check
     * @return true if the complex number is effectively zero, false otherwise
     * @see ComplexNumber
     */
    private boolean isZero(ComplexNumber value) {
        // Using a small epsilon value for floating-point comparison
        final double EPSILON = 1e-10;
        return Math.abs(value.getReal()) < EPSILON && Math.abs(value.getImag()) < EPSILON;
    }

    /**
     * Sets the data of this sparse matrix to match the data of another sparse matrix.
     * Performs a deep copy of all CSC components.
     *
     * @param other The other {@code ComplexSparse} matrix to copy data from
     */
    public void setData(ComplexSparse other) {
        // Copy the dimensions
        this.rows = other.rows;
        this.cols = other.cols;

        // Clear existing data
        this.values.clear();
        this.rowIndices.clear();
        this.colPointers.clear();

        // Deep copy of values
        for (ComplexNumber value : other.values) {
            this.values.add(new ComplexNumber(value.getReal(), value.getImag()));
        }

        // Copy row indices
        this.rowIndices.addAll(other.rowIndices);

        // Copy column pointers
        this.colPointers.addAll(other.colPointers);
    }

    /**
     * Returns the number of non-zero elements in the matrix.
     *
     * @return the count of non-zero elements
     */
    public int size() {
        return values.size();
    }

    /**
     * Returns the number of rows in the matrix.
     *
     * @return the number of rows
     */
    public int getHeight() {
        return rows;
    }

    /**
     * Returns the number of columns in the matrix.
     *
     * @return the number of columns
     */
    public int getWidth() {
        return cols;
    }

    /**
     * Inserts or updates a value in the matrix.
     * If the value is zero, it may remove the element from the sparse representation.
     *
     * @param row   the row index
     * @param col   the column index
     * @param value the {@link ComplexNumber} to insert or update
     * @throws IndexOutOfBoundsException if the specified indices are out of bounds
     * @see ComplexNumber
     */
    public void put(int row, int col, ComplexNumber value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid matrix indices");
        }

        int insertPos = colPointers.get(col);
        int nextColPos = colPointers.get(col + 1);

        // Find the insertion position
        while (insertPos < nextColPos && insertPos < rowIndices.size() && rowIndices.get(insertPos) < row) {
            insertPos++;
        }

        if (insertPos < nextColPos && insertPos < rowIndices.size() && rowIndices.get(insertPos) == row) {
            // Update existing value
            if (isZero(value)) {
                // Remove the element if it's zero
                values.remove(insertPos);
                rowIndices.remove(insertPos);
                for (int j = col + 1; j < colPointers.size(); j++) {
                    colPointers.set(j, colPointers.get(j) - 1);
                }
            } else {
                values.set(insertPos, value);
            }
        } else if (!isZero(value)) {
            // Insert new non-zero value
            values.add(insertPos, value);
            rowIndices.add(insertPos, row);
            for (int j = col + 1; j < colPointers.size(); j++) {
                colPointers.set(j, colPointers.get(j) + 1);
            }
        }
    }

    /**
     * Retrieves the value at the specified position in the matrix.
     * Returns zero (as a ComplexNumber) if no value is stored at the specified position.
     *
     * @param row the row index
     * @param col the column index
     * @return the {@link ComplexNumber} at the specified position
     * @throws IndexOutOfBoundsException if the specified indices are out of bounds
     * @see ComplexNumber
     */
    public ComplexNumber get(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid matrix indices");
        }

        for (int i = colPointers.get(col); i < colPointers.get(col + 1); i++) {
            if (rowIndices.get(i) == row) {
                return values.get(i);
            }
            if (rowIndices.get(i) > row) {
                break;
            }
        }
        return new ComplexNumber(0, 0);
    }
}