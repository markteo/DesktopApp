package main;

import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.json.JSONArray;

import ui.frame.KAIQRFrame;

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
	public static HashMap<String, JSONArray> featureList;
	public static String accessKey;
	public static JDialog loadingScreen;
	public static JFrame loadingFrame;
	public static KAIQRFrame mainFrame;
	public static JFrame loginFrame;
	public static HashMap<String, String> fieldNames;
}
