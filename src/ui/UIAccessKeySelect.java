package ui;

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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import qrcode.JavaQR;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APIProcess;
import customColor.CustomColor;

public class UIAccessKeySelect{

	Thread accessKey;
	public static DefaultListModel<String> model = new DefaultListModel<String>();
	private JFrame accessKeyFrame;

	public UIAccessKeySelect() {
		runaccessKeySelect();

	}

	public void runaccessKeySelect() {
		getAccessKeyData();
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();
		accessKeyFrame = new JFrame("Bucket");

		JList listBucket = new JList(model);
		
		// start of ui
		accessKeyFrame.setLayout(new BorderLayout());
		

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l
				.createLabel("Access Keys");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlBucketList = p.createPanel(Layouts.flow);
		JLabel lblBucketList = l
				.createLabel("Access Keys List : \n  (Access Key, Remaining Uses, Expiry Date)");

		listBucket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollBucket = new JScrollPane(listBucket);
		scrollBucket.setPreferredSize(new Dimension(300, 150));
		Component[] BucketListComponents = { lblBucketList, scrollBucket };
		p.addComponentsToPanel(pnlBucketList, BucketListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		
		JButton btnBack = b.createButton("Back");
		JButton btnSelectElements = b.createButton("Next");
		JButton btnAdd = b.createButton("Add");
		JButton btnRefresh = b.createButton("Refresh");
	

		pnlButtons.add(btnBack);
		pnlButtons.add(btnSelectElements);
		pnlButtons.add(btnAdd);
		pnlButtons.add(btnRefresh);

		accessKeyFrame.add(pnlInstruction, BorderLayout.NORTH);
		accessKeyFrame.add(pnlBucketList, BorderLayout.CENTER);
		accessKeyFrame.add(pnlButtons, BorderLayout.SOUTH);
		accessKeyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		accessKeyFrame.pack();
		accessKeyFrame.setVisible(true);
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// add Bucket code here
				// open add frame and close current frame.
				accessKeyFrame.setVisible(false);
				Data.uiLicenseDetail.setFrameVisible();
			}
		});
		
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getAccessKeyData();
				listBucket.setModel(model);
				
			}
		});
		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Data.uiGenerateKey = new UIGenerateKey();
				accessKeyFrame.setVisible(false);
			}
		});
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						String itemSelected = listBucket.getModel()
								.getElementAt(listBucket.getSelectedIndex()).toString();
						String[] itemData = itemSelected.split("\\,");
						Data.accessKey = itemData[0].trim();
						accessKeyFrame.setVisible(false);
						Data.qrGenerator = new JavaQR();
						return null;
					}
				};
		
				Window win = SwingUtilities.getWindowAncestor((AbstractButton) e
						.getSource());
				final JDialog dialog = new JDialog(win, "Dialog",
						ModalityType.APPLICATION_MODAL);
		
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
				dialog.setLocationRelativeTo(win);
				dialog.setBounds(50,50,300,100);
				dialog.setVisible(true);
				
				
			}
		});
	}

	private static void getAccessKeyData() {

		JSONArray accessKeyList = new APIProcess().getUnuseAccessKey(Data.targetURL, Data.sessionKey);
		model = new DefaultListModel<String>();
		try {

			for (int i = 0; i < accessKeyList.length(); i++) {
				JSONObject accessKey = accessKeyList.getJSONObject(i);
				model.addElement(accessKey.get("key") + " , "
						+ accessKey.get("remainingUses") + " , " + accessKey.get("expiryDate"));
			}

			System.out.println(" model data finish");

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void setFrameVisible() {
		accessKeyFrame.setVisible(true);
	}

}
