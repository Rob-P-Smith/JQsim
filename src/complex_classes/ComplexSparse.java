package complex_classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a sparse matrix of complex numbers and supports basic matrix operations.
 *
 * <p>
 * A {@code ComplexSparse} object can be constructed using its dimensions or from an existing
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
 * @since 03 August 2024
 */
public final class ComplexSparse {
    private Map<Integer, Map<Integer, ComplexNumber>> rowsMap;
    private Set<Integer> rowsMapKeySet;
    private int rows;
    private int cols;

    /**
     * Constructs an empty sparse matrix.
     */
    public ComplexSparse() {
        rowsMap = new HashMap<>();
        rows = 0;
        cols = 0;
    }

    /**
     * Constructs a sparse matrix to serve as the system state vector. It places a 1 in the 0 index to
     * represent a freshly initialized state vector with a |0> value and an empty column map in each other
     * row to facilitate working with setter methods.
     *
     * @param numQubits the number of qubits to represent in the state vector.
     */
    public ComplexSparse(int numQubits) {
        rowsMap = new HashMap<>();
        rows = numQubits;
        cols = 1;
        Map<Integer, ComplexNumber> aMap = new HashMap<>();
        aMap.put(0, new ComplexNumber(1.0, 0.0));
        rowsMap.put(0, aMap);

        for (int i = 1; i < numQubits; i++) {
            Map<Integer, ComplexNumber> aRowMap = new HashMap<>();
            rowsMap.put(i, aRowMap);
        }
    }

    /**
     * Constructs a sparse matrix for use as a standard matrix of ComplexNumbers
     *
     * @param height the height of the matrix
     * @param width  the width of the matrix
     */
    public ComplexSparse(int height, int width) {
        rowsMap = new HashMap<>();
        rows = height;
        cols = width;
        for (int i = 0; i < height; i++) {
            Map<Integer, ComplexNumber> aRowMap = new HashMap<>();
            rowsMap.put(i, aRowMap);
        }
    }

    public ComplexSparse(ComplexNumber[][] matrix) {
        rowsMap = new HashMap<>();
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            Map<Integer, ComplexNumber> aRowMap = new HashMap<>();
            rowsMap.put(i, aRowMap);
            for (int j = 0; j < cols; j++) {
                rowsMap.get(i).put(j, new ComplexNumber(matrix[i][j].getReal(), matrix[i][j].getImag()));
            }
        }
    }

    /**
     * Returns a string representation of the sparse matrix.
     *
     * @return a string representation of the matrix
     */
    @Override
    public String toString() {
        Set<Integer> rowMapKeys = rowsMap.keySet();
        String result = "RowMap" + "(" + rows + "x" + cols + ")" + "={\n";
        for (Integer key : rowMapKeys) {
            result += "Row " + key + " = " + rowsMap.get(key) + "\n";
        }
        result += "}";
        return result;
    }

    /**
     * Print the entire matrix, including zero values.
     *
     * @return the string of the entire matrix
     */
    public String printMatrix() {
        String result = "Matrix\n";
        for (int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++) {
            result += "[";
            for (int colIdx = 0; colIdx < this.getWidth(); colIdx++) {
                Map<Integer, ComplexNumber> currentRow = rowsMap.get(rowIdx);
                if (!currentRow.containsKey(colIdx)) {
                    result += 0.0;
                } else {
                    result += currentRow.get(colIdx);
                }
                if (colIdx != this.getWidth() - 1) {
                    result += ", ";
                }
            }
            result += "]\n";
        }
        return result;
    }

    /**
     * Returns the size of the sparse matrix it represents the number of rows the map contains as
     * Map<Integer, ComplexNumber>>.
     *
     * @return the size of the matrix
     */
    private int size() {
        int size = 0;
        for (Integer row : rowsMap.keySet()) {
            size += rowsMap.get(row).size();
        }
        return size;
    }

    /**
     * returns the number of rows in the sparse matrix Map
     *
     * @return the integer value of how many rows are present
     */
    public int getHeight() {
        return rows;
    }

    /**
     * returns the number of columns in the sparse matrix Map
     *
     * @return the integer value of how many columns are present
     */
    public int getWidth() {
        return cols;
    }

    /**
     * Support method that cleans up the 0.0 values in the matrix to reduce memory use.
     */
    private void condenseSparse() {
        rowsMapKeySet = rowsMap.keySet();
        for (Integer rowKey : rowsMapKeySet) {
            Set<Integer> columnKeys = rowsMap.get(rowKey).keySet();
            for (Integer columnKey : columnKeys) {
                if (rowsMap.get(rowKey).get(columnKey).getReal() == 0.0 &&
                        rowsMap.get(rowKey).get(columnKey).getImag() == 0.0) {
                    rowsMap.get(rowKey).remove(columnKey);
                }
            }
        }
    }

    /**
     * Checks if location in matrix sparse storage is empty/null or if a value is present.
     *
     * @param rowMapKey    row index to look up.
     * @param columnMapKey column index to look up.
     * @return true if value found, otherwise false.
     */
    private boolean entryNotEmpty(int rowMapKey, int columnMapKey) {
        return rowsMap.containsKey(rowMapKey) && rowsMap.get(rowMapKey).containsKey(columnMapKey);
    }

    /**
     * Removes the value at the location specified.
     *
     * @param row    row index
     * @param column column index
     */
    public void remove(int row, int column) {
        if (rowsMapKeySet.contains(row) && rowsMap.get(row).containsKey(column)) {
            rowsMap.get(row).remove(column);
        }
        this.rowsMapKeySet = rowsMap.keySet();
    }

    /**
     * Puts a row map into the rowsMap Map.
     *
     * @param rowMap   the column map to be put
     * @param rowIndex the row to place the map
     */
    public void putRow(Map<Integer, ComplexNumber> rowMap, int rowIndex) {
        if (!rowsMap.containsKey(rowIndex)) {
            rowsMap.put(rowIndex, rowMap);
        } else {
            System.out.println("Row index not empty.");
        }
        this.rowsMapKeySet = rowsMap.keySet();
    }

    /**
     * Inserts the ComplexNumber in the row/column location in the sparse storage structure.
     *
     * @param row    the row
     * @param column the column
     * @param real   the real portion of the complex number
     * @param imag   the imaginary portion of the complex number
     */
    public void put(int row, int column, double real, double imag) {
        if (rowsMap.containsKey(row)) {
            if (real == 0.0 && imag == 0.0) {
                rowsMap.get(row).remove(column);
                return;
            }
            rowsMap.get(row).put(column, new ComplexNumber(real, imag));
        } else {
            Map<Integer, ComplexNumber> thisRow = new HashMap<>();
            thisRow.put(column, new ComplexNumber(real, imag));
            rowsMap.put(row, thisRow);
        }
//        condenseSparse();
        this.rowsMapKeySet = rowsMap.keySet();
    }

    /**
     * Inserts the ComplexNumber in the row/column location in the sparse storage structure.
     *
     * @param row    the row
     * @param column the column
     * @param value  the value to store
     */
    public void put(int row, int column, ComplexNumber value) {
        if (rowsMap.containsKey(row)) {
            if (value.getReal() == 0.0 && value.getImag() == 0.0) {
                rowsMap.get(row).remove(column);
                return;
            }
            rowsMap.get(row).put(column, new ComplexNumber(value.getReal(), value.getImag()));
        } else {
            Map<Integer, ComplexNumber> thisRow = new HashMap<>();
            thisRow.put(column, new ComplexNumber(value.getReal(), value.getImag()));
            rowsMap.put(row, thisRow);
        }
//        condenseSparse();
        this.rowsMapKeySet = rowsMap.keySet();
    }

    public void setData(ComplexSparse data) {
        this.rowsMap = data.getSparseMatrix();
    }

//    /**
//     * Sets the value at the specified position in the matrix.
//     *
//     * @param row    the row index
//     * @param column the column index
//     * @param real   the real part of the complex number
//     * @param imag   the imaginary part of the complex number
//     */
//    public void set(int row, int column, double real, double imag) {
//        if(real == 0.0 && imag == 0.0){
//            return;
//        }
//        if (rowsMap.containsKey(row)) {
//            rowsMap.get(row).put(column, new ComplexNumber(real, imag));
//        } else {
//            Map<Integer, ComplexNumber> thisRow = new HashMap<>();
//            thisRow.put(column, new ComplexNumber(real, imag));
//            rowsMap.put(row, thisRow);
//        }
//        this.rowsMapKeySet = rowsMap.keySet();
//    }
//
//    /**
//     * Sets the value at the specified position in the matrix.
//     *
//     * @param row    the row index
//     * @param column the column index
//     * @param value  the ComplexNumber to put in the row/column location
//     */
//    public void set(int row, int column, ComplexNumber value) {
//        if(value.getReal() == 0.0 && value.getImag() == 0.0){
//            return;
//        }
//        if (rowsMap.containsKey(row)) {
//            rowsMap.get(row).put(column, new ComplexNumber(value.getReal(), value.getImag()));
//        } else {
//            Map<Integer, ComplexNumber> thisRow = new HashMap<>();
//            thisRow.put(column, new ComplexNumber(value.getReal(), value.getImag()));
//            rowsMap.put(row, thisRow);
//        }
//        this.rowsMapKeySet = rowsMap.keySet();
//    }

    /**
     * Returns the row map of maps representing the sparse matrix.
     *
     * @return the row map
     */
    public Map<Integer, Map<Integer, ComplexNumber>> getSparseMatrix() {
        return rowsMap;
    }

    /**
     * Gets a row
     *
     * @param row the row to look up
     * @return the map representing the row
     */
    public Map<Integer, ComplexNumber> getRow(int row) {
        return rowsMap.get(row);
    }

    /**
     * Retrieves the ComplexNumber located at (row, column) of the matrix.
     *
     * @param rowMapKey    the row index
     * @param columnMapKey the column index
     * @return the ComplexNumber or null;
     */
    public ComplexNumber get(int rowMapKey, int columnMapKey) {
        if (!entryNotEmpty(rowMapKey, columnMapKey)) {
            return new ComplexNumber(0.0, 0.0);
        } else {
            return rowsMap.get(rowMapKey).get(columnMapKey);
        }
    }

    public static void main(String[] args) {
        ComplexSparse ns = new ComplexSparse(2);
        ns.put(1, 0, 1.0, 0.0);
        ns.remove(0, 0);
        System.out.println("Starting Matrix: \n" + ns);

        ComplexSparse exx = new ComplexSparse(2, 2);
        exx.put(0, 1, 1.0, 0.0);
        exx.put(1, 0, 1.0, 0.0);

        ComplexSparse eyd = new ComplexSparse(2, 2);
        eyd.put(0, 0, 1.0, 0.0);
        eyd.put(1, 1, 1.0, 0.0);

        ComplexSparse tensorTest = ComplexMath.multiplyMatrix(exx, eyd);
        System.out.println(tensorTest);
        System.out.println(tensorTest.printMatrix());
    }
}