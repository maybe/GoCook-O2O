package com.m6.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.text.TextUtils;

public class SecurityUtils {

	// 定义加密算法，有DES、DESede(即3DES)、Blowfish
	private static final String Algorithm = "DESede/CBC/PKCS5Padding";
	public static final String PASSWORD_CRYPT_KEY = "DAB578EC-6C01-4180-939A-37E6BE8A81AF";
	public static final String PASSWORD_CRYPT_IV = "117A5C0F";

	
	/**
	 * 加密方法
	 * 
	 * @param src
	 *            源数据的字节数组
	 * @return
	 */
	public static String encryptMode(byte[] src) {
		try {
			byte[] bkey = getMD5Encry(PASSWORD_CRYPT_KEY);
			byte[] ebkey = new byte[24];
			for(int i = 0; i < 24; i++) {
				ebkey[i] = 0;
			}
			System.arraycopy(bkey, 0, ebkey, 0, bkey.length);
			DESedeKeySpec dks = new DESedeKeySpec(ebkey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			
			byte[] bIV = getMD5Encry(PASSWORD_CRYPT_IV);
			byte[] ebIV = new byte[8];
			for (int i = 0; i < 8; i++) {
				ebIV[i] = (byte) Math.abs((byteToInt(bIV[i]) - byteToInt(bIV[i + 1])));
			}
			IvParameterSpec iv = new IvParameterSpec(ebIV);
			
			Cipher c1 = Cipher.getInstance(Algorithm); // 实例化负责加密/解密的Cipher工具类
			c1.init(Cipher.ENCRYPT_MODE, securekey, iv); // 初始化为加密模式
			return Base64.encodeToString(c1.doFinal(src), Base64.DEFAULT);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}
	
	private static int byteToInt(byte src) {
		return src & 0XFF;
	}
	
	public static byte[] getMD5Encry(String text) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(text.getBytes());
			return md5.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密函数
	 * 
	 * @param src
	 *            密文的字节数组
	 * @return
	 */
	public static String decryptMode(byte[] src) {
		try {
//			SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
			DESedeKeySpec dks = new DESedeKeySpec(PASSWORD_CRYPT_KEY.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			IvParameterSpec iv = new IvParameterSpec(PASSWORD_CRYPT_IV.getBytes());
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, securekey, iv); // 初始化为解密模式
			return Base64.encodeToString(c1.doFinal(src), Base64.DEFAULT);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据字符串生成密钥字节数组
	 * 
	 * @param keyStr 密钥字符串
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
		byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
		byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组

		/*
		 * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		 */
		if (key.length > temp.length) {
			// 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, temp.length);
		} else {
			// 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	}

	public static String MD5Encry(String text) {
		if (TextUtils.isEmpty(text)) {
			return null;
		}
//		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(text.getBytes());
			// 获得密文
            byte[] md = md5.digest();
            // 把密文转换成十六进制的字符串形式
//            int j = md.length;
//            char str[] = new char[j * 2];
//            int k = 0;
//            for (int i = 0; i < j; i++) {
//                byte byte0 = md[i];
//                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//                str[k++] = hexDigits[byte0 & 0xf];
//            }
			return Base64.encodeToString(md, Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
