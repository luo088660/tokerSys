package com.toker.reslib.sm4;

//import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fsr
 * @Date 2019/7/17 14:04
 * @Description:
 **/
public class RSAEncryptor {

    public static void main(String[] args) throws Exception {

        try {

            String test = "JeF4U5wHFOMfs7Y2";
            String testRSAEnWith64 = encryptWithBase64(test);
            System.out.println(testRSAEnWith64);

            String decryptWithBase64 = decryptWithBase64(testRSAEnWith64);
            System.out.println(decryptWithBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static String publicKeyStr ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCQRyQDUDRrLlE1xST+UeoTH2V\n" +
            "fYZ35ewBtlNi8P6Tj+6Zrftxj7q0Xes3YDYgWzdLVa/KPcIPoc/OmxvofvDlssEA\n" +
            "XdHKmSRq3gn6JiwIfKgqqo9GS/7h2V/C7m3aJYygxeGd+CZ74/hvYJufjJBNwtDW\n" +
            "Kjz7EqNj2pzTjMRvVQIDAQAB";

    static String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMJBHJANQNGsuUTX\n" +
            "FJP5R6hMfZV9hnfl7AG2U2Lw/pOP7pmt+3GPurRd6zdgNiBbN0tVr8o9wg+hz86b\n" +
            "G+h+8OWywQBd0cqZJGreCfomLAh8qCqqj0ZL/uHZX8LubdoljKDF4Z34Jnvj+G9g\n" +
            "m5+MkE3C0NYqPPsSo2PanNOMxG9VAgMBAAECgYBLasQQGVUlEo4LcAWJParYaHoJ\n" +
            "Y+ak/hsWvxa4vx/XYnYcLDQG7uppxFzh7vSUs1VQZkANoNSLBSOM7yNyrHYRyifQ\n" +
            "Yed77BlzsfXeK7gVpVGU9kR40Ar/uDZPgt4NNJjDoGaXV5ESYPr54Hr7vcBiYwyH\n" +
            "xtT0oT1fFFkEm0xKMQJBAPARJrdnHinHLI+2SdSQOSdmTh0/cJvCuf/CERfr45Ee\n" +
            "yEmQPwIof+/S+vTv7VIxy/ruqXkZMGaj6rtRMdZk+rsCQQDPJZUODK67shUZyogZ\n" +
            "CpdgxnJYqBoPVh3Bpk9YkIf/3TO1ubq9FMie0BDb/Ivyh7d5QVN+qjdaKML0eThO\n" +
            "kkUvAkEAipXMHOoZXpLNZbUcz+nmJJmJlJbP4gs6pquYhI+3UkpL4zOzPAK3QHIy\n" +
            "T33GKcH3eEeYbAEfkNElq7cGpzaqCwJAdXQx2A2I5hRLspwMH2bJ/q2HGI34eMmz\n" +
            "haHvS6Bn/MRIFbqCD8RBHQuNA69nCtW5Sp+n4CJT0GNcZ5J0QtjdMQJAV3APUZOY\n" +
            "+O1vPv1v7EyWRj8nPUOjYt6FUfYDBb86S99zU6xRMKI9tTTDZ2YgpUvF0CobjL1j\n" +
            "/uYuEDo2dVtD8g==";



    public static String decryptWithBase64(String base64String) throws Exception {

        byte[] binaryData = decrypt(getPrivateKey(),Base64.decodeBase64(base64String) /*org.apache.commons.codec.binary.Base64.decodeBase64(base46String.getBytes())*/);
        String string = new String(binaryData);
        return string;
    }

    public static RSAPublicKey getPublicKey() throws Exception {
        RSAPublicKey publicKey = null;
        try {
            byte[] buffer = Base64.decodeBase64(publicKeyStr);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
        return publicKey;
    }
    public static RSAPrivateKey getPrivateKey() throws Exception {
        RSAPrivateKey privateKey = null;
        try {
            byte[] buffer= Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
        return privateKey;
    }

    public static String encryptWithBase64(String string) throws Exception {

        byte[] binaryData = encrypt(getPublicKey(), string.getBytes());
        String base64String = Base64.encodeBase64String(binaryData) /* org.apache.commons.codec.binary.Base64.encodeBase64(binaryData) */;
        return base64String;
    }



    // convenient properties
    public static RSAEncryptor sharedInstance = null;
    public static void setSharedInstance (RSAEncryptor rsaEncryptor) {
        sharedInstance = rsaEncryptor;
    }


    /**
     * 随机生成密钥对
     */
    public Map genKeyPair(){
        KeyPairGenerator keyPairGen= null;
        try {
            keyPairGen= KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair= keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey= (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey= (RSAPublicKey) keyPair.getPublic();
        Map map = new HashMap<String ,Object>();
        map.put("privateKey",privateKey);
        map.put("publicKey",publicKey);
        return map;
    }



    /**
     * 加密过程
     * @param publicKey 公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if(publicKey== null){
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA/None/PKCS1Padding");//, new BouncyCastleProvider()); RSAne/PKCS1Padding
//            cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider()); RSAne/PKCS1Padding
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output= cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey== null){
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
//            cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider());
            cipher= Cipher.getInstance("RSA/None/PKCS1Padding");//, new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output= cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }



    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 字节数据转十六进制字符串
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data){
        StringBuilder stringBuilder= new StringBuilder();
        for (int i=0; i<data.length; i++){
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i<data.length-1){
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }
}
