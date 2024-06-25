package qubits;

/**
 * This class represents a complex number that can have a real component an imaginary component or both. It formats
 * the string output based on the values of real and imag to display it properly in the console.
 *
 * @author Robert Smith
 * @version 0.1
 * @since 25 June 2024
 *
 */
public class ComplexNumber {
    private double real;
    private double imag;

    public ComplexNumber() {
        this.real = 0.0;
        this.imag = 0.0;
    }

    public ComplexNumber(double real) {
        this.real = real;
        this.imag = 0.0;
    }

    public ComplexNumber(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImag() {
        return imag;
    }

    public void setImag(double imag) {
        this.imag = imag;
    }

    public double magnitudeSquared() {
        return real * real + imag * imag;
    }

    @Override
    public String toString() {
        if(this.imag == 0.0){
            return String.valueOf(real);
        } else {
            if(this.imag >= 0){
                return String.valueOf(real)+"+"+String.valueOf(imag)+'\u2148';
            } else {
                return String.valueOf(real)+""+String.valueOf(imag)+'\u2148';
            }
        }
     }
}
