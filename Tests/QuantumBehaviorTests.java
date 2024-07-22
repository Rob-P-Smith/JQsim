import complex_classes.ComplexMath;
import interpreter.jqs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuantumBehaviorTests {

    @Test
    void testCNOT() {
        jqs jqs = new jqs();
        jqs.device(2);

        System.out.println("Test flipping bit 1 if bit 0 is not 0.0");
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|11⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("Testing reverse CNOT, flip 1 to 1 if 0 is not 0.0, then flip 2 based on 1, then 0 based on 2");
        jqs = new jqs();
        jqs.device(3);
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.CX(1, 2);
        jqs.CX(2, 0);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|110⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("Test flipping bit 1 if bit 0 is not 0.0 with an imaginary value for control only.");
        jqs = new jqs();
        jqs.device(2);
        jqs.X(0);
        jqs.S(0);
        jqs.CX(0, 1);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000i|11⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTest flipping bit 1 if bit 0 is not 0.0, then flipping bit 2 if 1 is not 0.0");
        jqs = new jqs();
        jqs.device(3);
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.CX(1, 2);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));


        System.out.println("\nTest flipping qubit 2 of 4 qubit system based on qubit 0 value being not 0.0");
        jqs = new jqs();
        jqs.device(4);
        jqs.X(0);
        jqs.CX(0, 2);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|0101⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTest flipping bits of 10 qubit system 1 at a time based on the previous qubits value being not 0.0");
        jqs = new jqs();
        jqs.device(10);
        jqs.X(0);
        for (int i = 1; i < 10; i++) {
            jqs.CX(0, i);
        }
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|1111111111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting flipping qubit 1 and qubit 3 if qubit 0 is not 0.0 using a CGate of CX on Control 0 Target 1 and 3");
        jqs = new jqs();
        jqs.device(4);
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.CX(0, 3);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|1011⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting not flipping bit 1 if bit 0 is a 0.0");
        jqs = new jqs();
        jqs.device(4);
        jqs.CX(0, 1);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|0000⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting not flipping qubit target if qubit control is a 0.0");
        jqs = new jqs();
        jqs.device(10);
        jqs.CX(6, 2);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|0000000000⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting not flipping bit 1 or bit 3 if bit 0 is a 0.0 using a CGate of CX on Control 0 Target 1 and 3");
        jqs = new jqs();
        jqs.device(4);
        jqs.CX(0, 1);
        jqs.CX(0, 3);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 1.000|0000⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }

    @Test
    public void testQuantumKickback() {
        jqs jqs = new jqs(3);

        jqs.H(0);
        jqs.X(1);
        //test state that doesn't have kickback
        jqs.CX(0, 1);
        jqs.getComputationalState();
//        System.out.println("Final Outcome: " + ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 0.707|001⟩, 0.707|010⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        jqs jqs2 = new jqs(3);
        //test that kickback occurs on control qubit
        jqs2.H(0);
        jqs2.X(1);
        jqs2.CZ(0, 1);
        jqs2.getComputationalState();
//        System.out.println("Final Outcome: " + ComplexMath.complexMatrixToDiracNotation(jqs2.getStateVec()));
        assertEquals("|ψ⟩ = 0.707|010⟩, -0.707|011⟩", ComplexMath.complexMatrixToDiracNotation(jqs2.getStateVec()));
    }

    @Test
    public void testQuantumTeleportation(){
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.H(1);
        jqs.CX(1,2);
        jqs.H(0);
        jqs.CX(0,1);
        jqs.measureQubit(0);
        jqs.measureQubit(1);
        jqs.CX(1,2);
        jqs.CZ(0,2); jqs.measureQubit(2);
    }

    @Test
        //pics/testCircuitOne.jpg
    void testCircuitOne() {
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.Z(1);
        jqs.CX(1, 2);
        jqs.H(1);
        jqs.H(2);
        jqs.S(2);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = -0.500|001⟩, 0.500|011⟩, 0.500i|101⟩, -0.500i|111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }

    @Test
        //pics/testCircuitTwo.jpg
    void testCircuitTwo() {
        //https://quantum.ibm.com/composer/files/61874091bc455a56c155195a02f0495a2c7829e4e8006c2d23bdff6e9e87da45
        jqs jqs = new jqs(2);
        jqs.X(0);
        jqs.H(0);
        jqs.CX(0, 1);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 0.707|00⟩, -0.707|11⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

    }

    @Test
        //pics/testCircuitThree.jpg
    void testCircuitThree() {
        jqs jqs = new jqs(3);
        jqs.H(0);
        jqs.CX(0, 1);
        jqs.Z(1);
        jqs.CX(1, 2);
        jqs.S(2);
        jqs.S(2);
        jqs.H(2);
        jqs.S(2);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 0.500|000⟩, 0.500|011⟩, 0.500i|100⟩, -0.500i|111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }

    @Test
        //pics/testCircuitFour.jpg
    void testCircuitFour() {
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.H(1);
        jqs.CX(1, 2);
        jqs.CX(0, 1);
        jqs.getComputationalState();
        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = 0.707|011⟩, 0.707|101⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }
}

