package supportClasses;

public enum GreekEnums {
    ALPHA('Α', 'α'),
    BETA('Β', 'β'),
    GAMMA('Γ', 'γ'),
    DELTA('Δ', 'δ'),
    EPSILON('Ε', 'ε'),
    ZETA('Ζ', 'ζ'),
    ETA('Η', 'η'),
    THETA('Θ', 'θ'),
    IOTA('Ι', 'ι'),
    KAPPA('Κ', 'κ'),
    LAMBDA('Λ', 'λ'),
    MU('Μ', 'μ'),
    NU('Ν', 'ν'),
    XI('Ξ', 'ξ'),
    OMICRON('Ο', 'ο'),
    PI('Π', 'π'),
    RHO('Ρ', 'ρ'),
    SIGMA('Σ', 'σ'),
    TAU('Τ', 'τ'),
    UPSILON('Υ', 'υ'),
    PHI('Φ', 'φ'),
    CHI('Χ', 'χ'),
    PSI('Ψ', 'ψ'),
    OMEGA('Ω', 'ω');

    private final char upperCase;
    private final char lowerCase;

    GreekEnums(char upperCase, char lowerCase) {
        this.upperCase = upperCase;
        this.lowerCase = lowerCase;
    }

    public char upper() {
        return upperCase;
    }

    public char lower() {
        return lowerCase;
    }
}