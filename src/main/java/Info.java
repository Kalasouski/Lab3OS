import java.math.BigInteger;

public class Info {

    private static BigInteger factorial(int number) {
        BigInteger factorial = BigInteger.ONE;
        for (int i = number; i > 0; i--) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }



    public static String getInfo(){

        return "The factorial of 100 is: "+ factorial(100).toString();
    }


}
