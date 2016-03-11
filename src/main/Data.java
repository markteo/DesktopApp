package main;

import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.json.JSONArray;

import qrcode.JavaQR;
import ui.UIAccessKeySelect;
import ui.UIBucketSelect;
import ui.UIGenerateKey;
import ui.UIInventorySelect;
import ui.UILicenseDetail;
import ui.UILicenseSelect;
import ui.UILogin;

public class Data {
	public static String bucket;
	public static String targetURL;
	public static String sessionKey;
	public static boolean accessRights = false;
	public static String URL;
	public static final String protocol = "http://";
	public static String registrationNumber;
	public static int bucketID;
	public static String licenseNumber;
	public static UIInventorySelect uiInventorySelect;
	public static UIBucketSelect uiBucketSelect;
	public static UILicenseSelect uiLicenseSelect;
	public static HashMap<String, JSONArray> featureList;
	public static UIGenerateKey uiGenerateKey;
	public static UIAccessKeySelect uiAccessKeySelect;
	public static UILicenseDetail uiLicenseDetail;
	public static String accessKey;
	public static JavaQR qrGenerator;
	public static UILogin uiLogin;
	public static JDialog loadingScreen;
	public static JFrame loadingFrame;
}
