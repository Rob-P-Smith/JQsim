package measurement;

import qubits.ComplexMatrix;
import qubits.ComplexQubit;

import java.util.LinkedList;
import java.util.Queue;

public class LineCircuit {
    Queue<ComplexMatrix> workQueue = new LinkedList<>();

    public void addGate(ComplexQubit qubit) {

    }

    public void executGate(ComplexQubit qubit){

    }

    public void viewLine(ComplexQubit qubit){
        Queue<ComplexMatrix> workQueueCopy = new LinkedList<>(workQueue);
        for(ComplexMatrix item : workQueueCopy){
            System.out.println();
        }
    }
}
