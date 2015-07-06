package com.game.server.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StreamUtil {
	/**
	 * 把inputStream转成String
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public static InputStream string2InputStream(String content)
			throws UnsupportedEncodingException {
		InputStream is = string2InputStream(content, "UTF8");
		return is;
	}

	/**
	 * 把content转成input流
	 * 
	 * @param content
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static InputStream string2InputStream(String content, String encode)
			throws UnsupportedEncodingException {
		InputStream is = new ByteArrayInputStream(content.getBytes(encode));
		return is;
	}
}