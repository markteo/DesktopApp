package api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;

import main.Helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import file.HttpDownloadUtility;

public class APICall {
	
	public static final String enc = "UTF-8";

	public String loginBucket(String targetURL, String username, String password) {
		String api = "login";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			
			urlParameters = "user-name=" + URLEncoder.encode(username, enc)
					+ "&password=" + URLEncoder.encode(password, enc);

			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 

		return null;

	}
	public String getUserFeatures(String targetURL, String sessionKey){
		String api = "getuserfeatures";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key="
					+ URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public String getInventoryList(String targetURL, String sessionKey) {

		String api = "getinventorylist";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key="
					+ URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public String updateInventoryList(String targetURL, JSONObject inventoryDetails, String sessionKey){
		
		String api = "updateinventory";
		targetURL = targetURL + api;
		String urlParameters;
		
		try {
			urlParameters = "session-key="
					+ URLEncoder.encode(sessionKey, enc) + "&inventory-id=" 
					+ URLEncoder.encode(inventoryDetails.getString("inventoryID"), enc)
					+ "&registration-name=" + URLEncoder.encode(inventoryDetails.getString("registrationName"), enc)
					+ "&model-name=" + URLEncoder.encode(inventoryDetails.getString("modelName"), enc)
					+ "&mac-address=" + URLEncoder.encode(inventoryDetails.getString("macAddress"), enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getBucketDevices(String targetURL, String sessionKey){
		
		String api = "getbucketdevices";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key="
					+ URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getBuckets(String targetURL, String sessionKey){
		
		String api = "getbuckets";
		targetURL = targetURL + api;
		String urlParameters;
		try{
			urlParameters = "session-key=" 
					+ URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			return response;
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getNodeLicense(String targetURL, String sessionKey, int bucketID){
		
		String api = "getnodelicenses";
		targetURL = targetURL + api;
		String urlParameters;
		try{
			urlParameters = "session-key=" 
					+ URLEncoder.encode(sessionKey, enc) + "&bucket-id=" 
					+ URLEncoder.encode(Integer.toString(bucketID), enc);
			
			String response = executePost(targetURL, urlParameters);
			return response;
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return null;
	}

	public String addNodeLicense(String targetURL, String sessionKey, int bucketID, String features){
		String api = "addnodelicense";
		targetURL = targetURL + api;
		
		String urlParameters;
		
		String durationMonths = Helper.readString("Enter duration (months) > ");
		String cloudStorage = Helper.readString("Enter cloud storage space (GB) > ");
		String maxVCA = Helper.readString("Enter max VCA count > ");
		
		try{
			urlParameters = "session-key=" 
					+ URLEncoder.encode(sessionKey, enc) + "&bucket-id=" 
					+ URLEncoder.encode(Integer.toString(bucketID), enc) + "&duration-months="
					+ URLEncoder.encode(durationMonths, enc) + "&cloud-storage-gb="
					+ URLEncoder.encode(cloudStorage, enc) + "&max-vca-count="
					+ URLEncoder.encode(maxVCA, enc) + "&features="
					+ URLEncoder.encode(features, enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
					
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String updateNodeLicense(String targetURL, String sessionKey, String licenseNumber, String features){
		String api = "updatenodelicense";
		targetURL = targetURL + api;
		
		String urlParameters;
		
		String durationMonths = Helper.readString("Enter duration (months) > ");
		String cloudStorage = Helper.readString("Enter cloud storage space (GB) > ");
		String maxVCA = Helper.readString("Enter max VCA count > ");
		
		try{
			urlParameters = "session-key=" 
					+ URLEncoder.encode(sessionKey, enc) + "&license-number=" 
					+ URLEncoder.encode(licenseNumber, enc) + "&duration-months="
					+ URLEncoder.encode(durationMonths, enc) + "&cloud-storage-gb="
					+ URLEncoder.encode(cloudStorage, enc) + "&max-vca-count="
					+ URLEncoder.encode(maxVCA, enc) + "&features="
					+ URLEncoder.encode(features, enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
					
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String getAssignableFeatures(String targetURL, String sessionKey, int bucketID){
		String api = "getassignablenodefeatures";
		targetURL = targetURL + api;
		String urlParameters;

		try{
			urlParameters = "session-key=" 
					+ URLEncoder.encode(sessionKey, enc) + "&bucket-id=" 
					+ URLEncoder.encode(Integer.toString(bucketID), enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String getCSVSample(String targetURL, String sessionKey, String fileLocation) throws IOException{
		String api = "content/csvSample";
		String[] splitURL = targetURL.split("\\/api");
		targetURL = splitURL[0] + splitURL[1] + api;
		
		
		
		try{
			
			System.out.println(targetURL);
			String response = HttpDownloadUtility.downloadFile(targetURL, "./", sessionKey);
			System.out.println(response);
			return response;
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public String uploadInventory(String targetURL, String sessionKey){
		String api = "/uplaodinventory";
		
		
		return null;
	}
	public String generateAccessKey(String targetURL, String sessionKey, int userID){
		
		String api = "generateaccesskey";
		targetURL = targetURL + api;
		String urlParameters;
		
		String ttl = Helper.readString("Enter ttl > ");
		String maxUse = Helper.readString("Enter max use count > ");
		
		try{
			urlParameters = "session-key=" 
					+ URLEncoder.encode(sessionKey, enc) + "&bucket-id=" 
					+ URLEncoder.encode(Integer.toString(userID), enc) + "&ttl="
					+ URLEncoder.encode(ttl, enc) + "&max-use-count="
					+ URLEncoder.encode(maxUse, enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String executePost(String targetURL, String urlParameters) {
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

		}catch (SocketException se){
			se.printStackTrace();
			System.out.println("Server is down.");
			return null;
		}catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
