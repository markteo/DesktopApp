package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import customColor.CustomColor;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.jtree.CheckTreeManager;
import customColor.CustomColor;

public class UILicenseDetail {

	private JFrame frame;

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

	private JButton btnSubmit;
	private JButton btnCancel;
	private JButton btnAdd;

	private CheckTreeManager checkTreeManager;

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public void runLicenseDetails() {
		frame = new JFrame("License Details");
		frame.getContentPane().setBackground(CustomColor.NavyBlue.returnColor());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().setLayout(new BorderLayout());

		pnlDetails = createDetailsPnl();
		pnlFeatures = createPnlFeature();
		pnlButtons = createButtonPanel();
		frame.getContentPane().add(pnlDetails, BorderLayout.NORTH);
		frame.getContentPane().add(pnlFeatures, BorderLayout.CENTER);
		frame.getContentPane().add(pnlButtons, BorderLayout.SOUTH);

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
		String[] columnNames = { "Name", "License", "Services", "Company" };
		String[][] data = { { "Cheok Jia Chin", "123456", "Perimeter Defense, Face Indexing", "Cheok Company" },
				{ "Ho Yuan Yi", "123457", "Audience Profiling, People Counting", "HOHOHO Co" },
				{ "Mark Teo", "1234568", "Audience Profiling", "Teo Pte Ltd" } };

		tblInfo = new JTable();
		tblInfo.setModel(new UneditableModel(data, columnNames));
		tblInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblInfo.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					System.out.println("Value selected is " + target.getModel().getValueAt(row, 0));
				}
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
		lblSelectFeature = l.createLabel("Select Features", SwingConstants.LEFT);
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

		DefaultMutableTreeNode root = createNode("root");

		DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("child1");
		DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("child2");

		child1.add(createNode("gchild3"));
		child1.add(createNode("gchild4"));
		child2.add(createNode("gchild5"));
		child2.add(createNode("gchild6"));
		child2.add(createNode("gchild7"));

		root.add(child1);
		root.add(child2);

		tree = new JTree(root);
		checkTreeManager = new CheckTreeManager(tree);
		checkTreeManager.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				TreePath[] path = checkTreeManager.getSelectionModel().getSelectionPaths();
				ArrayList<Object> alSelected = new ArrayList<Object>();
				// remove all the element in the list
				model.removeAllElements();

				// TreePath can be more than one if not all is selected.
				// For instance, Child 1 is Partially selected and Child 2 is
				// Fully Selected
				// It would show loop and show up as
				// Child2
				// GrandChildA
				// GrandChildB
				for (TreePath tp : path) {
					System.out.println(tp.getLastPathComponent());
					if (tp.getLastPathComponent().toString().equalsIgnoreCase("root")) {
						System.out.println("Selected All");
						// root element
					} else if (tp.getPathCount() == 2) {
						// Definitely need to map to hashmap to get all API
						// use tp.getLastPathComponent(); to get the text of the
						// name
					} else if (tp.getPathCount() == 3) {
						// final level, use getParentPath() to get the header of
						// your
						// hashmap then fetch the API from there
						System.out.println("Level 3");
					}
				}
			}
		});
				
		
		

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

		btnSubmit = b.createButton("Submit");
		btnCancel = b.createButton("Cancel");

		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				UILicenseAdd uiLicenseAdd = new UILicenseAdd();
				//uiLicenseAdd.runLicenseAdd();
				uiLicenseAdd.runTestJCheckBox();
				
			}
		});

		panel.add(btnSubmit);
		panel.add(btnCancel);

		return panel;
	}
	public void setFrameVisible(){
		 frame.setVisible(true);
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

	// private String[] arrayLicense = new String[]{};
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

