//TODO Needs a full rewrite to use state vector tracking instead of per qubit tracking

package Tests;

import complexClasses.ComplexMath;
import complexClasses.ComplexMatrix;
import complexClasses.ComplexNumber;
import interpreter.jqs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComplexMathTest {
    private static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    private static final ComplexNumber ONE = new ComplexNumber(1, 0);

    @Test
    void testCNOT() {
        jqs jqs = new jqs();
        jqs.device(2);

        System.out.println("Test flipping bit 1 if bit 0 is not 0.0");
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|11⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("Testing reverse CNOT, flip 1 to 1 if 0 is not 0.0, then flip 2 based on 1, then 0 based on 2");
        jqs = new jqs();
        jqs.device(3);
        jqs.X(0);
        jqs.CX(0,1);
        jqs.CX(1,2);
        jqs.CX(2,0);
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|110⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("Test flipping bit 1 if bit 0 is not 0.0 with an imaginary value for control only.");
        jqs = new jqs();
        jqs.device(2);
        jqs.X(0);
        jqs.S(0);
        jqs.CX(0,1);
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000i|11⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTest flipping bit 1 if bit 0 is not 0.0, then flipping bit 2 if 1 is not 0.0");
        jqs = new jqs();
        jqs.device(3);
        jqs.X(0);
        jqs.CX(0,1);
        jqs.CX(1,2);
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));


        System.out.println("\nTest flipping qubit 2 of 4 qubit system based on qubit 0 value being not 0.0");
        jqs = new jqs();
        jqs.device(4);
        jqs.X(0);
        jqs.CX(0,2);
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|0101⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTest flipping bits of 10 qubit system 1 at a time based on the previous qubits value being not 0.0");
        jqs = new jqs();
        jqs.device(10);
        jqs.X(0);
        for(int i = 1; i < 10; i++){
            jqs.CX(0,i);
        }
         jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|1111111111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting flipping qubit 1 and qubit 3 if qubit 0 is not 0.0 using a CGate of CX on Control 0 Target 1 and 3");
        jqs = new jqs();
        jqs.device(4);
        jqs.X(0);
        jqs.CGate("CX", new int[]{0}, new int[]{1, 3});
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|1011⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting not flipping bit 1 if bit 0 is a 0.0");
        jqs = new jqs();
        jqs.device(4);
        jqs.CX(0,1);
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|0000⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting not flipping qubit target if qubit control is a 0.0");
        jqs = new jqs();
        jqs.device(10);
        jqs.CX(6,2);
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|0000000000⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting not flipping bit 1 or bit 3 if bit 0 is a 0.0 using a CGate of CX on Control 0 Target 1 and 3");
        jqs = new jqs();
        jqs.device(4);
        jqs.CGate("CX", new int[]{0}, new int[]{1, 3});
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|0000⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
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
//        System.out.println("MatrixOne: \n" + matrixOne);
//        System.out.println("MatrixTwo: \n" + matrixTwo);
//        System.out.println("Result: \n" + result);
//        System.out.println("Expected: \n" + expected);

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

        System.out.println("\nTranspose Test 1");
        System.out.println("Starting Matrix: \n"+matrix);
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

        System.out.println("\nTranspose Test 2");
        System.out.println("Starting Matrix: \n"+matrix);
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

        System.out.println("\nConjugate Transpose Test");
        System.out.println("Starting Matrix: \n"+matrix);
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
    void testCircuitOne(){
        jqs jqs = new jqs(4);
        jqs.X(0);
        jqs.CX(0,1);
        jqs.Z(1);
        jqs.CX(1,2);
        jqs.S(2);
        jqs.S(2);
        jqs.H(2);
        jqs.S(2);
        jqs.expval();
        assertEquals("|ψ⟩ = 0.707|0011⟩ - -0.707i|0111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }

    @Test
    void testCircuitTwo(){
        //https://quantum.ibm.com/composer/files/61874091bc455a56c155195a02f0495a2c7829e4e8006c2d23bdff6e9e87da45
        jqs jqs = new jqs(4);
        jqs.H(1);
        jqs.X(0);
        jqs.CX(0,1);
        jqs.CX(1,2);
        jqs.expval();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 0.707|0001⟩ + 0.707|0111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

    }

}
