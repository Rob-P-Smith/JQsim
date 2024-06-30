package Tests;

import complexClasses.ComplexMatrix;
import complexClasses.ComplexNumber;
import complexClasses.ComplexObject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ComplexObjectTest {
    private static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    private static final ComplexNumber ONE = new ComplexNumber(1, 0);

    //Tests the 4 output conditions of applying a CNOT gate
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


        control.set(0, 0, ZERO);
        control.set(1, 0, ONE);
        target.set(0, 0, ONE);
        target.set(1, 0, ZERO);
        ComplexMatrix result10 = control.tensorMultiply(control, target);
        assertEquals(0.0, result10.get(0, 0).getReal());
        assertEquals(1.0, result10.get(1, 0).getReal());


        control.set(0, 0, ZERO);
        control.set(1, 0, ONE);
        target.set(0, 0, ZERO);
        target.set(1, 0, ONE);
        ComplexMatrix result11 = control.tensorMultiply(control, target);
        assertEquals(1.0, result11.get(0, 0).getReal());
        assertEquals(0.0, result11.get(1, 0).getReal());

        control.set(0, 0, ONE);
        control.set(1, 0, ZERO);
        target.set(0, 0, ZERO);
        target.set(1, 0, ONE);
        ComplexMatrix result01 = control.tensorMultiply(control, target);
        assertEquals(0.0, result01.get(0, 0).getReal());
        assertEquals(1.0, result01.get(1, 0).getReal());
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

        ComplexMatrix result = matrixOne.multiply(matrixOne, matrixTwo);
        ComplexMatrix expected = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(4, 0), new ComplexNumber(4, 0)},
                {new ComplexNumber(10, 0), new ComplexNumber(8, 0)}
        });
        System.out.println("MatrixOne: \n"+matrixOne);
        System.out.println("MatrixTwo: \n"+matrixTwo);
        System.out.println("Result: \n"+result);
        System.out.println("Expected: \n"+expected);

        for(int i = 0; i < result.getHeight(); i++){
            for(int j = 0; j < result.getWidth(); j++){
                assertTrue(result.get(i,j).getReal() == expected.get(i,j).getReal());
                assertTrue(result.get(i,j).getImag() == expected.get(i,j).getImag());
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

        ComplexMatrix result = matrixOne.add(matrixOne, matrixTwo);
        ComplexMatrix expected = new ComplexMatrix(new ComplexNumber[][]{
                {new ComplexNumber(3, 0), new ComplexNumber(2, 0)},
                {new ComplexNumber(4, 0), new ComplexNumber(6, 0)}
        });

        for(int i = 0; i < result.getHeight(); i++){
            for(int j = 0; j < result.getWidth(); j++){
                assertTrue(result.get(i,j).getReal() == expected.get(i,j).getReal());
                assertTrue(result.get(i,j).getImag() == expected.get(i,j).getImag());
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
            for(int j = 0; j < result.getWidth(); j++){
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
            for(int j = 0; j < result.getWidth(); j++){
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
