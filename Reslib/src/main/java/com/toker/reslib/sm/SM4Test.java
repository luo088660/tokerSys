package com.toker.reslib.sm;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toker.reslib.aesrsa.RSA;

import java.util.TreeMap;


public class SM4Test {


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

    public void main() {
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("userid", "152255855");
        params.put("phone", "18965621420");
//         System.out.println("SM4Test"+ "keyBytes.length():" + keyBytes.length());
//         System.out.println("SM4Test"+"keyBytes:"+ keyBytes);
        try {
            client(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        server();
    }

    public static void client(TreeMap<String, Object> params) throws Exception {

        String info = JSON.toJSONString(params);
        String key = "1eF4U5wHFOMfs7Y2";
        String cipherText =SM4Utils.encryptData_ECB( key,info);
        System.out.println("加密数据："+cipherText);
        // 使用RSA算法将商户自己随机生成的AESkey加密
//        String encryptkey = RSA.encrypt(cipherText, serverPublicKey);

        Req.data = cipherText;
//        Req.encryptkey = encryptkey;

        System.out.println("加密后的请求数据:\n" + new Req().toString());
    }


    public void server() throws Exception {
//        Req.encryptkey= "SITez7HOHSNHQgX607lAGxL//TzQqi90IAr+mieFVwTEfgyElAs5pDOtYZgsdoLnHQPEKQRtbi22Hck7QPi9XRbeGcuJ+NW4Na+DF7hVS9q6wlrO56wu9Zl4BJIwQgE3k2Z4ipG7dPYgUkaWJsczceGnnC+TtfJT+UQc2fa89NA=";
        // 验签
        // 验签通过
//        String aeskey = RSA.decrypt(Req.encryptkey, serverPrivateKey);
//        System.out.println("SM4Test"+ "RSA解密数据:" + aeskey);


//        String data = "m0pdaTxrHAjpG0qPoNFuLtFzlk7Xu44HkseT0Z4IRUE60wnaUZh1hyidrGnq+V+86RbB9Kg8AyR4dXOmvYoamw==";
        String key = "1eF4U5wHFOMfs7Y2";
        String data = Req.data;
//        String key = "1eF4U5wHFOMfs7Y2JeF4U5wHFOMfs7Y112";

//        密文:
//        System.out.println(key.length());
        String decryptECB = SM4Utils.decryptData_ECB(key,data);
//        String decryptECB = SM4Utils.decryptData_ECB(aeskey, keyBytes);
        System.out.println("解密后的明文 data:" + decryptECB);
//        JSONObject jsonObj = JSONObject.parseObject(decryptECB);
//        String userid = jsonObj.getString("userid");
//        String phone = jsonObj.getString("phone");
//
//        System.out.println("解密后的明文:userid:" + userid + " phone:" + phone);

    }

    static class Req {
        public static String data;
        public static String encryptkey;

        @Override
        public String toString() {
            return "\ndata:" + data+"\nencryptkey:" + encryptkey;
        }
    }

}