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

    /**
     * Main method for testing out quantum circuits while building the simulator.
     *
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
        System.out.println((long)1048576*1048576 * 8);
//        long startTime = System.currentTimeMillis();
//        jqs jqs = new jqs(11);
//        jqs.X(0);
//        jqs.getComputationalState();
////        jqs.QFT();
//        long endTime = System.currentTimeMillis();
//        System.out.println(jqs);
//        System.out.println("Time: " + (endTime - startTime) + " ms");
    }

    public static void benchmark(String type, int maxQubits, int runsCount, int threadLimit) {

        switch (type) {
            case "Basic" -> {
                for (int qubits = 12; qubits <= maxQubits; qubits = qubits + 4) {
                    long totalRunsTime = 0;
                    System.out.println("Testing " + qubits + " qubits.");
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.currentTimeMillis();

                        jqs jqs = new jqs(qubits);
                        for (int i = 0; i < qubits - 1; i++) {
                            jqs.X(i);
                        }
                        jqs.getComputationalState();
//                        jqs.CX(0, qubits - 1);
//                        jqs.getComputationalState();

                        long endTime = System.currentTimeMillis();
                        if (r > -1) {
                            totalRunsTime += endTime - startTime;
                        }
                    }
                    int count = runsCount == 1? runsCount : runsCount -1;
                    System.out.println("Average time: " + (totalRunsTime / count) + " ms");
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
        }
    }
}

