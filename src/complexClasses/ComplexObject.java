package complexClasses;

/**
 * ComplexObject serves as the parent class for other complex classes, providing a single location for linear algebra
 * functions to be collected and giving universal access to those methods on each complex class.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 29 June 2024
 */
public class ComplexObject {
    private static final boolean DEBUG = false;

    /**
     * Computes the tensor product of the control and target matrices using the provided matrices.
     *
     * @param firstMatrix  The first matrix.
     * @param secondMatrix The second matrix.
     * @return A new {@code ComplexMatrix} object that is the result of the tensor product.
     */
    public ComplexMatrix tensorMultiply(ComplexMatrix firstMatrix, ComplexMatrix secondMatrix) {
        int firstHeight = firstMatrix.getHeight();
        int firstWidth = firstMatrix.getWidth();
        int secondHeight = secondMatrix.getHeight();
        int secondWidth = secondMatrix.getWidth();

        ComplexMatrix result = new ComplexMatrix((firstHeight * secondHeight), (firstWidth * secondWidth));
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
    public ComplexMatrix multiplyMatrix(ComplexMatrix matrixOne, ComplexMatrix matrixTwo) {
        if (matrixOne.getWidth() != matrixTwo.getHeight()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication: "
                    + matrixOne.getHeight() + "x" + matrixOne.getWidth()
                    + " cannot be multiplied with " + matrixTwo.getHeight()
                    + "x" + matrixTwo.getWidth());
        }
        ComplexNumber[][] result = new ComplexNumber[matrixOne.getHeight()][matrixTwo.getWidth()];
        for (int i = 0; i < matrixOne.getHeight(); i++) {
            for (int j = 0; j < matrixTwo.getWidth(); j++) {
                result[i][j] = new ComplexNumber();
                for (int k = 0; k < matrixOne.getWidth(); k++) {
                    result[i][j] = addComplexNumbers(result[i][j], multiplyComplexNumbers(matrixOne.get(i, k), matrixTwo.get(k, j)));
                }
            }
        }
        return new ComplexMatrix(result);
    }

    /**
     * Adds two matrices and returns the result as a new matrix.
     *
     * @param matrixOne The first matrix.
     * @param matrixTwo The second matrix.
     * @return A new {@code ComplexMatrix} object that is the result of the matrix addition.
     * @throws IllegalArgumentException If the matrices have different dimensions.
     */
    public ComplexMatrix addMatrix(ComplexMatrix matrixOne, ComplexMatrix matrixTwo) {
        if (matrixOne.getHeight() != matrixTwo.getHeight() || matrixOne.getWidth() != matrixTwo.getWidth()) {
            throw new IllegalArgumentException("Matrix dimensions must be the same for addition: "
                    + matrixOne.getHeight() + "x" + matrixOne.getWidth()
                    + " cannot be added with " + matrixTwo.getHeight()
                    + "x" + matrixTwo.getWidth());
        }
        ComplexNumber[][] result = new ComplexNumber[matrixOne.getHeight()][matrixOne.getWidth()];
        for (int i = 0; i < matrixOne.getHeight(); i++) {
            for (int j = 0; j < matrixOne.getWidth(); j++) {
                result[i][j] = addComplexNumbers(matrixOne.get(i, j), matrixTwo.get(i, j));
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
    private ComplexNumber multiplyComplexNumbers(ComplexNumber aVec, ComplexNumber bVec) {
        double real = aVec.getReal() * bVec.getReal() - aVec.getImag() * bVec.getImag();
        double imag = aVec.getReal() * bVec.getImag() + aVec.getImag() * bVec.getReal();
        if (DEBUG) {
            System.out.println("Real result: " + real);
        }
        if (DEBUG) {
            System.out.println("Imag result: " + imag);
        }
        return new ComplexNumber(real, imag);
    }

    /**
     * Adds two complex numbers.
     *
     * @param aVec The first complex number.
     * @param bVec The second complex number.
     * @return The complex number result of the addition.
     */
    private ComplexNumber addComplexNumbers(ComplexNumber aVec, ComplexNumber bVec) {
        double real = aVec.getReal() + bVec.getReal();
        double imag = aVec.getImag() + bVec.getImag();
        return new ComplexNumber(real, imag);
    }

    /**
     * Calculates the tensor product of two input qubits to create the state vector used for two-qubit gate application.
     *
     * @param control The control qubit.
     * @param target  The target qubit.
     * @return A {@code ComplexNumber[]} with length 4, containing the state vector resultant from the tensor multiplication.
     */
    public ComplexMatrix deriveStateVector(ComplexMatrix control, ComplexMatrix target) {
        ComplexMatrix stateVector = new ComplexMatrix(4, 1);
        stateVector.set(0, 0, multiplyComplexNumbers(control.get(0, 0), target.get(0, 0)));
        stateVector.set(1, 0, multiplyComplexNumbers(control.get(0, 0), target.get(1, 0)));
        stateVector.set(2, 0, multiplyComplexNumbers(control.get(1, 0), target.get(0, 0)));
        stateVector.set(3, 0, multiplyComplexNumbers(control.get(1, 0), target.get(1, 0)));
        if (DEBUG) {
            System.out.println("StateVector is: \n"+stateVector);
        }
        return stateVector;
    }

    /**
     * Multiplies a vector against it's transpose and returns a matrix that is 2*vector.getHeight() wide and tall
     *
     * @param vector The input vector, usually a qubit state
     * @return
     */
    public ComplexMatrix dotProduct(ComplexMatrix vector) {
        ComplexMatrix transpose = vector.getTranspose(vector);
        ComplexMatrix dotProduct = new ComplexMatrix(vector.getHeight(), vector.getHeight());
        for (int i = 0; i < vector.getHeight(); i++) {
            ComplexNumber sample = new ComplexNumber(vector.get(i, 0).getReal(), vector.get(i, 0).getImag());
            for (int j = 0; j < vector.getHeight(); j++) {
                dotProduct.set(i, j, multiplyComplexNumbers(sample, transpose.get(0, j)));
            }
        }
        return dotProduct;
    }

    /**
     * Transposes a given matrix.
     *
     * @param originMatrix The matrix to transpose.
     * @return A new {@code ComplexMatrix} that is the transpose of the original matrix.
     */
    public ComplexMatrix getTranspose(ComplexMatrix originMatrix) {
        int height = originMatrix.getHeight();
        int width = originMatrix.getWidth();
        ComplexMatrix resultMatrix = new ComplexMatrix(0, 0);
        if (height == width) {
            resultMatrix = new ComplexMatrix(height, width);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    resultMatrix.set(i, j, originMatrix.get(j, i));
                }
            }
        } else {
            resultMatrix = new ComplexMatrix(width, height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    resultMatrix.set(i, j, originMatrix.get(j, i));
                }
            }
        }
        return resultMatrix;
    }

    /**
     * Computes the conjugate transpose (Hermitian transpose) of a given matrix.
     *
     * @param originMatrix The matrix to compute the conjugate transpose of.
     * @return A new {@code ComplexMatrix} that is the conjugate transpose of the original matrix.
     */
    public ComplexMatrix getConjugateTranspose(ComplexMatrix originMatrix) {
        int height = originMatrix.getHeight();
        int width = originMatrix.getWidth();
        ComplexMatrix resultMatrix = new ComplexMatrix(0, 0);
        if (height == width) {
            resultMatrix = new ComplexMatrix(height, width);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    resultMatrix.set(i, j, conjugate(originMatrix.get(j, i)));
                }
            }
        } else {
            resultMatrix = new ComplexMatrix(width, height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    resultMatrix.set(i, j, conjugate(originMatrix.get(j, i)));
                }
            }
        }
        return resultMatrix;
    }

    /**
     * Computes the conjugate of a complex number.
     *
     * @param sampleNumber The complex number to conjugate.
     * @return The conjugated complex number.
     */
    private ComplexNumber conjugate(ComplexNumber sampleNumber) {
        return new ComplexNumber(sampleNumber.getReal(), -sampleNumber.getImag());
    }
}
