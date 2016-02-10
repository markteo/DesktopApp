package APICalls;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import main.Helper;

public class APICall {

	public static String loginBucket(String targetURL) {
		String api = "login";
		targetURL = targetURL + api;
		String urlParameters;
		try {

			String username = Helper.readString("Enter username > ");
			String password = Helper.readString("Enter password > ");
			urlParameters = "user-name=" + URLEncoder.encode(username, "UTF-8")
					+ "&password=" + URLEncoder.encode(password, "UTF-8");

			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static String getInventoryItem(String targetURL, String sessionKey) {

		String api = "getinventorylist";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key="
					+ URLEncoder.encode(sessionKey, "UTF-8");
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String updateInventoryList(String targetURL, String sessionKey){
		
		String api = "updateinventory";
		targetURL = targetURL + api;
		String urlParameters;
		
		try {
			urlParameters = "session-key="
					+ URLEncoder.encode(sessionKey, "UTF-8");
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static String getBucketDevices(String targetURL, String sessionKey){
		
		String api = "getuserdevices";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key="
					+ URLEncoder.encode(sessionKey, "UTF-8");
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String executePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
