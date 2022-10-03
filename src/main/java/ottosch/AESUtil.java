package ottosch;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import com.samourai.wallet.RandomUtil;

/**
 * AESUtil - code from ExtLibJ.
 * @author Samourai Wallet
 * {@link} https://code.samourai.io/wallet/ExtLibJ/-/blob/master/java/com/samourai/wallet/crypto/AESUtil.java
 *
 */
public class AESUtil {
	public static String encrypt(char[] cleartext, char[] passphrase) {
		return encrypt(String.valueOf(cleartext), passphrase);
	}

	public static String encrypt(String cleartext, char[] passphrase) {
		byte iv[] = RandomUtil.getInstance().nextBytes(16);
		byte[] clearbytes = cleartext.trim().getBytes(StandardCharsets.UTF_8);

		PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
		generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(passphrase), iv, 5000);
		KeyParameter keyParam = (KeyParameter) generator.generateDerivedParameters(256);

		CipherParameters params = new ParametersWithIV(keyParam, iv);
		BlockCipher cipherMode = new CBCBlockCipher(new AESEngine());
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(cipherMode, new ISO10126d2Padding());
		cipher.reset();
		cipher.init(true, params);

		byte[] outBuf = cipherData(cipher, clearbytes);
		int len1 = iv.length;
		int len2 = outBuf.length;
		byte[] ivAppended = new byte[len1 + len2];
		System.arraycopy(iv, 0, ivAppended, 0, len1);
		System.arraycopy(outBuf, 0, ivAppended, len1, len2);

		byte[] raw = Base64.encodeBase64(ivAppended);
		return new String(raw, StandardCharsets.UTF_8);
	}

	private static byte[] cipherData(BufferedBlockCipher cipher, byte[] data) {
		int minSize = cipher.getOutputSize(data.length);
		byte[] outBuf = new byte[minSize];
		int len1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
		int len2 = -1;
		try {
				len2 = cipher.doFinal(outBuf, len1);
		} catch (InvalidCipherTextException icte) {
				icte.printStackTrace();
		}

		int actualLength = len1 + len2;
		byte[] result = new byte[actualLength];
		System.arraycopy(outBuf, 0, result, 0, result.length);
		return result;
	}	
}
