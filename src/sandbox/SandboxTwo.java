package sandbox;

import complexClasses.*;
import menus.Console;

public class SandboxTwo {
    public static void main(String[] args) {

        ComplexObject coj = new ComplexObject();
        ComplexQubit one = new ComplexQubit();
        ComplexQubit two = new ComplexQubit();
        ComplexMatrix systemState = new ComplexMatrix(4,1);
        systemState.set(2,0, new ComplexNumber(1));
        System.out.println("System State: \n"+systemState);
//        ComplexMatrix tempMatrix = coj.tensorMultiply(ComplexGates.getIdentity(), ComplexGates.getPauliX());
        ComplexMatrix tempMatrix = coj.tensorMultiply(ComplexGates.getPauliX(), ComplexGates.getIdentity());
        ComplexMatrix applyXtoStart = coj.multiplyMatrix(tempMatrix, systemState);
//        System.out.println("Identity to X: \n"+tempMatrix);
        System.out.println("X to Identity: \n"+tempMatrix);
        System.out.println("applyXtoStart \n"+applyXtoStart);
    }
}
