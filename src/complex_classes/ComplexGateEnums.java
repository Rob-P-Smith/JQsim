package complex_classes;

/**
 * Enums representing various fixed value quantum gates using complex matrices.
 *
 * @author Robert Smith
 * @version 1.0
 * @since 6 August 2024
 */
public enum ComplexGateEnums {
    /**
     * Pauli-X gate matrix.
     */
    PAULI_X(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(0), new ComplexNumber(1)},
            {new ComplexNumber(1), new ComplexNumber(0)}
    })),

    /**
     * Pauli-Z gate matrix.
     */
    PAULI_Z(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(-1)}
    })),

    /**
     * Pauli-Y gate matrix.
     */
    PAULI_Y(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(0), new ComplexNumber(0, -1)},
            {new ComplexNumber(0, 1), new ComplexNumber(0)}
    })),

    /**
     * Hadamard gate matrix.
     */
    HADAMARD(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(1 / Math.sqrt(2)), new ComplexNumber(1 / Math.sqrt(2))},
            {new ComplexNumber(1 / Math.sqrt(2)), new ComplexNumber(-(1 / Math.sqrt(2)))}
    })),

    /**
     * Identity gate matrix.
     */
    IDENTITY(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(1)}
    })),

    /**
     * S gate matrix.
     */
    S_GATE(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0, 1)}
    })),

    /**
     * Si gate matrix.
     */
    SI_GATE(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0, -1)}
    })),

    /**
     * T gate matrix.
     */
    T_GATE(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), getTValue()}
    })),
    /**
     * Ti gate matrix.
     */
    TI_GATE(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0)},
            {new ComplexNumber(0), getTIValue()}
    })),

    SWAP(new ComplexSparse(new ComplexNumber[][]{
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
    ISWAP(new ComplexSparse(new ComplexNumber[][]{
            {new ComplexNumber(1), new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(0, -1), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0, -1), new ComplexNumber(0), new ComplexNumber(0)},
            {new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(1)}
    }));

    /**
     * The complex matrix associated with each gate.
     */
    private final ComplexSparse matrix;

    /**
     * Constructs a ComplexGateEnums instance with the specified complex matrix.
     *
     * @param matrix the complex matrix associated with the gate
     */
    ComplexGateEnums(ComplexSparse matrix) {
        this.matrix = matrix;
    }

    /**
     * Returns the complex matrix associated with the gate.
     *
     * @return the complex matrix associated with the gate
     */
    public ComplexSparse getMatrix() {
        return matrix;
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