import interpreter.jqs;

/**
 * Just a class for debugging things while building the program.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 11 July 2024
 */

public class Sandbox {
    private static int maxQubits = 14;
    private static int runsCount = 1;

    /**
     * Main method for testing out quantum circuits while building the simulator.
     *
     * @param args none needed or accounted for.
     */
    public static void main(String[] args) {
//        qiskitBasicQPE();
//        qiskitMoreComplexQPE();
//        benchmark("Basic");
        jqs jqs = new jqs(3, 1000000);
        jqs.X(2);
        jqs.H(1);
        jqs.H(0);
        jqs.CGate("R1", 0,2, (2*Math.PI)/3);
        jqs.CGate("R1", 1,2, (2*Math.PI)/3);
        jqs.CGate("R1", 1,2, (2*Math.PI)/3);
        jqs.buildCircuit();
        jqs.QFTi();
        jqs.simulate();


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
                for (int qubits = 6; qubits <= maxQubits; qubits = qubits +2) {
                    long totalRunsTime = 0;
                    System.out.print("\nTesting " + qubits + " qubits.");
                    for (int r = 0; r < runsCount; r++) {
                        long startTime = System.nanoTime();

                        jqs qsk = new jqs(qubits, 10000);
                        qsk.QPE("R1", 2*Math.PI/3, 0,qubits-2, qubits-1);

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
        jqs.buildCircuit();
        jqs.simulate();
        System.out.println(jqs);
    }

    public static void qiskitBasicQPE(){
        jqs qsk = new jqs(4, 100000);
        qsk.QPE("T",0,2,3);
//        qsk.X(3);
//        for(int i = 0; i < qsk.size()-1; i++) {
//            qsk.H(i);
//        }
//        qsk.CGate("T", 0, 3);
//
//        qsk.CGate("T", 1, 3);
//        qsk.CGate("T", 1, 3);
//
//        qsk.CGate("T", 2, 3);
//        qsk.CGate("T", 2, 3);
//        qsk.CGate("T", 2, 3);
//        qsk.CGate("T", 2, 3);

//        qsk.buildCircuit();
//        qsk.QFTi(0,2);
//        System.out.println(qsk);
//        qsk.simulate();
//        System.out.println("Phase is : " + (1/Math.pow(2,3)));
    }

    public static void qiskitMoreComplexQPE(){
        jqs qsk = new jqs(4, 100000);
        qsk.QPE("R1", 2*Math.PI/3, 0,2, 3);
//        qsk.QPE("R1", 2*Math.PI/3, 0,4, 5);
//        qsk.X(5);
//        for(int i = 0; i < qsk.size()-1; i++) {
//            qsk.H(i);
//        }
//        qsk.CGate("R1", 0, 5, (2*Math.PI/3));
//
//        qsk.CGate("R1", 1, 5, (2*Math.PI/3));
//        qsk.CGate("R1", 1, 5, (2*Math.PI/3));
//
//        for(int i = 0; i < 4; i++) {
//            qsk.CGate("R1", 2, 5, (2*Math.PI/3));
//        }
//        for(int i = 0; i < 8; i++) {
//            qsk.CGate("R1", 3, 5, (2*Math.PI/3));
//        }
//        for(int i = 0; i < 16; i++){
//            qsk.CGate("R1", 4, 5, (2*Math.PI/3));
//        }

//        qsk.buildCircuit();
//        qsk.QFTi(0,4);
//        System.out.println(qsk);
//        qsk.simulate();
//        System.out.println("Phase is : " + (1/Math.pow(2,3)));
    }
}

