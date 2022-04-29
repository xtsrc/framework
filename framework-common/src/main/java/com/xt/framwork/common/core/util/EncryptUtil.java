package com.xt.framwork.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author tao.xiong
 * @Description 加解密
 * @Date 2022/4/1 16:52
 */
@Slf4j
public final class EncryptUtil {
    /**
     * 加密算法：aes，des，des3和rsa
     * CBC(有向量模式)和ECB(无向量模式)
     * 填充模式：NoPadding、PKCS5Padding
     * 创建秘钥：SecretKeySpec和KeyGenerator支持AES，DES，DESede;KeyPairGenerator支持RSA
     * salt: 匹配校验，调用方校验
     */

    public static final String RSA = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
    public static final String AES = "AES/CBC/PKCS5PADDING";

    public static void main(String[] args) throws Exception {
        String saltKey = getSlatKey("xt_salt");
        String publicKey = getPublicKey(saltKey);
        String privateKey = getPrivateKey(saltKey);
        //加密字符串
        String message = "df723820";
        String secretMsg = "357AFB5B9D1A75EB228A97D98BD61EA6BB3E4F21A399780F3A43FEAE8D5B5FF6A507F7C1D5E57831784A00CC83ED599339CA28E30371AD5B2B65DF78E82D765798F600DB3E7495312C1DB4676BDD54A8A58496833E7295690B4604A10C506A1972C887253EC3DF5E821E5BAE3D0B4F6522486B197875233F24208BC211FCA61FCD5CFC4D92C0FC7A47E7CCD7FE2E0EF979BABB76E2E8E8A3CA3BFE4A62C4838052E3F2B8EF201667BCCED7AD57FAC4ECB11C8EBC71886173347017BA245B08134A4E4B58BF277C7E93DD8DF65EB833431A4F3302B67CE24CA233D615E71973F92C28DA1EC2E361D902633BC0B9C42F464A7BF9B05F3A18DAA0702AF6645BF4FE";
        log.info("随机生成的公钥为:" + publicKey);
        log.info("随机生成的私钥为:" + privateKey);
        String messageEn = encrypt(message, publicKey);
        log.info(message + "加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn, privateKey);
        log.info("还原后的字符串为:" + messageDe);
        String msgDe = decrypt(secretMsg, privateKey);
        log.info("还原后的字符串为：" + msgDe);

        String messageEn2 = encrypt(message, saltKey, saltKey);
        log.info(message + "加密后的字符串为:" + messageEn2);
        String messageDe2 = decrypt(messageEn2, saltKey, saltKey);
        log.info("还原后的字符串为:" + messageDe2);
        String secretMsg2 = "8914A9335A8390E2FAF3C7028979736E";
        String msgDe2 = decrypt(secretMsg2, saltKey, saltKey);
        log.info("还原后的字符串为：" + msgDe2);

        String secretPhone = doFinal(1, RSA, "18811010745", "xt_salt");
        String secretPhone2 = doFinal(1, AES, "18811010745", "xt_salt");
        log.info(message + "加密后的字符串为:" + secretPhone);
        log.info(message + "加密后的字符串为:" + secretPhone2);
        String phone = doFinal(2, RSA, "77832069DF5EAF0B675CD9D341C1189A5C66DE15B50DFCA5E92B2731B258AC6BEF14436407D955E7529BC3B1628FDEB05A16EB5C8F84B62A9DBDEE2B2CBB3EF99F6447F62D96635941EABC19274BD27A69DBAEAE165780D2339E13941F6258580DA468F5783190495D286D86D7324123FBE6C0D1F87A417A0F4F7F6E6087613C4F1C9CDDAECFABF82AEB3191B93B1968B0C9D4D8B6B7E83D149716EB44FD7A6B2CECB166E97F2193A3AF6769FF5C7CA1C13F2419688184E28ECD131E01E992EBCB84FC84000A2A693F4CABFFC1E1518BCC887C4ABE7EAEAA3BB67419FD2D6F2680AC3BA9E8643BDF0C4BF06276D404DDE6425ACC6F43F8B663681ABC84123CB9", "xt_salt");
        String phone2 = doFinal(2, AES, "BF255119E20D36B1BE2C22762291C606", "xt_salt");
        log.info("还原后的字符串为:" + phone);
        log.info("还原后的字符串为:" + phone2);

    }

    public static String doFinal(int model, String arithmetic, String text, String salt) {
        try {
            if (ObjectUtil.isAnyNull(model, arithmetic, text, salt)) {
                return null;
            }
            String saltKey = getSlatKey(salt);
            if (Cipher.ENCRYPT_MODE == model) {
                if (RSA.equals(arithmetic)) {
                    String publicKey = getPublicKey(saltKey);
                    return encrypt(text, publicKey);
                } else if (AES.equals(arithmetic)) {
                    return encrypt(text, saltKey, saltKey);
                }
            } else if (Cipher.DECRYPT_MODE == model) {
                if (RSA.equals(arithmetic)) {
                    String privateKey = getPrivateKey(saltKey);
                    return decrypt(text, privateKey);
                } else if (AES.equals(arithmetic)) {
                    return decrypt(text, saltKey, saltKey);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 根据slatKey获取公匙，传入的slatKey作为SecureRandom的随机种子
     * 若使用new SecureRandom()创建公匙，则需要记录下私匙，解密时使用
     */
    protected static String getPublicKey(String slatKey) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        keyPairGenerator.initialize(2048, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /**
     * 根据slatKey获取私匙，传入的slatKey作为SecureRandom的随机种子
     */
    protected static String getPrivateKey(String slatKey) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        keyPairGenerator.initialize(2048, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    /**
     * 获取加密的密匙，传入的slatKey可以是任意长度的，作为SecureRandom的随机种子，
     * 而在KeyGenerator初始化时设置密匙的长度128bit(16位byte)
     */
    protected static String getSlatKey(String slatKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        kgen.init(128, random);
        return Base64.getEncoder().encodeToString(kgen.generateKey().getEncoded());
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     */
    protected static String encrypt(String str, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.getDecoder().decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return DatatypeConverter.printHexBinary(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("加密失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     */
    protected static String decrypt(String str, String privateKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = DatatypeConverter.parseHexBinary(str);
            //base64编码的私钥
            byte[] decoded = Base64.getDecoder().decode(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            log.error("解密失败：{}", e.getMessage());
            return null;
        }

    }

    /**
     * AES 加密
     *
     * @param plainText 原文
     * @param keyHex    秘钥
     * @param ivHex     偏移量（避免相同数据加密相同：随机不可预测）
     * @return 密文
     */
    protected static String encrypt(String plainText, String keyHex, String ivHex) {
        try {
            if (!StringUtils.isEmpty(plainText) && !StringUtils.isEmpty(keyHex) && !StringUtils.isEmpty(ivHex)) {
                byte[] plainTextArray = plainText.getBytes(StandardCharsets.UTF_8);
                byte[] keyArray = getDigest(keyHex);
                byte[] iv = getDigest(ivHex);
                SecretKeySpec secretKey = new SecretKeySpec(keyArray, "AES");
                Cipher cipher = Cipher.getInstance(AES);
                cipher.init(1, secretKey, new IvParameterSpec(iv));
                return DatatypeConverter.printHexBinary(cipher.doFinal(plainTextArray));
            } else {
                return plainText;
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return plainText;
        }

    }

    /**
     * AES 解密
     *
     * @param messageHex 密文
     * @param keyHex     秘钥
     * @param ivHex      偏移量
     * @return 原文
     */
    protected static String decrypt(String messageHex, String keyHex, String ivHex) {
        try {
            if (!StringUtils.isEmpty(messageHex) && !StringUtils.isEmpty(keyHex) && !StringUtils.isEmpty(ivHex)) {
                byte[] messageArray = DatatypeConverter.parseHexBinary(messageHex);
                byte[] keyArray = getDigest(keyHex);
                byte[] iv = getDigest(ivHex);
                SecretKey secretKey = new SecretKeySpec(keyArray, "AES");
                Cipher cipher = Cipher.getInstance(AES);
                cipher.init(2, secretKey, new IvParameterSpec(iv));
                return new String(cipher.doFinal(messageArray));
            } else {
                return messageHex;
            }
        } catch (Exception var8) {
            log.warn(var8.getMessage());
            return messageHex;
        }
    }

    /**
     * MD5 不可逆
     *
     * @param strValue 编码原文
     * @return 不可逆编码
     * @throws Exception e
     */
    protected static byte[] getDigest(String strValue) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return md5.digest(strValue.getBytes());
    }
}
