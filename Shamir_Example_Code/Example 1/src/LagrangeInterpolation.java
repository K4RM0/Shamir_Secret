import java.util.Arrays;
import java.util.Scanner;

public class LagrangeInterpolation {
    public static void main(String[] args) {
        double product, sum = 0;

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Number of Terms: ");
        int n = sc.nextInt();

        double[] x = new double[n];
        double[] y = new double[n];

        System.out.println("Enter all the x, y terms: ");
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextDouble();
            y[i] = sc.nextDouble();
        }

        System.out.println("x = {" + Arrays.toString(x) + "}");
        System.out.println("x = {" + Arrays.toString(y) + "}");

        System.out.print("Enter a point to Find it's value: ");
        int xPoint = sc.nextInt();
        // End of inputs

        product = 1;
        // Performing Arithmetic Operation
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    product *= (xPoint - x[j]) / (x[i] - x[j]);
                }
            }
            sum += product * y[i];

            product = 1;    // Must set to 1
        }
        System.out.println("The value at point  " + xPoint + " is : " + sum);

        // End of the Program
    }

}
