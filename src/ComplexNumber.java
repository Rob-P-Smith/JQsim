public class ComplexNumber {

    // Define a ComplexNumber class to represent complex numbers and compute magnitude
    double real = 0;

    public ComplexNumber(double real) {
//        System.out.println("Passed in as real: " + real);
        this.real = real;
    }

    public double magnitude() {
//        System.out.println("Magnitude Found: " + (double) Math.sqrt(real * real));
        return Math.sqrt(real * real);
    }

    public double getReal() {
        return real;
    }

    @Override
    public String toString() {
        return String.valueOf(real);
    }

}
