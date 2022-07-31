import java.util.*;
import java.math.*;
import java.io.FileWriter;  
import java.io.IOException;  

public class RSA_RECIEVER {
	public static void main(String[] args) {
		long e = 257; //public key p1
		long p = (long)GeneratePrime(26, 6); //bit size, k
		long q = (long)GeneratePrime(26, 6); //bit size, k
		long n = p*q; //public key p2
		long totientN = TotientN(p, q);
		long d = CalcD(e, totientN); //private key

		
		System.out.println("Your e value is: " + e);
		System.out.println("Your first randomly generated prime is: " +p);
		System.out.println("Your second randomly generated prime is: "+ q);
		System.out.println("Your public key is: " + n);
		System.out.println("Totient of pub key: " + totientN);
		System.out.println("Your private key is: " + d);
		

		try {
      		FileWriter myWriter = new FileWriter("ReceiverOnly.txt");
     		myWriter.write("" + d);
        	myWriter.close();
      		System.out.println("Private Key Saved");
    	} catch (IOException h) {
      		System.out.println("An error occurred.");
      		h.printStackTrace();
    	}

    	try {
      		FileWriter myWriter = new FileWriter("ReceiverToSender.txt");
     		myWriter.write("" + e + "\n");
     		myWriter.write("" + n);
        	myWriter.close();
      		System.out.println("Public Keys Shared");
    	} catch (IOException h) {
      		System.out.println("An error occurred.");
      		h.printStackTrace();
    	}

	}


/**
THE FOLLOWING IS THE CODE FOR THE GENERATION OF LARGE PRIMES. 
AS I USED RETURN INTEGERS, THEY CAN BE AT MAX 31 BITS.
THIS IS USING MILLER-RABIN AND IS REQUIRED FOR THE FIRST 
STEP OF RSA
**/


	public static boolean MillerTest(long d, long n) {
	
		// Pick a random number s.t. a is an element of the set [2..n-2]
		long a = 2 + (long)(Math.random() % (n-4));
	
		// Compute a^d % n
		long x = power(a, d, n);
	
		if (x==1 || x==n-1) return true;

		//core loop
		while (d != n-1) {
			x = (x*x) % n;
			d *= 2;
			if (x==1) return false;
			if (x==n-1) return true;
		}
	
		// Return composite
		return false;
	}
	
	public static boolean isPrime(long n, int k) {
		
		//Trivial cases: (-infin, 3) and even nums
		if (n==3 || n==2) return true;
		if (n<=1 || n%2==0) return false;
	
		// Find r such that n = 2^d * r + 1
		long d = n-1;
		while (d%2==0) {
			d /= 2;
		}
	
		// run miller test k times, probability of false-pos = 4^(-k)
		for (int i = 0; i < k; i++) {
			if (!MillerTest(d, n)) return false;
		}

		return true;
	}

	//(x^y) % p method
	public static long power(long x, long y, long p) {
		long result = 1L;

		//ensures x is mod p already
		x %= p;

		while (y > 0) {
			
			// If y is odd, multiply x with result
			if (y%2 == 1) result = (result * x) % p;
		
			// y is now even, so multiply by x^2 every time to speed this up
			y /= 2;
			x = (x * x) % p;
		}
		return result;
	}

	//method to generate a random number of a specified bit-length
	//used biginteger because it has a constructor that allows you to gen a random num of a max specified bit-length
	public static long RandomBitInt(int numBits) {
		BigInteger minLimit = BigInteger.valueOf(2).pow(numBits);
        BigInteger maxLimit = (BigInteger.valueOf(2).pow(numBits+1)).subtract(BigInteger.valueOf(1));
        BigInteger bigInteger = maxLimit.subtract(minLimit);

        Random randNum = new Random();

        BigInteger result = new BigInteger(numBits, randNum); //random big int up to numBits number of bits
        if (result.compareTo(minLimit) < 0) {
        	result = result.add(minLimit);
        }
        if (result.compareTo(bigInteger) >= 0) {
        	result = result.mod(bigInteger).add(minLimit);
        }
        return result.longValue();
	}

	public static int GeneratePrime(int numBits, int k) {
		long current = RandomBitInt(numBits);

		Boolean status = false;
		while(status == false) {
			if(isPrime(current, k) == true) {
				return (int)current;
			}
			current = RandomBitInt(numBits);
			
		}
		return (int)current;
	}

/** 
Now, we calculate ϕ(prime1*prime2), which 
is equal to (prime1-1)(prime2-1). This is easy to show.
This is now to be multiplied by some e that is coprime
to both prime 1 and prime 2. In practice, e is set to 
65537, but I will use 3 for simplicity's sake.
**/

	public static long TotientN(long p, long q) { //calculates totient n
		return (p-1)*(q-1);
	}

	public static long CalcD(long e, long totientN) {
		//form: totientN_0 = q(e_0) + c
		long temp = totientN;
        long y = 0;
        long x = 1;
 
        while (e > 1) {
            long q = e/totientN;
            long t = totientN;
 
            // Standard EA
            totientN = e % totientN;
            e = t;
            t = y;
            // Update x and y
            y = x-(q*y);
            x = t;
        }
        // Make x positive
        if (x < 0) x += temp;
 
        return x;
	}

}
