package io.polaris.core.crypto.asymmetric;

import io.polaris.core.crypto.CryptoKeys;
import io.polaris.core.err.CryptoRuntimeException;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Qt
 * @since 1.8
 */
public class DSA {

	public static KeyPair getKeyPair() {
		return CryptoKeys.generateKeyPair(AsymmetricAlgorithm.DSA.code());
	}

	public static byte[] sign(PrivateKey key, byte[] data)  {
		try {
			return Signatures.sign(AsymmetricAlgorithm.DSA.code(), key, data);
		} catch (GeneralSecurityException e) {
			throw new CryptoRuntimeException(e);
		}
	}

	public static boolean verify(PublicKey key, byte[] data, byte[] sign)  {
		try {
			return Signatures.verify(AsymmetricAlgorithm.DSA.code(), key, data, sign);
		} catch (GeneralSecurityException e) {
			throw new CryptoRuntimeException(e);
		}
	}


}
