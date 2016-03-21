package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

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
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;

import ui.components.Button;
import ui.components.CheckBoxList;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.jtree.CheckTreeManager;
import api.APIProcess;
import customColor.CustomColor;

public class UILicenseAdd {
	private JFrame frame;

	private JPanel pnlButtons;
	private JPanel pnlSetting;
	private JPanel pnlFeatures;

	private JTree tree;
	private JTable tblInfo;

	private JScrollPane spTreeCheckBox;
	private JScrollPane spList;

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
	private HashMap<String, String> servicesList; 

	private CheckTreeManager checkTreeManager;
	private APIProcess api = new APIProcess();
	private HashMap<String, DefaultMutableTreeNode> checkFeatures = new HashMap<String,
		 DefaultMutableTreeNode>();

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public void runTestJCheckBox() {
		frame = new JFrame("Add License");
		frame.getContentPane().setBackground(CustomColor.NavyBlue.returnColor());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().setLayout(new BorderLayout());

		pnlSetting = createSettingPnl();
		pnlFeatures = createPnlFeature();
		pnlButtons = createButtonPanel();
		frame.getContentPane().add(pnlSetting, BorderLayout.NORTH);
		frame.getContentPane().add(pnlFeatures, BorderLayout.CENTER);
		frame.getContentPane().add(pnlButtons, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}

	public DefaultMutableTreeNode createNode(String name) {
		return new DefaultMutableTreeNode(name);
	}

	private JPanel createSettingPnl() {
		JPanel panel = p.createPanel(Layouts.gridbag);
		GridBagConstraints g = new GridBagConstraints();

		lblCloud = l.createLabel("Cloud Storage (GB):", SwingConstants.LEFT);
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 2;
		g.insets = new Insets(5, 5, 5, 5);
		g.anchor = g.FIRST_LINE_START;
		panel.add(lblCloud, g);

		lblValidity = l.createLabel("Validity Period (Months):", SwingConstants.LEFT);
		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 2;
		g.weighty = 1.0;
		g.insets = new Insets(5, 5, 5, 5);
		panel.add(lblValidity, g);

		lblConcurrentVCA = l.createLabel("Max Concurrent VCA:", SwingConstants.LEFT);
		g.gridx = 0;
		g.gridy = 2;
		g.gridwidth = 2;
		g.weighty = 1.0;
		g.insets = new Insets(5, 5, 5, 5);
		g.anchor = g.LAST_LINE_START;
		panel.add(lblConcurrentVCA, g);

		spnCloud = new JSpinner(new SpinnerNumberModel(1, 0, 1, 1));
		spnCloud.setPreferredSize(new Dimension(50, 25));
		g.gridx = 2;
		g.gridy = 0;
		g.gridwidth = 2;
		g.fill = GridBagConstraints.VERTICAL;
		panel.add(spnCloud, g);

		spnValidity = new JSpinner(new SpinnerNumberModel(24, 1, 24, 1));
		spnValidity.setPreferredSize(new Dimension(50, 25));
		g.gridx = 2;
		g.gridy = 1;
		g.gridwidth = 2;
		g.fill = GridBagConstraints.VERTICAL;
		panel.add(spnValidity, g);

		spnConcurrentVCA = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
		spnConcurrentVCA.setPreferredSize(new Dimension(50, 25));
		g.gridx = 2;
		g.gridy = 2;
		g.gridwidth = 2;
		g.fill = GridBagConstraints.VERTICAL;
		panel.add(spnConcurrentVCA, g);

		cbPerpetual = new JCheckBox("Perpetual");
		cbPerpetual.setBackground(CustomColor.NavyBlue.returnColor());
		cbPerpetual.setForeground(CustomColor.Grey.returnColor());
		g.gridx = 5;
		g.gridy = 1;
		g.weighty = 1.0;
		g.fill = GridBagConstraints.VERTICAL;
		panel.add(cbPerpetual, g);

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
		getFeaturesData();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		
		
		for(String key : Data.featureList.keySet()){
			
			 JSONArray featureArray = Data.featureList.get(key);
			 DefaultMutableTreeNode element = new DefaultMutableTreeNode(key);
			 ArrayList<DefaultMutableTreeNode> arrayFeatureCheckBox = new ArrayList<DefaultMutableTreeNode>();
			
			 for(int i = 0; i < featureArray.length(); i ++){
				 try {
					 DefaultMutableTreeNode featureElement = new DefaultMutableTreeNode(featureArray.getJSONObject(i).getString("name"));
					 element.add(featureElement);
					 arrayFeatureCheckBox.add(featureElement);
					
					 
				 } catch (JSONException e1) {
					 e1.printStackTrace();
				 }
			 }
			 root.add(element);
			
		 }

//			
//			 element.addItemListener(new ItemListener() {
//			 @Override
//			 public void itemStateChanged(ItemEvent e) {
//			 // TODO Auto-generated method stub
//			 if(element.isSelected()){
//			// mdlFeature.removeAllElements();
//			 HashMap<String, String> servicesList = new HashMap<String, String>();
//			// mdlSvcUsed = new DefaultListModel<String>();
//			
//			 for(int i = 0; i < featureArray.length(); i ++){
//			 try {
//			// mdlFeature.addElement(featureArray.getJSONObject(i).getString("name"));
//			// featuresList.put(featureArray.getJSONObject(i).getString("name"), "");
//			 JSONArray servicesArray =
//			 featureArray.getJSONObject(i).getJSONArray("services");
//			 for(int x = 0; x < servicesArray.length(); x ++){
//			 servicesList.put(Integer.toString(servicesArray.getJSONObject(x).getInt("id")),
//			 servicesArray.getJSONObject(x).getString("name"));
//			 }
//			 } catch (JSONException e1) {
//			 e1.printStackTrace();
//			 }
//			 }
//			
//			 for(String serviceKey : servicesList.keySet()){
//			// mdlSvcUsed.addElement(servicesList.get(serviceKey));
//			 }
//			
//			// listSvcUsed.setModel(mdlSvcUsed);
//			 }else{
//			// currentSelected = element.getText();
//			 for(int i = 0; i < featureArray.length(); i ++){
//			 try {
//		//	 featuresList.remove(featureArray.getJSONObject(i).getString("name"));
//			 } catch (JSONException e1) {
//			 // TODO Auto-generated catch block
//			 e1.printStackTrace();
//			 }
//			 }}
//			 }
//			 });

		tree = new JTree(root);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = tree.getSelectionModel().getSelectionPath();
				model.removeAllElements();
				String selected = tree.getSelectionModel().getSelectionPath().getLastPathComponent().toString();
				servicesList = new HashMap<String, String>();
				try{
					if(selected.equalsIgnoreCase("root")){
						for(String key : Data.featureList.keySet()){
							JSONArray featureArray = Data.featureList.get(key);
							
							 for(int i = 0; i < featureArray.length(); i ++){
								 JSONArray servicesArray = featureArray.getJSONObject(i).getJSONArray("services");
								 
								 for(int x = 0; x < servicesArray.length(); x ++){
									 servicesList.put(Integer.toString(servicesArray.getJSONObject(x).getInt("id")), servicesArray.getJSONObject(x).getString("name"));
								 }
							 }
						}
					}else if(path.getPathCount() == 2){
						for(String key : Data.featureList.keySet()){
							if(key.equals(selected)){
								JSONArray featureArray = Data.featureList.get(key);
								
								 for(int i = 0; i < featureArray.length(); i ++){
									 JSONArray servicesArray = featureArray.getJSONObject(i).getJSONArray("services");
									 
									 for(int x = 0; x < servicesArray.length(); x ++){
										 servicesList.put(Integer.toString(servicesArray.getJSONObject(x).getInt("id")), servicesArray.getJSONObject(x).getString("name"));
									 }
								 }
							}else{
								continue;
							}
						}
					}else{
						for(String key : Data.featureList.keySet()){
							if(key.equals(path.getPathComponent(1).toString())){
								JSONArray featureArray = Data.featureList.get(key);
								
								 for(int i = 0; i < featureArray.length(); i ++){
									 if(featureArray.getJSONObject(i).getString("name").equals(selected)){
										 JSONArray servicesArray = featureArray.getJSONObject(i).getJSONArray("services");
										 
										 for(int x = 0; x < servicesArray.length(); x ++){
											 servicesList.put(Integer.toString(servicesArray.getJSONObject(x).getInt("id")), servicesArray.getJSONObject(x).getString("name"));
										 }
									 }else{
										 continue;
									 }
								 }
							}else{
								continue;
							}
						}
					}
				} catch(JSONException ex){
					ex.printStackTrace();
				}
				for(String serviceKey : servicesList.keySet()){
					 model.addElement(servicesList.get(serviceKey));
				}
				listServiceUsed.setModel(model);
			}
		});
		checkTreeManager = new CheckTreeManager(tree);
		checkTreeManager.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath[] path = checkTreeManager.getSelectionModel().getSelectionPaths();
				ArrayList<Object> alSelected = new ArrayList<>();
				// remove all the element in the list
				model.removeAllElements();

				// TreePath can be more than one if not all is selected.
				// For instance, Child 1 is Partially selected and Child 2 is
				// Fully Selected
				// It would show loop and show up as
				// Child2
				// GrandChildA
				// GrandChildB
				HashMap<String, String> servicesList = new HashMap<String, String>();
				model = new DefaultListModel<String>();

				for (TreePath tp : path) {
					System.out.println(tp.getLastPathComponent());
					if (tp.getLastPathComponent().toString().equalsIgnoreCase("root")) {
						for(String key : Data.featureList.keySet()){
							
							 JSONArray featureArray = Data.featureList.get(key);
							 for(int i = 0; i < featureArray.length(); i ++){
							 
							 }
						}
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

		panel.add(btnSubmit);
		panel.add(btnCancel);

		return panel;
	}
	
	public void getFeaturesData(){
		 api.featuresList(Data.targetURL, Data.sessionKey, Data.bucketID);
	}
	
	//
	// public Panel p = new Panel();
	// public Button b = new Button();
	// public Label l = new Label();
	//
	// private JFrame licenseAdd;
	// private JPanel pnlSetting;
	// private JPanel pnlFeature;
	// private JPanel pnlApiService;
	// private JPanel pnlBtns;
	// private DefaultListModel<String> mdlSvcUsed;
	// private DefaultListModel<String> mdlFeature;
	// private JList listSvcUsed;
	//
	// private CheckBoxList cblFeaturesCategory;
	// private CheckBoxList currentFeature;
	// private JScrollPane scrollFeature;
	// private APIProcess api = new APIProcess();
	// private APICall apiCall = new APICall();
	// private HashMap<String, String> featuresList = new HashMap<String,
	// String>();
	// private HashMap<String, CheckBoxList> checkFeatures = new HashMap<String,
	// CheckBoxList>();
	//
	// private String currentSelected;
	// private JSpinner spinnerStorage;
	// private JSpinner spinnerPeriod;
	// private JSpinner spinnerVca;
	// public void runLicenseAdd(){
	// getFeaturesData();
	// init();
	// }
	//
	// private void init() {
	// licenseAdd = new JFrame("Add License");
	// licenseAdd.setLayout(new BorderLayout());
	//
	//
	// pnlSetting = p.createPanel(Layouts.flow);
	// pnlFeature = p.createPanel(Layouts.flow);
	// pnlApiService = p.createPanel(Layouts.flow);
	// pnlBtns = p.createPanel(Layouts.flow);
	//
	// initSettingPanel();
	// initFeaturePanel();
	// //initApiServicePanel();
	// initBtnPanel();
	//
	// licenseAdd.add(pnlSetting, BorderLayout.NORTH);
	// licenseAdd.add(pnlFeature, BorderLayout.WEST);
	// licenseAdd.add(pnlApiService, BorderLayout.EAST);
	// licenseAdd.add(pnlBtns,BorderLayout.SOUTH);
	// licenseAdd.pack();
	// licenseAdd.setResizable(false);
	// licenseAdd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// licenseAdd.setVisible(true);
	// }
	//
	// private void initSettingPanel() {
	// JPanel pnlSettingLayout = p.createPanel(Layouts.grid, 2, 2);
	// pnlSettingLayout.setBorder(new EmptyBorder(10,10,10,10));
	// JLabel lblCloudStorage = l.createLabel("Cloud Storage:");
	// JLabel lblPeriod = l.createLabel("Validity Period:");
	// JLabel lblConcurrentVCA = l.createLabel("Maximum concurrent VCA:");
	//
	//
	// SpinnerModel smStorage = new SpinnerNumberModel(1, 1, 1, 1);
	// SpinnerModel smPeriod = new SpinnerNumberModel(1, 1, 24, 1);
	// SpinnerModel smVca = new SpinnerNumberModel(1, 1, 4, 1);
	//
	// spinnerStorage = new JSpinner(smStorage);
	// spinnerPeriod = new JSpinner(smPeriod);
	// spinnerVca = new JSpinner(smVca);
	//
	// Component[] listSettingComponent = { lblCloudStorage, spinnerStorage,
	// lblPeriod,
	// spinnerPeriod, lblConcurrentVCA, spinnerVca };
	//
	// for(Component c:listSettingComponent){
	// if(c instanceof JLabel){
	// ((JLabel) c).setBorder(new EmptyBorder(10,10,10,10));
	// }
	// }
	//
	// p.addComponentsToPanel(pnlSettingLayout, listSettingComponent);
	// pnlSetting.add(pnlSettingLayout);
	// }
	//
	// private void initFeaturePanel() {
	//
	// JPanel pnlFeatureLayout = p.createPanel(Layouts.grid, 2, 2);
	// pnlFeatureLayout.setBorder(new EmptyBorder(10,10,10,10));
	// JLabel lblFeatureCategory = l.createLabel("Feature Categories:");
	// JPanel pnlApiSvcLayout = p.createPanel(Layouts.grid,1,1);
	// JLabel lblServiceUsed = l.createLabel("Service APIs Used");
	// JLabel lblFeature = l.createLabel("Features:");
	//
	//
	// // Feature Category
	// cblFeaturesCategory = new CheckBoxList();
	// ArrayList<JCheckBox> arrayCheckBox = new ArrayList<JCheckBox>();
	// mdlFeature = new DefaultListModel<String>();
	// currentFeature = new CheckBoxList();
	// for(String key : Data.featureList.keySet()){
	// JSONArray featureArray = Data.featureList.get(key);
	// JCheckBox element = new JCheckBox(key);
	// CheckBoxList cblFeatures = new CheckBoxList();
	// ArrayList<JCheckBox> arrayFeatureCheckBox = new ArrayList<JCheckBox>();
	//
	// for(int i = 0; i < featureArray.length(); i ++){
	// try {
	// JCheckBox featureElement = new
	// JCheckBox(featureArray.getJSONObject(i).getString("name"));
	// arrayFeatureCheckBox.add(featureElement);
	// } catch (JSONException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	// cblFeatures.setListData(arrayFeatureCheckBox.toArray());
	// checkFeatures.put(key, cblFeatures);
	//
	//
	// element.addItemListener(new ItemListener() {
	// @Override
	// public void itemStateChanged(ItemEvent e) {
	// // TODO Auto-generated method stub
	// if(element.isSelected()){
	// mdlFeature.removeAllElements();
	// HashMap<String, String> servicesList = new HashMap<String, String>();
	// mdlSvcUsed = new DefaultListModel<String>();
	//
	// for(int i = 0; i < featureArray.length(); i ++){
	// try {
	// mdlFeature.addElement(featureArray.getJSONObject(i).getString("name"));
	// featuresList.put(featureArray.getJSONObject(i).getString("name"), "");
	// JSONArray servicesArray =
	// featureArray.getJSONObject(i).getJSONArray("services");
	// for(int x = 0; x < servicesArray.length(); x ++){
	// servicesList.put(Integer.toString(servicesArray.getJSONObject(x).getInt("id")),
	// servicesArray.getJSONObject(x).getString("name"));
	// }
	// } catch (JSONException e1) {
	// e1.printStackTrace();
	// }
	// }
	//
	// for(String serviceKey : servicesList.keySet()){
	// mdlSvcUsed.addElement(servicesList.get(serviceKey));
	// }
	//
	// listSvcUsed.setModel(mdlSvcUsed);
	// }else{
	// currentSelected = element.getText();
	// for(int i = 0; i < featureArray.length(); i ++){
	// try {
	// featuresList.remove(featureArray.getJSONObject(i).getString("name"));
	// } catch (JSONException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	// }
	// }
	// });
	//
	// arrayCheckBox.add(element);
	// }
	//
	//
	// cblFeaturesCategory.setListData(arrayCheckBox.toArray());
	// JScrollPane scrollFeaturesCategory = new
	// JScrollPane(cblFeaturesCategory);
	// Component[] arrayFeatureCategory = { lblFeatureCategory,
	// scrollFeaturesCategory };
	//
	// // Feature
	// JList listFeature = new JList(mdlFeature);
	// listFeature.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	// listFeature.addListSelectionListener(new ListSelectionListener() {
	//
	// @Override
	// public void valueChanged(ListSelectionEvent e) {
	//
	// int[] selected = listFeature.getSelectedIndices();
	// for (int i = 0; i < selected.length; i++) {
	// String name =
	// listFeature.getModel().getElementAt(selected[i]).toString();
	// featuresList.put(name, "");
	// }
	//
	//
	// }
	// });
	// listSvcUsed = new JList();
	//
	// scrollFeature = new JScrollPane(listFeature);
	// Component[] arrayFeature = { lblFeature, scrollFeature };
	//
	// JScrollPane scrollSvcUsed = new JScrollPane(listSvcUsed);
	// scrollSvcUsed.setPreferredSize(new Dimension(300,300));
	//
	// Component[] arraySvcUsedComp = {lblServiceUsed,scrollSvcUsed};
	//
	// p.addComponentsToPanel(pnlFeatureLayout, arrayFeatureCategory);
	// p.addComponentsToPanel(pnlFeatureLayout, arrayFeature);
	// p.addComponentsToPanel(pnlApiSvcLayout, arraySvcUsedComp);
	//
	//
	// pnlFeature.add(pnlFeatureLayout);
	// pnlApiService.add(pnlApiSvcLayout);
	// }
	//
	//// private void initApiServicePanel(){
	//// JPanel pnlApiSvcLayout = p.createPanel(Layouts.grid,1,1);
	//// JLabel lblServiceUsed = l.createLabel("Service APIs Used");
	//// DefaultListModel<String> mdlSvcUsed = new DefaultListModel<>();
	//// JList listSvcUsed = new JList(mdlSvcUsed);
	//// JScrollPane scrollSvcUsed = new JScrollPane(listSvcUsed);
	//// scrollSvcUsed.setPreferredSize(new Dimension(300,300));
	//// Component[] arraySvcUsedComp = {lblServiceUsed,scrollSvcUsed};
	//// p.addComponentsToPanel(pnlApiSvcLayout, arraySvcUsedComp);
	//// pnlApiService.add(pnlApiSvcLayout);
	//// }
	//
	// private void initBtnPanel(){
	// JPanel pnlButtonLayout = p.createPanel(Layouts.flow);
	// pnlButtonLayout.setAlignmentY(FlowLayout.CENTER);
	// JButton btnSubmit = b.createButton("Submit");
	// JButton btnCancel = b.createButton("Cancel");
	// btnSubmit.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	//
	// ArrayList<String> featureL = new ArrayList<String>();
	// String[] features = new String[]{};
	// //listSelected.
	// for(String key : featuresList.keySet()){
	// featureL.add(key);
	// }
	// features = featureL.toArray(features);
	// String duration = spinnerPeriod.getValue().toString();
	// String storage = spinnerStorage.getValue().toString();
	// String maxVCA = spinnerVca.getValue().toString();
	// String response = apiCall.addNodeLicense(Data.targetURL, Data.sessionKey,
	// Data.bucketID, features, duration, storage, maxVCA);
	//
	// try {
	// JSONObject responseObject = new JSONObject(response);
	// if(responseObject.get("result").equals("ok")){
	// licenseAdd.setVisible(false);
	// Data.uiLicenseDetail.setFrameVisible();
	// }
	// } catch (JSONException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// }
	// });
	//
	// btnCancel.addActionListener(new ActionListener(){
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// // TODO Auto-generated method stub
	// licenseAdd.setVisible(false);
	// Data.uiLicenseDetail.setFrameVisible();
	// }
	// });
	//
	// pnlButtonLayout.add(btnSubmit);
	// pnlButtonLayout.add(btnCancel);
	// pnlBtns.add(pnlButtonLayout);
	// }
	//
	// 

}
