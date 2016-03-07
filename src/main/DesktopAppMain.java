package main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.UILogin;
import api.APICall;

public class DesktopAppMain {
	

	public static void main(String args[]) {
		
		UILogin.runLogin();
		
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
