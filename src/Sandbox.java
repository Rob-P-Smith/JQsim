import complexClasses.ComplexMath;
import complexClasses.ComplexMatrix;
import complexClasses.ComplexNumber;
import complexClasses.ComplexQubit;
import interpreter.jqs;

import java.util.Arrays;

/**
 * Just a class for debugging things while building the program.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 11 July 2024
 */

public class Sandbox {
    public static void main(String[] args) {
        jqs jqs = new jqs(3);
        jqs.X(1);
        jqs.H(0);
        jqs.CX(0,2);
        jqs.getState();
        System.out.println("\nFinal State of System: \n"+ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        System.out.println(jqs.getStateVec());
    }

    //test code for finding possible states from a probabilistic state vector
//    private static void convert(ComplexMatrix state) {
////        System.out.println("Entry Point to convert method: "+ComplexMath.complexMatrixToDiracNotation(state));
//        int numQubits = (int) (Math.log(state.getHeight()) / Math.log(2));
//        int numRows = state.getHeight();
//
//        ComplexMatrix[] possibleStates = new ComplexMatrix[state.getHeight()];
//
//        //The magic loop!
//        // This needs to build state vectors that represent both cases of the control qubit, 0 and 1.
//        // Beta probability is found by loop at 'row' when not 0.0 and Alpha is found by 'row-qubitStep'.
//        for (int row = 0; row < numRows; row++) {
////            System.out.println("\nRow"+row);  //shows which row is currently being checked
//            for (int qubit = 0; qubit < numQubits; qubit++) {
//                int qubitStep = (int) Math.pow(2, qubit);
//                if ((row & qubitStep) != 0 || row == 0) {
//                    double realValue = state.get(row, 0).getReal();
//                    double imagValue = state.get(row, 0).getImag();
////                    System.out.println("Qubits: "+qubit); // shows which qubits are checked in a given row for their beta value
//                    if (realValue < 0.0 || imagValue < 0.0) {
//                        if ((state.get(row, 0).getReal()) != 0.0) {
//                            possibleStates[row] = new ComplexMatrix(numRows, 1);
//                            possibleStates[row].set(row, 0, new ComplexNumber(-1));
//
//                        }
//                        if (state.get(row, 0).getImag() != 0.0) {
//                            possibleStates[row] = new ComplexMatrix(state.getHeight(), 1);
//                            possibleStates[row].set(row, 0, new ComplexNumber(0, -1));
//                        }
//                    }
//                    if (realValue > 0.0 || imagValue > 0.0) {
//                        if ((state.get(row, 0).getReal()) != 0.0) {
//                            possibleStates[row] = new ComplexMatrix(numRows, 1);
//                            possibleStates[row].set(row, 0, new ComplexNumber(1));
//
//                        }
//                        if (state.get(row, 0).getImag() != 0.0) {
//                            possibleStates[row] = new ComplexMatrix(numRows, 1);
//                            possibleStates[row].set(row, 0, new ComplexNumber(0, 1));
//                        }
//                    }
//                }
//            }
//        }
//        for (ComplexMatrix possibility : possibleStates) {
//            if (possibility == null) {
//                System.out.println("Not a possibility.\n");
//            } else {
//                System.out.println(possibility);
//            }
//        }
//    }
}
