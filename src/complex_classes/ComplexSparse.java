package complex_classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int rows;
    private int cols;

    /**
     * Returns the list of non-zero values in the sparse matrix.
     *
     * @return A List of ComplexNumber objects representing the non-zero values in the matrix.
     */
    public List<ComplexNumber> getValues() {
        return values;
    }

    /**
     * Returns the list of row indices corresponding to the non-zero values.
     *
     * @return A List of Integer objects representing the row indices of non-zero values.
     */
    public List<Integer> getRowIndices() {
        return rowIndices;
    }

    /**
     * Returns the list of column pointers in the CSC format.
     *
     * @return A List of Integer objects representing the starting indices of each column in the values and rowIndices lists.
     */
    public List<Integer> getColPointers() {
        return colPointers;
    }

    /**
     * Sets the list of non-zero values in the sparse matrix.
     *
     * @param values A List of ComplexNumber objects representing the new non-zero values.
     */
    public void setValues(List<ComplexNumber> values) {
        this.values = values;
    }

    /**
     * Sets the list of row indices corresponding to the non-zero values.
     *
     * @param rowIndices A List of Integer objects representing the new row indices of non-zero values.
     */
    public void setRowIndices(List<Integer> rowIndices) {
        this.rowIndices = rowIndices;
    }

    /**
     * Sets the list of column pointers in the CSC format.
     *
     * @param colPointers A List of Integer objects representing the new starting indices of each column.
     */
    public void setColPointers(List<Integer> colPointers) {
        this.colPointers = colPointers;
    }

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
        colPointers = new ArrayList<>(Collections.nCopies(width + 1, 0));
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
    public boolean isZero(ComplexNumber value) {
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
     * Inserts or updates a value at the specified position in the matrix.
     * If the value is zero, it removes the element from the sparse representation.
     * This method uses a hybrid approach of linear and binary search for optimal performance.
     *
     * @param row   the row index
     * @param col   the column index
     * @param value the {@link ComplexNumber} value to insert or update
     * @throws IndexOutOfBoundsException if the specified indices are out of bounds
     *
     * Performance characteristics:
     * - For columns with 8 or fewer elements, a linear search is used for better cache locality.
     * - For columns with more than 8 elements, a binary search is employed for faster lookup.
     * - The method adapts to the sparsity of each column for optimized performance.
     */
    public void put(int row, int col, ComplexNumber value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid matrix indices");
        }

        lock.writeLock().lock();
        try {
            int start = colPointers.get(col);
            int end = colPointers.get(col + 1) - 1;
            int pos = start;

            // Linear search for small ranges, binary search for larger ones
            if (end - start > 8) {
                // Binary search
                while (start <= end) {
                    pos = (start + end) >>> 1;
                    int rowAtPos = rowIndices.get(pos);
                    if (rowAtPos < row) {
                        start = pos + 1;
                    } else if (rowAtPos > row) {
                        end = pos - 1;
                    } else {
                        break;
                    }
                }
                if (rowIndices.get(pos) < row) pos++;
            } else {
                // Linear search
                while (pos <= end && rowIndices.get(pos) < row) {
                    pos++;
                }
            }

            if (pos < colPointers.get(col + 1) && rowIndices.get(pos) == row) {
                // Update existing value
                if (isZero(value)) {
                    values.remove(pos);
                    rowIndices.remove(pos);
                    updateColPointers(col + 1, -1);
                } else {
                    values.set(pos, value);
                }
            } else if (!isZero(value)) {
                // Insert new non-zero value
                values.add(pos, value);
                rowIndices.add(pos, row);
                updateColPointers(col + 1, 1);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Updates the column pointers after inserting or removing an element.
     *
     * @param startCol the column index to start updating from
     * @param delta the value to add to each subsequent column pointer
     */
    private void updateColPointers(int startCol, int delta) {
        for (int j = startCol; j < colPointers.size(); j++) {
            colPointers.set(j, colPointers.get(j) + delta);
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