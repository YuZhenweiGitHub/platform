package com.yzw.platform.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * HMAC 基础加密组件
 * HMAC(Hash Message Authentication Code，散列消息鉴别码，基于密钥的Hash算法的认证协议。
 * 消息鉴别码实现鉴别的原理是，用公开函数和密钥产生一个固定长度的值作为认证标识，用这个标识鉴别消息的完整性。
 * 使用一个密钥生成一个固定大小的小数据块，
 * 即MAC，并将其加入到消息中，然后传输。接收方利用与发送方共享的密钥进行鉴别认证等。
 *
 * @ClassName HMAC
 * com.yzw.platform.utils
 * @Description
 * @Author yzw
 * @Date 2018/12/13 17:08
 * @Version 1.0.0
 */
public abstract class HMAC {
    public static final String KEY_MAC = "HmacMD";

    /**
     * 初始化HMAC密钥
     *
     * @return
     * @throws Exception
     */
    public static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return BASE.encryptBASE(secretKey.getEncoded());
    }

    /**
     * HMAC加密 ：主要方法
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptHMAC(byte[] data, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(BASE.decryptBASE(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return new String(mac.doFinal(data));
    }

    public static String getEncryptHMAC(String inputStr) {
        String result = null;
        try {
            byte[] inputData = inputStr.getBytes();
            String key = HMAC.initMacKey(); /*产生密钥*/
            System.out.println("Mac密钥:===" + key);
            result = HMAC.encryptHMAC(inputData, key);
            System.out.println("HMAC加密后:===" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String args[]) {
        try {
            String inputStr = "简单加密";
            /*使用同一密钥：对数据进行加密：查看两次加密的结果是否一样*/
            System.out.println(getEncryptHMAC(inputStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
