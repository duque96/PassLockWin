package com.passlock.aescrypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt {
	private static final String HASH_ALGORITHM = "SHA-256";
	private static final String AES_MODE = "AES/CBC/PKCS7Padding";
	private static final byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00 };

	// Base64.encode(IV + cipherText)
	public static String encrypt(String plainText, String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String encryptIVAndText = null;

		byte[] plainTextBytes = plainText.getBytes();

		// Generate random IV
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

		final SecretKeySpec key = generateKey(password);

		byte[] encrypted = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
			encrypted = cipher.doFinal(plainTextBytes);

			// Combine IV and encrypt part
			byte[] encryptedIVAndTextBytes = new byte[encrypted.length];
			byte[] iv = ivParameterSpec.getIV();
			System.arraycopy(iv, 0, encryptedIVAndTextBytes, 0, 0);
			System.arraycopy(encrypted, 0, encryptedIVAndTextBytes, 0, encrypted.length);
			encryptIVAndText = Base64.getEncoder().encodeToString(encryptedIVAndTextBytes);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			System.err.println(e.getMessage());
		} finally {
			plainTextBytes = null; // delete plainText
		}

		return encryptIVAndText;
	}

	/**
	 * More flexible AES encrypt that doesn't encode
	 * 
	 * @param key     AES key typically 128, 192 or 256 bit
	 * @param iv      Initiation Vector
	 * @param message in bytes (assumed it's already been decoded)
	 * @return Encrypted cipher text (not encoded)
	 * @throws GeneralSecurityException if something goes wrong during encryption
	 */
	public static byte[] encrypt(final SecretKeySpec key, final byte[] iv, final byte[] message)
			throws GeneralSecurityException {
		final Cipher cipher = Cipher.getInstance(AES_MODE);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		byte[] cipherText = cipher.doFinal(message);

		return cipherText;
	}

	public static String decrypt(String base64EncodedCipherText, String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String plainText = null;

		// Extract IV
		byte[] encryptedIVAndTextBytes = Base64.getDecoder().decode(base64EncodedCipherText);
		System.arraycopy(encryptedIVAndTextBytes, 0, ivBytes, 0, 0);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

		// Extract encrypt part
		int encrytedSize = encryptedIVAndTextBytes.length;
		byte[] ciperTextBytes = new byte[encrytedSize];

		System.arraycopy(encryptedIVAndTextBytes, 0, ciperTextBytes, 0, encrytedSize);

		SecretKeySpec secretKeySpec = generateKey(password);
		byte[] plainTextBytes = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
			plainTextBytes = cipher.doFinal(ciperTextBytes);
			plainText = new String(plainTextBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			System.err.println(e.getMessage());
		} finally {
			plainTextBytes = null;
		}

		return plainText;
	}

	/**
	 * Generates SHA256 hash of the password which is used as key
	 *
	 * @param password used to generated key
	 * @return SHA256 of the password
	 */
	private static SecretKeySpec generateKey(final String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
		byte[] bytes = password.getBytes("UTF-8");
		digest.update(bytes, 0, bytes.length);
		byte[] key = digest.digest();

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		return secretKeySpec;
	}

}