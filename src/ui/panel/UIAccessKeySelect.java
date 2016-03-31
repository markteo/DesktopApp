package ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import qrcode.JavaQR;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.table.model.UneditableModel;
import api.APIProcess;
import customColor.CustomColor;

public class UIAccessKeySelect extends JPanel {

	Thread accessKey;
	private JFrame accessKeyFrame;
	private JTable listBucket = new JTable();

	public UIAccessKeySelect() {
		runaccessKeySelect();
	}

	public void runaccessKeySelect() {
		getAccessKeyData();
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();


		// start of ui
		setLayout(new BorderLayout());

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("Access Keys");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlBucketList = p.createPanel(Layouts.flow);

		listBucket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollBucket = new JScrollPane(listBucket);
		scrollBucket.setPreferredSize(new Dimension(300, 150));
		Component[] BucketListComponents = { scrollBucket };
		p.addComponentsToPanel(pnlBucketList, BucketListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);

		JButton btnBack = b.createButton("Back");
		JButton btnSelectElements = b.createButton("Next");
		JButton btnAdd = b.createButton("Add");
		JButton btnRefresh = b.createButton("Refresh");

		pnlButtons.add(btnBack);
		pnlButtons.add(btnAdd);
		pnlButtons.add(btnRefresh);
		pnlButtons.add(btnSelectElements);
		
		add(pnlInstruction, BorderLayout.NORTH);
		add(pnlBucketList, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
		

		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Data.mainFrame.showPanel("license");
			}
		});

		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getAccessKeyData();
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Data.mainFrame.uiGenerateKey = new UIGenerateKey();
				Data.mainFrame.addPanel(Data.mainFrame.uiGenerateKey, "generateKey");
				Data.mainFrame.pack();
				Data.mainFrame.showPanel("generateKey");
			}
		});
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						int selected = listBucket.getSelectedRow();
						
						if(selected != -1){
							Data.accessKey = (String) listBucket.getModel().getValueAt(selected, 0);
							Data.mainFrame.qrGenerator = new JavaQR();
							Data.mainFrame.pack();
							Data.mainFrame.addPanel(Data.mainFrame.qrGenerator, "generateQR");
							Data.mainFrame.showPanel("generateQR");
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
				panel.add(new JLabel("Generating QR Code......."), BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setBounds(50, 50, 300, 100);
				dialog.setLocationRelativeTo(Data.mainFrame);
				dialog.setVisible(true);
			}
		});
	}

	public void getAccessKeyData() {

		JSONArray accessKeyList = new APIProcess().getUnuseAccessKey(Data.targetURL, Data.sessionKey);
		
		try {
			Object columnName[] = new Object[] { "Key", "Remaining Uses", "Expiry Date" };
			Object[][] rowData = new Object[accessKeyList.length()][columnName.length];
			
			for (int i = 0; i < accessKeyList.length(); i++) {
				JSONObject accessKey = accessKeyList.getJSONObject(i);
				rowData[i][0] = accessKey.get("key");
				rowData[i][1] = accessKey.get("remainingUses");
				rowData[i][2] = accessKey.get("expiryDate");
			}
			listBucket.setModel(new UneditableModel(rowData, columnName));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void setFrameVisible() {
		accessKeyFrame.setVisible(true);
	}

	

}
