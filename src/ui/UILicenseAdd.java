package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.FeatureDescriptor;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;

import main.Data;
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
	private JScrollPane scrollFeature;
	private APIProcess api = new APIProcess();
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
		initApiServicePanel();
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
		// Feature Category
		CheckBoxList cblFeaturesCategory = new CheckBoxList();
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
						for(int i = 0; i < featureArray.length(); i ++){
							try {
								mdlFeature.addElement(featureArray.getJSONObject(i).getString("name"));
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
						}
					}else{
						
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
		listFeature.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
								
			}
		});
		listFeature.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollFeature = new JScrollPane(listFeature);
		Component[] arrayFeature = { lblFeature, scrollFeature };

		p.addComponentsToPanel(pnlFeatureLayout, arrayFeatureCategory);
		p.addComponentsToPanel(pnlFeatureLayout, arrayFeature);

		pnlFeature.add(pnlFeatureLayout);
	}

	private void initApiServicePanel(){
		JPanel pnlApiSvcLayout = p.createPanel(Layouts.grid,1,1);
		JLabel lblServiceUsed = l.createLabel("Service APIs Used");
		DefaultListModel<String> mdlSvcUsed = new DefaultListModel<>();
		JList listSvcUsed = new JList(mdlSvcUsed);
		JScrollPane scrollSvcUsed = new JScrollPane(listSvcUsed);
		scrollSvcUsed.setPreferredSize(new Dimension(300,300));
		Component[] arraySvcUsedComp = {lblServiceUsed,scrollSvcUsed};
		p.addComponentsToPanel(pnlApiSvcLayout, arraySvcUsedComp);
		pnlApiService.add(pnlApiSvcLayout);
	}
	
	private void initBtnPanel(){
		JPanel pnlButtonLayout = p.createPanel(Layouts.flow);
		pnlButtonLayout.setAlignmentY(FlowLayout.CENTER);
		JButton btnSubmit = b.createButton("Submit");
		JButton btnCancel = b.createButton("Cancel");
		btnSubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Submitted");
			}
		});
		
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Cancel");
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
