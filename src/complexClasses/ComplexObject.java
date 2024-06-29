package complexClasses;

import java.util.Arrays;

/**
 * ComplexObject serves as the parent class for other complex classes, providing a single location for linear algebra
 * functions to be collected and giving universal across to those methods on each complex class.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 29 June 2024
 */
public class ComplexObject {
    private static final boolean DEBUG = false;

    /**
     * Computes the tensor product of this matrix with a state vector.
     *
     * @param control The matrix to tensor multiply with this matrix.
     * @return A new {@code ComplexMatrix} object that is the result of the tensor product.
     */
    public ComplexMatrix tensorMultiply(ComplexMatrix control, ComplexMatrix target) {
        ComplexMatrix CNOT = ComplexGates.CNOT;
        //get input state vector from input
        ComplexNumber[] stateVector = deriveStateVector(control, target);
        ComplexNumber[] resultVector = new ComplexNumber[4];
        ComplexNumber[] collector = new ComplexNumber[4];

        //multiply CNOT matrix against state vector to get resulting state vector
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                collector[j] = multiplyComplex(CNOT.get(i, j), stateVector[j]);
            }
            resultVector[i] = addComplex(
                    addComplex(collector[0], collector[1]),
                    addComplex(collector[2], collector[3])
            );
        }

        if (DEBUG) {
            System.out.println("ResultVector is: ");
            for (ComplexNumber num : resultVector) {
                System.out.println(num);
            }
        }

        // Convert result vector to a column matrix
        ComplexNumber[][] resultMatrix = new ComplexNumber[2][1];
        if (Math.abs(resultVector[1].getReal()) == 1.0 ||
                Math.abs(resultVector[3].getReal()) == 1.0 ||
                Math.abs(resultVector[1].getImag()) == 1.0 ||
                Math.abs(resultVector[3].getImag()) == 1.0) {
            resultMatrix[0][0] = new ComplexNumber(0, 0);
            resultMatrix[1][0] = new ComplexNumber(1, 0);
        } else {
            resultMatrix[0][0] = new ComplexNumber(1, 0);
            resultMatrix[1][0] = new ComplexNumber(0, 0);
        }

        System.out.println(Arrays.deepToString(resultMatrix));
        return new ComplexMatrix(resultMatrix);

    }

    /**
     * Multiplies this matrix with another matrix and returns the result as a new matrix.
     *
     * @param matrixOne The matrix to multiply with matrixTwo.
     * @param matrixTwo The matrix to multiply with matrixOne.
     * @return A new {@code ComplexMatrix} object that is the result of the matrix multiplication.
     * @throws IllegalArgumentException If the matrices have incompatible dimensions for multiplication.
     */
    public ComplexMatrix multiply(ComplexMatrix matrixOne, ComplexMatrix matrixTwo) {
        if (matrixOne.getWidth() != matrixTwo.getHeight()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication: "
                    + matrixOne.getHeight() + "x" + matrixOne.getWidth()
                    + " cannot be multiplied with " + matrixTwo.getHeight()
                    + "x" + matrixTwo.getWidth());
        }
        ComplexNumber[][] result = new ComplexNumber[matrixOne.getHeight()][matrixTwo.getWidth()];
        for (int i = 0; i < matrixOne.getHeight(); i++) {
            for (int j = 0; j < matrixTwo.getWidth(); j++) {
                result[i][j] = new ComplexNumber(); // Initialize the result matrix cell
                for (int k = 0; k < matrixOne.getWidth(); k++) {
                    result[i][j] = addComplex(result[i][j], multiplyComplex(matrixOne.get(i,k), matrixTwo.get(k,j)));
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
        if (DEBUG) {
            System.out.println("Real result: " + real);
        }
        if (DEBUG) {
            System.out.println("Imag result: " + imag);
        }
        return new ComplexNumber(real, imag);
    }

    /**
     * Adds another matrix to this matrix and returns the result as a new matrix.
     *
     * @param matrixOne THe matrix to add with matrixTwo
     * @param matrixTwo The matrix to add to matrixOne.
     * @return A new {@code ComplexMatrix} object that is the result of the matrix addition.
     * @throws IllegalArgumentException If the matrices have different dimensions.
     */
    public ComplexMatrix add(ComplexMatrix matrixOne, ComplexMatrix matrixTwo) {
        if (matrixOne.getHeight() != matrixTwo.getHeight() || matrixOne.getWidth() != matrixTwo.getWidth()) {
            throw new IllegalArgumentException("Matrix dimensions must be the same for addition: "
                    + matrixOne.getHeight() + "x" + matrixOne.getWidth()
                    + " cannot be added with " + matrixTwo.getHeight()
                    + "x" + matrixTwo.getWidth());
        }
        ComplexNumber[][] result = new ComplexNumber[matrixOne.getHeight()][matrixOne.getWidth()];
        for (int i = 0; i < matrixOne.getHeight(); i++) {
            for (int j = 0; j < matrixOne.getWidth(); j++) {
                result[i][j] = addComplex(matrixOne.get(i,j), matrixTwo.get(i,j));
            }
        }
        return new ComplexMatrix(result);
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
     * Calculates the tensor product of two input qubits to create the state vector used for two qubit gate application.
     * @param control the control qubit
     * @param target the target qubit
     * @return a ComplexNumber[] with length 4, containing the stateVector resultant from the tensor multiplication
     */
    public ComplexNumber[] deriveStateVector(ComplexMatrix control, ComplexMatrix target) {
        ComplexNumber[] stateVector = new ComplexNumber[4];
        stateVector[0] = multiplyComplex(control.get(0, 0), target.get(0, 0));
        stateVector[1] = multiplyComplex(control.get(0, 0), target.get(1, 0));
        stateVector[2] = multiplyComplex(control.get(1, 0), target.get(0, 0));
        stateVector[3] = multiplyComplex(control.get(1, 0), target.get(1, 0));
        System.out.println("StateVector is: ");
        for (int i = 0; i < 4; i++) {
            System.out.println(stateVector[i]);
        }
        return stateVector;

    }

    public ComplexMatrix getTranspose(ComplexMatrix originMatrix){
        int height = originMatrix.getHeight();
        int width = originMatrix.getWidth();
        ComplexMatrix resultMatrix = new ComplexMatrix(0, 0);
        if (height == width) {
            resultMatrix = new ComplexMatrix(height, width);
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    resultMatrix.set(i, j, originMatrix.get(j,i));
                }
            }
        } else {
            resultMatrix = new ComplexMatrix(width, height);
            for(int i =0; i < width; i++){
                for(int j = 0; j < height; j++){
                    resultMatrix.set(i,j,originMatrix.get(j,i));
                }
            }
        }
        return resultMatrix;
    }

    public ComplexMatrix getConjugateTranspose(ComplexMatrix originMatrix){
        ComplexMatrix resultMatrix = new ComplexMatrix(originMatrix.getHeight(), originMatrix.getWidth());
        for(int i = 0; i < originMatrix.getHeight(); i++){
            for(int j = 0; j < originMatrix.getWidth(); j++){
                resultMatrix.set(i, j, conjugate(originMatrix.get(j,i)));
            }
        }
        return resultMatrix;
    }

    private ComplexNumber conjugate(ComplexNumber sampleNumber) {
        return new ComplexNumber(sampleNumber.getReal(), -sampleNumber.getImag());
    }
}
