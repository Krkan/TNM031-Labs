//Izmir Labb 2.

import java.math.BigInteger;
import java.security.*;

public class RSA {

	private String randomPrime() {
		SecureRandom random = new SecureRandom();
		BigInteger prime = BigInteger.probablePrime(512, random);
		return prime + "";
	}

	private BigInteger p_prime = new BigInteger(randomPrime());
	private BigInteger q_prime = new BigInteger(randomPrime());
	private BigInteger n = p_prime.multiply(q_prime);
	private BigInteger phi_n = p_prime.subtract(BigInteger.valueOf(1))
			.multiply(q_prime.subtract(BigInteger.valueOf(1)));
	
	// e is the public key.
	//private BigInteger e = BigInteger.valueOf(65537);
	private BigInteger e = new BigInteger(randomPrime());
	
	
	// d is the generated private key.
	private BigInteger d = e.modInverse(phi_n);

	// The message that will be encrypted and decrypted.
	private String msg = "Ibrahimovic";
	private BigInteger m = new BigInteger(msg.getBytes());

	// Send e & n to the client so they can encrypt their message m. C is the
	// encrypted message.
	private BigInteger c = m.modPow(e, n);

	private void printResults() {
		System.out.println("GCD of e and phi_n = " + phi_n.gcd(e));
		System.out.println("Public key to be sent = " + e);
		System.out.println("N to be sent = " + n);

		System.out.println("Number to be encrypted = " + m);
		System.out.println("Message to be encrypted = " + msg);
		// Decrypt
		System.out.println("Number that was decrypted = " + c.modPow(d, n));
		System.out.println("Decrypted message = " + new String(c.modPow(d, n).toByteArray()));
	}

	public static void main(String[] args) {
		RSA start = new RSA();
		start.printResults();
	}

}
