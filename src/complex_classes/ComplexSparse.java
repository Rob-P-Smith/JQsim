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

    /**
     * Returns a string representation of the sparse matrix.
     *
     * @return a string representation of the matrix
     */
    @Override
    public String toString() {
        Set<Integer> rowMapKeys = rowsMap.keySet();
        String result = "RowMap={\n";
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
        for(int rowIdx = 0; rowIdx < this.getHeight(); rowIdx++){
            result += "[";
            for(int colIdx = 0; colIdx < this.getWidth(); colIdx++){
                Map<Integer, ComplexNumber> currentRow = rowsMap.get(rowIdx);
                if(!currentRow.containsKey(colIdx)){
                    result += 0.0;
                } else {
                    result += currentRow.get(colIdx);
                }
                if(colIdx != this.getWidth()-1){
                    result += ", ";
                }
            }
            result +="]\n";
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
        if (rowsMapKeySet.contains(row) && rowsMap.get(row).containsKey(column)){
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
            rowsMap.get(row).put(column, new ComplexNumber(real, imag));
        } else {
            Map<Integer, ComplexNumber> thisRow = new HashMap<>();
            thisRow.put(column, new ComplexNumber(real, imag));
            rowsMap.put(row, thisRow);
        }
        condenseSparse();
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
            rowsMap.get(row).put(column, new ComplexNumber(value.getReal(), value.getImag()));
        } else {
            Map<Integer, ComplexNumber> thisRow = new HashMap<>();
            thisRow.put(column, new ComplexNumber(value.getReal(), value.getImag()));
            rowsMap.put(row, thisRow);
        }
        condenseSparse();
        this.rowsMapKeySet = rowsMap.keySet();
    }

    public void setData(ComplexSparse data){
        this.rowsMap = data.getSparseMatrix();
    }

    /**
     * Sets the value at the specified position in the matrix.
     *
     * @param row    the row index
     * @param column the column index
     * @param real   the real part of the complex number
     * @param imag   the imaginary part of the complex number
     */
    public void set(int row, int column, double real, double imag) {
        if (rowsMap.containsKey(row)) {
            rowsMap.get(row).put(column, new ComplexNumber(real, imag));
        } else {
            Map<Integer, ComplexNumber> thisRow = new HashMap<>();
            thisRow.put(column, new ComplexNumber(real, imag));
            rowsMap.put(row, thisRow);
        }
        condenseSparse();
        this.rowsMapKeySet = rowsMap.keySet();
    }

    /**
     * Sets the value at the specified position in the matrix.
     *
     * @param row    the row index
     * @param column the column index
     * @param value  the ComplexNumber to put in the row/column location
     */
    public void set(int row, int column, ComplexNumber value) {
        if (rowsMap.containsKey(row)) {
            rowsMap.get(row).put(column, new ComplexNumber(value.getReal(), value.getImag()));
        } else {
            Map<Integer, ComplexNumber> thisRow = new HashMap<>();
            thisRow.put(column, new ComplexNumber(value.getReal(), value.getImag()));
            rowsMap.put(row, thisRow);
        }
        condenseSparse();
        this.rowsMapKeySet = rowsMap.keySet();
    }

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
        System.out.println("Starting Matrix: \n"+ns);

        ComplexSparse exx = new ComplexSparse(2, 2);
        exx.put(0, 1, 1.0, 0.0);
        exx.put(1, 0, 1.0, 0.0);

        ComplexSparse eyd = new ComplexSparse(2, 2);
        eyd.put(0, 0, 1.0, 0.0);
        eyd.put(1, 1, 1.0, 0.0);

        ComplexSparse tensorTest = ComplexSparse.ComplexMath.multiplyMatrix(exx, eyd);
        System.out.println(tensorTest);
        System.out.println(tensorTest.printMatrix());
    }

    private final class ComplexMath {
        private static final double PRECISION_LIMIT = 1e-10;
        
        /**
         * Computes the tensor product of the control and target matrices using the provided matrices.
         *
         * @param firstMatrix  The first matrix.
         * @param secondMatrix The second matrix.
         * @return A new {@code ComplexMatrix} object that is the result of the tensor product.
         */
        public static ComplexSparse tensorMultiply(ComplexSparse firstMatrix, ComplexSparse secondMatrix) {
            int firstHeight = firstMatrix.getHeight();
            int firstWidth = firstMatrix.getWidth();
            int secondHeight = secondMatrix.getHeight();
            int secondWidth = secondMatrix.getWidth();

            ComplexSparse result = new ComplexSparse((firstHeight * secondHeight), (firstWidth * secondWidth));
            for (int i = 0; i < firstHeight; i++) {
                for (int j = 0; j < firstWidth; j++) {
                    for (int k = 0; k < secondHeight; k++) {
                        for (int l = 0; l < secondWidth; l++) {
                            result.set((i * secondHeight + k), (j * secondWidth + l),
                                    multiplyComplexNumbers(firstMatrix.get(i, j), secondMatrix.get(k, l)));
                        }
                    }
                }
            }
            return result;
        }

        /**
         * Multiplies two matrices and returns the result as a new matrix.
         *
         * @param matrixOne The first matrix.
         * @param matrixTwo The second matrix.
         * @return A new {@code ComplexMatrix} object that is the result of the matrix multiplication.
         * @throws IllegalArgumentException If the matrices have incompatible dimensions for multiplication.
         */
        public static ComplexSparse multiplyMatrix(ComplexMatrix matrixOne, ComplexSparse matrixTwo) {
            if (matrixOne.getWidth() != matrixTwo.getHeight()) {
                throw new IllegalArgumentException("Matrix dimensions do not match for multiplication");
            }
            ComplexSparse product = new ComplexSparse(matrixOne.getHeight(), matrixTwo.getWidth());
            for (int i = 0; i < matrixOne.getHeight(); i++) {
                for (int j = 0; j < matrixTwo.getWidth(); j++) {
                    for (int k = 0; k < matrixOne.getWidth(); k++) {
                        product.put(i, j, addComplexNumbers(product.get(i, j), multiplyComplexNumbers(
                                matrixOne.get(i, k), matrixTwo.get(k, j))));
                    }
                }
            }
            return product;
        }

        /**
         * Multiplies two matrices and returns the result as a new matrix.
         *
         * @param matrixOne The first matrix.
         * @param matrixTwo The second matrix.
         * @return A new {@code ComplexMatrix} object that is the result of the matrix multiplication.
         * @throws IllegalArgumentException If the matrices have incompatible dimensions for multiplication.
         */
        public static ComplexSparse multiplyMatrix(ComplexSparse matrixOne, ComplexSparse matrixTwo) {
            if (matrixOne.getWidth() != matrixTwo.getHeight()) {
                throw new IllegalArgumentException("Matrix dimensions do not match for multiplication");
            }
            ComplexSparse product = new ComplexSparse(matrixOne.getHeight(), matrixTwo.getWidth());
            for (int i = 0; i < matrixOne.getHeight(); i++) {
                for (int j = 0; j < matrixTwo.getWidth(); j++) {
                    for (int k = 0; k < matrixOne.getWidth(); k++) {
                        product.put(i, j, addComplexNumbers(product.get(i, j), multiplyComplexNumbers(
                                matrixOne.get(i, k), matrixTwo.get(k, j))));
                    }
                }
            }
            return product;
        }

        /**
         * Adds two matrices and returns the result as a new matrix.
         *
         * @param matrixOne The first matrix.
         * @param matrixTwo The second matrix.
         * @return A new {@code ComplexMatrix} object that is the result of the matrix addition.
         * @throws IllegalArgumentException If the matrices have different dimensions.
         */
        public static ComplexSparse addMatrix(ComplexSparse matrixOne, ComplexSparse matrixTwo) {
            if (matrixOne.getHeight() != matrixTwo.getHeight() || matrixOne.getWidth() != matrixTwo.getWidth()) {
                throw new IllegalArgumentException("Matrix dimensions must be the same for addition: "
                        + matrixOne.getHeight() + "x" + matrixOne.getWidth()
                        + " cannot be added with " + matrixTwo.getHeight()
                        + "x" + matrixTwo.getWidth());
            }
            ComplexSparse result = new ComplexSparse(matrixOne.getHeight(), matrixOne.getWidth());
            for (int i = 0; i < matrixOne.getHeight(); i++) {
                for (int j = 0; j < matrixOne.getWidth(); j++) {
                    result.put(i,j, addComplexNumbers(matrixOne.get(i,j), matrixTwo.get(i,j)));
                }
            }
            return result;
        }

        /**
         * Multiplies two complex numbers.
         * If the result came back as 0.9999999999999998 it is changed to 1.0
         *
         * @param aVec The first complex number.
         * @param bVec The second complex number.
         * @return The complex number result of the multiplication.
         */
        private static ComplexNumber multiplyComplexNumbers(ComplexNumber aVec, ComplexNumber bVec) {
            double real = testResultForFloatErrorBuildup(aVec.getReal() * bVec.getReal() - aVec.getImag() * bVec.getImag());
            double imag = testResultForFloatErrorBuildup(aVec.getReal() * bVec.getImag() + aVec.getImag() * bVec.getReal());
            return new ComplexNumber(real, imag);
        }

        private static double testResultForFloatErrorBuildup(double number) {
            if (number > 0.9999999 && number < 1.0) {
                return 1.0;
            } else if (number < -0.9999999 && number > -1.0) {
                return -1.0;
            } else {
                return number;
            }
        }

        /**
         * Adds two complex numbers.
         *
         * @param aVec The first complex number.
         * @param bVec The second complex number.
         * @return The complex number result of the addition.
         */
        public static ComplexNumber addComplexNumbers(ComplexNumber aVec, ComplexNumber bVec) {
            double real = testResultForFloatErrorBuildup(aVec.getReal() + bVec.getReal());
            double imag = testResultForFloatErrorBuildup(aVec.getImag() + bVec.getImag());
            return new ComplexNumber(real, imag);
        }

        private static String complexToString(ComplexNumber number) {
            if (Math.abs(number.getImag()) < PRECISION_LIMIT) {
                return String.format("%.3f", number.getReal());
            } else if (Math.abs(number.getReal()) < PRECISION_LIMIT) {
                return String.format("%.3fi", number.getImag());
            } else {
                return String.format("(%.3f + %.3fi)", number.getReal(), number.getImag());
            }
        }

        /**
         * Computes the conjugate of a complex number.
         *
         * @param sampleNumber The complex number to conjugate.
         * @return The conjugated complex number.
         */
        private static ComplexNumber conjugate(ComplexNumber sampleNumber) {
            return new ComplexNumber(sampleNumber.getReal(), -sampleNumber.getImag());
        }
    }
}