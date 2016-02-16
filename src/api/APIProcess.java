package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIProcess {

	private APICall api = new APICall();
	
	public JSONArray bucketList (String targetURL, String sessionKey){
		
		JSONArray bucketList = new JSONArray();
		
		String response = api.getBuckets(targetURL, sessionKey);
		
		try {
			JSONObject jsonResponse = new JSONObject(response);
			JSONArray jsonArray = jsonResponse.getJSONArray("buckets");
			for(int i = 0; i < bucketList.length(); i ++){
				
				JSONObject bucketJSON = jsonArray.getJSONObject(i);
				int bucketID = Integer.parseInt(bucketJSON.get("id").toString());
				String bucketName = bucketJSON.getString("name");
				
				JSONObject bucket = new JSONObject();
				bucket.put("bucketID", bucketID);
				bucket.put("bucketName", bucketName);
				
				bucketList.put(bucket);
			}
			
			return bucketList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public JSONArray nodeLicenseList(String targetURL, String sessionKey, int bucketID){
		
		JSONArray nodeLicenseList = new JSONArray();
		
		String response = api.getNodeLicense(targetURL, sessionKey, bucketID);
		
		try {
			JSONObject licenseResponse = new JSONObject(response);
			
			JSONArray licenseList = licenseResponse.getJSONArray("node-licenses");
			
			for(int x = 0; x < licenseList.length(); x ++){
				
				JSONObject licenseJSON = licenseList.getJSONObject(x);
				
				String licenseStatus = licenseJSON.getString("status");
				
				if(licenseStatus.equals("UNUSED")){
					System.out.println("LicenseNumber: " + licenseJSON.getString("licenseNumber"));
					
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	
	
}
