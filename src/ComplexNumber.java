/**
 * Complex number class if I want to add imaginary number handling in the future
 * @author Robert P Smith
 * @version 0.1
 * @since 24 June 2024
 */
public class ComplexNumber {

    // Define a ComplexNumber class to represent complex numbers and compute magnitude
    double real = 0;

    /**
     * Constructor for the complex Number class
     * @param real the double being passed to create the complex number
     */
    public ComplexNumber(double real) {
        this.real = real;
    }

    /**
     * Gets the magnitude for checking compliance to Borne's rule
     * @return the magnitude of the complex number as a double
     */
    public double magnitude() {
        return Math.sqrt(real * real);
    }

    /**
     * Gets the real number from the complex number
     * @return the number as a double
     */
    public double getReal() {
        return real;
    }

    /**
     * Simple to string to get the number back as a string
     * @return a string representation of the double value
     */
    @Override
    public String toString() {
        return String.valueOf(real);
    }

}
