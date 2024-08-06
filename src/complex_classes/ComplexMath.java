package complex_classes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static supportClasses.GreekEnums.*;

/**
 * ComplexObject serves as the parent class for other complex classes, providing a single location for linear algebra
 * functions to be collected and giving universal access to those methods on each complex class.
 * <br>
 * Multithreaded functionality disabled via if/else statements in the tensor multiply and complex multiply methods
 * because the threaded overhead outweighs the performance gains.
 *
 * @author Robert Smith
 * @version 0.3
 * @since 06 August 2024
 */
public final class ComplexMath {
    private static final double EPSILON = 1e-10;
    /**
     * Threshold for matrix height below which single-threaded execution is used.
     * Matrices with height less than or equal to this value will use single-threaded multiplication.
     */
    private static int SINGLE_THREAD_THRESHOLD = Integer.MAX_VALUE;
    private static int BLOCK_SIZE = 128;
    /**
     * Number of threads to use for parallel execution. Default is virtual cores /2 to limit it to physical core count on machines with SMT on all cores.
     */
//    public static int NUM_THREADS = Runtime.getRuntime().availableProcessors() / 2;
    private static int NUM_THREADS = 4;

    /**
     * Computes the tensor product of two matrices using Gustavson's algorithm for sparse matrices.
     * This method is optimized for sparse matrices and uses parallel computation for large matrices.
     *
     * @param leftMatrix  The first {@link ComplexSparse} matrix.
     * @param rightMatrix The second {@link ComplexSparse} matrix.
     * @return A new {@link ComplexSparse} object that is the result of the tensor product.
     * @see ComplexSparse
     */
    public static ComplexSparse tensorMultiply(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
//        int leftHeight = leftMatrix.getHeight();
//        int rightHeight = rightMatrix.getHeight();

//        if (leftHeight * rightHeight >= (SINGLE_THREAD_THRESHOLD)*999) {
//            return tensorMultiplyParallel(leftMatrix, rightMatrix);
//        } else {
//            return tensorMultiplySequential(leftMatrix, rightMatrix);
//        }
        return tensorMultiplySequential(leftMatrix, rightMatrix);
    }

    /**
     * Performs parallel tensor multiplication of two sparse matrices.
     * This method implements the tensor product algorithm for sparse matrices
     * using parallel execution, suitable for larger matrices where parallelization
     * can provide significant performance benefits.
     *
     * <p>The method divides the computation into blocks and processes them in parallel
     * using multiple threads. This can lead to improved performance on multi-core systems
     * for large matrices.</p>
     *
     * @param leftMatrix  The first {@link ComplexSparse} matrix in the tensor product.
     * @param rightMatrix The second {@link ComplexSparse} matrix in the tensor product.
     * @return A new {@link ComplexSparse} matrix representing the tensor product of A and B.
     * @throws RuntimeException if an error occurs during parallel execution
     * @see ComplexSparse
     * @see #tensorMultiply(ComplexSparse, ComplexSparse)
     * @see #tensorMultiplySequential(ComplexSparse, ComplexSparse)
     */
    private static ComplexSparse tensorMultiplySequential(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        int leftHeight = leftMatrix.getHeight();
        int leftWidth = leftMatrix.getWidth();
        int rightHeight = rightMatrix.getHeight();
        int rightWidth = rightMatrix.getWidth();

        ComplexSparse result = new ComplexSparse(leftHeight * rightHeight, leftWidth * rightWidth);

        for (int i1 = 0; i1 < leftHeight; i1++) {
            for (int j1 = 0; j1 < leftWidth; j1++) {
                ComplexNumber leftNumber = leftMatrix.get(i1, j1);
                if (!isZero(leftNumber)) {
                    for (int i2 = 0; i2 < rightHeight; i2++) {
                        for (int j2 = 0; j2 < rightWidth; j2++) {
                            ComplexNumber rightNumber = rightMatrix.get(i2, j2);
                            if (!isZero(rightNumber)) {
                                int i = i1 * rightHeight + i2;
                                int j = j1 * rightWidth + j2;
                                result.put(i, j, multiplyComplexNumbers(leftNumber, rightNumber));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Performs sequential tensor multiplication of two sparse matrices.
     * This method implements the tensor product algorithm for sparse matrices
     * without parallelization, suitable for smaller matrices or when parallel
     * execution is not beneficial.
     *
     * @param leftMatrix The first {@link ComplexSparse} matrix in the tensor product.
     * @param rightMatrix The second {@link ComplexSparse} matrix in the tensor product.
     * @return A new {@link ComplexSparse} matrix representing the tensor product of A and B.
     * @see ComplexSparse
     * @see #tensorMultiply(ComplexSparse, ComplexSparse)
     * @see #tensorMultiplyParallel(ComplexSparse, ComplexSparse)
     */
    private static ComplexSparse tensorMultiplyParallel(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        int leftHeight = leftMatrix.getHeight();
        int leftWidth = leftMatrix.getWidth();
        int rightHeight = rightMatrix.getHeight();
        int rightWidth = rightMatrix.getWidth();

        ComplexSparse result = new ComplexSparse(leftHeight * rightHeight, leftWidth * rightWidth);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<?>> futures = new ArrayList<>();

        int blockSize = Math.max(1, leftHeight / NUM_THREADS);
        for (int block = 0; block < leftHeight; block += blockSize) {
            final int startRow = block;
            final int endRow = Math.min(startRow + blockSize, leftHeight);

            futures.add(executor.submit(() -> {
                for (int i1 = startRow; i1 < endRow; i1++) {
                    for (int j1 = 0; j1 < leftWidth; j1++) {
                        ComplexNumber leftElement = leftMatrix.get(i1, j1);
                        if (!isZero(leftElement)) {
                            for (int i2 = 0; i2 < rightHeight; i2++) {
                                for (int j2 = 0; j2 < rightWidth; j2++) {
                                    ComplexNumber rightElement = rightMatrix.get(i2, j2);
                                    if (!isZero(rightElement)) {
                                        int i = i1 * rightHeight + i2;
                                        int j = j1 * rightWidth + j2;
                                        result.put(i, j, multiplyComplexNumbers(leftElement, rightElement));
                                    }
                                }
                            }
                        }
                    }
                }
            }));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                System.out.println("Error in parallel Tensor Multiply.");
                throw new RuntimeException("Error in parallel tensor multiplication", e);
            }
        }

        executor.shutdown();

        return result;
    }

    public static ComplexSparse multiplyMatrix(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        if (leftMatrix.getWidth() != rightMatrix.getHeight()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
        }
        int height = leftMatrix.getHeight();

        if (rightMatrix.getWidth() == 1) {
            return multiplyMatrixVectorSequential(leftMatrix, rightMatrix);
        } else {
            return multiplyMatrixSequential(leftMatrix, rightMatrix);
        }


//        if (rightMatrix.getWidth() == 1 && height >= SINGLE_THREAD_THRESHOLD) {
//            return multiplyMatrixVectorParallel(leftMatrix, rightMatrix);
//        } else if (rightMatrix.getWidth() == 1) {
//            return multiplyMatrixVectorSequential(leftMatrix, rightMatrix);
//        } else if (height >= SINGLE_THREAD_THRESHOLD) {
//            return multiplyMatrixParallel(leftMatrix, rightMatrix);
//        } else {
//            return multiplyMatrixSequential(leftMatrix, rightMatrix);
//        }
    }

    /**
     * Performs sequential multiplication of a sparse matrix with a sparse column vector.
     * This method is optimized for the case where the right matrix is a column vector,
     * which is common in quantum computing operations.
     *
     * @param leftMatrix  The sparse matrix to be multiplied.
     * @param rightMatrix The sparse column vector to be multiplied.
     * @return A new {@code ComplexSparse} object representing the result of the multiplication.
     * @throws IllegalArgumentException If the dimensions of the matrices do not match for multiplication
     *                                  or if the right matrix is not a column vector.
     */
    public static ComplexSparse multiplyMatrixVectorSequential(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        if (leftMatrix.getWidth() != rightMatrix.getHeight() || rightMatrix.getWidth() != 1) {
            throw new IllegalArgumentException("Matrix dimensions do not match for matrix-vector multiplication.");
        }

        int leftHeight = leftMatrix.getHeight();

        ComplexSparse resultMatrix = new ComplexSparse(leftHeight, 1);

        // Temporary dense vector to accumulate results
        ComplexNumber[] dense = new ComplexNumber[leftHeight];
        for (int i = 0; i < leftHeight; i++) {
            dense[i] = new ComplexNumber(0, 0);
        }

        // Array to keep track of which rows in the dense vector are non-zero
        int[] nnzTracker = new int[leftHeight];
        int nnzTrackerLength = 0;

        // For each non-zero element in the column vector (rightMatrix)
        for (int pb = rightMatrix.getColPointers().get(0); pb < rightMatrix.getColPointers().get(1); pb++) {
            int rightRowIndex = rightMatrix.getRowIndices().get(pb);
            ComplexNumber rightValue = rightMatrix.getValues().get(pb);

            // For each non-zero element in column k of leftMatrix
            for (int pa = leftMatrix.getColPointers().get(rightRowIndex); pa < leftMatrix.getColPointers().get(rightRowIndex + 1); pa++) {
                int leftRowIndex = leftMatrix.getRowIndices().get(pa);
                ComplexNumber leftValue = leftMatrix.getValues().get(pa);

                if (isZero(dense[leftRowIndex])) {
                    nnzTracker[nnzTrackerLength++] = leftRowIndex;
                }
                dense[leftRowIndex].addInPlace(ComplexMath.multiplyComplexNumbers(leftValue, rightValue));
            }
        }

        // Store the non-zero results in resultMatrix
        for (int s = 0; s < nnzTrackerLength; s++) {
            int i = nnzTracker[s];
            if (!isZero(dense[i])) {
                resultMatrix.put(i, 0, dense[i]);
            }
        }

        return resultMatrix;
    }

    private static ComplexSparse multiplyMatrixVectorParallel(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        if (leftMatrix.getWidth() != rightMatrix.getHeight() || rightMatrix.getWidth() != 1) {
            throw new IllegalArgumentException("Matrix dimensions do not match for matrix-vector multiplication.");
        }

        int leftHeight = leftMatrix.getHeight();
        int leftWidth = leftMatrix.getWidth();

        ComplexSparse resultMatrix = new ComplexSparse(leftHeight, 1);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<?>> futures = new ArrayList<>();

        int rowsPerThread = Math.max(1, leftHeight / NUM_THREADS);
        for (int threadIdx = 0; threadIdx < NUM_THREADS; threadIdx++) {
            final int startRow = threadIdx * rowsPerThread;
            final int endRow = (threadIdx == NUM_THREADS - 1) ? leftHeight : (startRow + rowsPerThread);

            futures.add(executor.submit(() -> {
                for (int i = startRow; i < endRow; i++) {
                    ComplexNumber sum = new ComplexNumber(0, 0);
                    for (int j = 0; j < leftWidth; j++) {
                        ComplexNumber leftValue = leftMatrix.get(i, j);
                        if (!isZero(leftValue)) {
                            ComplexNumber rightValue = rightMatrix.get(j, 0);
                            sum.addInPlace(multiplyComplexNumbers(leftValue, rightValue));
                        }
                    }
                    if (!isZero(sum)) {
                        synchronized (resultMatrix) {
                            resultMatrix.put(i, 0, sum);
                        }
                    }
                }
            }));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                System.out.println("Error in matrix-vector multiplication parallel.");
                throw new RuntimeException("Error in parallel matrix-vector multiplication", e);
            }
        }

        executor.shutdown();

        return resultMatrix;
    }

    private static ComplexSparse multiplyMatrixSequential(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        if (leftMatrix.getWidth() != rightMatrix.getHeight()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
        }

        int leftHeight = leftMatrix.getHeight();
        int rightWidth = rightMatrix.getWidth();
        ComplexSparse resultMatrix = new ComplexSparse(leftHeight, rightWidth);

        // Temporary dense vector to accumulate results
        ComplexNumber[] dense = new ComplexNumber[leftHeight];
        for (int i = 0; i < leftHeight; i++) {
            dense[i] = new ComplexNumber(0, 0);
        }

        // Array to keep track of which rows in the dense vector are non-zero
        int[] nnzTracker = new int[leftHeight];
        int nnzTrackerIndex = 0;

        // For each column j of B
        for (int j = 0; j < rightWidth; j++) {
            // For each non-zero element in column j of B
            for (int pb = rightMatrix.getColPointers().get(j); pb < rightMatrix.getColPointers().get(j + 1); pb++) {
                int rightRowIndex = rightMatrix.getRowIndices().get(pb);
                ComplexNumber bVal = rightMatrix.getValues().get(pb);

                // For each non-zero element in column k of A
                for (int pa = leftMatrix.getColPointers().get(rightRowIndex); pa < leftMatrix.getColPointers().get(rightRowIndex + 1); pa++) {
                    int leftRowIndex = leftMatrix.getRowIndices().get(pa);
                    ComplexNumber aVal = leftMatrix.getValues().get(pa);

                    if (isZero(dense[leftRowIndex])) {
                        nnzTracker[nnzTrackerIndex++] = leftRowIndex;
                    }
                    dense[leftRowIndex] = ComplexMath.addComplexNumbers(dense[leftRowIndex], ComplexMath.multiplyComplexNumbers(aVal, bVal));
                }
            }

            // Store the non-zero results in C
            for (int s = 0; s < nnzTrackerIndex; s++) {
                int i = nnzTracker[s];
                if (!isZero(dense[i])) {
                    resultMatrix.put(i, j, dense[i]);
                    dense[i] = new ComplexNumber(0, 0);  // Reset for next iteration
                }
            }
            nnzTrackerIndex = 0;  // Reset for next column
        }

        return resultMatrix;
    }

    private static ComplexSparse multiplyMatrixParallel(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        int leftHeight = leftMatrix.getHeight();
        int rightWidth = rightMatrix.getWidth();

        List<ComplexSparse> partialResults = new ArrayList<>(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            partialResults.add(new ComplexSparse(leftHeight, rightWidth));
        }

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<?>> futures = new ArrayList<>();

        // Reuse these arrays for each block
        ComplexNumber[] dense = new ComplexNumber[leftHeight];
        for (int i = 0; i < leftHeight; i++) {
            dense[i] = new ComplexNumber(0, 0);
        }

        // Divide work into blocks
        int numBlocks = (rightWidth + BLOCK_SIZE - 1) / BLOCK_SIZE;
        for (int block = 0; block < numBlocks; block++) {
            final int startCol = block * BLOCK_SIZE;
            final int endCol = Math.min(startCol + BLOCK_SIZE, rightWidth);

            futures.add(executor.submit(() -> {
                int threadId = (int) (Thread.currentThread().threadId() % NUM_THREADS);
                ComplexSparse localResult = partialResults.get(threadId);
                ComplexNumber[] thisDense = dense.clone();

                int[] nnzTracker = new int[leftHeight];
                int nnzTrackerIndex = 0;

                for (int j = startCol; j < endCol; j++) {
                    for (int pb = rightMatrix.getColPointers().get(j); pb < rightMatrix.getColPointers().get(j + 1); pb++) {
                        int rightRowIndex = rightMatrix.getRowIndices().get(pb);
                        ComplexNumber rightValue = rightMatrix.getValues().get(pb);

                        for (int pa = leftMatrix.getColPointers().get(rightRowIndex); pa < leftMatrix.getColPointers().get(rightRowIndex + 1); pa++) {
                            int leftRowIndex = leftMatrix.getRowIndices().get(pa);
                            ComplexNumber leftValue = leftMatrix.getValues().get(pa);

                            if (isZero(thisDense[leftRowIndex])) {
                                nnzTracker[nnzTrackerIndex++] = leftRowIndex;
                            }
                            thisDense[leftRowIndex].addInPlace(ComplexMath.multiplyComplexNumbers(leftValue, rightValue));
                        }
                    }

                    for (int s = 0; s < nnzTrackerIndex; s++) {
                        int i = nnzTracker[s];
                        if (!isZero(thisDense[i])) {
                            localResult.put(i, j, thisDense[i]);
                            thisDense[i].setZero();
                        }
                    }
                    nnzTrackerIndex = 0;
                }
            }));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException("Error in parallel matrix multiplication", e);
            }
        }

        executor.shutdown();

        // Combine partial results
        ComplexSparse collectorMatrix = new ComplexSparse(leftHeight, rightWidth);
        for (ComplexSparse partial : partialResults) {
            for (int j = 0; j < rightWidth; j++) {
                for (int r = partial.getColPointers().get(j); r < partial.getColPointers().get(j + 1); r++) {
                    int i = partial.getRowIndices().get(r);
                    collectorMatrix.put(i, j, ComplexMath.addComplexNumbers(collectorMatrix.get(i, j), partial.getValues().get(r)));
                }
            }
        }
        return collectorMatrix;
    }

    /**
     * Checks passed number to be "essentially zero", and returns true if it is.
     * @param value the ComplexNumber to check
     * @return true if essentially zero, false otherwise
     */
    public static boolean isZero(ComplexNumber value) {
        final double EPSILON = 1e-10;
        return Math.abs(value.getReal()) < EPSILON && Math.abs(value.getImag()) < EPSILON;
    }

    /**
     * Adds two matrices and returns the result as a new sparse matrix.
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
        ComplexSparse result = new ComplexSparse(matrixOne.getHeight(), matrixTwo.getWidth());
        for (int i = 0; i < matrixOne.getHeight(); i++) {
            for (int j = 0; j < matrixOne.getWidth(); j++) {
                result.put(i, j, addComplexNumbers(matrixOne.get(i, j), matrixTwo.get(i, j)));
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
    public static ComplexNumber multiplyComplexNumbers(ComplexNumber aVec, ComplexNumber bVec) {
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

    /**
     * Multiplies a vector against it's transpose and returns a matrix that is 2*vector.getHeight() wide and tall
     *
     * @param vector The input vector, usually a qubit state
     * @return The outer product as matrix
     */
    public static ComplexSparse outerProduct(ComplexSparse vector) {
        ComplexSparse transpose = getTranspose(vector);
        ComplexSparse outerProduct = new ComplexSparse(vector.getHeight(), vector.getHeight());
        for (int i = 0; i < vector.getHeight(); i++) {
            ComplexNumber sample = new ComplexNumber(vector.get(i, 0).getReal(), vector.get(i, 0).getImag());
            for (int j = 0; j < vector.getHeight(); j++) {
                outerProduct.put(i, j, multiplyComplexNumbers(sample, transpose.get(0, j)));
            }
        }
        return outerProduct;
    }

    /**
     * Transposes a given matrix.
     *
     * @param originMatrix The matrix to transpose.
     * @return A new {@code ComplexMatrix} that is the transpose of the original matrix.
     */
    public static ComplexSparse getTranspose(ComplexSparse originMatrix) {
        int height = originMatrix.getHeight();
        int width = originMatrix.getWidth();
        ComplexSparse resultMatrix;
        if (height == width) {
            resultMatrix = new ComplexSparse(height, width);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    resultMatrix.put(i, j, originMatrix.get(j, i));
                }
            }
        } else {
            resultMatrix = new ComplexSparse(width, height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    resultMatrix.put(i, j, originMatrix.get(j, i));
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
    public static ComplexSparse getConjugateTranspose(ComplexSparse originMatrix) {
        int height = originMatrix.getHeight();
        int width = originMatrix.getWidth();
        ComplexSparse resultMatrix;
        if (height == width) {
            resultMatrix = new ComplexSparse(height, width);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    resultMatrix.put(i, j, conjugate(originMatrix.get(j, i)));
                }
            }
        } else {
            resultMatrix = new ComplexSparse(width, height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    resultMatrix.put(i, j, conjugate(originMatrix.get(j, i)));
                }
            }
        }
        return resultMatrix;
    }

    /**
     * Interprets the system state vector into a Dirac notation representation for printing to console.
     *
     * @param stateVector the current system state vector
     * @return A string of the dirac notation representation
     */
    public static String complexMatrixToDiracNotation(ComplexSparse stateVector) {
        if (stateVector.getWidth() != 1) {
            throw new IllegalArgumentException("State vector must be a column vector");
        }

        int numQubits = (int) (Math.log(stateVector.getHeight()) / Math.log(2));
        StringBuilder result = new StringBuilder();
        boolean firstTerm = true;

        for (int i = 0; i < stateVector.getHeight(); i++) {
            ComplexNumber amplitude = stateVector.get(i, 0);
            if (amplitude.magnitudeSquared() > EPSILON) {  // Threshold for considering non-zero amplitudes
                if (firstTerm) {
                    result.append("|").append(PSI.lower()).append("⟩ = \n");
                    result.append("""
                            {phase} amplitude |basis⟩\s
                            -------------------------
                            """);

                } else {
                    result.append("\n");
                }

                double phase = getPhase(amplitude);
                result.append("{").append(String.format("%.3f°" + "} ", phase));

                String coeffString = complexToString(amplitude);
                if (!coeffString.equals("1") && !coeffString.equals("-1")) {
                    result.append(coeffString);
                } else if (coeffString.equals("-1") && firstTerm) {
                    result.append("-");
                }
                result.append(" |").append(String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0')).append("⟩");
                firstTerm = false;
            }
        }
        return result.toString();
    }

    /**
     * Interprets the system state vector into a Dirac notation representation for printing to console.
     *
     * @param stateVector the current system state vector
     * @return A string of the dirac notation representation
     */
    public static String complexMatrixToBasisStates(ComplexSparse stateVector) {
        if (stateVector.getWidth() != 1) {
            throw new IllegalArgumentException("State vector must be a column vector");
        }

        int numQubits = (int) (Math.log(stateVector.getHeight()) / Math.log(2));
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < stateVector.getHeight(); i++) {
            ComplexNumber amplitude = stateVector.get(i, 0);
            if (amplitude.magnitudeSquared() > EPSILON) {  // Threshold for considering non-zero amplitudes
                result.append("|").append(String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0')).append("⟩").append('$');
            }
        }

        return result.toString();
    }

    /**
     * Converts the complex number to a string in a particular format for use elsewhere
     *
     * @param compNum the ComplexNumber to convert
     * @return the string in a special format
     */
    public static String complexToString(ComplexNumber compNum) {
        if (Math.abs(compNum.getImag()) < EPSILON) {
            return String.format("%.5f", compNum.getReal());
        } else if (Math.abs(compNum.getReal()) < EPSILON) {
            return String.format("%.5fi", compNum.getImag());
        } else {
            return String.format("(%.5f + %.5fi)", compNum.getReal(), compNum.getImag());
        }
    }

    /**
     * Converts the complex number to a string in a particular format for use elsewhere
     *
     * @param compNum the ComplexNumber to convert
     * @return the string in a special format
     */
    public static String complexPhaseToString(ComplexNumber compNum) {
        double real = compNum.getReal();
        double imag = compNum.getImag();

        if (real < EPSILON && imag < EPSILON) {
            return String.format("(%.1f + %.1fi)", compNum.getReal(), compNum.getImag());
        } else if (real < EPSILON) {
            return String.format("%.1fi", compNum.getImag());
        } else if (imag < EPSILON) {
            return String.format("%.1f", compNum.getReal());
        } else {
            return "";
        }
    }

    /**
     * Gets the phase of the computational basis state by taking the atan2 of the imaginary and real
     * values and converting it to degrees.
     *
     * @param amplitude the amplitude of the basis state
     * @return the double as degrees of rotation around the Z axis from the X axis as 0.0 degrees.
     */
    public static double getPhase(ComplexNumber amplitude) {
        double decimalPhase = Math.atan2(amplitude.getImag(), amplitude.getReal());
        return 180 / Math.PI * decimalPhase;
    }

    /**
     * Computes the conjugate of a complex number.
     *
     * @param sampleNumber The complex number to conjugate.
     * @return The conjugated complex number.
     */
    public static ComplexNumber conjugate(ComplexNumber sampleNumber) {
        return new ComplexNumber(sampleNumber.getReal(), -sampleNumber.getImag());
    }
}
