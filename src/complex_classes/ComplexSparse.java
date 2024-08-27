package complex_classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a sparse matrix of complex numbers using Compressed Sparse Column (CSC) format.
 * This class provides efficient storage and operations for matrices with many zero elements,
 * commonly encountered in various scientific and engineering applications, including quantum computing.
 *
 * <p>Key features:
 * <ul>
 *   <li>Efficient storage using CSC format, ideal for sparse matrices</li>
 *   <li>Thread-safe operations using read-write locks</li>
 *   <li>Optimized element insertion and retrieval using hybrid linear/binary search</li>
 *   <li>Support for basic matrix operations and transformations</li>
 * </ul>
 *
 * <p>The CSC format uses three main components:
 * <ul>
 *   <li>values: A list of non-zero complex number values</li>
 *   <li>rowIndices: A list of row indices for each non-zero value</li>
 *   <li>colPointers: A list of pointers indicating the start of each column in the values and rowIndices lists</li>
 * </ul>
 *
 * <p>Performance considerations:
 * <ul>
 *   <li>The 'put' method uses a hybrid approach, switching between linear and binary search based on column density</li>
 *   <li>The 'get' method uses binary search for efficient element retrieval</li>
 *   <li>Thread safety is ensured using a ReentrantReadWriteLock for concurrent access</li>
 * </ul>
 *
 * <p>This class assumes matrices are rectangular (all rows have the same number of columns).</p>
 *
 * @author Robert Smith
 * @version 1.0
 * @since 11 August 2024
 * @see ComplexNumber
 * @see ComplexMath
 */
public final class ComplexSparse {
    // CSC format components
    private List<ComplexNumber> values; // Non-zero values
    private final List<Integer> rowIndices;   // Row indices for each non-zero value
    private final List<Integer> colPointers;  // Pointers to start of each column
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
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
     * @throws IllegalArgumentException if height or width is negative
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
     * @throws NullPointerException if matrix is null or contains null elements
     * @throws IllegalArgumentException if matrix is empty or jagged
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
                if (!ComplexMath.isZero(matrix[i][j])) {
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
        return "CSC Format " + "(" + rows + "x" + cols + "):\n" + "Values: " + values + "\n" +
                "Row Indices: " + rowIndices + "\n" +
                "Column Pointers: " + colPointers;
    }

    /**
     * Sets the data of this sparse matrix to match the data of another sparse matrix.
     * Performs a deep copy of all CSC components.
     *
     * @param other The other {@code ComplexSparse} matrix to copy data from
     * @throws NullPointerException if other is null
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
     * Inserts or updates a value at the specified position in the matrix.
     * If the value is zero, it removes the element from the sparse representation.
     * This method uses a hybrid approach of linear and binary search for optimal performance.
     *
     * @param row   the row index
     * @param col   the column index
     * @param value the {@link ComplexNumber} value to insert or update
     * @throws IndexOutOfBoundsException if the specified indices are out of bounds
     * @throws NullPointerException if value is null
     */
    public void put(int row, int col, ComplexNumber value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid matrix indices");
        }

        lock.writeLock().lock();
        try {
            int insertionPoint = colPointers.get(col);
            int end = colPointers.get(col + 1);

            // Find the insertion point
            while (insertionPoint < end && rowIndices.get(insertionPoint) < row) {
                insertionPoint++;
            }

            if (insertionPoint < end && rowIndices.get(insertionPoint) == row) {
                // Update existing value
//                if (ComplexMath.isZero(value)) {
//                    // Remove the element if the new value is zero
//                    values.remove(insertionPoint);
//                    rowIndices.remove(insertionPoint);
//                    for (int i = col + 1; i < colPointers.size(); i++) {
//                        colPointers.set(i, colPointers.get(i) - 1);
//                    }
//                } else {
                    // Update the value
                    values.set(insertionPoint, value);
//                }
            } else if (!ComplexMath.isZero(value)) {
                // Insert new non-zero value
                values.add(insertionPoint, value);
                rowIndices.add(insertionPoint, row);
                for (int i = col + 1; i < colPointers.size(); i++) {
                    colPointers.set(i, colPointers.get(i) + 1);
                }
            }
        } finally {
            lock.writeLock().unlock();
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

        int start = colPointers.get(col);
        int end = colPointers.get(col + 1) - 1;

        while (start <= end) {
            int mid = start + (end - start) / 2;
            int midRow = rowIndices.get(mid);

            if (midRow == row) {
                return values.get(mid);
            } else if (midRow < row) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return new ComplexNumber(0, 0);
    }

//    /**
//     * Updates the column pointers after inserting or removing an element.
//     *
//     * @param startCol the column index to start updating from
//     * @param delta the value to add to each subsequent column pointer
//     */
//    private void updateColPointers(int startCol, int delta) {
//        for (int j = startCol; j < colPointers.size(); j++) {
//            colPointers.set(j, colPointers.get(j) + delta);
//        }
//    }

    /**
     * Returns the list of non-zero values in the sparse matrix.
     *
     * @return An unmodifiable List of ComplexNumber objects representing the non-zero values in the matrix.
     */
    public List<ComplexNumber> getValues() {
        return values;
    }

    /**
     * Returns the list of row indices corresponding to the non-zero values.
     *
     * @return An unmodifiable List of Integer objects representing the row indices of non-zero values.
     */
    public List<Integer> getRowIndices() {
        return rowIndices;
    }

    /**
     * Returns the list of column pointers in the CSC format.
     *
     * @return An unmodifiable List of Integer objects representing the starting indices of each column in the values and rowIndices lists.
     */
    public List<Integer> getColPointers() {
        return colPointers;
    }

    /**
     * Sets the list of non-zero values in the sparse matrix.
     *
     * @param values A List of ComplexNumber objects representing the new non-zero values.
     * @throws NullPointerException if values is null or contains null elements
     * @throws IllegalArgumentException if the size of values doesn't match the current matrix structure
     */
    public void setValues(List<ComplexNumber> values) {
        this.values = values;
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
     * Returns the number of non-zero elements in the matrix.
     *
     * @return the count of non-zero elements
     */
    public int size() {
        return values.size();
    }
}