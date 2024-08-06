import complex_classes.ComplexMath;
import interpreter.jqs;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Just a class for debugging things while building the program.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 11 July 2024
 */

public class Sandbox {
    private static int maxQubits = 8;
    private static int runsCount = 5;

    /**
     * Main method for testing out quantum circuits while building the simulator.
     *
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
//        simulateTest();
//        benchmark("QFTi");
    }

    /**
     * Benchmark method for testing various configurations against different circuits to find hot spots
     * @param type the type of benchmark to run.
     */
    public static void benchmark(String type) {

        switch (type) {
            case "Basic" -> {
                for (int qubits = 11; qubits <= maxQubits; qubits++) {
                    System.out.println("Testing " + qubits + " qubits.");
                    long totalRunsTime = 0;
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.currentTimeMillis();

                        jqs jqs = new jqs(qubits);
                        for (int i = 0; i < qubits - 1; i++) {
                            jqs.X(i);
                        }
                        jqs.getComputationalState();
                        jqs.CX(0, qubits - 1);
                        jqs.getComputationalState();

                        long endTime = System.currentTimeMillis();
                        System.out.println("Run Time: " + (endTime - startTime));
                        if (r > 0) {
                            totalRunsTime += endTime - startTime;
                        }
                    }
                    int count = runsCount == 1? runsCount : runsCount -1;
                    System.out.println("Average execution time using Sparse: " + (totalRunsTime / count) + " ms");
                }
            }
            case "QFT" -> {
                for (int qubits = 6; qubits <= maxQubits; qubits++) {
                    long totalRunsTime = 0;
                    System.out.println("Testing " + qubits + " qubits.");
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.currentTimeMillis();

                        jqs jqs = new jqs(qubits);
                        for (int i = 0; i < qubits; i++) {
                            jqs.X(i);
                        }
                        for (int i = 0; i < qubits / 2; i++) {
                            jqs.T(i);
                        }
                        jqs.H(qubits / 2);
                        jqs.getComputationalState();
                        jqs.QFT();
                        jqs.QFTi();

                        long endTime = System.currentTimeMillis();
                        if (r > 0) {
                            totalRunsTime += endTime - startTime;
                        }
                    }
                    System.out.println("Average time: " + (totalRunsTime / (runsCount - 1)) + " ms");
                }
            }
            case "QFTi" -> {
                for (int qubits = 3; qubits <= maxQubits; qubits++) {
                    long totalRunsTime = 0;
                    System.out.println("Testing " + qubits + " qubits.");
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.currentTimeMillis();

                        jqs jqs = new jqs(qubits);
                        for(int i = 0; i < qubits; i++){
                            jqs.X(i);
                        }
                        jqs.getComputationalState();
                        jqs.QFT();
                        jqs.QFTi();

                        long endTime = System.currentTimeMillis();
                        if (r > 0) {
                            totalRunsTime += endTime - startTime;
                        }
                    }
                    System.out.println("Average time: " + (totalRunsTime / (runsCount - 1)) + " ms");
                }
            }
        }
    }

    /**
     * Test the simulation function. Haven't addressed how to build this as a test yet because of probabilistic results.
     */
    public static void simulateTest(){
        jqs jqs = new jqs(3);
        jqs.X(0);
        jqs.H(1);
        jqs.X(2);
        jqs.CX(0,1);
        jqs.T(0);
        jqs.Z(1);
        jqs.S(2);
        jqs.H(0);
        jqs.H(2);
        jqs.T(0);
        jqs.getComputationalState();
        jqs.simulate();
    }
}

