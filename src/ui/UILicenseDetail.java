package ui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.jtree.CheckTreeManager;
import api.APIProcess;
import customColor.CustomColor;

public class UILicenseDetail {

	private JFrame frame;
	private String[] arrayLicense = new String[] {};

	private JPanel pnlButtons;
	private JPanel pnlDetails;
	private JPanel pnlFeatures;

	private JTree tree;
	private JTable tblInfo;

	private JScrollPane spTreeCheckBox;
	private JScrollPane spList;
	private JScrollPane spTable;

	private DefaultListModel<String> model;
	private JList listServiceUsed;

	private JLabel lblCloud;
	private JLabel lblValidity;
	private JLabel lblConcurrentVCA;
	private JLabel lblSelectFeature;
	private JLabel lblServiceApi;

	private JSpinner spnCloud;
	private JSpinner spnValidity;
	private JSpinner spnConcurrentVCA;

	private JCheckBox cbPerpetual;
	private DefaultMutableTreeNode root;
	private JButton btnSubmit;
	private JButton btnCancel;
	private JButton btnAdd;
	private APIProcess api = new APIProcess();
	private CheckTreeManager checkTreeManager;
	private final SimpleDateFormat simpleDate = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public UILicenseDetail() {
		getFeaturesData();
		getLicenseData();
		runLicenseDetails();
	}

	public void runLicenseDetails() {
		frame = new JFrame("License Details");

		frame.getContentPane().setLayout(new BorderLayout());

		pnlDetails = createDetailsPnl();
		pnlFeatures = createPnlFeature();
		pnlButtons = createButtonPanel();
		frame.getContentPane().add(pnlDetails, BorderLayout.NORTH);
		frame.getContentPane().add(pnlFeatures, BorderLayout.CENTER);
		frame.getContentPane().add(pnlButtons, BorderLayout.SOUTH);
		frame.getContentPane()
				.setBackground(CustomColor.NavyBlue.returnColor());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	public DefaultMutableTreeNode createNode(String name) {
		return new DefaultMutableTreeNode(name);
	}

	private JPanel createDetailsPnl() {
		JPanel panel = p.createPanel(Layouts.flow);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// init the columnNames, data
		tblInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tblInfo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				int selected = tblInfo.getSelectedRow();
				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				getData((String) tblInfo.getModel().getValueAt(selected, 0));
				model.setRoot(root);
			}
		});
		spTable = new JScrollPane(tblInfo);
		spTable.setPreferredSize(new Dimension(600, 100));

		panel.add(spTable);
		return panel;
	}

	private JPanel createPnlFeature() {
		JPanel panel = p.createPanel(Layouts.gridbag);

		GridBagConstraints g = new GridBagConstraints();
		lblSelectFeature = l
				.createLabel("Select Features", SwingConstants.LEFT);
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 2;
		g.insets = new Insets(5, 5, 5, 5);
		panel.add(lblSelectFeature, g);

		lblServiceApi = l.createLabel("Service APIs Used", SwingConstants.LEFT);
		g.gridx = 4;
		g.gridy = 0;
		g.gridwidth = 2;
		g.insets = new Insets(5, 5, 5, 5);
		panel.add(lblServiceApi, g);
		tree = new JTree(root);
		spTreeCheckBox = new JScrollPane(tree);
		spTreeCheckBox.setPreferredSize(new Dimension(300, 300));
		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 3;
		g.anchor = g.LINE_START;
		g.insets = new Insets(5, 5, 5, 5);
		panel.add(spTreeCheckBox, g);

		model = new DefaultListModel<>();
		model.addElement("");
		listServiceUsed = new JList<String>(model);

		spList = new JScrollPane(listServiceUsed);
		spList.setPreferredSize(new Dimension(300, 300));
		g.gridx = 4;
		g.gridy = 1;
		g.gridwidth = 3;
		g.anchor = g.LINE_START;
		panel.add(spList, g);

		return panel;
	}

	public JPanel createButtonPanel() {
		JPanel panel = p.createPanel(Layouts.flow);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		btnSubmit = b.createButton("Next");
		btnCancel = b.createButton("Cancel");
		btnAdd = b.createButton("Add License");

		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						int row = tblInfo.getSelectedRow();
						Data.licenseNumber = (String) tblInfo.getModel()
								.getValueAt(row, 0);
						frame.setVisible(false);
						Data.uiAccessKeySelect = new UIAccessKeySelect();
						return null;
					}
				};

				Window win = SwingUtilities
						.getWindowAncestor((AbstractButton) e.getSource());
				final JDialog dialog = new JDialog(win, "Dialog",
						ModalityType.APPLICATION_MODAL);

				mySwingWorker
						.addPropertyChangeListener(new PropertyChangeListener() {

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
				panel.add(new JLabel("Retrieving Access Keys"),
						BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setLocationRelativeTo(win);
				dialog.setBounds(50, 50, 300, 100);
				dialog.setVisible(true);

			}

		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				Data.uiBucketSelect.setFrameVisible();
			}
		});

		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				UILicenseAdd uiLicenseAdd = new UILicenseAdd();
				uiLicenseAdd.runTestJCheckBox();

			}
		});

		panel.add(btnSubmit);
		panel.add(btnCancel);
		panel.add(btnAdd);

		return panel;
	}

	public void setFrameVisible() {
		frame.setVisible(true);
	}

	private void getLicenseData() {
		ArrayList<String> arrayLicenses = new ArrayList<String>();
		JSONArray licenseList = new APIProcess().nodeLicenseList(
				Data.targetURL, Data.sessionKey, Data.bucketID);
		try {
			String[] columnNames = { "License Number", "Validity", "Storage",
					"Date Created", "Maximum VCA", "Bucket Name" };
			Object[][] rowData = new Object[licenseList.length()][columnNames.length];

			for (int i = 0; i < licenseList.length(); i++) {
				JSONObject license = licenseList.getJSONObject(i);

				String licenseNumber = license.getString("licenseNumber");
				char[] charArray = licenseNumber.toCharArray();
				String licenseAdd = "";
				for (int x = 0; x < charArray.length; x++) {
					if (x % 5 == 0 && x != 0) {

						licenseAdd += " - " + charArray[x];

					} else {
						licenseAdd += charArray[x];
					}
				}
				JSONObject response = api.getNodeLicenseDetails(Data.targetURL,
						Data.sessionKey, Data.bucketID, licenseAdd);
				rowData[i][0] = licenseAdd;
				if (response.getInt("duration") == -1) {
					rowData[i][1] = "Perpetual";
				} else {
					rowData[i][1] = response.getInt("duration");
				}
				rowData[i][2] = response.getInt("cloudStorage");
				rowData[i][3] = simpleDate.format(response.getLong("created"));
				rowData[i][4] = response.getInt("maxVCA");
				rowData[i][5] = response.getString("bucketName");
				arrayLicenses.add(licenseAdd);
			}
			tblInfo = new JTable(rowData, columnNames);
			arrayLicense = arrayLicenses.toArray(arrayLicense);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void getData(String licenseNumber) {
		root = new DefaultMutableTreeNode("root");

		try {
			JSONObject response = api.getNodeLicenseDetails(Data.targetURL,
					Data.sessionKey, Data.bucketID, licenseNumber);
			JSONArray features = response.getJSONArray("features");
			for (String key : Data.featureList.keySet()) {
				boolean contains = false;

				JSONArray featureArray = Data.featureList.get(key);

				DefaultMutableTreeNode element = new DefaultMutableTreeNode(key);
				ArrayList<DefaultMutableTreeNode> arrayFeatureCheckBox = new ArrayList<DefaultMutableTreeNode>();
				for (int i = 0; i < features.length(); i++) {
					String feature = features.getString(i);
					for (int x = 0; x < featureArray.length(); x++) {
						try {
							if (feature.equals(featureArray.getJSONObject(x)
									.getString("name"))) {
								DefaultMutableTreeNode featureElement = new DefaultMutableTreeNode(
										featureArray.getJSONObject(x)
												.getString("name"));
								element.add(featureElement);
								arrayFeatureCheckBox.add(featureElement);
								contains = true;
							}

						} catch (JSONException e1) {
							e1.printStackTrace();
						}
					}
				}
				if (contains) {
					root.add(element);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getFeaturesData() {
		api.featuresList(Data.targetURL, Data.sessionKey, Data.bucketID);
	}

}

// need this model to make table uneditable
class UneditableModel extends DefaultTableModel {

	UneditableModel(Object[][] data, String[] columnNames) {
		super(data, columnNames);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	//
	// private JFrame licenseDetail;
	//
	// private JPanel pnlButtons;
	// private JPanel pnlDetails;
	// private JPanel pnlFeatures;
	// private JPanel pnlVca;
	// private JPanel pnlReports;
	// private JPanel pnlAdminSetting;
	// private JPanel pnlNotification;
	//
	// private JButton btnNext;
	// private JButton btnBack;
	// private JButton btnAdd;
	//
	// private JComboBox<String> cbLicenses;
	//
	// private JList listVCA;
	// private JList listReports;
	// private JList listAdminSetting;
	// private JList listNotification;
	// private JList listMonitoring;
	// private JList listRecording;
	//
	// private DefaultListModel<String> mdlVCA;
	// private DefaultListModel<String> mdlReports;
	// private DefaultListModel<String> mdlAdmin;
	// private DefaultListModel<String> mdlNotification;
	// private DefaultListModel<String> mdlMonitoring;
	// private DefaultListModel<String> mdlRecording;
	//
	//
	//
	// private JLabel lblLicense;
	// private JLabel lblCustomerID;
	// private JLabel lblLicenseKey;
	// private JLabel lblDateCreated;
	// private JLabel lblCloudStorage;
	// private JLabel lblValidity;
	// private JLabel lblMaxConcurrentVCA;
	// private JLabel lblCustomerIDValue;
	// private JLabel lblLicenseKeyValue;
	// private JLabel lblDateCreatedValue;
	// private JLabel lblCloudStorageValue;
	// private JLabel lblValidityValue;
	// private JLabel lblMaxConcurrentVCAValue;
	// private final SimpleDateFormat simpleDate = new
	// SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	//
	// String licenseNumber;
	// int cloudStorage;
	// int maxVCA;
	// String dateCreated;
	// int customerID;
	// String validity;
	// String bucketName;
	//
	// TitledBorder detailBorder = BorderFactory.createTitledBorder("Details");
	//
	// Panel p = new Panel();
	// Button b = new Button();
	// Label l = new Label();
	// APIProcess api = new APIProcess();
	//
	// private JPanel pnlRecording;
	//
	// private JPanel pnlMonitoring;
	//
	// public UILicenseDetail(){
	// getLicenseData();
	// getData(arrayLicense[0]);
	// runLicenseDetails();
	// }
	//
	// public void runLicenseDetails() {
	// licenseDetail = new JFrame("License Detail");
	// licenseDetail.setLayout(new BorderLayout());
	// licenseDetail.setBackground(CustomColor.NavyBlue.returnColor());
	//
	// pnlButtons = p.createPanel(Layouts.flow);
	// btnNext = b.createButton("Next");
	// btnAdd = b.createButton("Add License");
	// btnBack = b.createButton("Back");
	// Component[] buttonList = { btnBack, btnAdd, btnNext };
	// p.addComponentsToPanel(pnlButtons, buttonList);
	//
	// pnlDetails = p.createPanel(Layouts.gridbag);
	// detailBorder.setTitleColor(CustomColor.LightBlue.returnColor());
	// pnlDetails.setBorder(detailBorder);
	//
	// GridBagConstraints gc = new GridBagConstraints();
	// gc.gridx = 1;
	// gc.gridy = 0;
	// lblLicense = l.createLabel("License");
	// pnlDetails.add(lblLicense, gc);
	//
	// gc.gridx = 2;
	// gc.gridy = 0;
	// cbLicenses = new JComboBox<>(arrayLicense);
	//
	// pnlDetails.add(cbLicenses, gc);
	//
	// createFieldLabel(gc);
	// createValueLabel(gc);
	//
	// JTabbedPane tabbedPane = new JTabbedPane();
	//
	// listMonitoring = new JList(mdlMonitoring);
	// JScrollPane spMonitoring = new JScrollPane(listMonitoring);
	// spMonitoring.setPreferredSize(new Dimension(300, 210));
	// pnlMonitoring = p.createPanel(Layouts.grid,1,1);
	// pnlMonitoring.add(listMonitoring);
	// tabbedPane.addTab("Monitoring", pnlMonitoring);
	//
	// listRecording = new JList(mdlRecording);
	// JScrollPane spRecording = new JScrollPane(listRecording);
	// spRecording.setPreferredSize(new Dimension(300, 210));
	// pnlRecording = p.createPanel(Layouts.grid,1,1);
	// pnlRecording.add(listRecording);
	// tabbedPane.addTab("Recording", pnlRecording);
	//
	// listVCA = new JList(mdlVCA);
	// JScrollPane spVCA = new JScrollPane(listVCA);
	// spVCA.setPreferredSize(new Dimension(300, 210));
	// pnlVca = p.createPanel(Layouts.grid,1,1);
	// pnlVca.add(spVCA);
	// tabbedPane.addTab("Video Analytics", pnlVca);
	//
	// listReports = new JList(mdlReports);
	// JScrollPane spReports = new JScrollPane(listReports);
	// spReports.setPreferredSize(new Dimension(300, 210));
	// pnlReports = p.createPanel(Layouts.grid,1,1);
	// pnlReports.add(spReports);
	// tabbedPane.addTab("Reports",pnlReports);
	//
	// listAdminSetting = new JList(mdlAdmin);
	// JScrollPane spSetting = new JScrollPane(listAdminSetting);
	// spSetting.setPreferredSize(new Dimension(300, 210));
	// pnlAdminSetting = p.createPanel(Layouts.grid,1,1);
	// pnlAdminSetting.add(listAdminSetting);
	// tabbedPane.addTab("Admin Setting",pnlAdminSetting);
	//
	// listNotification = new JList(mdlNotification);
	// JScrollPane spNotification = new JScrollPane(listNotification);
	// spNotification.setPreferredSize(new Dimension(300, 210));
	// pnlNotification = p.createPanel(Layouts.grid,1,1);
	// pnlNotification.add(listNotification);
	// tabbedPane.addTab("Notification Management", pnlNotification);
	//
	// licenseDetail.add(pnlButtons, BorderLayout.SOUTH);
	// licenseDetail.add(tabbedPane, BorderLayout.CENTER);
	// licenseDetail.add(pnlDetails, BorderLayout.NORTH);
	// licenseDetail.setSize(new Dimension(1500, 500));
	// licenseDetail.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// licenseDetail.pack();
	// licenseDetail.setVisible(true);
	//
	// cbLicenses.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// // TODO Auto-generated method stub
	// getData(cbLicenses.getSelectedItem().toString());
	// lblCustomerIDValue.setText(bucketName);
	// lblLicenseKeyValue.setText(licenseNumber);
	// lblDateCreatedValue.setText(dateCreated);
	//
	// lblCloudStorageValue.setText(Integer.toString(cloudStorage));
	// lblValidityValue.setText(validity);
	// lblMaxConcurrentVCAValue.setText(Integer.toString(maxVCA));
	//
	// listVCA.setModel(mdlVCA);
	// listNotification.setModel(mdlNotification);
	// listAdminSetting.setModel(mdlAdmin);
	// listRecording.setModel(mdlRecording);
	// listReports.setModel(mdlReports);
	// listMonitoring.setModel(mdlMonitoring);
	//
	// }
	// });
	//
	// btnBack.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// // TODO Auto-generated method stub
	// licenseDetail.setVisible(false);
	// Data.uiBucketSelect.setFrameVisible();
	// }
	// });
	// btnNext.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	//
	// SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
	// @Override
	// protected Void doInBackground() throws Exception {
	// Data.licenseNumber = licenseNumber;
	// licenseDetail.setVisible(false);
	// Data.uiAccessKeySelect = new UIAccessKeySelect();
	// return null;
	// }
	// };
	//
	// Window win = SwingUtilities.getWindowAncestor((AbstractButton) e
	// .getSource());
	// final JDialog dialog = new JDialog(win, "Dialog",
	// ModalityType.APPLICATION_MODAL);
	//
	// mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {
	//
	// @Override
	// public void propertyChange(PropertyChangeEvent evt) {
	// if (evt.getPropertyName().equals("state")) {
	// if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
	// dialog.dispose();
	// }
	// }
	// }
	// });
	// mySwingWorker.execute();
	//
	// JProgressBar progressBar = new JProgressBar();
	// progressBar.setIndeterminate(true);
	// JPanel panel = new JPanel(new BorderLayout());
	// panel.add(progressBar, BorderLayout.CENTER);
	// panel.add(new JLabel("Retrieving Access Keys"), BorderLayout.PAGE_START);
	// dialog.add(panel);
	// dialog.pack();
	// dialog.setLocationRelativeTo(win);
	// dialog.setBounds(50,50,300,100);
	// dialog.setVisible(true);
	//
	// }
	// });
	//
	// btnAdd.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// licenseDetail.setVisible(false);
	// UILicenseAdd uiLicenseAdd = new UILicenseAdd();
	// uiLicenseAdd.runLicenseAdd();
	//
	// }
	// });
	// }
	//
	// public void createFieldLabel(GridBagConstraints gc) {
	// lblCustomerID = l.createLabel("Bucket Name");
	// lblCloudStorage = l.createLabel("Cloud Storage");
	// lblDateCreated = l.createLabel("Date Created");
	// lblLicenseKey = l.createLabel("License Key");
	// lblValidity = l.createLabel("Validity (months)");
	// lblMaxConcurrentVCA = l.createLabel("Max VCA");
	//
	// gc.gridx = 1;
	// gc.gridy = 5;
	// pnlDetails.add(lblCustomerID, gc);
	//
	// gc.gridx = 1;
	// gc.gridy = 10;
	// pnlDetails.add(lblLicenseKey, gc);
	//
	// gc.gridx = 1;
	// gc.gridy = 15;
	// pnlDetails.add(lblDateCreated, gc);
	//
	// gc.gridx = 3;
	// gc.gridy = 5;
	// pnlDetails.add(lblCloudStorage, gc);
	//
	// gc.gridx = 3;
	// gc.gridy = 10;
	// pnlDetails.add(lblValidity, gc);
	//
	// gc.gridx = 3;
	// gc.gridy = 15;
	// pnlDetails.add(lblMaxConcurrentVCA, gc);
	//
	// }
	//
	// public void createValueLabel(GridBagConstraints gc) {
	// // value label
	// lblCustomerIDValue = l.createResultLabel(bucketName);
	// lblLicenseKeyValue = l.createResultLabel(licenseNumber);
	// lblDateCreatedValue = l.createResultLabel(dateCreated);
	//
	// lblCloudStorageValue =
	// l.createResultLabel(Integer.toString(cloudStorage));
	// lblValidityValue = l.createResultLabel(validity);
	// lblMaxConcurrentVCAValue = l.createResultLabel(Integer.toString(maxVCA));
	//
	//
	// gc.gridx = 2;
	// gc.gridy = 5;
	// pnlDetails.add(lblCustomerIDValue, gc);
	//
	// gc.gridx = 2;
	// gc.gridy = 10;
	// pnlDetails.add(lblLicenseKeyValue, gc);
	//
	// gc.gridx = 2;
	// gc.gridy = 15;
	// pnlDetails.add(lblDateCreatedValue, gc);
	//
	//
	// gc.gridx = 4;
	// gc.gridy = 5;
	// pnlDetails.add(lblCloudStorageValue, gc);
	//
	// gc.gridx = 4;
	// gc.gridy = 10;
	// pnlDetails.add(lblValidityValue, gc);
	//
	// gc.gridx = 4;
	// gc.gridy = 15;
	// pnlDetails.add(lblMaxConcurrentVCAValue, gc);
	//
	// }
	//
	// public void getData (String licenseNumber){
	// JSONObject response = api.getNodeLicenseDetails(Data.targetURL,
	// Data.sessionKey, Data.bucketID, licenseNumber);
	// try {
	// cloudStorage = response.getInt("cloudStorage");
	// dateCreated = simpleDate.format(response.getLong("created"));
	// maxVCA = response.getInt("maxVCA");
	// this.licenseNumber = licenseNumber;
	//
	// if(response.getInt("duration") == -1){
	// validity = "Unlimited";
	// }else{
	// validity = Integer.toString(response.getInt("duration"));
	// }
	// bucketName = response.getString("bucketName");
	//
	// JSONArray features = response.getJSONArray("features");
	// mdlAdmin = new DefaultListModel<String>();
	// mdlMonitoring = new DefaultListModel<String>();
	// mdlNotification = new DefaultListModel<String>();
	// mdlRecording = new DefaultListModel<String>();
	// mdlReports = new DefaultListModel<String>();
	// mdlVCA = new DefaultListModel<String>();
	//
	//
	//
	// for(int i = 0; i < features.length(); i++){
	// String feature = features.getString(i);
	// if(feature.equals("live-view")){
	// mdlMonitoring.addElement(feature);
	// }else if(feature.equals("node-playback")){
	// mdlRecording.addElement(feature);
	// }else if(feature.equals("historical-alerts")){
	// mdlNotification.addElement(feature);
	// }else if(feature.split("-")[0].equals("analytics")){
	// mdlVCA.addElement(feature);
	// }else if(feature.split("-")[0].equals("report")){
	// mdlReports.addElement(feature);
	// }else{
	// mdlAdmin.addElement(feature);
	// }
	// }
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// private void getLicenseData(){
	//
	// JSONArray licenseList = new APIProcess().nodeLicenseList(Data.targetURL,
	// Data.sessionKey, Data.bucketID);
	// ArrayList<String> licenses = new ArrayList<String>();
	// try {
	// for (int i = 0; i < licenseList.length(); i++) {
	// JSONObject license = licenseList.getJSONObject(i);
	//
	// String licenseNumber = license.getString("licenseNumber");
	// char[] charArray = licenseNumber.toCharArray();
	// String licenseAdd = "";
	// for(int x =0; x < charArray.length; x ++ ){
	// if(x%5 == 0 && x != 0){
	//
	// licenseAdd += " - " + charArray[x];
	//
	// }else{
	// licenseAdd += charArray[x];
	// }
	// }
	// licenses.add(licenseAdd);
	// arrayLicense = licenses.toArray(arrayLicense);
	// }
	// } catch (JSONException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	//

}
