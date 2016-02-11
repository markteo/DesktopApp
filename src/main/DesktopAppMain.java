package main;
import java.util.HashMap;

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
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
