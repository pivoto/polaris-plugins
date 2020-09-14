package io.polaris.core.crypto.symmetric;

import io.polaris.core.crypto.Ciphers;

/**
 * @author Qt
 * @since 1.8
 */
public class DES {

	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		return Ciphers.encrypt(SymmetricAlgorithm.DES.code(), key, data);
	}

	public static byte[] encryptByKeySeed(byte[] data, byte[] key) throws Exception {
		return Ciphers.encryptByKeySeed(SymmetricAlgorithm.DES.code(), key, data);
	}

	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		return Ciphers.decrypt(SymmetricAlgorithm.DES.code(), key, data);
	}

	public static byte[] decryptByKeySeed(byte[] data, byte[] key) throws Exception {
		return Ciphers.decryptByKeySeed(SymmetricAlgorithm.DES.code(), key, data);
	}
}
