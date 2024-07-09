import complexClasses.ComplexMath;
import interpreter.jqs;

public class Sandbox {
    public static void main(String[] args) {
        //https://quantum.ibm.com/composer/files/61874091bc455a56c155195a02f0495a2c7829e4e8006c2d23bdff6e9e87da45
        //https://github.com/qiskit-community/qiskit-community-tutorials/blob/master/Coding_With_Qiskit/ep5_Quantum_Teleportation.ipynb
        jqs jqs = new jqs(4);
        jqs.X(0);
        jqs.CX(0,1);
        jqs.Z(1);
        jqs.CX(1,2);
        jqs.S(2);
        jqs.S(2);
        jqs.H(2);
        jqs.S(2);
        jqs.CX(2,1);
        jqs.expval();

        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
    }


}
