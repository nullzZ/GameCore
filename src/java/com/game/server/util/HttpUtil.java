package com.game.server.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.AbstractMessageLite.Builder;

public class HttpUtil {

	public static JSONObject doPost(String url) throws JSONException,
			IOException {
		String data = "";
		URL dataUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Proxy-Connection", "Keep-Alive");
		con.setRequestProperty("accept", "*/*");
		con.setRequestProperty("connection", "Keep-Alive");
		// con.setConnectTimeout(5000);
		// con.setRequestProperty("Content-type", "application/json"
		// + ";charset=UTF-8");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		con.setDoOutput(true);
		con.setDoInput(true);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String lines;
		StringBuffer sb = new StringBuffer("");
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes(), "utf-8");
			sb.append(lines);
		}

		data = sb.toString();
		reader.close();
		con.disconnect();
		JSONObject ret = null;
		if (data != null && !data.equals("")) {
			ret = new JSONObject(data);
		}
		return ret;
	}

	public static JSONObject doPost(String url, JSONObject postData)
			throws JSONException, IOException {
		String data = "";
		URL dataUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Proxy-Connection", "Keep-Alive");
		con.setRequestProperty("accept", "*/*");
		con.setRequestProperty("connection", "Keep-Alive");
		// con.setRequestProperty("Content-type", "application/json"
		// + ";charset=UTF-8");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		con.setDoOutput(true);
		con.setDoInput(true);
		OutputStream os = con.getOutputStream();
		os.write(postData.toString().getBytes("UTF-8"));
		os.flush();
		os.close();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String lines;
		StringBuffer sb = new StringBuffer("");
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes(), "utf-8");
			sb.append(lines);
		}

		data = sb.toString();
		reader.close();
		con.disconnect();
		JSONObject ret = null;
		if (data != null && !data.equals("")) {
			ret = new JSONObject(data);
		}
		return ret;
	}

	public static JSONObject doPostParameters(String url,
			Map<String, String> parameters) throws JSONException, IOException {
		String data = "";
		URL dataUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Proxy-Connection", "Keep-Alive");
		con.setRequestProperty("accept", "*/*");
		con.setRequestProperty("connection", "Keep-Alive");
		// con.setRequestProperty("Content-type", "application/json"
		// + ";charset=UTF-8");
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		con.setDoOutput(true);
		con.setDoInput(true);
		OutputStream os = con.getOutputStream();
		os.write(generatorParamString(parameters).getBytes());
		os.flush();
		os.close();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String lines;
		StringBuffer sb = new StringBuffer("");
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes(), "utf-8");
			sb.append(lines);
		}

		data = sb.toString();
		reader.close();
		con.disconnect();
		JSONObject ret = null;
		if (data != null && !data.equals("")) {
			ret = new JSONObject(data);
		}
		return ret;
	}

	@SuppressWarnings("rawtypes")
	public static void doPost(String url, Builder message) throws JSONException {
		try {
			URL dataUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) dataUrl
					.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Proxy-Connection", "Keep-Alive");
			con.setRequestProperty("accept", "*/*");
			con.setRequestProperty("connection", "Keep-Alive");
			// con.setRequestProperty("Content-type", "application/json"
			// + ";charset=UTF-8");
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStream os = con.getOutputStream();
			os.write(message.build().toByteArray());
			os.flush();
			os.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			reader.close();
			con.disconnect();
		} catch (Exception ex) {
			System.err.println("HTTP异常");
			ex.printStackTrace();
		}
	}

	public static void doPost(String url, Object data) throws JSONException {
		try {
			URL dataUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) dataUrl
					.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Proxy-Connection", "Keep-Alive");
			con.setRequestProperty("accept", "*/*");
			con.setRequestProperty("connection", "Keep-Alive");
			// con.setRequestProperty("Content-type", "application/json"
			// + ";charset=UTF-8");
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStream os = con.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(data);
			oos.flush();
			oos.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			reader.close();
			con.disconnect();
		} catch (Exception ex) {
			System.err.println("HTTP异常");
			ex.printStackTrace();
		}
	}

	/**
	 * 获取IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (ip != null)
			ip = getFirstIp(ip);

		return ip;
	}

	private static String getFirstIp(String ipString) {
		String ip = null;
		String[] ipList = ipString.split(",");
		if (ipList != null && ipList.length > 1) {
			ip = ipList[0];
		} else {
			ip = ipString;
		}
		return ip;
	}

	/**
	 * 将JSON写入到response
	 * 
	 * @param response
	 * @param object
	 */
	public static void writeJson(HttpServletResponse response, JSONObject object) {
		try {
			response.setCharacterEncoding("UTF-8");
			if (object != null) {
				response.getWriter().write(object.toString());
			}
			response.flushBuffer();
			response.getWriter().close();
		} catch (Exception e) {
		}
	}

	public static void write(HttpServletResponse response, String str) {
		try {
			response.setCharacterEncoding("UTF-8");
			if (str != null && !str.equals("")) {
				response.getWriter().write(str);
			}
			response.flushBuffer();
			response.getWriter().close();
		} catch (Exception e) {
		}
	}

	public static void write(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.flushBuffer();
			response.getWriter().close();
		} catch (Exception e) {
		}
	}

	public static String doPost(String reqUrl, Map<String, String> parameters)
			throws RuntimeException {
		HttpURLConnection urlConn = null;
		try {
			urlConn = sendPost(reqUrl, parameters);
			String responseContent = getContent(urlConn);

			return responseContent.trim();
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}

	/**
	 * 
	 * @param link
	 * @param charset
	 * @return
	 */
	public static String doGet(String link, String charset) {
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Linux JDK1.6 ");
			BufferedInputStream in = new BufferedInputStream(
					conn.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int i = 0; (i = in.read(buf)) > 0;) {
				out.write(buf, 0, i);
			}
			out.flush();
			String s = new String(out.toByteArray(), charset);
			return s;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * UTF-8编码GET请求
	 * 
	 * @param link
	 * @return
	 */
	public static String doGet(String link) {
		return doGet(link, "UTF-8");
	}

	private static String getContent(HttpURLConnection urlConn) {
		try {
			String responseContent = null;
			InputStream in = urlConn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
			return responseContent;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static String doPost(String url, String postData) {
		return doPost(url, postData, "text/plain");
	}

	public static String doPost(String url, String postData, String contentType) {
		StringBuffer data = new StringBuffer();
		try {
			URL dataUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) dataUrl
					.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Proxy-Connection", "Keep-Alive");
			// con.setRequestProperty("Accept-Charset",
			// "GB2312,utf-8;q=0.7,*;q=0.7");
			con.setRequestProperty("accept", "*/*");
			con.setRequestProperty("connection", "Keep-Alive");
			con.setRequestProperty("Content-type", contentType
					+ ";charset=UTF-8");
			/*
			 * con.setRequestProperty("user-agent",
			 * "Opera/9.80 (Windows NT 6.1; U; zh-cn) Presto/2.10.229 Version/11.61"
			 * );
			 */
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStream os = con.getOutputStream();
			OutputStreamWriter dos = new OutputStreamWriter(os);
			dos.write(postData);
			dos.flush();
			dos.close();
			InputStream is = con.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				data.append(line);
			}
			con.disconnect();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data.toString();
	}

	/**
	 * 内部方法，发送post数据
	 * 
	 * @param reqUrl
	 * @param parameters
	 * @return
	 */
	private static HttpURLConnection sendPost(String reqUrl,
			Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		try {
			String params = generatorParamString(parameters);
			URL url = new URL(reqUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			// urlConn
			// .setRequestProperty(
			// "User-Agent",
			// "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
			urlConn.setConnectTimeout(30 * 1000);
			urlConn.setReadTimeout(30 * 1000);
			urlConn.setDoOutput(true);
			byte[] b = params.getBytes();
			// System.err.println(params);
			urlConn.getOutputStream().write(b, 0, b.length);
			urlConn.getOutputStream().flush();
			urlConn.getOutputStream().close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return urlConn;
	}

	/**
	 * 把map转换为键值对的字符串用于url请求
	 * 
	 * @param parameters
	 * @return
	 */
	public static String generatorParamString(Map<String, String> parameters) {
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
			for (Iterator<String> iter = parameters.keySet().iterator(); iter
					.hasNext();) {
				String name = iter.next();
				String value = parameters.get(name);
				params.append(name + "=");
				try {
					params.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (Exception e) {
					String message = String.format("'%s'='%s'", name, value);
					throw new RuntimeException(message, e);
				}
				if (iter.hasNext()) {
					params.append("&");
				}
			}
		}
		return params.toString();
	}

}
