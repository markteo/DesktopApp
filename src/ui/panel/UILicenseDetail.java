package ui.panel;

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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import customColor.CustomColor;
import customColor.CustomColor;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.jtree.CheckTreeManager;
import ui.components.table.model.UneditableModel;

public class UILicenseDetail extends JPanel {


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

	public UILicenseDetail() {
		runLicenseDetails();
	}

	public void runLicenseDetails() {

		setLayout(new BorderLayout());

		pnlDetails = createDetailsPnl();
		pnlFeatures = createPnlFeature();
		pnlButtons = createButtonPanel();
		add(pnlDetails, BorderLayout.NORTH);
		add(pnlFeatures, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
		setBackground(CustomColor.NavyBlue.returnColor());
	}

	public DefaultMutableTreeNode createNode(String name) {
		return new DefaultMutableTreeNode(name);
	}

	private JPanel createDetailsPnl() {
		JPanel panel = p.createPanel(Layouts.flow);
		panel.setLayout(new BorderLayout());
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

		panel.add(spTable,BorderLayout.CENTER);
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
		g.insets = new Insets(5, 5, 5, 5);
		panel.add(spTreeCheckBox, g);

		model = new DefaultListModel<>();
		model.addElement("");
		listServiceUsed = new JList<String>(model);
		spList = new JScrollPane(listServiceUsed);
		spList.setPreferredSize(new Dimension(300, 300));
		g.weightx=1;
		g.gridx = 4;
		g.gridy = 1;
		g.gridwidth = 3;
		panel.add(spList, g);

		return panel;
	}

	public JPanel createButtonPanel() {
		JPanel panel = p.createPanel(Layouts.flow);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));

		btnSubmit = b.createButton("Submit");
		btnCancel = b.createButton("Cancel");
		btnAdd = b.createButton("Add");

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
				setVisible(false);
				UILicenseAdd uiLicenseAdd = new UILicenseAdd();
				uiLicenseAdd.runTestJCheckBox();
				// uiLicenseAdd.runLicenseAdd();
				uiLicenseAdd.runTestJCheckBox();

			}
		});

		panel.add(btnSubmit);
		panel.add(btnCancel);
		panel.add(btnAdd);

		return panel;
	}

	public void setFrameVisible() {
		setVisible(true);
	}
}
