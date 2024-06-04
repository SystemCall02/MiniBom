package com.idme.minibom.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


/**
 * @Author: znly
 * @Description:
 * @Date: 2024/4/2 15:04
 */
@Component
@Slf4j
public class AESUtil {
    private static final String slatKey = "wYDJtx8o6HE6YjkS";
    private static final String vectorKey = "SC31fdGsQozTmT5a";
    /**
     * AES/CBC/PKCS5Padding 加密
     *
     * @param content    :待加密的内容.
     * @return 返回 Cipher 加密后的数据，对加密后的字节数组用 Base64 进行编码转成了可视字符串，如 7giH2bqIMH3kDMIg8gq0nY
     * @throws Exception
     */
    public static String encrypt(String content) throws Exception {
        //实例化 Cipher 对象。使用：AES-高级加密标准算法、CBC-有向量模式、PKCS5Padding-填充方案:（加密内容不足8位时用余位数补足8位）
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //使用 SecretKeySpec(byte[] key, String algorithm) 创建密钥. 算法要与 Cipher.getInstance 保持一致.
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), "AES");
        /**
         * init(int opMode,Key key,AlgorithmParameterSpec params)：初始化 Cipher，
         * 1、Cipher.ENCRYPT_MODE 表示加密模式
         * 2、key 表示加密密钥
         * 3、params 表示算法参数规范，使用 CBC 有向量模式时，必须传入,如果是 ECB-无向量模式,那么可以不传
         * 4、所有参数规范都必须实现 {@link AlgorithmParameterSpec} 接口.
         */
        IvParameterSpec parameterSpec = new IvParameterSpec(vectorKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        /**
         * byte[] doFinal(byte[] content)：对 content 完成加密操作，如果 cipher.init 初始化时使用的解密模式，则此时是解密操作.
         * 返回的是加密后的字节数组，如果直接 new String(byte[] bytes) 是会乱码的，可以借助 BASE64 转为可视字符串，或者转成 16 进制字符
         */
        byte[] encrypted = cipher.doFinal(content.getBytes());
        //BASE64Encoder.encode：BASE64 对字节数组内容进行编码，转为可视字符串，这样方便存储和转换.

        String base64Encode = Base64.getEncoder().encodeToString(encrypted);
        return base64Encode;
    }

    /**
     * AES/CBC/PKCS5Padding 解密
     *
     * @param base64Encode :待解密的内容，因为加密时使用了 Base64 进行了编码，所以这里传入的也是 Base64 编码后的可视化字符串
     * @return
     * @throws Exception
     */
    public static String decrypt(String base64Encode) throws Exception {
        //实例化 Cipher 对象。加密算法/反馈模式/填充方案，解密与加密需要保持一致.
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //创建密钥。算法也要与实例化 Cipher 一致.
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), "AES");
        //有向量模式(CBC)需要传入 AlgorithmParameterSpec 算法参数规范参数.
        IvParameterSpec parameterSpec = new IvParameterSpec(vectorKey.getBytes());
        //初始化 cipher。使用解密模式.
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        //将 Base64 编码的内容解码成字节数组(因为加密的时候，对密文使用了 Base64编码，所以这里需要先解码)
        byte[] content = Base64.getDecoder().decode(base64Encode);
        //执行解密操作。返回解密后的字节数组，此时可以使用 String(byte bytes[]) 转成源字符串.
        byte[] decrypted = cipher.doFinal(content);
        return new String(decrypted);
    }


}
