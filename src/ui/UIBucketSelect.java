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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.Data;

import org.json.JSONArray;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APIProcess;
import customColor.CustomColor;

public class UIBucketSelect {
	
	public static void runBucketSelect(){
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();

		DefaultListModel<String> model = new DefaultListModel<String>();
		
		JSONArray bucketList = new APIProcess().bucketList(Data.targetURL, Data.sessionKey);
		
		// start of ui
		JFrame bucketFrame = new JFrame("Bucket");
		bucketFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		bucketFrame.setLayout(new BorderLayout());
		bucketFrame.setVisible(true);

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("The Bucket List show the currently unactivated  Nodes");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlBucketList = p.createPanel(Layouts.flow);
		JLabel lblBucketList = l.createLabel("Bucket List : \n  (ID, Registration Number, MAC Address)");
		JList listBucket = new JList(model);
		listBucket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollBucket = new JScrollPane(listBucket);
		scrollBucket.setPreferredSize(new Dimension(300, 150));
		Component[] BucketListComponents = { lblBucketList, scrollBucket };
		p.addComponentsToPanel(pnlBucketList, BucketListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnAddElements = b.createButton("Add Item");

		// Button events
		btnAddElements.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// add Bucket code here
				// open add frame and close current frame.
				bucketFrame.setVisible(false);
			}
		});

		JButton btnSelectElements = b.createButton("Next");
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something with selected Bucket
				
			}
		});

		pnlButtons.add(btnAddElements);
		pnlButtons.add(btnSelectElements);

		bucketFrame.add(pnlInstruction, BorderLayout.NORTH);
		bucketFrame.add(pnlBucketList, BorderLayout.CENTER);
		bucketFrame.add(pnlButtons, BorderLayout.SOUTH);
		bucketFrame.pack();
	}

}
