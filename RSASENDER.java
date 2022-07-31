import java.math.*;
import java.io.*;
import java.util.*;

public class RSASENDER {

	public static void main(String[] args) throws Exception {
		Scanner userinput = new Scanner(System.in);
		long e = 429L;
		long n = 69L;
		Scanner console = new Scanner(new File("ReceiverToSender.txt"));
		e = console.nextLong();
		n = console.nextLong();

		long message = 8273846577L; //NOTE, m must be less than n.
		long c = power(message, e, n); //ciphertext -- public info, sends to receiver

		try {
      		FileWriter myWriter = new FileWriter("SenderToReceiver.txt");
     		myWriter.write("" + c);
        	myWriter.close();
      		System.out.println("Encrypted Message Sent!");
    	} catch (IOException h) {
      		System.out.println("An error occurred.");
      		h.printStackTrace();
    	}
		
	}


	//(x^y) % p method for larger nums
	public static long power(long a, long b, long c) {
		long result1 = 1L;
		BigInteger result = BigInteger.valueOf(result1);
		BigInteger x = BigInteger.valueOf(a);
		BigInteger y = BigInteger.valueOf(b);
		BigInteger p = BigInteger.valueOf(c);


		while (y.compareTo(BigInteger.valueOf(0)) > 0) {
			
			// If y is odd, multiply x with result
			if ((y.mod(BigInteger.valueOf(2))).equals(BigInteger.valueOf(1))){
				result = (result.multiply(x)).mod(p);
			}
		
			// y is now even, so multiply by x^2 every time to speed this up
			y = y.divide(BigInteger.valueOf(2));
			x = (x.multiply(x)).mod(p);
		}
		return result.longValue();
	}
}