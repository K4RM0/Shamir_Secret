import java.awt.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main (String[]args) {

        String str;
        Scanner scan = new Scanner(System.in);
        System.out.println("Insérer un secret : ");
        str = scan.nextLine();

        BigInteger bigIntegerStr=new BigInteger(str);
        System.out.println("Converted String to BigInteger: "+bigIntegerStr);
        System.out.println("Converted String to BigInteger: "+bigIntegerStr);



    }

}
