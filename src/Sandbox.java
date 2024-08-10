import interpreter.jqs;

/**
 * Just a class for debugging things while building the program.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 11 July 2024
 */

public class Sandbox {
    private static int maxQubits = 6;
    private static int runsCount = 1;

    /**
     * Main method for testing out quantum circuits while building the simulator.
     *
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
        jqs qsk = new jqs(5);
        System.out.println(qsk.QPE("R1", 5*Math.PI/30, 0,3, 4));
//        benchmark("QPE");
    }

    /**
     * Benchmark method for testing various configurations against different circuits to find hot spots
     * @param type the type of benchmark to run.
     */
    public static void benchmark(String type) {

        switch (type) {
            case "Basic" -> {
                for (int qubits = 2; qubits <= maxQubits; qubits++) {
                    System.out.println("\nTesting " + qubits + " qubits.");
                    long totalRunsTime = 0;
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.nanoTime();

                        jqs jqs = new jqs(qubits);
                        for (int i = 0; i < qubits - 1; i++) {
                            jqs.X(i);
                        }
                        jqs.buildCircuit();
                        jqs.CX(0, qubits - 1);
                        jqs.buildCircuit();

                        long endTime = System.nanoTime();
                        double runTimeSeconds = (endTime - startTime) / 1_000_000_000.0;
                        System.out.printf("Run Time: %.2f seconds%n", runTimeSeconds);
                        if (runsCount > 1) {
                            if (r > 0) {
                                totalRunsTime += (endTime - startTime);
                            }
                        } else {
                            totalRunsTime += (endTime - startTime);
                        }
                    }
                    int count = runsCount == 1 ? runsCount : runsCount - 1;
                    double averageTimeSeconds = ((double) totalRunsTime / count) / 1_000_000_000.0;
                    System.out.printf("Average execution time using Sparse: %.2f seconds%n", averageTimeSeconds);
                }

            }
            case "QFT" -> {
                for (int qubits = 3; qubits <= maxQubits; qubits++) {
                    long totalRunsTime = 0;
                    System.out.println("Testing " + qubits + " qubits.");
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.nanoTime();

                        jqs jqs = new jqs(qubits);
                        for (int i = 0; i < qubits; i++) {
                            jqs.X(i);
                        }
                        for (int i = 0; i < qubits / 2; i++) {
                            jqs.T(i);
                        }
                        jqs.H(qubits / 2);
                        jqs.buildCircuit();
                        jqs.QFT();
                        jqs.QFTi();

                        long endTime = System.nanoTime();
                        double runTimeSeconds = (endTime - startTime) / 1_000_000_000.0;
                        System.out.printf("Run Time: %.2f seconds%n", runTimeSeconds);
                        if (runsCount > 1) {
                            if (r > 0) {
                                totalRunsTime += (endTime - startTime);
                            }
                        } else {
                            totalRunsTime += (endTime - startTime);
                        }
                    }
                    int count = runsCount == 1 ? runsCount : runsCount - 1;
                    double averageTimeSeconds = ((double) totalRunsTime / count) / 1_000_000_000.0;
                    System.out.printf("Average execution time using Sparse: %.2f seconds%n", averageTimeSeconds);
                }
            }
            case "QFTi" -> {
                for (int qubits = 3; qubits <= maxQubits; qubits++) {
                    long totalRunsTime = 0;
                    System.out.println("Testing " + qubits + " qubits.");
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.nanoTime();

                        jqs jqs = new jqs(qubits);
                        for(int i = 0; i < qubits; i++){
                            jqs.X(i);
                        }
                        jqs.buildCircuit();
                        jqs.QFT();
                        jqs.QFTi();

                        long endTime = System.nanoTime();
                        double runTimeSeconds = (endTime - startTime) / 1_000_000_000.0;
                        System.out.printf("Run Time: %.2f seconds%n", runTimeSeconds);
                        if (runsCount > 1) {
                            if (r > 0) {
                                totalRunsTime += (endTime - startTime);
                            }
                        } else {
                            totalRunsTime += (endTime - startTime);
                        }
                    }
                    int count = runsCount == 1 ? runsCount : runsCount - 1;
                    double averageTimeSeconds = ((double) totalRunsTime / count) / 1_000_000_000.0;
                    System.out.printf("Average execution time using Sparse: %.2f seconds%n", averageTimeSeconds);
                }
            }
            case "QPE" -> {
                for (int qubits = 6; qubits <= 6; qubits = qubits +2) {
                    long totalRunsTime = 0;
                    System.out.print("\nTesting " + qubits + " qubits.\n");
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.nanoTime();

                        jqs qsk = new jqs(qubits, 100000);
                        qsk.QPE("R1", 2*Math.PI/3, 0,4, 5);
//                        qsk.QPE("R1", 2*Math.PI/3, 0,qubits-2, qubits-1);
//                        qsk.QPE("T", 0,qubits-2, qubits-1);

                        long endTime = System.nanoTime();
                        double runTimeSeconds = (endTime - startTime) / 1_000_000_000.0;
                        System.out.printf("Run Time: %.2f seconds%n", runTimeSeconds);
                        if (runsCount > 1) {
                            if (r > 0) {
                                totalRunsTime += (endTime - startTime);
                            }
                        } else {
                            totalRunsTime += (endTime - startTime);
                        }
                    }
                    int count = runsCount == 1 ? runsCount : runsCount - 1;
                    double averageTimeSeconds = ((double) totalRunsTime / count) / 1_000_000_000.0;
                    System.out.printf("Average execution time: %.2f seconds%n", averageTimeSeconds);
                }
            }
        }
    }
}

