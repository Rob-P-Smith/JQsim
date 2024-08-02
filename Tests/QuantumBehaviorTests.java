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
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 1.00000 |11⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("Testing reverse CNOT, flip 1 to 1 if 0 is not 0.0, then flip 2 based on 1, then 0 based on 2");
        jqs = new jqs();
        jqs.device(3);
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.CX(1, 2);
        jqs.CX(2, 0);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 1.00000 |110⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("Test flipping bit 1 if bit 0 is not 0.0 with an imaginary value for control only.");
        jqs = new jqs();
        jqs.device(2);
        jqs.X(0);
        jqs.S(0);
        jqs.CX(0, 1);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{90.000°} 1.00000i |11⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTest flipping bit 1 if bit 0 is not 0.0, then flipping bit 2 if 1 is not 0.0");
        jqs = new jqs();
        jqs.device(3);
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.CX(1, 2);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 1.00000 |111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting flipping qubit 1 and qubit 3 if qubit 0 is not 0.0 using a CGate of CX on Control 0 Target 1 and 3");
        jqs = new jqs();
        jqs.device(4);
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.CX(0, 3);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 1.00000 |1011⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting not flipping bit 1 if bit 0 is a 0.0");
        jqs = new jqs();
        jqs.device(4);
        jqs.CX(0, 1);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 1.00000 |0000⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        System.out.println("\nTesting not flipping bit 1 or bit 3 if bit 0 is a 0.0 using a CGate of CX on Control 0 Target 1 and 3");
        jqs = new jqs();
        jqs.device(4);
        jqs.CX(0, 1);
        jqs.CX(0, 3);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 1.00000 |0000⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }

    @Test
    void testCSWAP() {
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.X(2);
        jqs.CSWAP(0, 1, 2);
        jqs.getComputationalState();
//        System.out.println(jqs);
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 1.00000 |011⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        jqs = new jqs(3);
        jqs.X(2);
        jqs.CSWAP(0, 1, 2);
        jqs.getComputationalState();
//        System.out.println(jqs);
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 1.00000 |100⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22X%22,1,%22X%22],[1,1,%22Z^%C2%BC%22]]}
        jqs = new jqs(3);
        jqs.X(0);
        jqs.X(2);
        jqs.T(2);
        jqs.getComputationalState();
//        System.out.println(jqs);
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{45.000°} (0.70711 + 0.70711i) |101⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22X%22,1,%22X%22],[1,1,%22Z^%C2%BC%22],[%22Swap%22,1,%22Swap%22]]}
        jqs = new jqs(3);
        jqs.X(0);
        jqs.X(2);
        jqs.T(2);
        jqs.CSWAP(0, 1, 2);
        jqs.getComputationalState();
//        System.out.println(jqs);
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{45.000°} (0.70711 + 0.70711i) |011⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }

    @Test
    public void testQuantumKickback() {
        jqs jqs = new jqs(2);

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22X%22],[%22%E2%80%A2%22,%22X%22]]}
        jqs.H(0);
        jqs.X(1);
        jqs.CX(0, 1);
        jqs.getComputationalState();
//        System.out.println("Final Outcome: " + ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 0.70711 |01⟩\n" +
                "{0.000°} 0.70711 |10⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22X%22],[%22%E2%80%A2%22,%22Z%22]]}
        jqs jqs2 = new jqs(2);
        //test that kickback occurs on control qubit
        jqs2.H(0);
        jqs2.X(1);
        jqs2.CZ(0, 1);
        jqs2.getComputationalState();
//        System.out.println("Final Outcome: " + ComplexMath.complexMatrixToDiracNotation(jqs2.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 0.70711 |10⟩\n" +
                "{-180.000°} -0.70711 |11⟩", ComplexMath.complexMatrixToDiracNotation(jqs2.getStateVec()));

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22,%22H%22],[1,%22Z^%C2%BC%22],[%22%E2%80%A2%22,%22X%22],[%22H%22],[%22%E2%80%A2%22,1,%22X%22]]}
        jqs jqs3 = new jqs(4);
        jqs3.H(0);
        jqs3.H(1);
        jqs3.T(1);
        jqs3.CX(0, 1);
        jqs3.H(0);
        jqs3.CX(0, 2);
        jqs3.getComputationalState();
//        System.out.println(jqs3);
        assertEquals("|ψ⟩ = \n" +
                        "{phase} amplitude |basis⟩ \n" +
                        "-------------------------\n" +
                        "{22.500°} (0.60355 + 0.25000i) |0000⟩\n" +
                        "{22.500°} (0.60355 + 0.25000i) |0010⟩\n" +
                        "{-67.500°} (0.10355 + -0.25000i) |0101⟩\n" +
                        "{112.500°} (-0.10355 + 0.25000i) |0111⟩",
                ComplexMath.complexMatrixToDiracNotation(jqs3.getStateVec()));

    }

    @Test
    public void testQuantumTeleportation() {
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.H(1);
        jqs.CX(1, 2);
        jqs.H(0);
        jqs.CX(0, 1);
        jqs.measureQubit(0);
        jqs.measureQubit(1);
        jqs.CX(1, 2);
        jqs.CZ(0, 2);
        jqs.measureQubit(2);
    }

    @Test
    public void quantumFourierTransformTests() {

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22X%22,%22X%22,%22X%22,%22X%22],[1,1,%22Z^%C2%BC%22],[1,1,%22Z^%C2%BD%22],[%22QFT4%22]]}
        jqs jqs = new jqs(4);
        jqs.X(0);
        jqs.X(1);
        jqs.X(2);
        jqs.T(2);
        jqs.S(2);
        jqs.X(3);
        jqs.getComputationalState();
        jqs.QFT();
//        System.out.println(jqs);
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{135.000°} (-0.17678 + 0.17678i) |0000⟩\n" +
                "{112.500°} (-0.09567 + 0.23097i) |0001⟩\n" +
                "{90.000°} 0.25000i |0010⟩\n" +
                "{67.500°} (0.09567 + 0.23097i) |0011⟩\n" +
                "{45.000°} (0.17678 + 0.17678i) |0100⟩\n" +
                "{22.500°} (0.23097 + 0.09567i) |0101⟩\n" +
                "{-0.000°} 0.25000 |0110⟩\n" +
                "{-22.500°} (0.23097 + -0.09567i) |0111⟩\n" +
                "{-45.000°} (0.17678 + -0.17678i) |1000⟩\n" +
                "{-67.500°} (0.09567 + -0.23097i) |1001⟩\n" +
                "{-90.000°} -0.25000i |1010⟩\n" +
                "{-112.500°} (-0.09567 + -0.23097i) |1011⟩\n" +
                "{-135.000°} (-0.17678 + -0.17678i) |1100⟩\n" +
                "{-157.500°} (-0.23097 + -0.09567i) |1101⟩\n" +
                "{180.000°} -0.25000 |1110⟩\n" +
                "{157.500°} (-0.23097 + 0.09567i) |1111⟩", jqs.toString());

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22X%22,%22X%22,%22X%22,%22X%22],[1,1,%22Z^%C2%BC%22],[1,1,%22Z^%C2%BD%22],[%22QFT4%22]]}
        jqs = new jqs(4);
        jqs.X(0);
        jqs.X(1);
        jqs.X(2);
        jqs.T(2);
        jqs.S(2);
        jqs.X(3);
        jqs.getComputationalState();
        jqs.QFT();
//        System.out.println(jqs);
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{135.000°} (-0.17678 + 0.17678i) |0000⟩\n" +
                "{112.500°} (-0.09567 + 0.23097i) |0001⟩\n" +
                "{90.000°} 0.25000i |0010⟩\n" +
                "{67.500°} (0.09567 + 0.23097i) |0011⟩\n" +
                "{45.000°} (0.17678 + 0.17678i) |0100⟩\n" +
                "{22.500°} (0.23097 + 0.09567i) |0101⟩\n" +
                "{-0.000°} 0.25000 |0110⟩\n" +
                "{-22.500°} (0.23097 + -0.09567i) |0111⟩\n" +
                "{-45.000°} (0.17678 + -0.17678i) |1000⟩\n" +
                "{-67.500°} (0.09567 + -0.23097i) |1001⟩\n" +
                "{-90.000°} -0.25000i |1010⟩\n" +
                "{-112.500°} (-0.09567 + -0.23097i) |1011⟩\n" +
                "{-135.000°} (-0.17678 + -0.17678i) |1100⟩\n" +
                "{-157.500°} (-0.23097 + -0.09567i) |1101⟩\n" +
                "{180.000°} -0.25000 |1110⟩\n" +
                "{157.500°} (-0.23097 + 0.09567i) |1111⟩", jqs.toString());

        //https://algassert.com/quirk#circuit={%22cols%22:[[1,%22X%22,%22X%22,%22X%22],[%22Z^%C2%BC%22],[%22Z^%C2%BD%22],[%22QFT4%22]]}
        jqs = new jqs(4);
        jqs.T(0);
        jqs.S(0);
        jqs.X(1);
        jqs.X(2);
        jqs.X(3);
        jqs.getComputationalState();
        jqs.QFT();
//        System.out.println(jqs);
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 0.25000 |0000⟩\n" +
                "{-45.000°} (0.17678 + -0.17678i) |0001⟩\n" +
                "{-90.000°} -0.25000i |0010⟩\n" +
                "{-135.000°} (-0.17678 + -0.17678i) |0011⟩\n" +
                "{180.000°} -0.25000 |0100⟩\n" +
                "{135.000°} (-0.17678 + 0.17678i) |0101⟩\n" +
                "{90.000°} 0.25000i |0110⟩\n" +
                "{45.000°} (0.17678 + 0.17678i) |0111⟩\n" +
                "{0.000°} 0.25000 |1000⟩\n" +
                "{-45.000°} (0.17678 + -0.17678i) |1001⟩\n" +
                "{-90.000°} -0.25000i |1010⟩\n" +
                "{-135.000°} (-0.17678 + -0.17678i) |1011⟩\n" +
                "{180.000°} -0.25000 |1100⟩\n" +
                "{135.000°} (-0.17678 + 0.17678i) |1101⟩\n" +
                "{90.000°} 0.25000i |1110⟩\n" +
                "{45.000°} (0.17678 + 0.17678i) |1111⟩", jqs.toString());

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22X%22,%22X%22,%22X%22,%22X%22,%22X%22,%22X%22,%22X%22],[1,1,%22Z^%C2%BC%22,1,%22Z^-%C2%BC%22,1,%22Z^-%C2%BD%22],[1,1,1,1,1,1,%22Z%22],[1,1,%22Z^%C2%BD%22,1,%22Z^-%C2%BD%22,1,%22Z^%C2%BC%22],[%22QFT7%22]]}
        jqs = new jqs(7);
        jqs.X(0);
        jqs.X(1);
        jqs.X(2);
        jqs.T(2);
        jqs.S(2);
        jqs.X(3);
        jqs.X(4);
        jqs.Ti(4);
        jqs.Si(4);
        jqs.X(5);
        jqs.X(6);
        jqs.Si(6);
        jqs.Z(6);
        jqs.T(6);
        jqs.getComputationalState();
        jqs.QFT();
//        System.out.println(jqs);
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{135.000°} (-0.06250 + 0.06250i) |0000000⟩\n" +
                "{132.187°} (-0.05936 + 0.06549i) |0000001⟩\n" +
                "{129.375°} (-0.05607 + 0.06833i) |0000010⟩\n" +
                "{126.562°} (-0.05265 + 0.07099i) |0000011⟩\n" +
                "{123.750°} (-0.04911 + 0.07349i) |0000100⟩\n" +
                "{120.938°} (-0.04544 + 0.07581i) |0000101⟩\n" +
                "{118.125°} (-0.04167 + 0.07795i) |0000110⟩\n" +
                "{115.312°} (-0.03779 + 0.07990i) |0000111⟩\n" +
                "{112.500°} (-0.03382 + 0.08166i) |0001000⟩\n" +
                "{109.688°} (-0.02978 + 0.08322i) |0001001⟩\n" +
                "{106.875°} (-0.02566 + 0.08458i) |0001010⟩\n" +
                "{104.062°} (-0.02148 + 0.08574i) |0001011⟩\n" +
                "{101.250°} (-0.01724 + 0.08669i) |0001100⟩\n" +
                "{98.437°} (-0.01297 + 0.08743i) |0001101⟩\n" +
                "{95.625°} (-0.00866 + 0.08796i) |0001110⟩\n" +
                "{92.812°} (-0.00434 + 0.08828i) |0001111⟩\n" +
                "{90.000°} 0.08839i |0010000⟩\n" +
                "{87.187°} (0.00434 + 0.08828i) |0010001⟩\n" +
                "{84.375°} (0.00866 + 0.08796i) |0010010⟩\n" +
                "{81.562°} (0.01297 + 0.08743i) |0010011⟩\n" +
                "{78.750°} (0.01724 + 0.08669i) |0010100⟩\n" +
                "{75.937°} (0.02148 + 0.08574i) |0010101⟩\n" +
                "{73.125°} (0.02566 + 0.08458i) |0010110⟩\n" +
                "{70.312°} (0.02978 + 0.08322i) |0010111⟩\n" +
                "{67.500°} (0.03382 + 0.08166i) |0011000⟩\n" +
                "{64.687°} (0.03779 + 0.07990i) |0011001⟩\n" +
                "{61.875°} (0.04167 + 0.07795i) |0011010⟩\n" +
                "{59.062°} (0.04544 + 0.07581i) |0011011⟩\n" +
                "{56.250°} (0.04911 + 0.07349i) |0011100⟩\n" +
                "{53.437°} (0.05265 + 0.07099i) |0011101⟩\n" +
                "{50.625°} (0.05607 + 0.06833i) |0011110⟩\n" +
                "{47.812°} (0.05936 + 0.06549i) |0011111⟩\n" +
                "{45.000°} (0.06250 + 0.06250i) |0100000⟩\n" +
                "{42.187°} (0.06549 + 0.05936i) |0100001⟩\n" +
                "{39.375°} (0.06833 + 0.05607i) |0100010⟩\n" +
                "{36.562°} (0.07099 + 0.05265i) |0100011⟩\n" +
                "{33.750°} (0.07349 + 0.04911i) |0100100⟩\n" +
                "{30.937°} (0.07581 + 0.04544i) |0100101⟩\n" +
                "{28.125°} (0.07795 + 0.04167i) |0100110⟩\n" +
                "{25.312°} (0.07990 + 0.03779i) |0100111⟩\n" +
                "{22.500°} (0.08166 + 0.03382i) |0101000⟩\n" +
                "{19.687°} (0.08322 + 0.02978i) |0101001⟩\n" +
                "{16.875°} (0.08458 + 0.02566i) |0101010⟩\n" +
                "{14.062°} (0.08574 + 0.02148i) |0101011⟩\n" +
                "{11.250°} (0.08669 + 0.01724i) |0101100⟩\n" +
                "{8.437°} (0.08743 + 0.01297i) |0101101⟩\n" +
                "{5.625°} (0.08796 + 0.00866i) |0101110⟩\n" +
                "{2.812°} (0.08828 + 0.00434i) |0101111⟩\n" +
                "{-0.000°} 0.08839 |0110000⟩\n" +
                "{-2.813°} (0.08828 + -0.00434i) |0110001⟩\n" +
                "{-5.625°} (0.08796 + -0.00866i) |0110010⟩\n" +
                "{-8.438°} (0.08743 + -0.01297i) |0110011⟩\n" +
                "{-11.250°} (0.08669 + -0.01724i) |0110100⟩\n" +
                "{-14.063°} (0.08574 + -0.02148i) |0110101⟩\n" +
                "{-16.875°} (0.08458 + -0.02566i) |0110110⟩\n" +
                "{-19.688°} (0.08322 + -0.02978i) |0110111⟩\n" +
                "{-22.500°} (0.08166 + -0.03382i) |0111000⟩\n" +
                "{-25.313°} (0.07990 + -0.03779i) |0111001⟩\n" +
                "{-28.125°} (0.07795 + -0.04167i) |0111010⟩\n" +
                "{-30.938°} (0.07581 + -0.04544i) |0111011⟩\n" +
                "{-33.750°} (0.07349 + -0.04911i) |0111100⟩\n" +
                "{-36.563°} (0.07099 + -0.05265i) |0111101⟩\n" +
                "{-39.375°} (0.06833 + -0.05607i) |0111110⟩\n" +
                "{-42.188°} (0.06549 + -0.05936i) |0111111⟩\n" +
                "{-45.000°} (0.06250 + -0.06250i) |1000000⟩\n" +
                "{-47.813°} (0.05936 + -0.06549i) |1000001⟩\n" +
                "{-50.625°} (0.05607 + -0.06833i) |1000010⟩\n" +
                "{-53.438°} (0.05265 + -0.07099i) |1000011⟩\n" +
                "{-56.250°} (0.04911 + -0.07349i) |1000100⟩\n" +
                "{-59.063°} (0.04544 + -0.07581i) |1000101⟩\n" +
                "{-61.875°} (0.04167 + -0.07795i) |1000110⟩\n" +
                "{-64.688°} (0.03779 + -0.07990i) |1000111⟩\n" +
                "{-67.500°} (0.03382 + -0.08166i) |1001000⟩\n" +
                "{-70.313°} (0.02978 + -0.08322i) |1001001⟩\n" +
                "{-73.125°} (0.02566 + -0.08458i) |1001010⟩\n" +
                "{-75.938°} (0.02148 + -0.08574i) |1001011⟩\n" +
                "{-78.750°} (0.01724 + -0.08669i) |1001100⟩\n" +
                "{-81.563°} (0.01297 + -0.08743i) |1001101⟩\n" +
                "{-84.375°} (0.00866 + -0.08796i) |1001110⟩\n" +
                "{-87.188°} (0.00434 + -0.08828i) |1001111⟩\n" +
                "{-90.000°} -0.08839i |1010000⟩\n" +
                "{-92.813°} (-0.00434 + -0.08828i) |1010001⟩\n" +
                "{-95.625°} (-0.00866 + -0.08796i) |1010010⟩\n" +
                "{-98.438°} (-0.01297 + -0.08743i) |1010011⟩\n" +
                "{-101.250°} (-0.01724 + -0.08669i) |1010100⟩\n" +
                "{-104.063°} (-0.02148 + -0.08574i) |1010101⟩\n" +
                "{-106.875°} (-0.02566 + -0.08458i) |1010110⟩\n" +
                "{-109.688°} (-0.02978 + -0.08322i) |1010111⟩\n" +
                "{-112.500°} (-0.03382 + -0.08166i) |1011000⟩\n" +
                "{-115.313°} (-0.03779 + -0.07990i) |1011001⟩\n" +
                "{-118.125°} (-0.04167 + -0.07795i) |1011010⟩\n" +
                "{-120.938°} (-0.04544 + -0.07581i) |1011011⟩\n" +
                "{-123.750°} (-0.04911 + -0.07349i) |1011100⟩\n" +
                "{-126.563°} (-0.05265 + -0.07099i) |1011101⟩\n" +
                "{-129.375°} (-0.05607 + -0.06833i) |1011110⟩\n" +
                "{-132.188°} (-0.05936 + -0.06549i) |1011111⟩\n" +
                "{-135.000°} (-0.06250 + -0.06250i) |1100000⟩\n" +
                "{-137.813°} (-0.06549 + -0.05936i) |1100001⟩\n" +
                "{-140.625°} (-0.06833 + -0.05607i) |1100010⟩\n" +
                "{-143.438°} (-0.07099 + -0.05265i) |1100011⟩\n" +
                "{-146.250°} (-0.07349 + -0.04911i) |1100100⟩\n" +
                "{-149.063°} (-0.07581 + -0.04544i) |1100101⟩\n" +
                "{-151.875°} (-0.07795 + -0.04167i) |1100110⟩\n" +
                "{-154.688°} (-0.07990 + -0.03779i) |1100111⟩\n" +
                "{-157.500°} (-0.08166 + -0.03382i) |1101000⟩\n" +
                "{-160.313°} (-0.08322 + -0.02978i) |1101001⟩\n" +
                "{-163.125°} (-0.08458 + -0.02566i) |1101010⟩\n" +
                "{-165.938°} (-0.08574 + -0.02148i) |1101011⟩\n" +
                "{-168.750°} (-0.08669 + -0.01724i) |1101100⟩\n" +
                "{-171.563°} (-0.08743 + -0.01297i) |1101101⟩\n" +
                "{-174.375°} (-0.08796 + -0.00866i) |1101110⟩\n" +
                "{-177.188°} (-0.08828 + -0.00434i) |1101111⟩\n" +
                "{180.000°} -0.08839 |1110000⟩\n" +
                "{177.187°} (-0.08828 + 0.00434i) |1110001⟩\n" +
                "{174.375°} (-0.08796 + 0.00866i) |1110010⟩\n" +
                "{171.562°} (-0.08743 + 0.01297i) |1110011⟩\n" +
                "{168.750°} (-0.08669 + 0.01724i) |1110100⟩\n" +
                "{165.937°} (-0.08574 + 0.02148i) |1110101⟩\n" +
                "{163.125°} (-0.08458 + 0.02566i) |1110110⟩\n" +
                "{160.312°} (-0.08322 + 0.02978i) |1110111⟩\n" +
                "{157.500°} (-0.08166 + 0.03382i) |1111000⟩\n" +
                "{154.687°} (-0.07990 + 0.03779i) |1111001⟩\n" +
                "{151.875°} (-0.07795 + 0.04167i) |1111010⟩\n" +
                "{149.062°} (-0.07581 + 0.04544i) |1111011⟩\n" +
                "{146.250°} (-0.07349 + 0.04911i) |1111100⟩\n" +
                "{143.437°} (-0.07099 + 0.05265i) |1111101⟩\n" +
                "{140.625°} (-0.06833 + 0.05607i) |1111110⟩\n" +
                "{137.812°} (-0.06549 + 0.05936i) |1111111⟩", jqs.toString());
    }

    @Test
    void testSimpleCircuits() {
        //https://algassert.com/quirk#circuit={%22cols%22:[[%22X%22],[%22%E2%80%A2%22,%22X%22],[1,%22Z%22],[1,%22%E2%80%A2%22,%22X%22],[1,%22H%22,%22H%22],[1,1,%22Z^%C2%BD%22]]}
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.CX(0, 1);
        jqs.Z(1);
        jqs.CX(1, 2);
        jqs.H(1);
        jqs.H(2);
        jqs.S(2);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{180.000°} -0.50000 |001⟩\n" +
                "{0.000°} 0.50000 |011⟩\n" +
                "{90.000°} 0.50000i |101⟩\n" +
                "{-90.000°} -0.50000i |111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22X%22],[%22H%22],[%22%E2%80%A2%22,%22X%22]]}
        jqs = new jqs(2);
        jqs.X(0);
        jqs.H(0);
        jqs.CX(0, 1);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 0.70711 |00⟩\n" +
                "{180.000°} -0.70711 |11⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22H%22],[%22%E2%80%A2%22,%22X%22],[1,%22Z%22],[1,%22%E2%80%A2%22,%22X%22],[1,1,%22Z^%C2%BD%22],[1,1,%22Z^%C2%BD%22],[1,1,%22H%22],[1,1,%22Z^%C2%BD%22]]}
        jqs = new jqs(3);
        jqs.H(0);
        jqs.CX(0, 1);
        jqs.Z(1);
        jqs.CX(1, 2);
        jqs.S(2);
        jqs.S(2);
        jqs.H(2);
        jqs.S(2);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 0.50000 |000⟩\n" +
                "{0.000°} 0.50000 |011⟩\n" +
                "{90.000°} 0.50000i |100⟩\n" +
                "{-90.000°} -0.50000i |111⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));

        //https://algassert.com/quirk#circuit={%22cols%22:[[%22X%22],[1,%22H%22],[1,%22%E2%80%A2%22,%22X%22],[%22%E2%80%A2%22,%22X%22]]}
        jqs = new jqs(3);
        jqs.X(0);
        jqs.H(1);
        jqs.CX(1, 2);
        jqs.CX(0, 1);
        jqs.getComputationalState();
//        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        assertEquals("|ψ⟩ = \n" +
                "{phase} amplitude |basis⟩ \n" +
                "-------------------------\n" +
                "{0.000°} 0.70711 |011⟩\n" +
                "{0.000°} 0.70711 |101⟩", ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }
}