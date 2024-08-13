import interpreter.jqs;

/**
 * Just a class for debugging things while building the program.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 11 July 2024
 */

public class Sandbox {
    private static int maxQubits = 13;
    private static int runsCount = 3;

    /**
     * Main method for testing out quantum circuits while building the simulator.
     *
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
        jqs jqs = new jqs(6);
        jqs.X(5);
        jqs.buildCircuit();
        String result = jqs.QPE("R1",2*Math.PI/5, 0, 4,5);
        System.out.println(result);

//        benchmark("Basic");
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
                        jqs.X(0);
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
                for (int qubits = 6; qubits <= maxQubits; qubits = qubits +2) {
                    long totalRunsTime = 0;
                    System.out.print("\nTesting " + qubits + " qubits.\n");
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.nanoTime();

                        jqs qsk = new jqs(qubits);
//                        String result = qsk.QPE("R1", 2*Math.PI/2, 0,qubits-2, qubits-1);
                        String result = qsk.QPE("S", 0,qubits-2, qubits-1);


                        long endTime = System.nanoTime();
                        System.out.println(result);
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

