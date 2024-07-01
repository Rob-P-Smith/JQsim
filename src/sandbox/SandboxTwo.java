package sandbox;

import complexClasses.ComplexGates;
import complexClasses.ComplexQubit;

public class SandboxTwo {
    public static void main(String[] args) {
        ComplexQubit one = new ComplexQubit();
        System.out.println("Qubit 1: \n"+ one);
        ComplexQubit two = new ComplexQubit();
        System.out.println("Qubit 2: \n"+ two);
//        one.setState(ComplexGates.applyPauliX(one.getState()));
        one.setState(ComplexGates.applyHadamard(one.getState()));
        System.out.println("Qubit 1 with X Gate: \n"+one);
//        two = (ComplexGates.applyCNOT(one, two));
        two = (ComplexGates.applyCNOT(two, one));
        System.out.println("After CNOT From 1 to 2: \n"+two);

    }
}
