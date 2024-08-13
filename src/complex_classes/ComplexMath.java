package complex_classes;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static supportClasses.GreekEnums.PSI;

/**
 * Provides utility methods for complex number arithmetic and linear algebra operations on sparse matrices.
 * This class is optimized for quantum computing simulations and other applications involving complex numbers
 * and sparse matrices.
 *
 * <p>Key features:
 * <ul>
 *   <li>Efficient sparse matrix operations using Compressed Sparse Column (CSC) format</li>
 *   <li>Sequential and parallel implementations of matrix operations</li>
 *   <li>Specialized methods for quantum computing operations (e.g., outer products, Dirac notation)</li>
 *   <li>Utility methods for complex number arithmetic and representation</li>
 * </ul>
 *
 * <p>The class uses a threshold ({@code SINGLE_THREAD_THRESHOLD}) to decide between
 * single-threaded and multi-threaded operations. Multi-threaded functionality is currently
 * disabled in some methods due to performance considerations.</p>
 *
 * @author Robert Smith
 * @version 0.5
 * @since 06 August 2024
 */
public final class ComplexMath {
    private static final double EPSILON = 1e-10;
    /**
     * Threshold for matrix height below which single-threaded execution is used.
     * Matrices with height less than or equal to this value will use single-threaded multiplication.
     */
    private static final int SINGLE_THREAD_THRESHOLD = 16384;
    public static int NUM_THREADS = Runtime.getRuntime().availableProcessors() / 2;

    /**
     * Computes the tensor product of two sparse matrices.
     * This method chooses between sequential and parallel implementations based on matrix size.
     * The tensor product, also known as the Kronecker product, is a way of putting two matrices together
     * to form a larger matrix with a special structure.
     *
     * @param leftMatrix  The first {@link ComplexSparse} matrix.
     * @param rightMatrix The second {@link ComplexSparse} matrix.
     * @return A new {@link ComplexSparse} object representing the tensor product.
     */
    public static ComplexSparse tensorMultiply(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        int leftHeight = leftMatrix.getHeight();
        int rightHeight = rightMatrix.getHeight();

        if (leftHeight * rightHeight >= (SINGLE_THREAD_THRESHOLD)) {
            return tensorMultiplyParallel(leftMatrix, rightMatrix);
        } else {
            return tensorMultiplySequential(leftMatrix, rightMatrix);
        }
//        return tensorMultiplySequential(leftMatrix, rightMatrix);
//        return tensorMultiplyParallel(leftMatrix, rightMatrix);
    }

    /**
     * Multiplies two sparse matrices, choosing the appropriate multiplication method based on the matrices' dimensions.
     * This method serves as a dispatcher, selecting between vector multiplication and general matrix multiplication.
     * It optimizes the computation based on whether the right matrix is a column vector.
     *
     * @param leftMatrix  The left sparse matrix in the multiplication.
     * @param rightMatrix The right sparse matrix in the multiplication.
     * @return A new {@link ComplexSparse} object representing the result of the matrix multiplication.
     * @throws IllegalArgumentException If the dimensions of the matrices do not match for multiplication.
     */
    public static ComplexSparse multiplyMatrix(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        if (leftMatrix.getWidth() != rightMatrix.getHeight()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
        }

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
     * Performs sequential tensor multiplication of two sparse matrices.
     * This method implements the tensor product algorithm for sparse matrices using a single thread.
     * It's optimized for memory usage and performance on smaller matrices.
     *
     * @param leftMatrix  The first {@link ComplexSparse} matrix in the tensor product.
     * @param rightMatrix The second {@link ComplexSparse} matrix in the tensor product.
     * @return A new {@link ComplexSparse} matrix representing the tensor product of leftMatrix and rightMatrix.
     */
    private static ComplexSparse tensorMultiplySequential(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        int leftHeight = leftMatrix.getHeight();
        int leftWidth = leftMatrix.getWidth();
        int rightHeight = rightMatrix.getHeight();
        int rightWidth = rightMatrix.getWidth();
        int resultHeight = leftHeight * rightHeight;
        int resultWidth = leftWidth * rightWidth;

        // Initialize result matrix
        ComplexSparse result = new ComplexSparse(resultHeight, resultWidth);

        final int bufferSize = 50;

        // Buffers for batched puts
        int[] rowBuffer = new int[bufferSize];
        int[] colBuffer = new int[bufferSize];
        ComplexNumber[] valueBuffer = new ComplexNumber[bufferSize];
        int bufferIndex = 0;

        // Temporary storage for complex multiplication
        double[] tempReal = new double[1];
        double[] tempImag = new double[1];

        // Single loop over non-zero elements of left matrix
        for (int leftCol = 0; leftCol < leftWidth; leftCol++) {
            for (int leftPtr = leftMatrix.getColPointers().get(leftCol); leftPtr < leftMatrix.getColPointers().get(leftCol + 1); leftPtr++) {
                int leftRow = leftMatrix.getRowIndices().get(leftPtr);
                ComplexNumber leftValue = leftMatrix.getValues().get(leftPtr);

                // Nested loop over non-zero elements of right matrix
                for (int rightCol = 0; rightCol < rightWidth; rightCol++) {
                    for (int rightPtr = rightMatrix.getColPointers().get(rightCol); rightPtr < rightMatrix.getColPointers().get(rightCol + 1); rightPtr++) {
                        int rightRow = rightMatrix.getRowIndices().get(rightPtr);
                        ComplexNumber rightValue = rightMatrix.getValues().get(rightPtr);

                        // Calculate indices for result matrix
                        int resultRow = leftRow * rightHeight + rightRow;
                        int resultCol = leftCol * rightWidth + rightCol;

                        // Multiply complex numbers using primitive operations
                        tempReal[0] = leftValue.getReal() * rightValue.getReal() - leftValue.getImag() * rightValue.getImag();
                        tempImag[0] = leftValue.getReal() * rightValue.getImag() + leftValue.getImag() * rightValue.getReal();

                        // Handle floating-point errors
                        if (Math.abs(tempReal[0]) < EPSILON) tempReal[0] = 0;
                        if (Math.abs(tempImag[0]) < EPSILON) tempImag[0] = 0;

                        // Only add non-zero results
                        if (tempReal[0] != 0 || tempImag[0] != 0) {
                            // Add to buffer
                            rowBuffer[bufferIndex] = resultRow;
                            colBuffer[bufferIndex] = resultCol;
                            valueBuffer[bufferIndex] = new ComplexNumber(tempReal[0], tempImag[0]);
                            bufferIndex++;

                            // If buffer is full, perform batched put
                            if (bufferIndex >= bufferSize) {
                                for (int i = 0; i < bufferSize; i++) {
                                    result.put(rowBuffer[i], colBuffer[i], valueBuffer[i]);
                                }
                                bufferIndex = 0;
                            }
                        }
                    }
                }
            }
        }

        // Perform final batched put for any remaining items in the buffer
        for (int i = 0; i < bufferIndex; i++) {
            result.put(rowBuffer[i], colBuffer[i], valueBuffer[i]);
        }

        return result;
    }

    /**
     * Performs parallel tensor multiplication of two sparse matrices.
     * This method implements the tensor product algorithm for sparse matrices using multiple threads.
     * It divides the computation into blocks and processes them in parallel, suitable for larger matrices.
     *
     * @param leftMatrix  The first {@link ComplexSparse} matrix in the tensor product.
     * @param rightMatrix The second {@link ComplexSparse} matrix in the tensor product.
     * @return A new {@link ComplexSparse} matrix representing the tensor product of leftMatrix and rightMatrix.
     * @throws RuntimeException if an error occurs during parallel execution.
     */
    private static ComplexSparse tensorMultiplyParallel(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        int leftHeight = leftMatrix.getHeight();
        int leftWidth = leftMatrix.getWidth();
        int rightHeight = rightMatrix.getHeight();
        int rightWidth = rightMatrix.getWidth();
        int resultHeight = leftHeight * rightHeight;
        int resultWidth = leftWidth * rightWidth;

        ComplexSparse result = new ComplexSparse(resultHeight, resultWidth);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<?>> futures = new ArrayList<>();

        // Use sparse matrix properties for iteration
        for (int thread = 0; thread < NUM_THREADS; thread++) {
            final int startCol = thread * (leftWidth / NUM_THREADS);
            final int endCol = (thread == NUM_THREADS - 1) ? leftWidth : (startCol + (leftWidth / NUM_THREADS));

            futures.add(executor.submit(() -> {
                // Buffers for batched puts
                int[] rowBuffer = new int[1000];
                int[] colBuffer = new int[1000];
                double[] realBuffer = new double[1000];
                double[] imagBuffer = new double[1000];
                int bufferIndex = 0;

                // Temporary storage for complex multiplication
                double tempReal;
                double tempImag;

                for (int j1 = startCol; j1 < endCol; j1++) {
                    for (int leftPtr = leftMatrix.getColPointers().get(j1); leftPtr < leftMatrix.getColPointers().get(j1 + 1); leftPtr++) {
                        int i1 = leftMatrix.getRowIndices().get(leftPtr);
                        ComplexNumber leftValue = leftMatrix.getValues().get(leftPtr);

                        for (int j2 = 0; j2 < rightWidth; j2++) {
                            for (int rightPtr = rightMatrix.getColPointers().get(j2); rightPtr < rightMatrix.getColPointers().get(j2 + 1); rightPtr++) {
                                int i2 = rightMatrix.getRowIndices().get(rightPtr);
                                ComplexNumber rightValue = rightMatrix.getValues().get(rightPtr);

                                int i = i1 * rightHeight + i2;
                                int j = j1 * rightWidth + j2;

                                // Multiply complex numbers using primitive operations
                                tempReal = leftValue.getReal() * rightValue.getReal() - leftValue.getImag() * rightValue.getImag();
                                tempImag = leftValue.getReal() * rightValue.getImag() + leftValue.getImag() * rightValue.getReal();

                                // Handle floating-point errors
                                if (Math.abs(tempReal) < EPSILON) tempReal = 0;
                                if (Math.abs(tempImag) < EPSILON) tempImag = 0;

                                // Only add non-zero results
                                if (tempReal != 0 || tempImag != 0) {
                                    rowBuffer[bufferIndex] = i;
                                    colBuffer[bufferIndex] = j;
                                    realBuffer[bufferIndex] = tempReal;
                                    imagBuffer[bufferIndex] = tempImag;
                                    bufferIndex++;

                                    // If buffer is full, perform batched put
                                    if (bufferIndex == 1000) {
                                        synchronized (result) {
                                            for (int k = 0; k < bufferIndex; k++) {
                                                result.put(rowBuffer[k], colBuffer[k], new ComplexNumber(realBuffer[k], imagBuffer[k]));
                                            }
                                        }
                                        bufferIndex = 0;
                                    }
                                }
                            }
                        }
                    }
                }

                // Perform final batched put for any remaining items in the buffer
                if (bufferIndex > 0) {
                    synchronized (result) {
                        for (int k = 0; k < bufferIndex; k++) {
                            result.put(rowBuffer[k], colBuffer[k], new ComplexNumber(realBuffer[k], imagBuffer[k]));
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

    /**
     * Performs sequential multiplication of a sparse matrix with a sparse column vector.
     * This method is optimized for the case where the right matrix is a column vector,
     * which is common in quantum computing operations. It uses a temporary dense vector
     * to accumulate results efficiently.
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

    /**
     * Performs sequential multiplication of two sparse matrices.
     * This method is optimized for sparse matrices using Compressed Sparse Column (CSC) format.
     * It uses a temporary dense vector to accumulate results for each column of the result matrix.
     *
     * @param leftMatrix  The first sparse matrix to be multiplied.
     * @param rightMatrix The second sparse matrix to be multiplied.
     * @return A new {@code ComplexSparse} object representing the result of the multiplication.
     * @throws IllegalArgumentException If the dimensions of the matrices do not match for multiplication.
     */
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

    /**
     * Performs parallel multiplication of a sparse matrix with a sparse column vector.
     * This method divides the rows of the left matrix among available threads for parallel computation.
     * It's optimized for large matrices where parallelization can provide significant performance benefits.
     *
     * @param leftMatrix  The sparse matrix to be multiplied.
     * @param rightMatrix The sparse column vector to be multiplied.
     * @return A new {@code ComplexSparse} object representing the result of the multiplication.
     * @throws IllegalArgumentException If the dimensions of the matrices do not match for multiplication
     *                                  or if the right matrix is not a column vector.
     * @throws RuntimeException         If an error occurs during parallel execution.
     */
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

    /**
     * Performs parallel multiplication of two sparse matrices.
     * This method divides the columns of the right matrix among available threads for parallel computation.
     * Each thread computes a partial result, which is then combined into the final result matrix.
     *
     * @param leftMatrix  The first sparse matrix to be multiplied.
     * @param rightMatrix The second sparse matrix to be multiplied.
     * @return A new {@code ComplexSparse} object representing the result of the multiplication.
     * @throws RuntimeException If an error occurs during parallel execution.
     */
    private static ComplexSparse multiplyMatrixParallel(ComplexSparse leftMatrix, ComplexSparse rightMatrix) {
        int leftHeight = leftMatrix.getHeight();
        int rightWidth = rightMatrix.getWidth();

        List<ComplexSparse> partialResults = new ArrayList<>(NUM_THREADS);
        for (int i = 0; i < NUM_THREADS; i++) {
            partialResults.add(new ComplexSparse(leftHeight, rightWidth));
        }

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<?>> futures = new ArrayList<>();

        ComplexNumber[] dense = new ComplexNumber[leftHeight];
        for (int i = 0; i < leftHeight; i++) {
            dense[i] = new ComplexNumber(0, 0);
        }

        // Divide work into NUM_THREADS parts
        int colsPerThread = (rightWidth + NUM_THREADS - 1) / NUM_THREADS;
        for (int thread = 0; thread < NUM_THREADS; thread++) {
            final int startCol = thread * colsPerThread;
            final int endCol = Math.min(startCol + colsPerThread, rightWidth);

            int finalThread = thread;
            futures.add(executor.submit(() -> {
                ComplexSparse localResult = partialResults.get(finalThread);
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
     * Checks if a complex number is essentially zero.
     * This method uses a small epsilon value to account for floating-point precision issues.
     *
     * @param value The ComplexNumber to check.
     * @return true if both the real and imaginary parts of the complex number are smaller than EPSILON, false otherwise.
     */
    public static boolean isZero(ComplexNumber value) {
        final double EPSILON = 1e-10;
        return Math.abs(value.getReal()) < EPSILON && Math.abs(value.getImag()) < EPSILON;
    }

    /**
     * Adds two matrices and returns the result as a new sparse matrix.
     * This method performs element-wise addition of the two input matrices.
     *
     * @param matrixOne The first matrix.
     * @param matrixTwo The second matrix.
     * @return A new {@link ComplexSparse} object that is the result of the matrix addition.
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
     * This method performs the standard complex number multiplication: (a+bi)(c+di) = (ac-bd) + (ad+bc)i.
     * It also includes a check for floating-point error buildup, correcting values very close to 1 or -1.
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

    /**
     * Corrects potential floating-point error buildup for values very close to 1 or -1.
     * This method addresses the common issue in floating-point arithmetic where
     * calculations that should result in exactly 1 or -1 end up slightly off due to
     * accumulated rounding errors. It rounds values extremely close to 1 or -1
     * to exactly 1 or -1, respectively.
     *
     * @param number The double value to check and potentially correct
     * @return The input number, or 1.0 if the input is between 0.9999999 and 1.0,
     * or -1.0 if the input is between -1.0 and -0.9999999
     */
    private static double testResultForFloatErrorBuildup(double number) {
        if (number > 0.999999999 && number < 1.0) {
            return 1.0;
        } else if (number < -0.999999999 && number > -1.0) {
            return -1.0;
        } else {
            return number;
        }
    }

    /**
     * Adds two complex numbers.
     * This method performs element-wise addition of the real and imaginary parts.
     * It also includes a check for floating-point error buildup, correcting values very close to 1 or -1.
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
     * Transposes a given matrix.
     * This method swaps the row and column indices of the matrix elements.
     *
     * @param originMatrix The matrix to transpose.
     * @return A new {@code ComplexSparse} that is the transpose of the original matrix.
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
     * This method transposes the matrix and takes the complex conjugate of each element.
     *
     * @param originMatrix The matrix to compute the conjugate transpose of.
     * @return A new {@code ComplexSparse} that is the conjugate transpose of the original matrix.
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
     * Interprets the system state vector into a Dirac notation representation.
     * This method converts a quantum state vector into the standard Dirac (bra-ket) notation used in quantum mechanics.
     * It includes the phase and amplitude of each non-zero basis state.
     *
     * @param stateVector The current system state vector.
     * @return A string of the Dirac notation representation.
     * @throws IllegalArgumentException If the state vector is not a column vector.
     */
    public static String stateVectorToDiracNotation(ComplexSparse stateVector) {
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
     * Interprets the system state vector into a basis states representation.
     * This method extracts the non-zero basis states from the state vector, representing them in binary notation.
     *
     * @param stateVector The current system state vector.
     * @return A string of the basis states representation, with each state separated by a '$' character.
     * @throws IllegalArgumentException If the state vector is not a column vector.
     */
    public static String stateVectorToBasisStates(ComplexSparse stateVector) {
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
     * Gets the magnitude of states from the state vector.
     * This method calculates the probability amplitudes (magnitudes squared) of the top two basis states in the vector.
     *
     * @param stateVector The current system state vector.
     * @return A map of basis states (in binary notation) to their probability amplitudes.
     * @throws IllegalArgumentException If the state vector is not a column vector.
     */
    public static Map<String, Double> getMagnitudeStates(ComplexSparse stateVector) {
        if (stateVector.getWidth() != 1) {
            throw new IllegalArgumentException("State vector must be a column vector");
        }

        int numQubits = (int) (Math.log(stateVector.getHeight()) / Math.log(2));
        DecimalFormat df = new DecimalFormat("0.00000");
        TreeMap<Double, String> topTwoStates = new TreeMap<>(Collections.reverseOrder());

        for (int i = 0; i < stateVector.getHeight(); i++) {
            double magnitude = Double.parseDouble(df.format(stateVector.get(i, 0).magnitudeSquared()));
            if (magnitude > EPSILON) {
                String state = String.format("%" + numQubits + "s", Integer.toBinaryString(i)).replace(' ', '0');
                topTwoStates.put(magnitude, state);
                if (topTwoStates.size() > 2) {
                    topTwoStates.pollLastEntry();
                }
            }
        }

        Map<String, Double> result = new HashMap<>();
        topTwoStates.forEach((magnitude, state) -> result.put(state, magnitude));
        return result;
    }

    /**
     * Converts a complex number to a string representation.
     * This method formats the complex number as a string, handling special cases like zero real or imaginary parts.
     *
     * @param compNum The ComplexNumber to convert.
     * @return The string representation of the complex number, formatted to 5 decimal places.
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
     * Converts a complex number to a string representation focusing on phase.
     * This method is used for displaying the phase of a complex number, with special handling for near-zero values.
     *
     * @param compNum The ComplexNumber to convert.
     * @return The string representation of the complex number's phase, formatted to 1 decimal place.
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
     * Calculates the phase of a complex number.
     * This method computes the angle (in degrees) that the complex number makes with the positive real axis.
     *
     * @param amplitude The complex number amplitude.
     * @return The phase in degrees, ranging from -180 to 180.
     */
    public static double getPhase(ComplexNumber amplitude) {
        double decimalPhase = Math.atan2(amplitude.getImag(), amplitude.getReal());
        return 180 / Math.PI * decimalPhase;
    }

    /**
     * Computes the conjugate of a complex number.
     * The conjugate of a complex number a + bi is a - bi.
     *
     * @param sampleNumber The complex number to conjugate.
     * @return The conjugated complex number.
     */
    public static ComplexNumber conjugate(ComplexNumber sampleNumber) {
        return new ComplexNumber(sampleNumber.getReal(), -sampleNumber.getImag());
    }
}
