package complex_classes;

/**
 * Represents a complex number with real and imaginary parts.
 *
 * <p>
 * A {@code ComplexNumber} object can be initialized with real and imaginary components,
 * or only with a real component (assuming the imaginary part as zero). It supports
 * basic operations like getting and setting the real and imaginary parts, computing
 * the squared magnitude, and providing a formatted string representation.
 * </p>
 *
 * <p>
 * The string representation of a complex number formats itself based on the values of
 * real and imag to display properly in the console. If the imaginary part is zero,
 * it displays only the real part. If the imaginary part is positive, it formats as
 * "real + imagⅈ". If the imaginary part is negative, it formats as "real - imagⅈ".
 * </p>
 *
 * @author Robert Smith
 * @version 0.3
 * @since 6 August 2024
 */
public final class ComplexNumber {
    private double real; // Real part of the complex number
    private double imag; // Imaginary part of the complex number

    /**
     * Constructs a complex number with both real and imaginary parts initialized to 0.0.
     */
    public ComplexNumber() {
        this.real = 0.0;
        this.imag = 0.0;
    }

    /**
     * Constructs a complex number with the given real part and an imaginary part initialized to 0.0.
     *
     * @param real The real part of the complex number.
     */
    public ComplexNumber(double real) {
        this.real = real;
        this.imag = 0.0;
    }

    /**
     * Constructs a complex number with the given real and imaginary parts.
     *
     * @param real The real part of the complex number.
     * @param imag The imaginary part of the complex number.
     */
    public ComplexNumber(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    /**
     * Returns the real part of the complex number.
     *
     * @return The real part of the complex number.
     */
    public double getReal() {
        return real;
    }

    /**
     * Sets the real part of the complex number to the specified value.
     *
     * @param real The new value for the real part of the complex number.
     */
    public void setReal(double real) {
        this.real = real;
    }

    /**
     * Returns the imaginary part of the complex number.
     *
     * @return The imaginary part of the complex number.
     */
    public double getImag() {
        return imag;
    }

    /**
     * Sets the imaginary part of the complex number to the specified value.
     *
     * @param imag The new value for the imaginary part of the complex number.
     */
    public void setImag(double imag) {
        this.imag = imag;
    }

    /**
     * Sets the real and imaginary part of the complex number to the specified value.
     *
     * @param real The new value for the real part of the complex number.
     * @param imag The new value for the imaginary part of the complex number.
     */
    public void setValues(double real, double imag){
        this.real = real;
        this.imag = imag;
    }

    /**
     * Computes the squared magnitude (absolute value squared) of the complex number.
     *
     * @return The squared magnitude of the complex number.
     */
    public double magnitudeSquared() {
        return real * real + imag * imag;
    }

    /**
     * Returns a string representation of the complex number.
     *
     * <p>
     * The string representation formats the complex number based on the values of
     * real and imag:
     * <ul>
     * <li>If imag is 0.0, it returns only the real part.</li>
     * <li>If imag is positive or negative and real is 0.0, it formats as "imagⅈ"</li>
     * <li>If imag is positive and real is not 0.0, it formats as "real + imagⅈ".</li>
     * <li>If imag is negative and real is not 0.0, it formats as "real - imagⅈ".</li>
     * </ul>
     * </p>
     *
     * @return A string representation of the complex number.
     */
    @Override
    public String toString() {
        if (this.imag == 0.0) {
            return String.valueOf(real);
        } else if (this.real == 0.0) {
            if (this.imag > 0) {
                return String.valueOf(imag) + 'ⅈ';
            } else if (this.imag < 0) {
                return String.valueOf(imag) + 'ⅈ';
            }
        } else {
            if (this.imag < 0) {
                return String.valueOf(real) + imag + 'ⅈ';
            } else {
                return String.valueOf(real) + "+" + imag + 'ⅈ';
            }
        }
        return "No values found";
    }

    /**
     * Sets both the real and imaginary parts of the complex number to zero.
     */
    public void setZero() {
        this.real = 0.0;
        this.imag = 0.0;
    }

    /**
     * Adds another complex number to this one, modifying this instance.
     *
     * @param other The complex number to add to this one
     */
    public void addInPlace(ComplexNumber other) {
        this.real += other.real;
        this.imag += other.imag;
    }
}