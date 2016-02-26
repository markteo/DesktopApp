package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.CheckBoxList;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APICall;
import api.APIProcess;

public class UILicenseAdd {
	
	public Panel p = new Panel();
	public Button b = new Button();
	public Label l = new Label();

	private JFrame licenseAdd;
	private JPanel pnlSetting;
	private JPanel pnlFeature;
	private JPanel pnlApiService;
	private JPanel pnlBtns;
	private DefaultListModel<String> mdlFeature;
	private DefaultListModel<String> mdlSvcUsed;
	private JList listSvcUsed;
	
	private CheckBoxList cblFeaturesCategory;
	private JScrollPane scrollFeature;
	private APIProcess api = new APIProcess();
	private APICall apiCall = new APICall();
	private HashMap<String, JSONArray> featuresList = new HashMap<String, JSONArray>();
	private String currentSelected;
	public void runLicenseAdd(){
		getFeaturesData();
		
		init();
	}
	
	private void init() {
		licenseAdd = new JFrame("Add License");
		licenseAdd.setLayout(new BorderLayout());
		licenseAdd.setResizable(false);
		licenseAdd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		pnlSetting = p.createPanel(Layouts.flow);
		pnlFeature = p.createPanel(Layouts.flow);
		pnlApiService = p.createPanel(Layouts.flow);
		pnlBtns = p.createPanel(Layouts.flow);

		initSettingPanel();
		initFeaturePanel();
		//initApiServicePanel();
		initBtnPanel();

		licenseAdd.add(pnlSetting, BorderLayout.NORTH);
		licenseAdd.add(pnlFeature, BorderLayout.WEST);
		licenseAdd.add(pnlApiService, BorderLayout.EAST);
		licenseAdd.add(pnlBtns,BorderLayout.SOUTH);
		licenseAdd.pack();
		licenseAdd.setVisible(true);
	}

	private void initSettingPanel() {
		JPanel pnlSettingLayout = p.createPanel(Layouts.grid, 2, 2);
		pnlSettingLayout.setBorder(new EmptyBorder(10,10,10,10));
		JLabel lblCloudStorage = l.createLabel("Cloud Storage:");
		JLabel lblPeriod = l.createLabel("Validity Period:");
		JLabel lblConcurrentVCA = l.createLabel("Maximum concurrent VCA:");

		
		SpinnerModel smStorage = new SpinnerNumberModel(1, 1, 1, 1);
		SpinnerModel smPeriod = new SpinnerNumberModel(1, 1, 24, 1);
		SpinnerModel smVca = new SpinnerNumberModel(1, 1, 4, 1);

		JSpinner spinnerStorage = new JSpinner(smStorage);
		JSpinner spinnerPeriod = new JSpinner(smPeriod);
		JSpinner spinnerVca = new JSpinner(smVca);

		Component[] listSettingComponent = { lblCloudStorage, spinnerStorage, lblPeriod,
				spinnerPeriod, lblConcurrentVCA, spinnerVca };
		
		for(Component c:listSettingComponent){
			if(c instanceof JLabel){
				((JLabel) c).setBorder(new EmptyBorder(10,10,10,10));
			}
		}
	
		p.addComponentsToPanel(pnlSettingLayout, listSettingComponent);
		pnlSetting.add(pnlSettingLayout);
	}

	private void initFeaturePanel() {

		JPanel pnlFeatureLayout = p.createPanel(Layouts.grid, 2, 2);
		pnlFeatureLayout.setBorder(new EmptyBorder(10,10,10,10));
		JLabel lblFeatureCategory = l.createLabel("Feature Categories:");
		JPanel pnlApiSvcLayout = p.createPanel(Layouts.grid,1,1);
		JLabel lblServiceUsed = l.createLabel("Service APIs Used");
		// Feature Category
		cblFeaturesCategory = new CheckBoxList();
		ArrayList<JCheckBox> arrayCheckBox = new ArrayList<JCheckBox>();
		mdlFeature = new DefaultListModel<>();
		
		for(String key : Data.featureList.keySet()){
			JSONArray featureArray = Data.featureList.get(key);
			JCheckBox element = new JCheckBox(key);
			element.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					if(element.isSelected()){
						mdlFeature.removeAllElements();
						HashMap<String, String> servicesList =  new HashMap<String, String>();
						mdlSvcUsed = new DefaultListModel<String>();

						for(int i = 0; i < featureArray.length(); i ++){
							try {
								mdlFeature.addElement(featureArray.getJSONObject(i).getString("name"));
								JSONArray servicesArray = featureArray.getJSONObject(i).getJSONArray("services");
								for(int x = 0; x < servicesArray.length(); x ++){
									servicesList.put(Integer.toString(servicesArray.getJSONObject(x).getInt("id")), servicesArray.getJSONObject(x).getString("name"));
								}
								
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
						}
						
						for(String serviceKey : servicesList.keySet()){
							mdlSvcUsed.addElement(servicesList.get(serviceKey));
						}
						listSvcUsed.setModel(mdlSvcUsed);
						
						currentSelected = element.getText();
						featuresList.put(currentSelected, featureArray);
						System.out.println(featuresList.toString());
					}else{
						currentSelected = element.getText();
						featuresList.remove(currentSelected);
						int[] selected = listSvcUsed.getSelectedIndices();
						JSONArray featureJSONArray = new JSONArray();
						
					    // Get all the selected items using the indices
					    for (int i = 0; i < selected.length; i++) {
					      listSvcUsed.getModel().getElementAt(selected[i]);
					    }
					
					}
				}
			});
			arrayCheckBox.add(element);
		}
		

		cblFeaturesCategory.setListData(arrayCheckBox.toArray());
		JScrollPane scrollFeaturesCategory = new JScrollPane(cblFeaturesCategory);
		Component[] arrayFeatureCategory = { lblFeatureCategory, scrollFeaturesCategory };

		// Feature
		JLabel lblFeature = l.createLabel("Features:");
		
		JList listFeature = new JList(mdlFeature);
		
		listFeature.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		listSvcUsed = new JList();
		JScrollPane scrollSvcUsed = new JScrollPane(listSvcUsed);
		scrollSvcUsed.setPreferredSize(new Dimension(300,300));
		
		scrollFeature = new JScrollPane(listFeature);
		Component[] arrayFeature = { lblFeature, scrollFeature };
		Component[] arraySvcUsedComp = {lblServiceUsed,scrollSvcUsed};

		p.addComponentsToPanel(pnlFeatureLayout, arrayFeatureCategory);
		p.addComponentsToPanel(pnlFeatureLayout, arrayFeature);
		p.addComponentsToPanel(pnlApiSvcLayout, arraySvcUsedComp);


		pnlFeature.add(pnlFeatureLayout);
		pnlApiService.add(pnlApiSvcLayout);
	}

//	private void initApiServicePanel(){
//		JPanel pnlApiSvcLayout = p.createPanel(Layouts.grid,1,1);
//		JLabel lblServiceUsed = l.createLabel("Service APIs Used");
//		DefaultListModel<String> mdlSvcUsed = new DefaultListModel<>();
//		JList listSvcUsed = new JList(mdlSvcUsed);
//		JScrollPane scrollSvcUsed = new JScrollPane(listSvcUsed);
//		scrollSvcUsed.setPreferredSize(new Dimension(300,300));
//		Component[] arraySvcUsedComp = {lblServiceUsed,scrollSvcUsed};
//		p.addComponentsToPanel(pnlApiSvcLayout, arraySvcUsedComp);
//		pnlApiService.add(pnlApiSvcLayout);
//	}
	
	private void initBtnPanel(){
		JPanel pnlButtonLayout = p.createPanel(Layouts.flow);
		pnlButtonLayout.setAlignmentY(FlowLayout.CENTER);
		JButton btnSubmit = b.createButton("Submit");
		JButton btnCancel = b.createButton("Cancel");
		btnSubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JSONObject features = new JSONObject();				
				//listSelected.
				JSONArray featureArray = new JSONArray();
				for(String key : featuresList.keySet()){
					featureArray.put(featuresList.get(key));
				}
				try {
					features.put("features", featureArray);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				apiCall.addNodeLicense(Data.targetURL, Data.sessionKey, Data.bucketID, features.toString());
			}
		});
		
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				licenseAdd.setVisible(false);
				Data.uiLicenseSelect.setFrameVisible();
			}
		});
		
		pnlButtonLayout.add(btnSubmit);
		pnlButtonLayout.add(btnCancel);
		pnlBtns.add(pnlButtonLayout);
	}
	
	public void getFeaturesData(){
		api.featuresList(Data.targetURL, Data.sessionKey, Data.bucketID);
		
	}

}
