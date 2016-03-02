package main;

import java.util.HashMap;

import org.json.JSONArray;

import ui.UIBucketSelect;
import ui.UIGenerateKey;
import ui.UIInventorySelect;
import ui.UILicenseSelect;

public class Data {
	public static String bucket;
	public static String targetURL;
	public static String sessionKey;
	public static boolean accessRights = false;
	public static String URL;
	public static String registrationNumber;
	public static int bucketID;
	public static String licenseNumber;
	public static UIInventorySelect uiInventorySelect;
	public static UIBucketSelect uiBucketSelect;
	public static UILicenseSelect uiLicenseSelect;
	public static HashMap<String, JSONArray> featureList;
	public static UIGenerateKey uiGenerateKey;
}
