/**
 * 
 */
package ma.cdgp.af;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * @author mjadli
 *
 */
public class GateWaySecurity {
	public static void main(String[] args) throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance("RC4");
		SecretKey originalSecretKey = keygen.generateKey();
		// encryption
		byte[] plaintext = "AgilyS@3001".getBytes(StandardCharsets.UTF_8);
		byte[] ciphertext = encryptRC4(plaintext, originalSecretKey);

		String secret = Base64.getEncoder().encodeToString(
				originalSecretKey.getEncoded());
	}

	public static byte[] encryptRC4(byte[] b, SecretKey k) throws Exception {
		Cipher cipher = Cipher.getInstance("RC4");
		cipher.init(Cipher.ENCRYPT_MODE, k);
		byte[] encrypted = cipher.doFinal(b);
		return encrypted;
	}

	public static byte[] decryptRC4(byte[] b, SecretKey k) throws Exception {
		Cipher cipher = Cipher.getInstance("RC4");
		cipher.init(Cipher.DECRYPT_MODE, k);
		byte[] decrypted = cipher.doFinal(b);
		return decrypted;
	}
	
	public static Map<String, String> getSecret() throws Exception {
		Map<String, String> mapCredentials = new HashMap();
		KeyGenerator keygen = KeyGenerator.getInstance("RC4");
		SecretKey originalSecretKey = keygen.generateKey();
		// encryption
		byte[] plaintext = "AgilyS@3001".getBytes(StandardCharsets.UTF_8);
		byte[] ciphertext = encryptRC4(plaintext, originalSecretKey);

		String secret = Base64.getEncoder().encodeToString(
				originalSecretKey.getEncoded());
		String cipher = Base64.getEncoder().encodeToString(ciphertext);
		mapCredentials.put("SECRET", secret);
		mapCredentials.put("CIPHER", cipher);
		System.out.println(secret);
		System.out.println(cipher);
		return mapCredentials;
	}
	
}
