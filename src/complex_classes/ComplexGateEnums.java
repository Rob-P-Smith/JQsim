package complex_classes;

/**
 * Enum representing various quantum gates using complex matrices.
 * TODO: Figure out how I'm going to deal with the R gates and the user provided theta, pretty sure they can't be ENUMs.
 *
 * @author Robert Smith
 * @version 1.0
 * @since 16 July 2024
 */
public enum ComplexGateEnums {
    /**
     * Pauli-X gate matrix.
     */
    PAULI_X(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(0), new ComplexNumber(1)},
            {new ComplexNumber(1), new ComplexNumber(0)}
    })),

    /**
     * Pauli-Z gate matrix.
     */
    PAULI_Z(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(-1)}
    })),

    /**
     * Pauli-Y gate matrix.
     */
    PAULI_Y(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(0), new ComplexNumber(0, -1)},
            {new ComplexNumber(0, 1), new ComplexNumber(0)}
    })),

    /**
     * Hadamard gate matrix.
     */
    HADAMARD(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1 / Math.sqrt(2)), new ComplexNumber(1 / Math.sqrt(2))},
            {new ComplexNumber(1 / Math.sqrt(2)), new ComplexNumber(-(1 / Math.sqrt(2)))}
    })),

    /**
     * Identity gate matrix.
     */
    IDENTITY(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(1)}
    })),

    /**
     * S gate matrix.
     */
    S_GATE(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0, 1)}
    })),

    /**
     * Si gate matrix.
     */
    SI_GATE(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0, -1)}
    })),

    /**
     * T gate matrix.
     */
    T_GATE(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), getTValue()}
    })),
    /**
     * Ti gate matrix.
     */
    TI_GATE(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), getTIValue()}
    })),

    SWAP(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(1), new ComplexNumber(0), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(1)}
    })),

    /**
     * 1  0  0  0
     * 0  0 -i  0
     * 0 -i  0  0
     * 0  0  0  1
     */
    ISWAP(new ComplexMatrix(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(0, -1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0, -1), new ComplexNumber(0), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(1)}
    }));

    /**
     * The complex matrix associated with each gate.
     */
    private final ComplexMatrix matrix;

    /**
     * Constructs a ComplexGateEnums instance with the specified complex matrix.
     *
     * @param matrix the complex matrix associated with the gate
     */
    ComplexGateEnums(ComplexMatrix matrix) {
        this.matrix = matrix;
    }

    /**
     * Returns the complex matrix associated with the gate.
     *
     * @return the complex matrix associated with the gate
     */
    public ComplexMatrix getMatrix() {
        return matrix;
    }

    private static ComplexNumber getRXTop(double degrees) {
        double real = Math.cos(degrees / Math.PI / 4);
        double imag = -Math.sin(degrees / Math.PI / 4);
        return new ComplexNumber(real, imag);
    }

    /**
     * Returns the complex value used in the T gate matrix.
     *
     * @return the complex value used in the T gate matrix
     */
    private static ComplexNumber getTValue() {
        double real = Math.cos(Math.PI / 4);
        double imag = Math.sin(Math.PI / 4);
        return new ComplexNumber(real, imag);
    }

    /**
     * Returns the complex value used in the Ti gate matrix.
     *
     * @return the complex value used in the Ti gate matrix
     */
    private static ComplexNumber getTIValue() {
        double real = Math.cos(Math.PI / 4);
        double imag = -Math.sin(Math.PI / 4);
        return new ComplexNumber(real, imag);
    }

    @Override
    public String toString() {
        return "Nope. Use the getMatrix() method instead";
    }
}