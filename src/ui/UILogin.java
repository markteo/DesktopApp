package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.Data;
import main.DesktopAppMain;

import org.jdesktop.xswingx.PromptSupport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Layouts;
import ui.components.Panel;
import api.APICall;
import customColor.CustomColor;


public class UILogin {
	
	public static APICall api = new APICall();
	
	public static void runLogin(String apiURL){
		JFrame loginFrame = new JFrame("Login");
		loginFrame.setLayout(new BorderLayout());
		loginFrame.setPreferredSize(new Dimension(400, 400));
		loginFrame.setResizable(false);
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		File pathToImage = new File("image/KaiSquare_logoFA.jpg");
		Image myPicture = null;
		try {
			myPicture = ImageIO.read(pathToImage);
			myPicture = myPicture.getScaledInstance(250, 150, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
	
		Panel p = new Panel();
		JPanel loginPanel = p.createPanel(Layouts.grid,3,3);
		loginPanel.setBorder(new EmptyBorder(25,25,0,25));
		JLabel lblUser = new JLabel("Username:");
		JLabel lblPassword = new JLabel("Password:");
		lblUser.setForeground(CustomColor.Grey.returnColor());
		lblPassword.setForeground(CustomColor.Grey.returnColor());
		JTextField tfUser = new JTextField();
		PromptSupport.setPrompt("Username", tfUser);
		JPasswordField pfPassword = new JPasswordField();
		PromptSupport.setPrompt("Password", pfPassword);
		
		
		
		JPanel buttonPanel = p.createPanel(Layouts.flow);
		buttonPanel.setBorder(new EmptyBorder(0, 0, 25, 0));
		JButton btnLogin = Button.createButton("Login");
		JButton btnExit = Button.createButton("Exit");
		btnLogin.setPreferredSize(new Dimension(150,50));
		btnExit.setPreferredSize(new Dimension(150,50));
		Component[] arrayBtn = {btnLogin,btnExit};
		p.addComponentsToPanel(buttonPanel,arrayBtn);
		
		btnLogin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = tfUser.getText();
				String password = String.valueOf(pfPassword.getPassword());
				String response = api.loginBucket(apiURL, username, password);	
				try {
					
					if(DesktopAppMain.checkResult(response)){
						JSONObject responseJSON = new JSONObject(response);
						Data.sessionKey = responseJSON.get("session-key").toString();
						response = api.getUserFeatures(apiURL, Data.sessionKey);
						Data.targetURL = apiURL;
						if(checkFeatures(response)){
							UIInventorySelect inventory = new UIInventorySelect();
							loginFrame.setVisible(false);
							inventory.runInventorySelect();
							
						}else{
							System.exit(0);
						}
					}else{
						System.exit(0);
					}
					
					

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		
		btnExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		Component[] arrayComponents = {lblUser,tfUser,lblPassword,pfPassword};
		picLabel.setBounds(50, 50, 50, 50);
		p.addComponentsToPanel(loginPanel, arrayComponents);
		loginFrame.add(picLabel,BorderLayout.NORTH);
		loginFrame.add(loginPanel,BorderLayout.CENTER);
		loginFrame.add(buttonPanel,BorderLayout.SOUTH);
		loginFrame.pack();
		
	}

	
	public static boolean checkFeatures(String response){
		boolean result = false;
		HashMap<String, String> featureList = new HashMap<String, String>();
		JSONObject responseObject;
		try {
			responseObject = new JSONObject(response);
			if(responseObject.get("result").equals("ok")){
				JSONArray features = responseObject.getJSONArray("features");
				
				for(int i = 0; i < features.length(); i ++){
					JSONObject feature = features.getJSONObject(i);
					
					String featureName = feature.getString("name");
					
					if(featureName.equals("bucket-management") || featureName.equals("inventory-management") || 
							featureName.equals("access-key-management")){
						System.out.println("Feature: " + feature.getString("name"));
						featureList.put(featureName, feature.getString("name"));
						
					}
				}

				if(featureList.size() == 3){
					return true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
