package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.APICall;
import api.APIProcess;
import customColor.CustomColor;
import main.Data;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;

public class UIInventorySelect {
	public static APICall api = new APICall();
	private JFrame inventoryFrame;
	private JList listInventory;
	public void runInventorySelect() {
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();

		DefaultListModel<String> model;
		// fetch list from server
		model = getInventoryData(new DefaultListModel<String>());

		// start of ui
		inventoryFrame = new JFrame("Inventory");
		inventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		inventoryFrame.setLayout(new BorderLayout());
		inventoryFrame.setVisible(true);

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("The Inventory List show the currently unactivated  Nodes");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlInventoryList = p.createPanel(Layouts.flow);
		JLabel lblInventoryList = l.createLabel("Inventory List : \n  (ID, Registration Number, MAC Address)");
		listInventory = new JList(model);
		listInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollInventory = new JScrollPane(listInventory);
		scrollInventory.setPreferredSize(new Dimension(300, 150));
		Component[] inventoryListComponents = { lblInventoryList, scrollInventory };
		p.addComponentsToPanel(pnlInventoryList, inventoryListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnSkipDownload = b.createButton("I have the template");
		btnSkipDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIFileUploadHTTP uploadFrame = new UIFileUploadHTTP();
				uploadFrame.runUpload();
			}
		});
		JButton btnAddElements = b.createButton("Add Item");

		// Button events
		btnAddElements.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				inventoryFrame.setVisible(false);
				UIFileDownloadHTTP dl = new UIFileDownloadHTTP();
				dl.setVisible(true);
			}
		});

		JButton btnSelectElements = b.createButton("Next");
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something with selected inventory
				System.out.println(listInventory.getModel().getElementAt(listInventory.getSelectedIndex()));
				String itemSelected = listInventory.getModel().getElementAt(listInventory.getSelectedIndex())
						.toString();
				String[] itemData = itemSelected.split("\\,");
				Data.registrationNumber = itemData[1].trim();
				inventoryFrame.setVisible(false);

				if(Data.uiBucketSelect != null){
					Data.uiBucketSelect.setFrameVisible();
				}else{
					Data.uiBucketSelect = new UIBucketSelect();
				}
				
			}
		});

		pnlButtons.add(btnAddElements);
		pnlButtons.add(btnSelectElements);

		inventoryFrame.add(pnlInstruction, BorderLayout.NORTH);
		inventoryFrame.add(pnlInventoryList, BorderLayout.CENTER);
		inventoryFrame.add(pnlButtons, BorderLayout.SOUTH);
		inventoryFrame.pack();
	}

	public DefaultListModel<String> getInventoryData(DefaultListModel<String> model) {
		JSONArray inventoryList = new APIProcess().inventoryList(Data.targetURL, Data.sessionKey);

		try {
			System.out.println(inventoryList.length());
			for (int i = 0; i < inventoryList.length(); i++) {
				JSONObject inventoryItem = inventoryList.getJSONObject(i);
				model.addElement(inventoryItem.get("id") + " , " + inventoryItem.get("registrationNumber") + " , "
						+ inventoryItem.get("macAddress"));
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return model;
	}
	
	public void setFrameVisible(){
		inventoryFrame.setVisible(true);
	}
	
	public void updateInventoryList(){
		DefaultListModel<String> inventoryList = getInventoryData(new DefaultListModel<String>());
		listInventory.setModel(inventoryList);
	}
}
