package main;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import APICalls.APICall;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DesktopAppMain {
	private static String apiURL = "";
	private static final String bucket = "/api/superadmin/";
	private static String api = "";
	private static String sessionKey;

	public static void main(String args[]) {

		String apiURL = Helper.readString("Enter api url > ");
		
		String targetURL = apiURL + bucket;
		APICall apicall = new APICall();
		//Login to bucket
		String response = apicall.loginBucket(targetURL);
		try {
			JSONObject responseJSON = new JSONObject(response);
			sessionKey = responseJSON.get("session-key").toString();
			
			response = apicall.getBucketDevices(targetURL, sessionKey);
			response = apicall.getInventoryList(targetURL, sessionKey);
			response = apicall.getBuckets(targetURL, sessionKey);
			
			JSONObject bucketResponse = new JSONObject(response);
			JSONArray bucketList = bucketResponse.getJSONArray("buckets");
			for(int i = 0; i < bucketList.length(); i ++){
				JSONObject bucketJSON = bucketList.getJSONObject(i);
				int bucketID = Integer.parseInt(bucketJSON.get("id").toString());
				String bucketName = bucketJSON.getString("name");
				System.out.println();
				System.out.println("Bucket ID: " + bucketID);
				System.out.println("Bucket Name: " + bucketName);
				response = apicall.getAssignableFeatures(targetURL, sessionKey, bucketID);
				
				response = apicall.getNodeLicense(targetURL, sessionKey, bucketID);
				JSONObject licenseResponse = new JSONObject(response);
				JSONArray licenseList = licenseResponse.getJSONArray("node-licenses");
				for(int x = 0; x < licenseList.length(); x ++){
					JSONObject licenseJSON = licenseList.getJSONObject(x);
					
					String licenseStatus = licenseJSON.getString("status");
					
					if(licenseStatus.equals("UNUSED")){
						System.out.println("LicenseNumber: " + licenseJSON.getString("licenseNumber"));
						
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
