package Tests;

import complexClasses.ComplexGates;
import complexClasses.ComplexMatrix;
import complexClasses.ComplexNumber;
import complexClasses.ComplexQubit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexObjectTest {
    private static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    private static final ComplexNumber ONE = new ComplexNumber(1, 0);

    @Test
    void testCNOT_old() {
        ComplexQubit qubitOne = new ComplexQubit();
        ComplexQubit qubitTwo = new ComplexQubit();
        String breaker = ("/////////////////////////////////");
        System.out.println("Starting qubit tests of |00>, |01>, |10>, |11> for CX 0,1 and CX 1,0");

        ////////////////////////////////
        System.out.println("\n" + breaker + "\n|00> Test for CX 0,1\n" + qubitOne + "\n" + qubitTwo);
        ComplexGates.applyCNOT(qubitOne, qubitTwo);
        System.out.println("Result: \n" + qubitOne + "\n" + qubitTwo + "\n" + breaker);
        assertEquals(1.0, qubitOne.getState().get(0, 0).getReal());
        assertEquals(0.0, qubitOne.getState().get(1, 0).getReal());
        assertEquals(1.0, qubitTwo.getState().get(0, 0).getReal());
        assertEquals(0.0, qubitTwo.getState().get(1, 0).getReal());

        ////////////////////////////////
        System.out.println("\n" + breaker + "\n|00> Test for CX 1,0\n" + qubitOne + "\n" + qubitTwo);
        ComplexGates.applyCNOT(qubitTwo, qubitOne);
        System.out.println("Result \n" + qubitOne + "\n" + breaker);
        assertEquals(1.0, qubitOne.getState().get(0, 0).getReal());
        assertEquals(0.0, qubitOne.getState().get(1, 0).getReal());
        assertEquals(1.0, qubitTwo.getState().get(0, 0).getReal());
        assertEquals(0.0, qubitTwo.getState().get(1, 0).getReal());

        ////////////////////////////////
        qubitOne = new ComplexQubit();
        qubitTwo = new ComplexQubit();
        System.out.println("\n" + breaker + "\n|10> Test for CX 0,1");
        qubitOne.setState(ComplexGates.applyPauliX(qubitOne.getState()));
        System.out.println(qubitOne + "\n" + qubitTwo);
        ComplexGates.applyCNOT(qubitOne, qubitTwo);
        System.out.println("\nResult: \n" + qubitOne + "\n" + qubitTwo + "\n" + breaker);
        assertEquals(0.0, qubitOne.getState().get(0, 0).getReal());
        assertEquals(1.0, qubitOne.getState().get(1, 0).getReal());
        assertEquals(0.0, qubitTwo.getState().get(0, 0).getReal());
        assertEquals(1.0, qubitTwo.getState().get(1, 0).getReal());

        ////////////////////////////////
        qubitOne = new ComplexQubit();
        qubitTwo = new ComplexQubit();
        System.out.println("\n" + breaker + "\n|10> Test for CX 1,0");
        qubitOne.setState(ComplexGates.applyPauliX(qubitOne.getState()));
        System.out.println(qubitOne + "\n" + qubitTwo);
        ComplexGates.applyCNOT(qubitTwo, qubitOne);
        System.out.println("Result \n" + qubitOne + "\n" + qubitTwo + "\n" + breaker);
        assertEquals(0.0, qubitOne.getState().get(0, 0).getReal());
        assertEquals(1.0, qubitOne.getState().get(1, 0).getReal());
        assertEquals(1.0, qubitTwo.getState().get(0, 0).getReal());
        assertEquals(0.0, qubitTwo.getState().get(1, 0).getReal());

        ////////////////////////////////
        qubitOne = new ComplexQubit();
        qubitTwo = new ComplexQubit();
        System.out.println("\n" + breaker + "\n|01> Test for CX 0,1");
        qubitTwo.setState(ComplexGates.applyPauliX(qubitTwo.getState()));
        System.out.println(qubitOne + "\n" + qubitTwo);
        ComplexGates.applyCNOT(qubitOne, qubitTwo);
        System.out.println("Result: \n" + qubitOne + "\n" + qubitTwo + "\n" + breaker);
        assertEquals(1.0, qubitOne.getState().get(0, 0).getReal());
        assertEquals(0.0, qubitOne.getState().get(1, 0).getReal());
        assertEquals(0.0, qubitTwo.getState().get(0, 0).getReal());
        assertEquals(1.0, qubitTwo.getState().get(1, 0).getReal());

        //////////////////////////////
        qubitOne = new ComplexQubit();
        qubitTwo = new ComplexQubit();
        System.out.println("\n" + breaker + "\n|01> Test for CX 1,0");
        qubitTwo.setState(ComplexGates.applyPauliX(qubitTwo.getState()));
        System.out.println(qubitOne + "\n" + qubitTwo);
        ComplexGates.applyCNOT(qubitTwo, qubitOne);
        System.out.println("Result: \n" + qubitOne + "\n" + qubitTwo + "\n" + breaker);
        assertEquals(0.0, qubitOne.getState().get(0, 0).getReal());
        assertEquals(1.0, qubitOne.getState().get(1, 0).getReal());
        assertEquals(0.0, qubitTwo.getState().get(0, 0).getReal());
        assertEquals(1.0, qubitTwo.getState().get(1, 0).getReal());

        ////////////////////////////////
        qubitOne = new ComplexQubit();
        qubitTwo = new ComplexQubit();
        System.out.println("\n" + breaker + "\n|11> Test for CX 0,1");
        qubitTwo.setState(ComplexGates.applyPauliX(qubitTwo.getState()));
        qubitOne.setState(ComplexGates.applyPauliX(qubitOne.getState()));
        System.out.println(qubitOne + "\n" + qubitTwo);
        ComplexGates.applyCNOT(qubitOne, qubitTwo);
        System.out.println("Result: \n" + qubitOne + "\n" + qubitTwo + "\n" + breaker);
        assertEquals(0.0, qubitOne.getState().get(0, 0).getReal());
        assertEquals(1.0, qubitOne.getState().get(1, 0).getReal());
        assertEquals(1.0, qubitTwo.getState().get(0, 0).getReal());
        assertEquals(0.0, qubitTwo.getState().get(1, 0).getReal());

        ////////////////////////////////
        qubitOne = new ComplexQubit();
        qubitTwo = new ComplexQubit();
        System.out.println("\n" + breaker + "\n|11> Test for CX 1,0");
        qubitTwo.setState(ComplexGates.applyPauliX(qubitTwo.getState()));
        qubitOne.setState(ComplexGates.applyPauliX(qubitOne.getState()));
        System.out.println(qubitOne + "\n" + qubitTwo);
        ComplexGates.applyCNOT(qubitTwo, qubitOne);
        System.out.println("Result: \n" + qubitOne + "\n" + qubitTwo + "\n" + breaker);
        assertEquals(1.0, qubitOne.getState().get(0, 0).getReal());
        assertEquals(0.0, qubitOne.getState().get(1, 0).getReal());
        assertEquals(0.0, qubitTwo.getState().get(0, 0).getReal());
        assertEquals(1.0, qubitTwo.getState().get(1, 0).getReal());
    }

    @Test
    void testTensorMultiply() {
        ComplexMatrix control = new ComplexMatrix(2, 1);
        ComplexMatrix target = new ComplexMatrix(2, 1);

        control.set(0, 0, ONE);
        control.set(1, 0, ZERO);
        target.set(0, 0, ONE);
        target.set(1, 0, ZERO);
        ComplexMatrix result00 = control.tensorMultiply(control, target);
        assertEquals(1.0, result00.get(0, 0).getReal());
        assertEquals(0.0, result00.get(1, 0).getReal());
        assertEquals(0.0, result00.get(2, 0).getReal());
        assertEquals(0.0, result00.get(3, 0).getReal());


        control.set(0, 0, ZERO);
        control.set(1, 0, ONE);
        target.set(0, 0, ONE);
        target.set(1, 0, ZERO);
        ComplexMatrix result10 = control.tensorMultiply(control, target);
        assertEquals(0.0, result10.get(0, 0).getReal());
        assertEquals(0.0, result10.get(1, 0).getReal());
        assertEquals(1.0, result10.get(2, 0).getReal());
        assertEquals(0.0, result10.get(3, 0).getReal());


        control.set(0, 0, ZERO);
        control.set(1, 0, ONE);
        target.set(0, 0, ZERO);
        target.set(1, 0, ONE);
        ComplexMatrix result11 = control.tensorMultiply(control, target);
        assertEquals(0.0, result11.get(0, 0).getReal());
        assertEquals(0.0, result11.get(1, 0).getReal());
        assertEquals(0.0, result11.get(2, 0).getReal());
        assertEquals(1.0, result11.get(3, 0).getReal());

        control.set(0, 0, ONE);
        control.set(1, 0, ZERO);
        target.set(0, 0, ZERO);
        target.set(1, 0, ONE);
        ComplexMatrix result01 = control.tensorMultiply(control, target);
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

        ComplexMatrix result = matrixOne.multiplyMatrix(matrixOne, matrixTwo);
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

        ComplexMatrix result = matrixOne.addMatrix(matrixOne, matrixTwo);
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

        ComplexMatrix result = matrix.getTranspose(matrix);
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

        result = matrix.getTranspose(matrix);
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

        ComplexMatrix result = matrix.getConjugateTranspose(matrix);
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
}
