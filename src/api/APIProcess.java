package api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIProcess {

	private APICall api = new APICall();
	private final SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public JSONArray bucketList(String targetURL, String sessionKey) {

		JSONArray bucketList = new JSONArray();
		String response = api.getBuckets(targetURL, sessionKey);

		try {
			JSONObject jsonResponse = new JSONObject(response);
			JSONArray jsonArray = jsonResponse.getJSONArray("buckets");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject bucket = new JSONObject();
				JSONObject bucketJSON = jsonArray.getJSONObject(i);

				String bucketName = bucketJSON.getString("name");

				JSONArray bucketArray = bucketJSON.getJSONArray("users");

				for (int x = 0; x < 1; x++) {
					JSONObject userJSON = bucketArray.getJSONObject(x);
					int bucketID = userJSON.getInt("bucketId");
					bucket.put("bucketID", bucketID);
				}

				bucket.put("bucketName", bucketName);
				bucketList.put(bucket);
			}

			return bucketList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		// return null;
	}

	public JSONArray nodeLicenseList(String targetURL, String sessionKey, int bucketID) {

		JSONArray nodeLicenseList = new JSONArray();

		String response = api.getNodeLicense(targetURL, sessionKey, bucketID);

		try {
			JSONObject licenseResponse = new JSONObject(response);

			JSONArray licenseList = licenseResponse.getJSONArray("node-licenses");

			for (int x = 0; x < licenseList.length(); x++) {

				JSONObject licenseJSON = licenseList.getJSONObject(x);

				String licenseStatus = licenseJSON.getString("status");

				if (licenseStatus.equals("UNUSED")) {
					JSONObject license = new JSONObject();
					license.put("licenseNumber", licenseJSON.getString("licenseNumber"));
					nodeLicenseList.put(license);
				}
			}

			return nodeLicenseList;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public JSONObject getNodeLicenseDetails(String targetURL, String sessionKey, int bucketID, String licenseNumber) {

		String response = api.getNodeLicense(targetURL, sessionKey, bucketID);

		try {
			JSONObject licenseResponse = new JSONObject(response);

			JSONArray licenseList = licenseResponse.getJSONArray("node-licenses");

			for (int x = 0; x < licenseList.length(); x++) {

				JSONObject licenseJSON = licenseList.getJSONObject(x);

				String licenseString = licenseJSON.getString("licenseNumber");
				char[] charArray = licenseString.toCharArray();
				String licenseAdd = "";
				for (int i = 0; i < charArray.length; i++) {
					if (i % 5 == 0 && i != 0) {

						licenseAdd += " - " + charArray[i];

					} else {
						licenseAdd += charArray[i];
					}
				}

				if (licenseAdd.equals(licenseNumber)) {
					JSONObject license = new JSONObject();
					license.put("cloudStorage", licenseJSON.getInt("cloudStorageGb"));
					license.put("duration", licenseJSON.getInt("durationMonths"));
					license.put("maxVCA", licenseJSON.getInt("maxVcaCount"));
					license.put("features", licenseJSON.get("featureNameList"));
					license.put("created", licenseJSON.getLong("created"));
					license.put("bucketName", licenseJSON.getString("bucketName"));
					return license;

				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public JSONArray inventoryList(String targetURL, String sessionKey) {
		JSONArray inventoryList = new JSONArray();

		String response = api.getInventoryList(targetURL, sessionKey);

		try {

			JSONObject inventoryResponse = new JSONObject(response);

			JSONArray inventory = inventoryResponse.getJSONArray("inventory-list");

			for (int x = 0; x < inventory.length(); x++) {

				JSONObject item = inventory.getJSONObject(x);

				boolean itemStatus = item.getBoolean("activated");

				if (itemStatus == false) {
					JSONObject itemAdd = new JSONObject();
					itemAdd.put("id", item.get("inventoryId"));
					itemAdd.put("registrationNumber", item.get("registrationNumber"));
					itemAdd.put("macAddress", item.get("macAddress"));

					inventoryList.put(itemAdd);
				}
			}

			return inventoryList;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void featuresList(String targetURL, String sessionKey, int bucketID) {
		Data.featureList = new HashMap<String, JSONArray>();

		String response = api.getAssignableFeatures(targetURL, sessionKey, bucketID);

		try {
			JSONObject featureResponse = new JSONObject(response);
			JSONArray features = featureResponse.getJSONArray("features");
			ArrayList<String> types = new ArrayList<String>();
			for (int x = 0; x < features.length(); x++) {
				JSONObject feature = features.getJSONObject(x);
				String type = feature.getString("type");
				if (Data.featureList.containsKey(type)) {
					JSONArray featureArray = Data.featureList.get(type);
					featureArray.put(feature);
				} else {
					JSONArray featureArray = new JSONArray();
					featureArray.put(feature);
					Data.featureList.put(type, featureArray);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		String[] arrayFeatures = {};
		Data.features = new HashMap<String, String[]>();
		try{
			for (String key: Data.featureList.keySet()){
				ArrayList<String> stringFeatures = new ArrayList<String>();
				String newKey = Data.fieldNames.get(key);
				JSONArray features = Data.featureList.get(key);
				for(int i = 0; i < features.length(); i ++){
					JSONObject feature = features.getJSONObject(i);
					stringFeatures.add(Data.fieldNames.get(feature.get("name")));
				}
				Data.features.put(newKey, stringFeatures.toArray(arrayFeatures));
			}
		}catch(JSONException e){
			e.printStackTrace();
		}

	}

	public JSONArray getUnuseAccessKey(String targetURL, String sessionKey) {
		String response = api.getAccessKeyList(targetURL, sessionKey);
		JSONArray accessKeyList = new JSONArray();

		try {
			JSONObject jsonResponse = new JSONObject(response);
			JSONArray jsonArray = jsonResponse.getJSONArray("key-list");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject accessKey = new JSONObject();
				JSONObject accessJSON = jsonArray.getJSONObject(i);

				int maxUseCount = accessJSON.getInt("maxUseCount");
				int currentUseCount = accessJSON.getInt("currentUseCount");
				String key = accessJSON.getString("key");
				Date expiry = new Date(accessJSON.getLong("ttl"));

				accessKey.put("expiryDate", simpleDate.format(expiry));
				accessKey.put("key", key);

				if (accessJSON.getBoolean("isValid")) {
					if (maxUseCount == -1) {
						accessKey.put("remainingUses", "Unlimited");
						accessKeyList.put(accessKey);
					} else if (currentUseCount < maxUseCount) {
						accessKey.put("remainingUses", Integer.toString(maxUseCount - currentUseCount));
						accessKeyList.put(accessKey);
					}
				}
			}

			return accessKeyList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
