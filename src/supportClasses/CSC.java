package supportClasses;

import complex_classes.ComplexNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a sparse matrix of complex numbers using Compressed Sparse Column (CSC) format.
 *
 * <p>
 * A {@code ComplexSparse} object can be constructed using its dimensions or from an existing
 * two-dimensional array of {@link ComplexNumber} objects. It supports basic matrix operations.
 * </p>
 *
 * <p>
 * This class assumes matrices are rectangular (all rows have the same number of columns).
 * </p>
 *
 * @author Robert Smith
 * @version 0.2
 * @since 05 August 2024
 */
public final class CSC {
    // CSC format components
    private List<ComplexNumber> values; // Non-zero values
    private List<Integer> rowIndices;   // Row indices for each non-zero value
    private List<Integer> colPointers;  // Pointers to start of each column

    private int rows;
    private int cols;

    /**
     * Constructs an empty sparse matrix.
     */
    public CSC() {
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
     *
     * @param height the height of the matrix
     * @param width  the width of the matrix
     */
    public CSC(int height, int width) {
        values = new ArrayList<>();
        rowIndices = new ArrayList<>();
        colPointers = new ArrayList<>();
        // First column always starts at index 0
        colPointers.add(0);
        rows = height;
        cols = width;
    }

    /**
     * Constructs a sparse matrix from a 2D array of ComplexNumbers.
     *
     * @param matrix the 2D array of ComplexNumbers
     */
    public CSC(ComplexNumber[][] matrix) {
        this(matrix.length, matrix[0].length);
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < rows; i++) {
                if (!isZero(matrix[i][j])){
                    values.add(new ComplexNumber(matrix[i][j].getReal(), matrix[i][j].getImag()));
                    rowIndices.add(i);
                }
            }
            colPointers.set(j + 1, values.size());
        }
    }

    /**
     * Returns a string representation of the sparse matrix.
     *
     * @return a string representation of the matrix
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
     * Checks if this complex number is zero (both real and imaginary parts are zero).
     *
     * @return true if the complex number is zero, false otherwise
     */
    private boolean isZero(ComplexNumber value) {
        // Using a small epsilon value for floating-point comparison
        final double EPSILON = 1e-10;
        return Math.abs(value.getReal()) < EPSILON && Math.abs(value.getImag()) < EPSILON;
    }


    /**
     * Returns the number of non-zero elements in the matrix.
     *
     * @return the number of non-zero elements
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
     *
     * @param row    the row index
     * @param col    the column index
     * @param value  the complex number to insert
     */
    public void put(int row, int col, ComplexNumber value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid matrix indices");
        }

        int insertPos = colPointers.get(col);
        while (insertPos < colPointers.get(col + 1) && rowIndices.get(insertPos) < row) {
            insertPos++;
        }

        if (insertPos < colPointers.get(col + 1) && rowIndices.get(insertPos) == row) {
            // Update existing value
            if (isZero(value)) {
                // Remove the element if it's zero
                values.remove(insertPos);
                rowIndices.remove(insertPos);
                for (int j = col + 1; j <= cols; j++) {
                    colPointers.set(j, colPointers.get(j) - 1);
                }
            } else {
                values.set(insertPos, value);
            }
        } else if (!isZero(value)) {
            // Insert new non-zero value
            values.add(insertPos, value);
            rowIndices.add(insertPos, row);
            for (int j = col + 1; j <= cols; j++) {
                colPointers.set(j, colPointers.get(j) + 1);
            }
        }
    }

    /**
     * Retrieves the value at the specified position in the matrix.
     *
     * @param row the row index
     * @param col the column index
     * @return the complex number at the specified position
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