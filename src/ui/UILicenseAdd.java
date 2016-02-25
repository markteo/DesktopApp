package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

import ui.components.Button;
import ui.components.CheckBoxList;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;

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
	
	public void runLicenseAdd(){
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
		JLabel lblCustomerID = l.createLabel("Customer ID:");
		JLabel lblCloudStorage = l.createLabel("Cloud Storage:");
		JLabel lblPeriod = l.createLabel("Validity Period:");
		JLabel lblConcurrentVCA = l.createLabel("Maximum concurrent VCA:");

		JComboBox ddCustomerId = new JComboBox();
		String[] arrayCustomerId = {};
		for (int i = 0; i < arrayCustomerId.length; i++) {
			ddCustomerId.addItem(arrayCustomerId[i]);
		}

		SpinnerModel smStorage = new SpinnerNumberModel(1, 1, 1, 1);
		SpinnerModel smPeriod = new SpinnerNumberModel(1, 1, 24, 1);
		SpinnerModel smVca = new SpinnerNumberModel(1, 1, 4, 1);

		JSpinner spinnerStorage = new JSpinner(smStorage);
		JSpinner spinnerPeriod = new JSpinner(smPeriod);
		JSpinner spinnerVca = new JSpinner(smVca);

		Component[] listSettingComponent = { lblCustomerID, ddCustomerId, lblCloudStorage, spinnerStorage, lblPeriod,
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
		JCheckBox element1 = new JCheckBox("Element 1");
		JCheckBox element2 = new JCheckBox("Element 2");

		element1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String[] arrayName = { "Test 1", "Test 2", "Test 3", "Test 4" };
				if (element1.isSelected()) {
					for (String n : arrayName)
						mdlFeature.addElement(n);
				} else {
					for (String n : arrayName)
						mdlFeature.removeElement(n);
				}
			}
		});
		element2.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (element2.isSelected()) {
					mdlFeature.addElement("Inner Element 2");
				} else {
					mdlFeature.removeElement("Inner Element 2");
				}
			}
		});

		JCheckBox[] arrayCheckBox = new JCheckBox[] { element1, element2 };
		cblFeaturesCategory.setListData(arrayCheckBox);
		JScrollPane scrollFeaturesCategory = new JScrollPane(cblFeaturesCategory);
		Component[] arrayFeatureCategory = { lblFeatureCategory, scrollFeaturesCategory };

		// Feature
		JLabel lblFeature = l.createLabel("Features:");
		mdlFeature = new DefaultListModel<>();
		JList listFeature = new JList(mdlFeature);
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

}
