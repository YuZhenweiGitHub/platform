package com.yzw.platform.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);
	/**
	 * 通过MD5加密算法返回加密后的字符串
	 * 
	 * @param text
	 *            明文（要加密的字符串）
	 * @return
	 */
	public static String createMD5(String text) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {// 理论上不会有这个异常
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}
		md.update(text.getBytes());
		byte b[] = md.digest();
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			int i = b[offset];
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");// 不足两位，补0
			}
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
	
	  /**
     * 通过MD5加密算法返回加密后的字符串
     *
     * @param str 明文（要加密的字符串）
     * @return 大写的32为串
     */
    public static String createCapitalizationMD5(String str) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = str.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char strt[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                strt[k++] = hexDigits[byte0 >>> 4 & 0xf];
                strt[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(strt);
        } catch (Exception e) {
			LOGGER.error("通过MD5加密算法错误:{}", e);
            return null;
        }
    }

	public static String getShiroMD5Pwd(String username, String pwd) {
		// 加密算法MD5
		// salt盐 username + salt
		// 迭代次数
		String md5Pwd = new SimpleHash("MD5", pwd,
				ByteSource.Util.bytes(username + "salt"), 2).toHex();
		return md5Pwd;
	}
}
