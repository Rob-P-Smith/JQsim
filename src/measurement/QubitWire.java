package measurement;

import java.util.LinkedList;
import java.util.Queue;

public class QubitWire {
    private int wireNumber;
    Queue<String[]> workQueue = new LinkedList<>();

    public QubitWire(int wireNumber){
        this.wireNumber = wireNumber;

    }

    public void addGate(String[] gate) {
        workQueue.add(gate);
    }

    public Queue<String[]> getWorkQueue() {
        return workQueue;
    }

    public void viewLine() {
        System.out.println("QubitWire #"+this.wireNumber+":");
        for (String[] gate : workQueue) {
            if (gate.length == 2 && gate[1] == "0") {
                System.out.print(gate[0] + " -> ");
            } else if(gate.length == 3) {
                System.out.println(gate[0] + " {control: " + gate[1] + ", target: " + gate[2] + "} -> ");
            }
        }
    }
}
