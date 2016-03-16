package com.cdeledu.application.security;

/**
 * 
 * @类名称：SecureUtil @功能说明： 安全相关的工具类，包括各种加密算法
 * 
 * @创建人：dell
 *
 */
public class SecureUtil {
	public static final String MD2 = "MD2";
	public static final String MD4 = "MD4";
	public static final String MD5 = "MD5";

	public static final String SHA1 = "SHA-1";
	public static final String SHA256 = "SHA-256";

	public static final String HMAC_SHA1 = "HmacSHA1";

	public static final String RIPEMD128 = "RIPEMD128";
	public static final String RIPEMD160 = "RIPEMD160";

	/** base64码表 */
	private static char[] base64EncodeTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };

}
