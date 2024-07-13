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
        jqs.X(2);
        jqs.getState();
        ComplexMatrix state = new ComplexMatrix(jqs.getStateVec().getHeight(), 1);
        state.setData(jqs.getStateVec().getData());
        convert(state);

    }

    private static void convert(ComplexMatrix state) {
        System.out.println(state);
        int numQubits = (int) (Math.log(state.getHeight()) / Math.log(2));
        int numRows = state.getHeight();
        ComplexQubit[] qubits = new ComplexQubit[numQubits];
        for (int i = 0; i < numQubits; i++) {
            qubits[i] = new ComplexQubit(new ComplexNumber(1), new ComplexNumber(0), i);
        }

        //The magic loop!
        for (int row = 0; row < numRows; row++) {
            for (int qubit = 0; qubit < numQubits; qubit++) {
                int qubitStep = (int) Math.pow(2, qubit);
                if ((row & qubitStep) != 0) {
                    if ((state.get(row, 0).getReal()) != 0.0 ||
                         state.get(row, 0).getImag() != 0.0) {
                        qubits[qubit] = new ComplexQubit(new ComplexNumber(0, 0),
                                                         new ComplexNumber(state.get(row, 0).getReal(), state.get(row, 0).getImag()),
                                                         qubit);
                    }
                }
            }
        }

        for (int qubitRow = 0; qubitRow < numQubits; qubitRow++) {
            System.out.println(qubits[qubitRow]);
        }
    }
}
