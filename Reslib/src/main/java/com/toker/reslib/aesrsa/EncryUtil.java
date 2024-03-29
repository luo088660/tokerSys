package com.toker.reslib.aesrsa;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

public class EncryUtil {
	private static final Logger log = Logger.getLogger(EncryUtil.class.getSimpleName());
	/**
	 * 生成RSA签名
	 */
	public static String handleRSA(TreeMap<String, Object> map,
			String privateKey) {
		StringBuffer sbuffer = new StringBuffer();
		for (Entry<String, Object> entry : map.entrySet()) {
			sbuffer.append(entry.getValue());
		}
		String signTemp = sbuffer.toString();

		String sign = "";
		if (!isEmpty(privateKey)) {
			sign = RSA.sign(signTemp, privateKey);
		}
		return sign;
	}
	public static String handleRSA(String data,
								   String privateKey) {

		String sign = "";
		if (!TextUtils.isEmpty(privateKey)) {
			sign = RSA.sign(data, privateKey);
		}
		return sign;
	}
	public static boolean isEmpty( CharSequence str) {
		return str == null || str.length() == 0;
	}
	/**
	 * 对易宝支付返回的结果进行验签
	 *
	 * @param data
	 *            易宝支付返回的业务数据密文
	 * @param encrypt_key
	 *            易宝支付返回的对ybAesKey加密后的密文
	 * @param yibaoPublickKey
	 *            易宝支付提供的公钥
	 * @param merchantPrivateKey
	 *            商户自己的私钥
	 * @return 验签是否通过
	 * @throws Exception
	 */
	public static boolean checkDecryptAndSign(String data, String encrypt_key,
			String yibaoPublickKey, String merchantPrivateKey) throws Exception {

		/** 1.使用YBprivatekey解开aesEncrypt。 */
		String AESKey = "";
		try {
			AESKey = RSA.decrypt(encrypt_key, merchantPrivateKey);
		} catch (Exception e) {
			e.printStackTrace();
			/** AES密钥解密失败 */
			return false;
		}

		/** 2.用aeskey解开data。取得data明文 */
		String realData = AES.decryptFromBase64(data, AESKey);

		TreeMap<String, String> map = JSON.parseObject(realData,
				new TypeReference<TreeMap<String, String>>() {
				});

		/** 3.取得data明文sign。 */
		String sign = map.get("sign").trim();

		/** 4.对map中的值进行验证 */
		StringBuffer signData = new StringBuffer();
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();

			/** 把sign参数隔过去 */
			if ("sign".equals(entry.getKey())) {
				continue;
			}
			signData.append(entry.getValue() == null ? "" : entry.getValue());
		}

		/** 5. result为true时表明验签通过 */
		boolean result = RSA.checkSign(signData.toString(), sign,
				yibaoPublickKey);

		return result;
	}

	/**
	 * 生成hmac
	 */
	public static String handleHmac(TreeMap<String, String> map, String hmacKey) {
		StringBuffer sbuffer = new StringBuffer();
		for (Entry<String, String> entry : map.entrySet()) {
			sbuffer.append(entry.getValue());
		}
		String hmacTemp = sbuffer.toString();

		String hmac = "";
		if (!TextUtils.isEmpty(hmacKey)) {
			hmac = Digest.hmacSHASign(hmacTemp, hmacKey, Digest.ENCODE);
		}
		return hmac;
	}
}
