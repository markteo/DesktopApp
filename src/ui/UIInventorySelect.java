package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

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
	private DefaultListModel<String> model;

	public void runInventorySelect() {
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();

		// fetch list from server
		model = getInventoryData(new DefaultListModel<String>());

		// start of ui
		inventoryFrame = new JFrame("Inventory");
		inventoryFrame.setLayout(new BorderLayout());
		

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("Inventory List");
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
		JButton btnAddElements = b.createButton("Add Inventory Item");
		JButton btnSelectElements = b.createButton("Next");
		

		pnlButtons.add(btnAddElements);
		pnlButtons.add(btnSelectElements);

		inventoryFrame.add(pnlInstruction, BorderLayout.NORTH);
		inventoryFrame.add(pnlInventoryList, BorderLayout.CENTER);
		inventoryFrame.add(pnlButtons, BorderLayout.SOUTH);
		inventoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inventoryFrame.pack();		
		inventoryFrame.setVisible(true);
		btnAddElements.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Data.uiFileUpload = new UIFileUploadHTTP();
				Data.uiFileUpload.runUpload();
			}
		});
		
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {

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
						return null;
					}
				};

				Window win = SwingUtilities.getWindowAncestor((AbstractButton) e
						.getSource());
				final JDialog dialog = new JDialog(win, "Bucket",
						ModalityType.APPLICATION_MODAL);

				mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals("state")) {
							if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
								dialog.dispose();
							}
						}
					}
				});
				mySwingWorker.execute();

				JProgressBar progressBar = new JProgressBar();
				progressBar.setIndeterminate(true);
				JPanel panel = new JPanel(new BorderLayout());
				panel.add(progressBar, BorderLayout.CENTER);
				panel.add(new JLabel("Getting Buckets......."), BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setLocationRelativeTo(win);
				dialog.setBounds(50,50,300,100);
				dialog.setVisible(true);
				
				
			}
		});
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
