import complexClasses.ComplexMath;
import interpreter.jqs;

public class Sandbox {
    public static void main(String[] args) {
        //https://quantum.ibm.com/composer/files/61874091bc455a56c155195a02f0495a2c7829e4e8006c2d23bdff6e9e87da45
        //https://github.com/qiskit-community/qiskit-community-tutorials/blob/master/Coding_With_Qiskit/ep5_Quantum_Teleportation.ipynb
        jqs jqs = new jqs(3);
        jqs.H(0);
        jqs.Si(0);
//        jqs.H(2);
//        jqs.S(0);
//        jqs.Z(1);
//        jqs.X(0);
//        jqs.X(1);
        jqs.CX(0,1);
        jqs.CX(0,2);
        jqs.expval();

        System.out.println(ComplexMath.complexMatrixToDiracNotation(jqs.getStateVec()));
        System.out.println(jqs.getStateVec());
    }


}
