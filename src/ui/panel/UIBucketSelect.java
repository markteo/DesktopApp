package ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.APIProcess;
import customColor.CustomColor;
import main.Data;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.table.model.UneditableModel;

public class UIBucketSelect extends JPanel {
	Thread buckets;
	public JTable listBucket = new JTable();
	private DefaultTableModel model;

	public UIBucketSelect() {
		getBucketData();
		runBucketSelect();
	}

	public void runBucketSelect() {
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();

		// start of ui
		setLayout(new BorderLayout());

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("Bucket List");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlBucketList = p.createPanel(Layouts.border);

		listBucket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollBucket = new JScrollPane(listBucket);
		scrollBucket.setPreferredSize(new Dimension(300, 150));
		pnlBucketList.add(scrollBucket, BorderLayout.CENTER);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnBack = b.createButton("Back");
		JButton btnSelectElements = b.createButton("Next");
		JButton btnRefresh = b.createButton("Refresh Bucket List");

		pnlButtons.add(btnBack);
		pnlButtons.add(btnRefresh);
		pnlButtons.add(btnSelectElements);


		add(pnlInstruction, BorderLayout.NORTH);
		add(pnlBucketList, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
		setVisible(true);

		btnRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getBucketData();
			}
		});
		btnSelectElements.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {

						int selected = listBucket.getSelectedRow();
						if (selected == -1) {
							JOptionPane.showMessageDialog(Data.mainFrame, "Please Select a Bucket", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							Data.bucketID = (int) listBucket.getModel().getValueAt(selected, 0);
							Data.mainFrame.addPanel(Data.mainFrame.uiLicenseDetail = new UILicenseDetail(), "license");
							Data.mainFrame.pack();
							Data.mainFrame.showPanel("license");
						}
						return null;
					}
				};

				Window win = SwingUtilities.getWindowAncestor((AbstractButton) e.getSource());
				final JDialog dialog = new JDialog(win, "Loading", ModalityType.APPLICATION_MODAL);

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
				panel.add(new JLabel("Retrieving Licenses......."), BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setBounds(50, 50, 300, 100);
				dialog.setLocationRelativeTo(Data.mainFrame);
				dialog.setVisible(true);

				// do something with selected Bucket

			}
		});
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// add Bucket code here
				// open add frame and close current frame.
				setVisible(false);
				Data.mainFrame.showPanel("inventory");
			}
		});
	}

	private void getBucketData() {

		JSONArray bucketList = new APIProcess().bucketList(Data.targetURL, Data.sessionKey);
		try {
			Object columnName[] = new Object[] { "Bucket ID", "Bucket Name" };
			Object[][] rowData = new Object[bucketList.length()][columnName.length];
			for (int i = 0; i < bucketList.length(); i++) {
				JSONObject bucket = bucketList.getJSONObject(i);
				rowData[i][0] = bucket.get("bucketID");
				rowData[i][1] = bucket.get("bucketName");
			}
			listBucket.setModel(new UneditableModel(rowData, columnName));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void setPanelVisible() {
		setVisible(true);
	}

}
