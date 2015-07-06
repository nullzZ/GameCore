package com.game.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	/**
	 * 检测用户名是否合法
	 * 
	 * @param userName
	 */
	public static boolean checkUserName(String userName) {
		String regEx = "\\w{6,16}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(userName);
		return mat.matches();
	}

	/**
	 * 检测密码是否符合规定，长度6到16
	 * 
	 * @param password
	 * @return
	 */
	public static boolean checkPassword(String password) {
		String regEx = "\\w{6,16}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(password);
		return mat.matches();
	}

	public static boolean checkPhoneOrEmail(int type, String newStr) {
		// 验证邮箱
		if (type == 1) {
			String regEx = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
			Pattern pat = Pattern.compile(regEx);
			Matcher mat = pat.matcher(newStr);
			return mat.matches();
		}
		// 验证手机
		else if (type == 2) {
			String regEx = "^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\\d{8}$";
			Pattern pat = Pattern.compile(regEx);
			Matcher mat = pat.matcher(newStr);
			return mat.matches();
		}
		return false;
	}
}
