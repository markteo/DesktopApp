package main;
import APICalls.APICall;

public class DesktopAppMain {
	private static String apiURL = "";
	private static final String bucket = "/api/superadmin/";
	private static String api = "";

	public static void main(String args[]) {

		String apiURL = Helper.readString("Enter api url > ");
		
		String targetURL = apiURL + bucket;
		APICall apicall = new APICall();
		//Login to bucket
		String response = apicall.loginBucket(targetURL);
		
	}
	
}
