package main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.UILogin;
import api.APICall;

public class DesktopAppMain {
	private static String apiURL = "";
	private static final String bucket = "/api/superadmin/";
	public static String sessionKey;

	public static void main(String args[]) {
		//ask user for url
		//start login frame
		//if login successful check userFeatures
		//if has features, load workflow(wizard)
			//load inventory items
			//select bucket
			//select license
			//generate accesskey
			//get QR code
		

		String apiURL = Helper.readString("Enter api url > ");
		Data.URL = apiURL;
		String targetURL = apiURL + bucket;
		UILogin.runLogin(targetURL);
		
		
		
		//Login to bucket
		
//		String response = apicall.loginBucket(targetURL);
		
//		try {
//			JSONObject responseJSON = new JSONObject(response);
//			
//			if(checkResult(response)){
//				sessionKey = responseJSON.get("session-key").toString();
//				response = apicall.getInventoryList(targetURL, sessionKey);
//				response = apicall.getBuckets(targetURL, sessionKey);
//				
//			}
//			
//			JSONObject bucketResponse = new JSONObject(response);
//			JSONArray bucketList = bucketResponse.getJSONArray("buckets");
//			for(int i = 0; i < bucketList.length(); i ++){
//				JSONObject bucketJSON = bucketList.getJSONObject(i);
//				int bucketID = Integer.parseInt(bucketJSON.get("id").toString());
//				String bucketName = bucketJSON.getString("name");
//				System.out.println();
//				System.out.println("Bucket ID: " + bucketID);
//				System.out.println("Bucket Name: " + bucketName);
//				response = apicall.getAssignableFeatures(targetURL, sessionKey, bucketID);
//				
//				response = apicall.getNodeLicense(targetURL, sessionKey, bucketID);
//				JSONObject licenseResponse = new JSONObject(response);
//				JSONArray licenseList = licenseResponse.getJSONArray("node-licenses");
//				for(int x = 0; x < licenseList.length(); x ++){
//					JSONObject licenseJSON = licenseList.getJSONObject(x);
//					
//					String licenseStatus = licenseJSON.getString("status");
//					
//					if(licenseStatus.equals("UNUSED")){
//						System.out.println("LicenseNumber: " + licenseJSON.getString("licenseNumber"));
//						
//					}
//				}
//			}
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
	}
	
	public static boolean checkResult (String response){
		boolean result = false;
		
		JSONObject responseObject;
		try {
			responseObject = new JSONObject(response);
			if(responseObject.get("result").equals("ok")){
				result = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
