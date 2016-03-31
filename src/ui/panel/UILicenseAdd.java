package ui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.jtree.CheckTreeManager;
import api.APICall;
import api.APIProcess;
import customColor.CustomColor;

public class UILicenseAdd extends JPanel {
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
	private HashMap<String, DefaultMutableTreeNode> checkFeatures = new HashMap<String, DefaultMutableTreeNode>();
	private APICall apiCall = new APICall();

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public UILicenseAdd() {
		setBackground(CustomColor.NavyBlue.returnColor());
		setLayout(new BorderLayout());

		pnlSetting = createSettingPnl();
		pnlFeatures = createPnlFeature();
		pnlButtons = createButtonPanel();
		add(pnlSetting, BorderLayout.NORTH);
		add(pnlFeatures, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
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
		cbPerpetual.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbPerpetual.isSelected()) {
					spnValidity.setEnabled(false);
				} else {
					spnValidity.setEnabled(true);
				}
			}
		});
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

		for (String key : Data.featureList.keySet()) {

			JSONArray featureArray = Data.featureList.get(key);
			DefaultMutableTreeNode element = new DefaultMutableTreeNode(key);
			ArrayList<DefaultMutableTreeNode> arrayFeatureCheckBox = new ArrayList<DefaultMutableTreeNode>();

			for (int i = 0; i < featureArray.length(); i++) {
				try {
					DefaultMutableTreeNode featureElement = new DefaultMutableTreeNode(
							featureArray.getJSONObject(i).getString("name"));
					element.add(featureElement);
					arrayFeatureCheckBox.add(featureElement);

				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
			root.add(element);

		}

		tree = new JTree(root);
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = tree.getSelectionModel().getSelectionPath();
				model.removeAllElements();
				String selected = tree.getSelectionModel().getSelectionPath().getLastPathComponent().toString();
				servicesList = new HashMap<String, String>();
				try {
					if (selected.equalsIgnoreCase("root")) {
						for (String key : Data.featureList.keySet()) {
							JSONArray featureArray = Data.featureList.get(key);

							for (int i = 0; i < featureArray.length(); i++) {
								JSONArray servicesArray = featureArray.getJSONObject(i).getJSONArray("services");

								for (int x = 0; x < servicesArray.length(); x++) {
									servicesList.put(Integer.toString(servicesArray.getJSONObject(x).getInt("id")),
											servicesArray.getJSONObject(x).getString("name"));
								}
							}
						}
					} else if (path.getPathCount() == 2) {
						for (String key : Data.featureList.keySet()) {
							if (key.equals(selected)) {
								JSONArray featureArray = Data.featureList.get(key);

								for (int i = 0; i < featureArray.length(); i++) {
									JSONArray servicesArray = featureArray.getJSONObject(i).getJSONArray("services");

									for (int x = 0; x < servicesArray.length(); x++) {
										servicesList.put(Integer.toString(servicesArray.getJSONObject(x).getInt("id")),
												servicesArray.getJSONObject(x).getString("name"));
									}
								}
							} else {
								continue;
							}
						}
					} else {
						for (String key : Data.featureList.keySet()) {
							if (key.equals(path.getPathComponent(1).toString())) {
								JSONArray featureArray = Data.featureList.get(key);

								for (int i = 0; i < featureArray.length(); i++) {
									if (featureArray.getJSONObject(i).getString("name").equals(selected)) {
										JSONArray servicesArray = featureArray.getJSONObject(i)
												.getJSONArray("services");

										for (int x = 0; x < servicesArray.length(); x++) {
											servicesList.put(
													Integer.toString(servicesArray.getJSONObject(x).getInt("id")),
													servicesArray.getJSONObject(x).getString("name"));
										}
									} else {
										continue;
									}
								}
							} else {
								continue;
							}
						}
					}
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
				for (String serviceKey : servicesList.keySet()) {
					model.addElement(servicesList.get(serviceKey));
				}
				listServiceUsed.setModel(model);
			}
		});
		checkTreeManager = new CheckTreeManager(tree);

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
				TreePath[] path = checkTreeManager.getSelectionModel().getSelectionPaths();
				ArrayList<String> featureL = new ArrayList<String>();
				String[] features = new String[] {};
				// listSelected.
				for (TreePath tp : path) {
					System.out.println(tp);

					if (tp.getLastPathComponent().toString().equals("root")) {
						Object rootNode = tree.getModel().getRoot();
						int parentCount = tree.getModel().getChildCount(rootNode);
						for (int i = 0; i < parentCount; i++) {
							Object parentNode = tree.getModel().getChild(rootNode, i);
							int childrenCount = tree.getModel().getChildCount(parentNode);

							for (int x = 0; x < childrenCount; x++) {
								featureL.add(tree.getModel().getChild(parentNode, x).toString());
							}
						}
					} else if (tp.getPathCount() == 2) {
						Object rootNode = tree.getModel().getRoot();
						int parentCount = tree.getModel().getChildCount(rootNode);
						for (int i = 0; i < parentCount; i++) {
							Object parentNode = tree.getModel().getChild(rootNode, i);
							if (parentNode.toString().equals(tp.getLastPathComponent().toString())) {

								int childrenCount = tree.getModel().getChildCount(parentNode);

								for (int x = 0; x < childrenCount; x++) {
									featureL.add(tree.getModel().getChild(parentNode, x).toString());
								}
							}
						}
					} else if (tp.getPathCount() == 3) {
						featureL.add(tp.getLastPathComponent().toString());
					}

				}
				features = featureL.toArray(features);
				String duration = spnValidity.getValue().toString();
				if (cbPerpetual.isSelected()) {
					duration = "-1";
				}
				String storage = spnCloud.getValue().toString();
				String maxVCA = spnConcurrentVCA.getValue().toString();
				String response = apiCall.addNodeLicense(Data.targetURL, Data.sessionKey, Data.bucketID, features,
						duration, storage, maxVCA);

				try {
					JSONObject responseObject = new JSONObject(response);
					if (responseObject.get("result").equals("ok")) {
						Data.mainFrame.showPanel("license");
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Data.mainFrame.showPanel("license");
			}
		});

		panel.add(btnSubmit);
		panel.add(btnCancel);

		return panel;
	}

	public void getFeaturesData() {
		api.featuresList(Data.targetURL, Data.sessionKey, Data.bucketID);
	}

}
