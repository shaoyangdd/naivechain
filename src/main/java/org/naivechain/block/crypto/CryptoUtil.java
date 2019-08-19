package org.naivechain.block.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * 加密工具类
 * @author kangshaofei
 * @date 2019-08-19
 */
public class CryptoUtil {

    private static final Logger logger = LoggerFactory.getLogger(CryptoUtil.class);

    private CryptoUtil() {
    }

    /**
     * 获取sha256
     *
     * @param string 字符串
     * @return sha256
     */
    public static String getSHA256(String string) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(string.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            logger.error("获取sha256失败:" + string, e);
        }
        return encodeStr;
    }

    /**
     * 字节转8进制字符串
     * @param bytes 字节数组
     * @return 8进制字符串
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 0; i < bytes.length; i++) {
            //此处&0xFF是为了保持二进制的一致性，(补码) 详细知识参考: https://www.cnblogs.com/think-in-java/p/5527389.html
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString();
    }
}
