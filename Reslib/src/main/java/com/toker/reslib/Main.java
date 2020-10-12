package com.toker.reslib;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toker.reslib.aesrsa.AES;
import com.toker.reslib.aesrsa.EncryUtil;
import com.toker.reslib.aesrsa.RSA;
import com.toker.reslib.aesrsa.RandomUtil;

import java.util.TreeMap;

/**
 * AES+RSA签名，加密 验签，解密
 *
 * @author wustrive
 * @ClassName: Main
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015年8月23日 上午1:14:27
 */
public class Main {


    public static final String clientPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOb4gCU/0Q8Xjz5f\n" +
            "bzmPmbAzP78FU7a3xNhsnQfvDP1S/Lisj9ozTmG5Sjp9Ztdrcrgt7S82pnKRcCkM\n" +
            "zdf9W8q/Sro7+0da+B/NDPSYzzmqiLjeb6jO5N/GOGbXKcwmszemQlchdpNY80IZ\n" +
            "eA2XaUmCrGR/fyMOvpo9qQCqVObhAgMBAAECgYEAgTiiqTRtCF6L+791AOVAfP4P\n" +
            "OhYG4gEqllMd/+RUhh452jBhQxjVT4NU7ihes0MgKb84JeQTd6G6+Jx7Rc4v3czT\n" +
            "qzNYCLHzEPOhg68bNVzb5j2ZxkGXUe3ze7bF0cVNbhq65MdbaQtBDkdaarc59/Ti\n" +
            "4WhbI95EFvbcg6Aqv4ECQQD02T50wgbE7ubMyMVuNkdo1vZUoohA1ppw7/00guEB\n" +
            "Q4gSfDGPyVFp2EmLU+orm+5/5M/1NOh4nkl/xIwTi89pAkEA8X1zS4luw8iMshtx\n" +
            "QM7JrVVMHTCmGJSbV4HZNH6A1NB5RUMVycEjq2vzZlNEGV5ZKWJIvE4GhOxKk7mf\n" +
            "8S9kuQJADOd+MS0gqjRlGZnjPeHCdbYOmXtPbwtgbF9ZmvDi5uwmOcMt4YQctFVf\n" +
            "2uX7S30WXssyYpv9PI4rFX7IWyddMQJAVR5e0TUz8ZZy4SSDGtDIjWwPVAqdwPRA\n" +
            "fOur37DnQBvrYtpuh5qoM/fs5xhaXIbA7rFR6e2mppuS/dbj5WNLIQJBANGRTZKd\n" +
            "Ul5f3GnY58jwTh5SUYmiYY6C61YaE3p6PyluBmxPzY0AWk6l4zWO4fGZzJ8s/k1u\n" +
            "dI4hyPYeVRkpChk=";
    public static final String clientPublicKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDm+IAlP9EPF48+X285j5mwMz+/\n" +
                    "BVO2t8TYbJ0H7wz9Uvy4rI/aM05huUo6fWbXa3K4Le0vNqZykXApDM3X/VvKv0q6\n" +
                    "O/tHWvgfzQz0mM85qoi43m+ozuTfxjhm1ynMJrM3pkJXIXaTWPNCGXgNl2lJgqxk\n" +
                    "f38jDr6aPakAqlTm4QIDAQAB";

    public static final String serverPrivateKey =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOb4gCU/0Q8Xjz5f\n" +
                    "bzmPmbAzP78FU7a3xNhsnQfvDP1S/Lisj9ozTmG5Sjp9Ztdrcrgt7S82pnKRcCkM\n" +
                    "zdf9W8q/Sro7+0da+B/NDPSYzzmqiLjeb6jO5N/GOGbXKcwmszemQlchdpNY80IZ\n" +
                    "eA2XaUmCrGR/fyMOvpo9qQCqVObhAgMBAAECgYEAgTiiqTRtCF6L+791AOVAfP4P\n" +
                    "OhYG4gEqllMd/+RUhh452jBhQxjVT4NU7ihes0MgKb84JeQTd6G6+Jx7Rc4v3czT\n" +
                    "qzNYCLHzEPOhg68bNVzb5j2ZxkGXUe3ze7bF0cVNbhq65MdbaQtBDkdaarc59/Ti\n" +
                    "4WhbI95EFvbcg6Aqv4ECQQD02T50wgbE7ubMyMVuNkdo1vZUoohA1ppw7/00guEB\n" +
                    "Q4gSfDGPyVFp2EmLU+orm+5/5M/1NOh4nkl/xIwTi89pAkEA8X1zS4luw8iMshtx\n" +
                    "QM7JrVVMHTCmGJSbV4HZNH6A1NB5RUMVycEjq2vzZlNEGV5ZKWJIvE4GhOxKk7mf\n" +
                    "8S9kuQJADOd+MS0gqjRlGZnjPeHCdbYOmXtPbwtgbF9ZmvDi5uwmOcMt4YQctFVf\n" +
                    "2uX7S30WXssyYpv9PI4rFX7IWyddMQJAVR5e0TUz8ZZy4SSDGtDIjWwPVAqdwPRA\n" +
                    "fOur37DnQBvrYtpuh5qoM/fs5xhaXIbA7rFR6e2mppuS/dbj5WNLIQJBANGRTZKd\n" +
                    "Ul5f3GnY58jwTh5SUYmiYY6C61YaE3p6PyluBmxPzY0AWk6l4zWO4fGZzJ8s/k1u\n" +
                    "dI4hyPYeVRkpChk=";
    public static final String serverPublicKey =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDm+IAlP9EPF48+X285j5mwMz+/\n" +
                    "BVO2t8TYbJ0H7wz9Uvy4rI/aM05huUo6fWbXa3K4Le0vNqZykXApDM3X/VvKv0q6\n" +
                    "O/tHWvgfzQz0mM85qoi43m+ozuTfxjhm1ynMJrM3pkJXIXaTWPNCGXgNl2lJgqxk\n" +
                    "f38jDr6aPakAqlTm4QIDAQAB";

    public static final String data = "mOMGwQGXd8Pt7jbhh44+Yz7NBpPe47Jz5rYbD4xxan1VGEoUZWgkr7AFOOk/fBErpYTgi3wGOh4X+MTgyeE7Pb/tczrb/SEPRmH6KWDp1Pe6JU9se/XzOtuf/iaCb/heCpWfGguKei65iRbgIHmWVeMwtID9NGRA5wrgGKZ0eK/mQfIDbopEfCtB4U6oKDQhtZIjXwV31MWcEjyJs36RucXvpXS5qc/PxpgAB02ozPOq7urmssg0KvhOojZCI2x40+MN1jyFp3kYGwBwrwzT/2Y6oPW2J5sgXdTPVjiBMgt3Gl3JEDoLx330a452aIq9";

    public static void main(String[] args) throws Exception {
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("userid", "152255855");
        params.put("phone", "18965621420");
        client(params);
        System.out.println("data.length：" + data.length());
        server();
    }

    public static void client(TreeMap<String, Object> params) throws Exception {
        // 生成RSA签名
        String sign = EncryUtil.handleRSA(params, clientPrivateKey);
        params.put("sign", sign);

        String info = JSON.toJSONString(params);
        //随机生成AES密钥
        String aesKey = RandomUtil.getRandom(AES.number);
        //AES加密数据
        String data = AES.encryptToBase64(info, aesKey);

        // 使用RSA算法将商户自己随机生成的AESkey加密
        String encryptkey = RSA.encrypt(aesKey, serverPublicKey);

        Req.data = data;
        Req.encryptkey = encryptkey;

        System.out.println("加密后的请求数据:\n" + new Req().toString());
    }

    public static void server() throws Exception {

        // 验签
        boolean passSign = EncryUtil.checkDecryptAndSign(Req.data,
                Req.encryptkey, clientPublicKey, serverPrivateKey);

        if (passSign) {
            // 验签通过
            String aeskey = RSA.decrypt(Req.encryptkey,
                    serverPrivateKey);
            String data = AES.decryptFromBase64(Req.data,
                    aeskey);
            System.out.println("解密后的明文 data:" + data);
            JSONObject jsonObj = JSONObject.parseObject(data);
            String userid = jsonObj.getString("userid");
            String phone = jsonObj.getString("phone");

            System.out.println("解密后的明文:userid:" + userid + " phone:" + phone);

        } else {
            System.out.println("验签失败");
        }
    }

    static class Req {
        public static String data;
        public static String encryptkey;

        @Override
        public String toString() {
            return "data:" + data + "\nencryptkey:" + encryptkey;
        }
    }
}
