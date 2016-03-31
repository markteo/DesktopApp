package ui.panel;

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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.APIProcess;
import customColor.CustomColor;
import customColor.CustomColor;
import main.Data;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.jtree.CheckTreeManager;
import ui.components.table.model.UneditableModel;

public class UILicenseDetail extends JPanel {
	private String[] arrayLicense = new String[] {};
	private JPanel pnlButtons;
	private JPanel pnlDetails;
	private JPanel pnlFeatures;

	private JTree tree;
	private JTable tblInfo = new JTable();

	private JScrollPane spTreeCheckBox;
	private JScrollPane spList;
	private JScrollPane spTable;

	private DefaultTableModel model;
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
	private JButton btnBack;
	private JButton btnAdd;

	private DefaultMutableTreeNode root;

	private APIProcess api = new APIProcess();

	private CheckTreeManager checkTreeManager;

	private final SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public UILicenseDetail() {
		getFeaturesData();
		System.out.println("Features Data");
		getLicenseData();
		System.out.println("License Data");
		setLayout(new BorderLayout());

		pnlDetails = createDetailsPnl();
		pnlFeatures = createPnlFeature();
		pnlButtons = createButtonPanel();
		add(pnlDetails, BorderLayout.NORTH);
		add(pnlFeatures, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
		setBackground(CustomColor.NavyBlue.returnColor());
		setVisible(true);
	}

	public DefaultMutableTreeNode createNode(String name) {
		return new DefaultMutableTreeNode(name);
	}

	private JPanel createDetailsPnl() {
		JPanel panel = p.createPanel(Layouts.flow);
		panel.setLayout(new BorderLayout());

		tblInfo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblInfo.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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

		panel.add(spTable, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createPnlFeature() {
		JPanel panel = p.createPanel(Layouts.gridbag);

		GridBagConstraints g = new GridBagConstraints();
		g.weightx = 0.25;
		g.weighty = 0.25;
		g.fill = g.BOTH;
		g.anchor = g.CENTER;

		lblSelectFeature = l.createLabel("Select Features", SwingConstants.LEFT);
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 2;
		g.insets = new Insets(5, 5, 5, 5);
		panel.add(lblSelectFeature, g);

		tree = new JTree(root);
		spTreeCheckBox = new JScrollPane(tree);
		spTreeCheckBox.setPreferredSize(new Dimension(300, 300));
		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 3;
		g.insets = new Insets(5, 5, 5, 5);
		panel.add(spTreeCheckBox, g);

		return panel;
	}

	public JPanel createButtonPanel() {
		JPanel panel = p.createPanel(Layouts.flow);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		btnSubmit = b.createButton("Next");
		btnBack = b.createButton("Back");
		btnAdd = b.createButton("Add");
		
		panel.add(btnBack);
		panel.add(btnAdd);
		panel.add(btnSubmit);
		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Data.mainFrame.showPanel("bucket");
			}
		});

		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Data.mainFrame.addPanel(Data.mainFrame.uiLicenseAdd = new UILicenseAdd(), "licenseAdd");
				Data.mainFrame.showPanel("licenseAdd");
			}
		});
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						int row = tblInfo.getSelectedRow();
						if (row == -1) {
							JOptionPane.showMessageDialog(Data.mainFrame, "Please Select a License", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							Data.licenseNumber = (String) tblInfo.getModel().getValueAt(row, 0);
							Data.mainFrame.uiAccessKeySelect = new UIAccessKeySelect();
							Data.mainFrame.addPanel(Data.mainFrame.uiAccessKeySelect, "access");
							Data.mainFrame.showPanel("access");
						}

						return null;
					}
				};

				Window win = SwingUtilities.getWindowAncestor((AbstractButton) e.getSource());
				final JDialog dialog = new JDialog(win, "Dialog", ModalityType.APPLICATION_MODAL);

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
				panel.add(new JLabel("Retrieving Access Keys"), BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setBounds(50, 50, 300, 100);
				dialog.setLocationRelativeTo(Data.mainFrame);
				dialog.setVisible(true);

			}

		});

		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
				Data.mainFrame.showPanel("bucket");
			}
		});

		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Data.mainFrame.addPanel(Data.mainFrame.uiLicenseAdd = new UILicenseAdd(), "licenseAdd");
				Data.mainFrame.pack();
				Data.mainFrame.showPanel("licenseAdd");
			}
		});

		

		return panel;
	}

	public void getData(String licenseNumber) {
		root = new DefaultMutableTreeNode("root");

		try {
			JSONObject response = api.getNodeLicenseDetails(Data.targetURL, Data.sessionKey, Data.bucketID,
					licenseNumber);
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
							if (feature.equals(featureArray.getJSONObject(x).getString("name"))) {
								DefaultMutableTreeNode featureElement = new DefaultMutableTreeNode(
										featureArray.getJSONObject(x).getString("name"));
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

	private void getLicenseData() {
		ArrayList<String> arrayLicenses = new ArrayList<String>();
		JSONArray licenseList = new APIProcess().nodeLicenseList(Data.targetURL, Data.sessionKey, Data.bucketID);
		try {
			String[] columnNames = { "License Number", "Validity", "Storage", "Date Created", "Maximum VCA",
					"Bucket Name" };
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
				JSONObject response = api.getNodeLicenseDetails(Data.targetURL, Data.sessionKey, Data.bucketID,
						licenseAdd);
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
			tblInfo.setModel(new UneditableModel(rowData, columnNames));
			arrayLicense = arrayLicenses.toArray(arrayLicense);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
