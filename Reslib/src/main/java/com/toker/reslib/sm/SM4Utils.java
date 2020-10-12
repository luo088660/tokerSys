package com.toker.reslib.sm;



import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fsr
 * @Date 2019/7/17 20:57
 * @Description:
 **/
public class SM4Utils {


    /**
     * 随机生成一个 SM4 秘钥
     *
     * @return byte数组，长度为16
     */
    public static String createSM4KeyInner() {
        byte[] keyBytes = new byte[16];

        SecureRandom random = new SecureRandom();
        random.nextBytes(keyBytes);
        return toHexString(keyBytes);
    }

    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString();
    }

    /**
     * sm4加密
     * @param secretKey
     * @param plainText
     * @return
     */

    public static String encryptData_ECB(String secretKey,String plainText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            if (false) {
//                keyBytes = ByteUtils.fromHexString(secretKey);
            }
            else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes("UTF-8"));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sm4 解密
     * @param secretKey
     * @param cipherText
     * @return
     */
    public static String decryptData_ECB(String secretKey , String cipherText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            if (false) {
//                keyBytes = ByteUtils.fromHexString(secretKey);
            }
            else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx,  new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) throws IOException {
        String plainText = "{\"name\":\"aa\",\"country\":\"中国\",\"city\":\"广州\"}";
        String key = "1eF4U5wHFOMfs7Y2";

        System.out.println("ECB模式");//电码本
        String cipherText = encryptData_ECB(key,plainText);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = decryptData_ECB("1eF4U5wHFOMfs7Y2JeF4U5wHFOMfs7Y112",cipherText);
        System.out.println("明文: " + plainText);
        System.out.println("");

        plainText = decryptData_ECB("913a3332ae06b5990a0204dca53f03b1","ExS0xejMmm2YSKTPZhqf2juaWmBez7wp9cnD0KoUIvYHSj9MUReCs2nmImwhE2RF");
        System.out.println("明文: " + plainText);
        System.out.println("");
    }

}
