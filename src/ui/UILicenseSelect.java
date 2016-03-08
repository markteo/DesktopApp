package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APICall;
import api.APIProcess;
import customColor.CustomColor;

public class UILicenseSelect {
	public static APICall api = new APICall();
	private JFrame licenseFrame;
	private JList listLicense;
	public void runLicenseSelect() {
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();

		DefaultListModel<String> model;
		model = getLicenseData(new DefaultListModel<String>());
		// fetch list from server
		
//		JSONArray LicenseList = new APIProcess().LicenseList(Data.targetURL, Data.sessionKey);
//
//		try {
//			System.out.println(LicenseList.length());
//			for (int i = 0; i < LicenseList.length(); i++){
//				JSONObject LicenseItem = LicenseList.getJSONObject(i);
//				model.addElement(LicenseItem.get("id") + " , " + LicenseItem.get("registrationNumber") + " , " + LicenseItem.get("macAddress"));
//			}
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		// start of ui
		licenseFrame = new JFrame("License");
		licenseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		licenseFrame.setLayout(new BorderLayout());
		licenseFrame.setVisible(true);

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("The License List shows the currently unused licenses");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlLicenseList = p.createPanel(Layouts.flow);
		JLabel lblLicenseList = l.createLabel("License List :");
		listLicense = new JList(model);
		listLicense.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollLicense = new JScrollPane(listLicense);
		scrollLicense.setPreferredSize(new Dimension(300, 150));
		Component[] LicenseListComponents = { lblLicenseList, scrollLicense };
		p.addComponentsToPanel(pnlLicenseList, LicenseListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnAddElements = b.createButton("Add Item");

		// Button events
		btnAddElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// add License code here using new swing UI
				licenseFrame.setVisible(false);
				UILicenseAdd uiLicenseAdd = new UILicenseAdd();
				uiLicenseAdd.runLicenseAdd();
			}
		});

		JButton btnSelectElements = b.createButton("Next");
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something with selected License
				String itemSelected = listLicense.getModel().getElementAt(listLicense.getSelectedIndex()).toString();
				
				Data.licenseNumber = itemSelected;
				licenseFrame.setVisible(false);
				Data.uiAccessKeySelect = new UIAccessKeySelect();
			}
		});
		
		JButton btnViewLicense = b.createButton("View Details");
		btnViewLicense.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String itemSelected = listLicense.getModel().getElementAt(listLicense.getSelectedIndex()).toString();
				Data.uiLicenseDetail = new UILicenseDetail();
				licenseFrame.setVisible(false);
			}
		});
		
		pnlButtons.add(btnViewLicense);
		pnlButtons.add(btnAddElements);
		pnlButtons.add(btnSelectElements);

		licenseFrame.add(pnlInstruction, BorderLayout.NORTH);
		licenseFrame.add(pnlLicenseList, BorderLayout.CENTER);
		licenseFrame.add(pnlButtons, BorderLayout.SOUTH);
		licenseFrame.pack();
	}
	private static DefaultListModel<String> getLicenseData(DefaultListModel<String> model){
		
		JSONArray licenseList = new APIProcess().nodeLicenseList(Data.targetURL, Data.sessionKey, Data.bucketID);

		try {
			for (int i = 0; i < licenseList.length(); i++) {
				JSONObject license = licenseList.getJSONObject(i);
				
				String licenseNumber = license.getString("licenseNumber");
				char[] charArray = licenseNumber.toCharArray();
				String licenseAdd = "";
				for(int x =0; x < charArray.length; x ++ ){
					if(x%5 == 0 && x != 0){
					
						licenseAdd += " - " + charArray[x];
						
					}else{
						licenseAdd += charArray[x];
					}
				}
				model.addElement(licenseAdd);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return model;
	}
	public void setFrameVisible(){
		licenseFrame.setVisible(true);
	}
	
	public void updateInventoryList(){
		DefaultListModel<String> licenseList = getLicenseData(new DefaultListModel<String>());
		listLicense.setModel(licenseList);
	}
}
