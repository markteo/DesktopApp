package main;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.json.JSONException;
import org.json.JSONObject;

import ui.frame.UILogin;

public class DesktopAppMain {

	public static JFrame initFrame;

	public static void main(String args[]) {
		
		Data.fieldNames = new HashMap<String, String>();
		Data.reverseNames = new HashMap<String, String>();
        String fileName = "./files/messages.en";

        String line = null;

        try {
        	
        	URL url = DesktopAppMain.class.getResource("/resources/messages.en");
            BufferedReader bufferedReader = 
                new BufferedReader(new InputStreamReader(url.openStream()));

            while((line = bufferedReader.readLine()) != null) {
            	String[] lineSplit = line.split("\\=");
            	if(lineSplit.length == 2){
            		Data.fieldNames.put(lineSplit[0].trim(), lineSplit[1].trim());
            		Data.reverseNames.put(lineSplit[1].trim(), lineSplit[0].trim());
            	}
            	
            }   
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");
        }

		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		Data.loginFrame = new UILogin();
		Data.loginFrame.setVisible(true);
	}

	public static boolean checkResult(String response) {
		boolean result = false;

		JSONObject responseObject;
		try {
			responseObject = new JSONObject(response);
			if (responseObject.get("result").equals("ok")) {
				result = true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
