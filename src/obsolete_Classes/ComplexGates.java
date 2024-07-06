package obsolete_Classes;


import complexClasses.ComplexMatrix;
import complexClasses.ComplexQubit;

/**
 * This class provides implementations of several quantum gates in complex form,
 * such as Pauli-X, Pauli-Y, Pauli-Z, and Hadamard gates. It also includes methods
 * to apply these gates to a {@link ComplexQubit} and to print these gates.
 *
 * <p>
 * Quantum gates are represented as {@link ComplexMatrix} objects, and the gates
 * provided here are static instances of predefined matrices.
 * </p>
 *
 * <p>
 * This class supports applying gates to {@link ComplexQubit} objects using matrix multiplication.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * ComplexQubit qubit = new ComplexQubit(new ComplexNumber(1, 0), new ComplexNumber(0, 0)); // Initialize qubit
 * ComplexQubit result = ComplexGates.applyPauliX(qubit); // Apply Pauli-X gate
 * }</pre>
 *
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 */
public class ComplexGates {
//    private static final boolean CNOTDEBUG = true;
//    private static final boolean DEBUG = false;
//    private static final double H_FACTOR = 1 / Math.sqrt(2);

//    /**
//     * Pauli-X gate matrix.
//     */
//    private static final ComplexMatrix PAULI_X = new ComplexMatrix(new ComplexNumber[][]{
//            {new ComplexNumber(0), new ComplexNumber(1)},
//            {new ComplexNumber(1), new ComplexNumber(0)}
//    });
//
//    /**
//     * Pauli-Z gate matrix.
//     */
//    private static final ComplexMatrix PAULI_Z = new ComplexMatrix(new ComplexNumber[][]{
//            {new ComplexNumber(1), new ComplexNumber(0)},
//            {new ComplexNumber(0), new ComplexNumber(-1)}
//    });
//
//    /**
//     * Pauli-Y gate matrix.
//     */
//    private static final ComplexMatrix PAULI_Y = new ComplexMatrix(new ComplexNumber[][]{
//            {new ComplexNumber(0), new ComplexNumber(0, -1)},
//            {new ComplexNumber(0, 1), new ComplexNumber(0)}
//    });
//
//    /**
//     * Hadamard gate matrix.
//     */
//    private static final ComplexMatrix HADAMARD = new ComplexMatrix(new ComplexNumber[][]{
//            {new ComplexNumber(H_FACTOR), new ComplexNumber(H_FACTOR)},
//            {new ComplexNumber(H_FACTOR), new ComplexNumber(-H_FACTOR)}
//    });
//
//    /**
//     * Identity gate matrix.
//     */
//    private static final ComplexMatrix IDENTITY = new ComplexMatrix(new ComplexNumber[][]{
//            {new ComplexNumber(1), new ComplexNumber(0)},
//            {new ComplexNumber(0), new ComplexNumber(1)}
//    });
//
//    private static final ComplexMatrix SGate = new ComplexMatrix(new ComplexNumber[][]{
//            {new ComplexNumber(1), new ComplexNumber(0)},
//            {new ComplexNumber(0), new ComplexNumber(0, 1)}
//    });
//
//    private static final ComplexMatrix TGate = new ComplexMatrix(new ComplexNumber[][]{
//            {new ComplexNumber(1), new ComplexNumber(0)},
//            {new ComplexNumber(0), getTvalue()}
//    });
//
//    private static ComplexNumber getTvalue() {
//        double real = Math.cos(Math.PI / 4);
//        double imag = Math.sin(Math.PI / 4);
//        return new ComplexNumber(real, imag);
//    }
//
//    /**
//     * Identity Matrix Getter
//     *
//     * @return the identity matrix
//     */
//    public static ComplexMatrix getIdentity() {
//        return IDENTITY.getMatrix();
//    }
//
//    /**
//     * Pauli_X Matrix Getter
//     *
//     * @return the Pauli_X matrix
//     */
//    public static ComplexMatrix getPauliX() {
//        return PAULI_X.getMatrix();
//    }
//
//    /**
//     * Pauli_Y Matrix Getter
//     *
//     * @return the Pauli_Y matrix
//     */
//    public static ComplexMatrix getPauliY() {
//        return PAULI_Y.getMatrix();
//    }
//
//    /**
//     * Pauli_Z Matrix Getter
//     *
//     * @return the Pauli_Z matrix
//     */
//    public static ComplexMatrix getPauliZ() {
//        return PAULI_Z.getMatrix();
//    }
//
//    /**
//     * S gate matrix getter
//     * @return the sgate as a complex matrix
//     */
//    public static ComplexMatrix getSGate(){return S_GATE.getMatrix();}
//
//    /**
//     * T gate Matrix getter
//     *
//     * @return the ComplexMatrix from the T gate field
//     */
//    public static ComplexMatrix getTGate(){return T_GATE.getMatrix();}
//
//    /**
//     * HADAMARD gate Matrix getter
//     *
//     * @return the ComplexMatrix from the HADAMARD gate field
//     */
//    public static ComplexMatrix getHadamard() {return HADAMARD.getMatrix();}
//
//
//    /**
//     * Applies the Pauli-X gate to a {@link ComplexQubit}.
//     *
//     * @param target The input {@link ComplexQubit}.
//     * @return The resulting {@link ComplexQubit} after applying the Pauli-X gate.
//     */
//    public static ComplexMatrix applyPauliX(ComplexMatrix target) {
//        return applyGate(PAULI_X.getMatrix(), target);
//    }
//
//    /**
//     * Applies the Pauli-Z gate to a {@link ComplexQubit}.
//     *
//     * @param qubit The input {@link ComplexQubit}.
//     * @return The resulting {@link ComplexQubit} after applying the Pauli-Z gate.
//     */
//    public static ComplexMatrix applyPauliZ(ComplexMatrix qubit) {
//        return applyGate(PAULI_Z.getMatrix(), qubit);
//    }
//
//    /**
//     * Applies the Pauli-Y gate to a {@link ComplexQubit}.
//     *
//     * @param qubit The input {@link ComplexQubit}.
//     * @return The resulting {@link ComplexQubit} after applying the Pauli-Y gate.
//     */
//    public static ComplexMatrix applyPauliY(ComplexMatrix qubit) {
//        return applyGate(PAULI_Y.getMatrix(), qubit);
//    }
//
//    /**
//     * Applies the Hadamard gate to a {@link ComplexQubit}.
//     *
//     * @param qubit The input {@link ComplexQubit}.
//     * @return The resulting {@link ComplexQubit} after applying the Hadamard gate.
//     */
//    public static ComplexMatrix applyHadamard(ComplexMatrix qubit) {
//        return applyGate(HADAMARD.getMatrix(), qubit);
//    }
//
//    /**
//     * Applies the Identity gate to a {@link ComplexQubit}.
//     *
//     * @param qubit The input {@link ComplexQubit}.
//     * @return The resulting {@link ComplexQubit} after applying the Identity gate.
//     */
//    public static ComplexMatrix applyIdentity(ComplexMatrix qubit) {
//        return applyGate(IDENTITY.getMatrix(), qubit);
//    }
//
//    /**
//     * Applies the S gate to a {@link ComplexQubit}.
//     * 10
//     * 0i
//     * @param qubit The input {@link ComplexQubit}.
//     * @return The resulting {@link ComplexQubit} after applying the S Gate.
//     */
//    public static ComplexMatrix applySGate(ComplexMatrix qubit) { return applyGate(S_GATE.getMatrix(), qubit);
//    }
//
//    /**
//     * Applies the T gate to a {@link ComplexQubit}.
//     * 10
//     * 0 i(pie/4)
//     * @param qubit The input {@link ComplexQubit}.
//     * @return The resulting {@link ComplexQubit} after applying the T Gate.
//     */
//    public static ComplexMatrix applyTGate(ComplexMatrix qubit) { return applyGate(T_GATE.getMatrix(), qubit);
//    }
//
//    /**
//     * Applies a given gate represented by a {@link ComplexMatrix} to a {@link ComplexQubit}.
//     *
//     * @param gate   The gate matrix to apply.
//     * @param target The input {@link ComplexQubit}.
//     * @return The resulting {@link ComplexQubit} after applying the gate.
//     */
//    private static ComplexMatrix applyGate(ComplexMatrix gate, ComplexMatrix target) {
//        return gate.multiplyMatrix(gate, target);
//    }
//
//    /**
//     * Applies the CNOT gate to control and target qubits. Checks for whether the input is CX 0,1 or CX 1,0. If 1,0 it
//     * applies the correct parameter flipping to use the correct CNOT matrix to gain the expected results without
//     * using if/else to solve the problem, but instead using the actual matrix math.
//     *
//     * @param controlQubit The control qubit.
//     * @param targetQubit  The target qubit.
//     * @throws IllegalArgumentException If the qubits are not properly initialized or have incompatible dimensions.
//     */
//
//    public static void applyCNOT(ComplexQubit controlQubit, ComplexQubit targetQubit) {
//        // Ensure dimensions are compatible
//        if ((controlQubit.getState().getHeight() != 2 || controlQubit.getState().getWidth() != 1) ||
//                (targetQubit.getState().getHeight() != 2 || targetQubit.getState().getWidth() != 1)) {
//            throw new IllegalArgumentException("Qubit states must be a column vector of size 2x1.");
//        }
//        boolean flipped = false;
//        ComplexMatrix controlState, targetState;
//        if (targetQubit.getQubitID() > controlQubit.getQubitID()) {
//            controlState = controlQubit.getState();
//            targetState = targetQubit.getState();
//        } else {
//            flipped = true;
//            targetState = controlQubit.getState();
//            controlState = targetQubit.getState();
//        }
//        ComplexObject coj = new ComplexObject();
//        ComplexMatrix CNOT = new ComplexMatrix(4, 4), controlProduct;
//        ComplexMatrix resultMatrix;
//
//
//        ComplexMatrix inputStateVector = coj.deriveStateVector(controlState, targetState);
//        if (CNOTDEBUG) System.out.println("Input State Vector: \n" + inputStateVector);
//
//        generateCNOT(flipped, CNOT);
//        if (CNOTDEBUG) System.out.println("CNOT is \n" + CNOT);
//        resultMatrix = coj.multiplyMatrix(CNOT, inputStateVector);
//        if (CNOTDEBUG) System.out.println("Result Matrix is: \n" + resultMatrix);
//
//        if (!flipped) {
//            controlQubit.setState(deriveControlState(controlState, resultMatrix));
//            targetQubit.setState(deriveTargetState(targetState, resultMatrix));
//        } else {
//            ComplexMatrix cTemp = new ComplexMatrix(controlState.getData());
//            ComplexMatrix tTemp = new ComplexMatrix(targetState.getData());
//
//            //get the qubit status for the target qubit and apply it to the target state, must use temp var to avoid
//            //side effect when deriving the control qubit state
//            deriveControlState(tTemp, resultMatrix).getData();
//            targetState.setData(tTemp.getData());
//            //get the qubit status for the control qubit and apply it to the control state, must temp var to avoid
//            //side effect when deriving the target qubit state
//            deriveTargetState(cTemp, resultMatrix).getData();
//            controlState.setData(cTemp.getData());
//
//            //set the parameter qubit states to be the state resultant of applying CNOT to them
//            controlQubit.setState(controlState);
//            targetQubit.setState(targetState);
//        }
//    }

//    /**
//     * This method calculates the correct CNOT gate to use based on whether the input was flipped or not. Using this
//     * is required because the CNOT gate is not the same for CX 0,1 as CX 1,0
//     * If not flipped it uses:
//     * 1000
//     * 0100
//     * 0001
//     * 0010
//     * If flipped it uses:
//     * 1000
//     * 0001
//     * 0010
//     * 0100
//     *
//     * @param flipped whether the target qubit parameter qubitID is greater than the control qubit parameter qubitID
//     * @param CNOT    the 4x4 matrix to apply as CNOT
//     */
//    private static void generateCNOT(boolean flipped, ComplexMatrix CNOT) {
//        ComplexObject coj = new ComplexObject();
//        ComplexMatrix qubitZero = new ComplexMatrix(new ComplexNumber[][]{
//                {new ComplexNumber(1)},
//                {new ComplexNumber()}
//        });
//        ComplexMatrix qubitOne = new ComplexMatrix(new ComplexNumber[][]{
//                {new ComplexNumber()},
//                {new ComplexNumber(1)}
//        });
//
//        ComplexMatrix zeroOuterProduct = coj.outerProduct(qubitZero);
//        ComplexMatrix oneOuterProduct = coj.outerProduct(qubitOne);
//        ComplexMatrix zeroProduct, oneProduct;
//
//        if (!flipped) {
//            zeroProduct = coj.tensorMultiply(zeroOuterProduct, ComplexGates.getIdentity());
//            oneProduct = coj.tensorMultiply(oneOuterProduct, ComplexGates.getPauliX());
//        } else {
//            zeroProduct = coj.tensorMultiply(ComplexGates.getPauliX(), oneOuterProduct);
//            oneProduct = coj.tensorMultiply(ComplexGates.getIdentity(), zeroOuterProduct);
//        }
//
//        if (CNOTDEBUG) System.out.println("zeroProduct: \n" + zeroProduct);
//        if (CNOTDEBUG) System.out.println("oneProduct: \n" + oneProduct);
//        ComplexMatrix temp = coj.addMatrix(oneProduct, zeroProduct);
//        CNOT.setData(temp.getData());
//    }

//    public static void applyCNOTNOT(ComplexQubit controlQ, ComplexQubit targetOneQ, ComplexQubit targetTwoQ) {
//        // Ensure dimensions are compatible
//        if ((controlQ.getState().getHeight() != 2 || controlQ.getState().getWidth() != 1) ||
//                (targetOneQ.getState().getHeight() != 2 || targetOneQ.getState().getWidth() != 1) ||
//                (targetTwoQ.getState().getHeight() != 2 || targetTwoQ.getState().getWidth() != 1)) {
//            throw new IllegalArgumentException("Qubit states must be a column vector of size 2x1.");
//        }
//        char scenario = 'Z';
//        ComplexMatrix controlState = new ComplexMatrix(2, 1);
//        ComplexMatrix targetOneState = new ComplexMatrix(2, 1);
//        ComplexMatrix targetTwoState = new ComplexMatrix(2, 1);
//        ComplexObject coj = new ComplexObject();
//        getInputOrder(scenario, controlState, targetOneState, targetTwoState, controlQ, targetOneQ, targetTwoQ);
//        if (CNOTDEBUG) System.out.println("Input detected was scenario: " + scenario);
//
//    }
//
//    /**
//     * Detects the state of the ordering of the input qubits for a 3 qubit CNOT with 1 control and 2 targets. Sorts
//     * the order out according to the order provided to ensure matrix application results in correct output.
//     *
//     * @param scenario which scenario it resulted in
//     * @param controlState the matrix of the control qubit state after evaluation of ordering
//     * @param targetOneState the matrix of the targetone qubit state after evaluation of ordering
//     * @param targetTwoState the matrix of the targettwo qubit state after evaluation of ordering
//     * @param controlQ The input control qubit
//     * @param targetOneQ The input target one qubit
//     * @param targetTwoQ The input target two qubit
//     */
//    private static void getInputOrder(char scenario,
//                                      ComplexMatrix controlState,
//                                      ComplexMatrix targetOneState,
//                                      ComplexMatrix targetTwoState,
//                                      ComplexQubit controlQ,
//                                      ComplexQubit targetOneQ,
//                                      ComplexQubit targetTwoQ) {
//
//        //condition where control ID is smallest then targetTwo ID, then targetOne ID aka: C < TWO < ONE
//        if (controlQ.getQubitID() < targetOneQ.getQubitID() &&
//                targetOneQ.getQubitID() > targetTwoQ.getQubitID() &&
//                targetTwoQ.getQubitID() > controlQ.getQubitID()) {
//
//            scenario = 'A';
//            controlState.setData(controlQ.getState().getData());
//            targetOneState.setData(targetTwoQ.getState().getData());
//            targetTwoState.setData(targetOneQ.getState().getData());
//        }
//        //condition where targetTwo ID is smallest, then ControlQ ID, then targetOne ID aka: TWO < C < ONE
//        if (controlQ.getQubitID() > targetTwoQ.getQubitID() &&
//                controlQ.getQubitID() < targetOneQ.getQubitID() &&
//                targetOneQ.getQubitID() > targetTwoQ.getQubitID()) {
//
//            scenario = 'B';
//            controlState.setData(targetTwoQ.getState().getData());
//            targetOneState.setData(controlQ.getState().getData());
//            targetTwoState.setData(targetOneQ.getState().getData());
//        }
//        //condition where TargetOne ID is smallest then Control ID then targetTwo ID aka: ONE < C < TWO
//        if (controlQ.getQubitID() > targetOneQ.getQubitID() &&
//                targetOneQ.getQubitID() < targetTwoQ.getQubitID() &&
//                controlQ.getQubitID() < targetTwoQ.getQubitID()) {
//
//            scenario = 'C';
//            controlState.setData(targetOneQ.getState().getData());
//            targetOneState.setData(controlQ.getState().getData());
//            targetTwoState.setData(targetTwoQ.getState().getData());
//        }
//        //condition where targetTwo ID is smallest, then targetOne ID, then control ID aka: TWO < ONE < C
//        if (controlQ.getQubitID() > targetOneQ.getQubitID() &&
//                targetOneQ.getQubitID() > targetTwoQ.getQubitID() &&
//                controlQ.getQubitID() > targetTwoQ.getQubitID()) {
//
//            scenario = 'D';
//            controlState.setData(targetTwoQ.getState().getData());
//            targetOneState.setData(targetOneQ.getState().getData());
//            targetTwoState.setData(controlQ.getState().getData());
//        }
//        //condition where targetOne ID is smallest, then targetTwo ID, then control ID aka: ONE < TWO < C
//        if (controlQ.getQubitID() > targetTwoQ.getQubitID() &&
//                targetTwoQ.getQubitID() > targetOneQ.getQubitID() &&
//                controlQ.getQubitID() > targetOneQ.getQubitID()) {
//
//            scenario = 'E';
//            controlState.setData(targetOneQ.getState().getData());
//            targetOneState.setData(targetTwoQ.getState().getData());
//            targetTwoState.setData(controlQ.getState().getData());
//        }
//        //condition all order as expected controlID < targetOneID < targetTwoID aka: C < ONE < TWO
//        if (controlQ.getQubitID() < targetOneQ.getQubitID() &&
//            targetOneQ.getQubitID() < targetTwoQ.getQubitID() &&
//            controlQ.getQubitID() < targetTwoQ.getQubitID()) {
//            scenario = 'F';
//            controlState.setData(controlQ.getState().getData());
//            targetOneState.setData(targetOneQ.getState().getData());
//            targetTwoState.setData(targetTwoQ.getState().getData());
//        }
//    }
//
//    private static ComplexMatrix deriveControlState(ComplexMatrix controlState, ComplexMatrix resultMatrix) {
//        if (resultMatrix.get(0, 0).getReal() == 1.0) {
//            controlState.setData(new ComplexNumber[][]{
//                    {new ComplexNumber(1)},
//                    {new ComplexNumber()}
//            });
//        } else if (resultMatrix.get(1, 0).getReal() == 1.0) {
//            controlState.setData(new ComplexNumber[][]{
//                    {new ComplexNumber(1)},
//                    {new ComplexNumber()}
//            });
//        } else if (resultMatrix.get(2, 0).getReal() == 1.0) {
//            controlState.setData(new ComplexNumber[][]{
//                    {new ComplexNumber()},
//                    {new ComplexNumber(1)}
//            });
//        } else if (resultMatrix.get(3, 0).getReal() == 1.0) {
//            controlState.setData(new ComplexNumber[][]{
//                    {new ComplexNumber()},
//                    {new ComplexNumber(1)}
//            });
//        }
//        return controlState;
//    }
//
//    private static ComplexMatrix deriveTargetState(ComplexMatrix targetState, ComplexMatrix resultMatrix) {
//
//        if (resultMatrix.get(0, 0).getReal() == 1.0) {
//            targetState.setData(new ComplexNumber[][]{
//                    {new ComplexNumber(1)},
//                    {new ComplexNumber()}
//            });
//        } else if (resultMatrix.get(1, 0).getReal() == 1.0) {
//            targetState.setData(new ComplexNumber[][]{
//                    {new ComplexNumber()},
//                    {new ComplexNumber(1)}
//            });
//        } else if (resultMatrix.get(2, 0).getReal() == 1.0) {
//            targetState.setData(new ComplexNumber[][]{
//                    {new ComplexNumber(1)},
//                    {new ComplexNumber()}
//            });
//        } else if (resultMatrix.get(3, 0).getReal() == 1.0) {
//            targetState.setData(new ComplexNumber[][]{
//                    {new ComplexNumber()},
//                    {new ComplexNumber(1)}
//            });
//        }
//        return targetState;
//    }
//
//    /**
//     * Prints the predefined quantum gates and their matrix.
//     */
//    public static void printGates() {
//        Map<String, ComplexMatrix> gates = new HashMap<>();
//        gates.put("PAULI_X", PAULI_X.getMatrix());
//        gates.put("PAULI_Y", PAULI_Y.getMatrix());
//        gates.put("PAULI_Z", PAULI_Z.getMatrix());
//        gates.put("HADAMARD", HADAMARD.getMatrix());
//        gates.put("IDENTITY", IDENTITY.getMatrix());
//
//        for (String gate : gates.keySet()) {
//            System.out.println(gate);
//            System.out.println(gates.get(gate));
//        }
//    }


}
