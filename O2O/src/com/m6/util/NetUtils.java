package com.m6.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;


public class NetUtils {

	public static final String TAG = "NetUtils";
	
	private static final String BOUNDARY = "---------------HttpPostO2O";
	
	public static final String POST = "POST";
	
	public static final String GET = "GET";
	
	private static final int HTTP_READ_TIMEOUT = 90000;
	
	private static final int HTTP_CONNECT_TIMEOUT = 90000;

	/**
	 * Check network connection status
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	/**
	 * Check wifi connection status
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * Check mobile network connection status
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * Given a string representation of a URL, sets up a connection and gets an
	 * input stream.
	 * 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	public static InputStream downloadUrlToStream(String urlString) {
		HttpURLConnection conn = null;
		InputStream stream = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();

			stream = conn.getInputStream();
		} catch (final IOException e) {
			System.out.println("Error in downloadBitmap - " + e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return stream;
	}

	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves the web page
	 * content as a InputStream, which it returns as a string.
	 * 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	public static String downloadUrlToString(String urlString) {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 500;
		try {
			is = downloadUrlToStream(urlString);
			// Convert the InputStream into a string
			return readStream(is, len);
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} catch (final IOException e) {
			System.out.println("Error in downloadBitmap - " + e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (final IOException e) {
			}
		}
		return null;
	}

	/**
	 * Reads an InputStream and converts it to a String.
	 * 
	 * @param in
	 *            The inputStream to read
	 * @param len
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private static String readStream(InputStream in, int len)
			throws IOException, UnsupportedEncodingException {
		if (in != null) {
			Reader reader = new InputStreamReader(in, "UTF-8");
			char[] buffer = new char[len];
			reader.read(buffer);
			return new String(buffer);
		}
		return null;
	}
	
	private static String readStream(InputStream in) throws IOException {
		if(in != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuilder stringBuilder = new StringBuilder();
			String data;
			while((data = reader.readLine()) != null) {
				stringBuilder.append(data);
			}
			return stringBuilder.toString();
		}
		return null;
	}
	
	private static void writeStream(OutputStream out, List<BasicNameValuePair> params) throws IOException {
		StringBuilder sbBuilder = new StringBuilder();
		for(NameValuePair param : params) {
			sbBuilder.append(param.getName());
			sbBuilder.append("=");
			sbBuilder.append(param.getValue());
			sbBuilder.append("&");
		}
		sbBuilder.deleteCharAt(sbBuilder.length() - 1);
		out.write(sbBuilder.toString().getBytes());
	}
	
	private static HttpURLConnection getHttpURLConnection(String urlString, String method) {
		if (TextUtils.isEmpty(urlString)) {
			return null;
		}
		
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(method);
			conn.setRequestProperty("x-client-identifier", "Mobile");
			System.setProperty("http.keepAlive", "false"); // maybe android bug
			conn.setUseCaches(false);
			conn.setChunkedStreamingMode(0);
			conn.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			conn.setReadTimeout(HTTP_READ_TIMEOUT);
			return conn;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String httpPost(String urlString, String data) {
		String result = null;
		HttpURLConnection conn = null;
		try {
			conn = getHttpURLConnection(urlString, POST);
			conn.connect();
			OutputStream out = new BufferedOutputStream(
					conn.getOutputStream());
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("data", URLEncoder.encode(data, "UTF-8")));
			writeStream(out, params);
			out.flush();
			out.close();
			InputStream in = new BufferedInputStream(
					conn.getInputStream());
			result = readStream(in);
			
			for(BasicNameValuePair pair : params) {
				System.out.println(pair.getName() + ":" + pair.getValue() + "\n");
			}
			System.out.println("result : " + result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}
	
	/**
	 * 带cookie的POST请求
	 * 
	 * @param context
	 * @param urlString
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static String httpPost(Context context, String urlString, List<BasicNameValuePair> params) {
		String result = null;
		HttpURLConnection conn = null;
		try {
			conn = getHttpURLConnection(urlString, POST);
			conn.connect();
			OutputStream out = new BufferedOutputStream(
					conn.getOutputStream());
			writeStream(out, params);
			out.flush();
			out.close();
			InputStream in = new BufferedInputStream(
					conn.getInputStream());
			result = readStream(in);
			
			for(BasicNameValuePair pair : params) {
				System.out.println(pair.getName() + ":" + pair.getValue() + "\n");
			}
			System.out.println("result : " + result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}
	
}
