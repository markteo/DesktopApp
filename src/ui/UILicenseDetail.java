package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import customColor.CustomColor;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;

public class UILicenseDetail {

	private String[] arrayLicense = { "1234-5678-9101", "1234-5678-9102", "1234-5678-9103" };

	private JFrame licenseDetail;

	private JPanel pnlButtons;
	private JPanel pnlDetails;
	private JPanel pnlFeatures;
	private JPanel pnlVca;
	private JPanel pnlReports;
	private JPanel pnlAdminSetting;
	private JPanel pnlNotification;

	private JButton btnNext;
	private JButton btnBack;
	private JButton btnCancel;
	
	private JComboBox<String> cbLicenses;
	
	private JList listVCA;
	private JList listReports;
	private JList listAdminSetting;
	private JList listNotification;
	
	private DefaultListModel<String> mdlVCA;
	private DefaultListModel<String> mdlReports;
	private DefaultListModel<String> mdlAdminSetting;
	private DefaultListModel<String> mdlNotification;
	
	private JLabel lblLicense;
	private JLabel lblCustomerID;
	private JLabel lblLicenseKey;
	private JLabel lblDateCreated;
	private JLabel lblNodeName;
	private JLabel lblRegistrationNo;
	private JLabel lblCloudStorage;
	private JLabel lblValidity;
	private JLabel lblMaxConcurrentVCA;
	private JLabel lblStatus;
	private JLabel lblActivated;
	private JLabel lblExpires;
	private JLabel lblCustomerIDValue;
	private JLabel lblLicenseKeyValue;
	private JLabel lblDateCreatedValue;
	private JLabel lblNodeNameValue;
	private JLabel lblRegistrationNoValue;
	private JLabel lblCloudStorageValue;
	private JLabel lblValidityValue;
	private JLabel lblMaxConcurrentVCAValue;
	private JLabel lblStatusValue;
	private JLabel lblActivatedValue;
	private JLabel lblExpiresValue;
	
	TitledBorder detailBorder = BorderFactory.createTitledBorder("Details");
	
	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public void runLicenseDetails() {
		licenseDetail = new JFrame("License Detail");
		licenseDetail.setLayout(new BorderLayout());
		licenseDetail.setBackground(CustomColor.NavyBlue.returnColor());
		
		pnlButtons = p.createPanel(Layouts.flow);
		btnNext = b.createButton("Next");
		btnBack = b.createButton("Back");
		btnCancel = b.createButton("Cancel");
		Component[] buttonList = { btnNext, btnBack, btnCancel };
		p.addComponentsToPanel(pnlButtons, buttonList);

		pnlDetails = p.createPanel(Layouts.gridbag);
		detailBorder.setTitleColor(CustomColor.LightBlue.returnColor());
		pnlDetails.setBorder(detailBorder);

		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 1;
		gc.gridy = 0;
		lblLicense = l.createLabel("License");
		pnlDetails.add(lblLicense, gc);

		gc.gridx = 2;
		gc.gridy = 0;
		cbLicenses = new JComboBox<>(arrayLicense);
		cbLicenses.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (cbLicenses.getSelectedItem().equals("1234-5678-9101")) {
					lblCustomerIDValue.setText("1");
					lblLicenseKeyValue.setText("1234-5678-9101");
					lblDateCreatedValue.setText("26/02/2016 16:55");
					lblNodeNameValue.setText("N/A");
					lblRegistrationNoValue.setText("N/A");
					lblCloudStorage.setText("1GB");
					lblValidityValue.setText("24 Months");
					lblMaxConcurrentVCAValue.setText("99");
					lblStatusValue.setText("Unused");
					lblActivatedValue.setText("N/A");
					lblExpiresValue.setText("N/A");
				}else{
					lblCustomerIDValue.setText("");
					lblLicenseKeyValue.setText("");
					lblDateCreatedValue.setText("");
					lblNodeNameValue.setText("");
					lblRegistrationNoValue.setText("");
					lblCloudStorage.setText("");
					lblValidityValue.setText("");
					lblMaxConcurrentVCAValue.setText("");
					lblStatusValue.setText("");
					lblActivatedValue.setText("");
					lblExpiresValue.setText("");
				}

			}
		});
		pnlDetails.add(cbLicenses, gc);

		createFieldLabel(gc);
		createValueLabel(gc);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		String[] arrayVCA = {"Perimeter Profiling","Face Defense","aaaa","Perimeter Profiling","Face Defense","aaaa","Perimeter Profiling","Face Defense","aaaa","Perimeter Profiling","Face Defense","aaaa"};
		listVCA = new JList(arrayVCA);
		JScrollPane spVCA = new JScrollPane(listVCA);
		spVCA.setPreferredSize(new Dimension(300, 210));
		pnlVca = p.createPanel(Layouts.grid,1,1);
		pnlVca.add(spVCA);
		tabbedPane.addTab("Video Analytics", pnlVca);
		
		String[] arrayReports = {"Report 1","Report 2","Report 3"};
		listReports = new JList(arrayReports);
		JScrollPane spReports = new JScrollPane(listReports);
		spReports.setPreferredSize(new Dimension(300, 210));
		pnlReports = p.createPanel(Layouts.grid,1,1);
		pnlReports.add(spReports);
		tabbedPane.addTab("Reports",pnlReports);
		
		String[] arraySetting = {"Backup","Delete","Logging"};
		listAdminSetting = new JList(arraySetting);
		JScrollPane spSetting = new JScrollPane(listAdminSetting);
		spSetting.setPreferredSize(new Dimension(300, 210));
		pnlAdminSetting = p.createPanel(Layouts.grid,1,1);
		pnlAdminSetting.add(listAdminSetting);
		tabbedPane.addTab("Admin Setting",pnlAdminSetting);
		
		String[] arrayNotification = {"On","Off"};
		listNotification = new JList(arrayNotification);
		JScrollPane spNotification = new JScrollPane(listNotification);
		spNotification.setPreferredSize(new Dimension(300, 210));
		pnlNotification = p.createPanel(Layouts.grid,1,1);
		pnlNotification.add(listNotification);
		tabbedPane.addTab("Notification Management", pnlNotification);
		
		licenseDetail.add(pnlButtons, BorderLayout.SOUTH);
		licenseDetail.add(tabbedPane, BorderLayout.CENTER);
		licenseDetail.add(pnlDetails, BorderLayout.NORTH);
		licenseDetail.pack();
		licenseDetail.setSize(new Dimension(500, 500));
		licenseDetail.setResizable(false);
		licenseDetail.setVisible(true);
		licenseDetail.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void createFieldLabel(GridBagConstraints gc) {
		lblCustomerID = l.createLabel("Customer ID");
		lblActivated = l.createLabel("Activated On");
		lblCloudStorage = l.createLabel("Cloud Storage");
		lblDateCreated = l.createLabel("Date Created");
		lblExpires = l.createLabel("Expires on");
		lblLicenseKey = l.createLabel("License Key");
		lblNodeName = l.createLabel("Node Name");
		lblRegistrationNo = l.createLabel("Registration Number");
		lblStatus = l.createLabel("Status");
		lblValidity = l.createLabel("Validity");
		lblMaxConcurrentVCA = l.createLabel("Max Concurrent. VCA");

		gc.gridx = 1;
		gc.gridy = 5;
		pnlDetails.add(lblCustomerID, gc);

		gc.gridx = 1;
		gc.gridy = 10;
		pnlDetails.add(lblLicenseKey, gc);

		gc.gridx = 1;
		gc.gridy = 15;
		pnlDetails.add(lblDateCreated, gc);

		gc.gridx = 1;
		gc.gridy = 20;
		pnlDetails.add(lblNodeName, gc);

		gc.gridx = 1;
		gc.gridy = 25;
		pnlDetails.add(lblRegistrationNo, gc);

		gc.gridx = 3;
		gc.gridy = 5;
		pnlDetails.add(lblCloudStorage, gc);

		gc.gridx = 3;
		gc.gridy = 10;
		pnlDetails.add(lblValidity, gc);

		gc.gridx = 3;
		gc.gridy = 15;
		pnlDetails.add(lblMaxConcurrentVCA, gc);

		gc.gridx = 3;
		gc.gridy = 20;
		pnlDetails.add(lblStatus, gc);

		gc.gridx = 3;
		gc.gridy = 25;
		pnlDetails.add(lblActivated, gc);

		gc.gridx = 3;
		gc.gridy = 30;
		pnlDetails.add(lblExpires, gc);
	}

	public void createValueLabel(GridBagConstraints gc) {
		// value label
		lblCustomerIDValue = l.createResultLabel("");
		lblLicenseKeyValue = l.createResultLabel("");
		lblDateCreatedValue = l.createResultLabel("");
		lblNodeNameValue = l.createResultLabel("test");
		lblRegistrationNoValue = l.createResultLabel("");

		lblCloudStorageValue = l.createResultLabel("");
		lblValidityValue = l.createResultLabel("");
		lblMaxConcurrentVCAValue = l.createResultLabel("");
		lblStatusValue = l.createResultLabel("");
		lblActivatedValue = l.createResultLabel("");
		lblExpiresValue = l.createResultLabel("");

		gc.gridx = 2;
		gc.gridy = 5;
		pnlDetails.add(lblCustomerIDValue, gc);

		gc.gridx = 2;
		gc.gridy = 10;
		pnlDetails.add(lblLicenseKeyValue, gc);

		gc.gridx = 2;
		gc.gridy = 15;
		pnlDetails.add(lblDateCreatedValue, gc);

		gc.gridx = 2;
		gc.gridy = 20;
		pnlDetails.add(lblNodeNameValue, gc);

		gc.gridx = 2;
		gc.gridy = 25;
		pnlDetails.add(lblRegistrationNoValue, gc);

		gc.gridx = 4;
		gc.gridy = 5;
		pnlDetails.add(lblCloudStorageValue, gc);

		gc.gridx = 4;
		gc.gridy = 10;
		pnlDetails.add(lblValidityValue, gc);

		gc.gridx = 4;
		gc.gridy = 15;
		pnlDetails.add(lblMaxConcurrentVCAValue, gc);

		gc.gridx = 4;
		gc.gridy = 20;
		pnlDetails.add(lblStatusValue, gc);

		gc.gridx = 4;
		gc.gridy = 25;
		pnlDetails.add(lblActivatedValue, gc);

		gc.gridx = 4;
		gc.gridy = 30;
		pnlDetails.add(lblExpiresValue, gc);
	}
}
