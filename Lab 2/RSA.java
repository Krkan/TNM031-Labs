//Izmir Labb 2.

import java.math.BigInteger;
import java.security.*;

public class RSA {

	private BigInteger randomPrime() {
		SecureRandom random = new SecureRandom();
		BigInteger prime = BigInteger.probablePrime(512, random);
		return prime;
	}

	private BigInteger p_prime = randomPrime();
	private BigInteger q_prime = randomPrime();
	private BigInteger phi_n = p_prime.subtract(BigInteger.valueOf(1))
			.multiply(q_prime.subtract(BigInteger.valueOf(1)));
	
	// e & n together is the public key.
	private BigInteger e = randomPrime();
	private BigInteger n = p_prime.multiply(q_prime);
	
	// d is the generated private key we do not share.
	private BigInteger d = e.modInverse(phi_n);


	// Send e & n to the client so they can encrypt their message "msg". C is the
	// encrypted message.
	
	private String msg = "Ibrahimovic";
	private BigInteger m = new BigInteger(msg.getBytes());
	private BigInteger c = m.modPow(e, n);

	private void printResults() {
		System.out.println("GCD of e and phi_n = " + phi_n.gcd(e));
		System.out.println("E to be sent = " + e);
		System.out.println("N to be sent = " + n + "\n");

		System.out.println("Number to be encrypted = " + m);
		System.out.println("Message to be encrypted = " + msg);
		// Decrypt using d & n
		System.out.println("Number that was decrypted = " + c.modPow(d, n));
		System.out.println("Decrypted message = " + new String(c.modPow(d, n).toByteArray()));
	}

	public static void main(String[] args) {
		RSA start = new RSA();
		start.printResults();
	}

}
