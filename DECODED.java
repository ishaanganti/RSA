import java.math.*;
import java.io.*;
import java.util.*;
public class DECODED {
	public static void main(String[] args) throws Exception {
		long n = 69L;
		long c = 420L;
		long d = 42069L;
		Scanner console = new Scanner(new File("ReceiverToSender.txt"));
		console.nextLong();
		n = console.nextLong();
		Scanner console2 = new Scanner(new File("SenderToReceiver.txt"));
		c = console2.nextLong();
		Scanner console3 = new Scanner(new File("ReceiverOnly.txt"));
		d = console3.nextLong();

		long message = power(c, d, n);
		int msglen = (int)Math.log10(message)+1;
		long temp = 0;
		for(int i = 1; i <= msglen/2; i++) {
			temp = message/((long)Math.pow(10, msglen-2*i));
			message%=(long)Math.pow(10, msglen-2*i);
			System.out.print((char)temp);
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