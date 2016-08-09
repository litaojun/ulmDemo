package com.example.registerandlogindemo.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAUtils {
	public static String encryptBase64(String text, String key) {
		return new String(new BASE64Encoder().encodeBuffer(encrypt(text, key)));
	}

	private static byte[] encrypt(String text, String key) {
		try {
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
					new X509EncodedKeySpec(new BASE64Decoder()
							.decodeBuffer(key)));
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] plainText = cipher.doFinal(new BASE64Decoder()
					.decodeBuffer(text));
			return plainText;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decryptBase64(String text, String key) {
		return new String(new BASE64Encoder().encodeBuffer(decrypt(text, key)));
	}

	private static byte[] decrypt(String text, String key) {
		try {
			PrivateKey privateKey = KeyFactory.getInstance("RSA")
					.generatePrivate(
							new PKCS8EncodedKeySpec(new BASE64Decoder()
									.decodeBuffer(key)));
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] plainText = cipher.doFinal(new BASE64Decoder().decodeBuffer(text));
			return plainText;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
    public static void main(String[] args) throws Exception {  
        File privfile = new File("D:\\Document\\Cryptographic\\privkey.txt");
        File pubfile = new File("D:\\Document\\Cryptographic\\pubkey.txt");
        FileReader reader = new FileReader(pubfile);

        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        br.close();
        reader.close();
        
        String encryptedText = encryptBase64("PLAIN TEXT", sb.toString());
        
        System.out.println("encrypted: " + encryptedText);
        reader = new FileReader(privfile);
        br = new BufferedReader(reader);
        sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        br.close();
        reader.close();
        String decryptedText = decryptBase64(encryptedText, sb.toString());
        System.out.println("decrypted: " + decryptedText);
    }  
}
