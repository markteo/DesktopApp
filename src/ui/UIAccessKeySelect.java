package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

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
		accessKeyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		accessKeyFrame.setLayout(new BorderLayout());
		accessKeyFrame.setVisible(true);

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l
				.createLabel("The List shows the all the AccessKeys");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlBucketList = p.createPanel(Layouts.flow);
		JLabel lblBucketList = l
				.createLabel("Access Keys List : \n  (Key, Remaining Uses, Expiry Date)");

		listBucket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollBucket = new JScrollPane(listBucket);
		scrollBucket.setPreferredSize(new Dimension(300, 150));
		Component[] BucketListComponents = { lblBucketList, scrollBucket };
		p.addComponentsToPanel(pnlBucketList, BucketListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		
		JButton btnBack = b.createButton("Back");

		// Button events
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// add Bucket code here
				// open add frame and close current frame.
				accessKeyFrame.setVisible(false);
				Data.uiLicenseSelect.setFrameVisible();
			}
		});

		JButton btnSelectElements = b.createButton("Next");
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something with selected Bucket
				String itemSelected = listBucket.getModel()
						.getElementAt(listBucket.getSelectedIndex()).toString();
				String[] itemData = itemSelected.split("\\,");
				Data.accessKey = itemData[0].trim();
				accessKeyFrame.setVisible(false);
				
			}
		});

		JButton btnAdd = b.createButton("Add");
		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Data.uiGenerateKey = new UIGenerateKey();
				accessKeyFrame.setVisible(false);
			}
		});

		pnlButtons.add(btnBack);
		pnlButtons.add(btnSelectElements);
		pnlButtons.add(btnAdd);

		accessKeyFrame.add(pnlInstruction, BorderLayout.NORTH);
		accessKeyFrame.add(pnlBucketList, BorderLayout.CENTER);
		accessKeyFrame.add(pnlButtons, BorderLayout.SOUTH);
		accessKeyFrame.pack();
	}

	private static void getAccessKeyData() {

		JSONArray accessKeyList = new APIProcess().getUnuseAccessKey(Data.targetURL, Data.sessionKey);
		model = new DefaultListModel<String>();
		try {
			model.addElement("Access Key , Remaining Uses, Expiry Date");

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
