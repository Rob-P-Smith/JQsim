//TODO Needs a full rewrite to use state vector tracking instead of per qubit tracking

package Tests;

import complexClasses.*;
import obsolete_Classes.Qops;
import org.junit.jupiter.api.Test;
import state.StateTracker;
import state.WorkItem;
import state.WorkQueue;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ComplexMathTest {
    private static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    private static final ComplexNumber ONE = new ComplexNumber(1, 0);

    @Test
    void testCNOT() {
        StateTracker testCNOTState = new StateTracker(2);
        WorkQueue workQueue = new WorkQueue();
        workQueue.addGate(new WorkItem("PAULI_X", 0));
        workQueue.addGate(new WorkItem("CNOT", 0));
    }

    @Test
    void testTensorMultiply() {
        ComplexMatrix control = new ComplexMatrix(2, 1);
        ComplexMatrix target = new ComplexMatrix(2, 1);

        control.set(0, 0, ONE);
        control.set(1, 0, ZERO);
        target.set(0, 0, ONE);
        target.set(1, 0, ZERO);
        ComplexMatrix result00 = ComplexMath.tensorMultiply(control, target);
        assertEquals(1.0, result00.get(0, 0).getReal());
        assertEquals(0.0, result00.get(1, 0).getReal());
        assertEquals(0.0, result00.get(2, 0).getReal());
        assertEquals(0.0, result00.get(3, 0).getReal());


        control.set(0, 0, ZERO);
        control.set(1, 0, ONE);
        target.set(0, 0, ONE);
        target.set(1, 0, ZERO);
        ComplexMatrix result10 = ComplexMath.tensorMultiply(control, target);
        assertEquals(0.0, result10.get(0, 0).getReal());
        assertEquals(0.0, result10.get(1, 0).getReal());
        assertEquals(1.0, result10.get(2, 0).getReal());
        assertEquals(0.0, result10.get(3, 0).getReal());


        control.set(0, 0, ZERO);
        control.set(1, 0, ONE);
        target.set(0, 0, ZERO);
        target.set(1, 0, ONE);
        ComplexMatrix result11 = ComplexMath.tensorMultiply(control, target);
        assertEquals(0.0, result11.get(0, 0).getReal());
        assertEquals(0.0, result11.get(1, 0).getReal());
        assertEquals(0.0, result11.get(2, 0).getReal());
        assertEquals(1.0, result11.get(3, 0).getReal());

        control.set(0, 0, ONE);
        control.set(1, 0, ZERO);
        target.set(0, 0, ZERO);
        target.set(1, 0, ONE);
        ComplexMatrix result01 = ComplexMath.tensorMultiply(control, target);
        assertEquals(0.0, result01.get(0, 0).getReal());
        assertEquals(1.0, result01.get(1, 0).getReal());
        assertEquals(0.0, result00.get(2, 0).getReal());
        assertEquals(0.0, result00.get(3, 0).getReal());
    }

    @Test
    void testMultiply() {
        ComplexMatrix matrixOne = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1, 0), new ComplexNumber(2, 0)},
                {new ComplexNumber(3, 0), new ComplexNumber(4, 0)}
        });
        ComplexMatrix matrixTwo = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(2, 0), new ComplexNumber(0, 0)},
                {new ComplexNumber(1, 0), new ComplexNumber(2, 0)}
        });

        ComplexMatrix result = ComplexMath.multiplyMatrix(matrixOne, matrixTwo);
        ComplexMatrix expected = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(4, 0), new ComplexNumber(4, 0)},
                {new ComplexNumber(10, 0), new ComplexNumber(8, 0)}
        });
        System.out.println("MatrixOne: \n" + matrixOne);
        System.out.println("MatrixTwo: \n" + matrixTwo);
        System.out.println("Result: \n" + result);
        System.out.println("Expected: \n" + expected);

        for (int i = 0; i < result.getHeight(); i++) {
            for (int j = 0; j < result.getWidth(); j++) {
                assertTrue(result.get(i, j).getReal() == expected.get(i, j).getReal());
                assertTrue(result.get(i, j).getImag() == expected.get(i, j).getImag());
            }
        }
    }

    @Test
    void testAdd() {
        ComplexMatrix matrixOne = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1, 0), new ComplexNumber(2, 0)},
                {new ComplexNumber(3, 0), new ComplexNumber(4, 0)}
        });
        ComplexMatrix matrixTwo = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(2, 0), new ComplexNumber(0, 0)},
                {new ComplexNumber(1, 0), new ComplexNumber(2, 0)}
        });

        ComplexMatrix result = ComplexMath.addMatrix(matrixOne, matrixTwo);
        ComplexMatrix expected = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(3, 0), new ComplexNumber(2, 0)},
                {new ComplexNumber(4, 0), new ComplexNumber(6, 0)}
        });

        for (int i = 0; i < result.getHeight(); i++) {
            for (int j = 0; j < result.getWidth(); j++) {
                assertTrue(result.get(i, j).getReal() == expected.get(i, j).getReal());
                assertTrue(result.get(i, j).getImag() == expected.get(i, j).getImag());
            }
        }
    }

    @Test
    void testTranspose() {
        ComplexMatrix matrix = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1, 0), new ComplexNumber(2, 0), new ComplexNumber(3, 0)},
                {new ComplexNumber(4, 0), new ComplexNumber(5, 0), new ComplexNumber(6, 0)}
        });

        ComplexMatrix result = ComplexMath.getTranspose(matrix);
        ComplexMatrix expected = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1, 0), new ComplexNumber(4, 0)},
                {new ComplexNumber(2, 0), new ComplexNumber(5, 0)},
                {new ComplexNumber(3, 0), new ComplexNumber(6, 0)}
        });

        System.out.println("Result \n" + result);
        System.out.println("Expected: \n" + expected);
        for (int i = 0; i < result.getHeight(); i++) {
            for (int j = 0; j < result.getWidth(); j++) {
                assertTrue(expected.get(i, j).getReal() == result.get(i, j).getReal());
                assertTrue(expected.get(i, j).getImag() == result.get(i, j).getImag());
            }
        }

        matrix = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1, 0), new ComplexNumber(4, 0)},
                {new ComplexNumber(2, 0), new ComplexNumber(5, 0)},
                {new ComplexNumber(3, 0), new ComplexNumber(6, 0)}
        });

        result = ComplexMath.getTranspose(matrix);
        expected = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1, 0), new ComplexNumber(2, 0), new ComplexNumber(3, 0)},
                {new ComplexNumber(4, 0), new ComplexNumber(5, 0), new ComplexNumber(6, 0)}
        });

        System.out.println("Result \n" + result);
        System.out.println("Expected: \n" + expected);
        for (int i = 0; i < result.getHeight(); i++) {
            for (int j = 0; j < result.getWidth(); j++) {
                assertTrue(expected.get(i, j).getReal() == result.get(i, j).getReal());
                assertTrue(expected.get(i, j).getImag() == result.get(i, j).getImag());
            }
        }
    }

    @Test
    void testConjugateTranspose() {
        ComplexMatrix matrix = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1, 1), new ComplexNumber(2, 2)},
                {new ComplexNumber(3, 3), new ComplexNumber(4, 4)}
        });

        ComplexMatrix result = ComplexMath.getConjugateTranspose(matrix);
        ComplexMatrix expected = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(1, -1), new ComplexNumber(3, -3)},
                {new ComplexNumber(2, -2), new ComplexNumber(4, -4)}
        });

        System.out.println("Result: \n" + result);
        System.out.println("Expected: \n" + expected);

        for (int i = 0; i < result.getHeight(); i++) {
            for (int j = 0; j < result.getWidth(); j++) {
                assertTrue(expected.get(i, j).getReal() == result.get(i, j).getReal());
                assertTrue(expected.get(i, j).getImag() == result.get(i, j).getImag());
            }
        }
    }

    @Test
    void probabilitiesTest(){
        ComplexQubit testBitOne = new ComplexQubit();
        ComplexQubit testBitTwo = new ComplexQubit();
        int shots = 1000000;
        Map<String, Integer> results = new HashMap<>();
//        testBitOne.setState(ComplexGates.applyHadamard(testBitOne.getState()));
//        ComplexGates.applyCNOT(testBitOne, testBitTwo);

        for(int i = 0; i < shots; i++) {
            ComplexMatrix result = Qops.measureMat(testBitOne.getState());
            results.put(result.toString(), results.getOrDefault(result.toString(), 0)+1);
        }

        for(String key : results.keySet()) {
            double percentage = (double) results.get(key).intValue()*100 /shots;
            System.out.println("Qubit State: ");
            System.out.println(key + " occured "+results.get(key)+" times "+ "average of "+percentage+"%");
        }
        String[] keys = results.keySet().toArray(new String[0]);
        assertTrue(keys.length == 2);
        assertEquals(shots, results.get(keys[0]) + results.get(keys[1]));

    }

    @Test
    void testSGate(){
        // write Sgate test for
        // H -> S == .707 real and .707 imag
        // H -> S -> S == .707 real and -.707 imag
        // H -> S -> S -> H == 1.0
        assertEquals(true, (String.valueOf(true).equals(String.valueOf(false))));
    }
}
