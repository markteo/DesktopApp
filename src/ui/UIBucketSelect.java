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

public class UIBucketSelect implements Runnable{
	Thread buckets;
	public static DefaultListModel<String> model = new DefaultListModel<String>();
	public UIBucketSelect(){
		buckets = new Thread(this);
		runBucketSelect();
		
	}
	public static void runBucketSelect(){
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();
		JFrame bucketFrame = new JFrame("Bucket");
		
		JList listBucket = new JList(model);
		
		System.out.println("Hello");
//		synchronized(model){
//            try{
//                System.out.println("Waiting for b to complete...");
//                model.wait();
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
// 
//           
//        }
		model.addElement("ID, BucketName");
		
		
		// start of ui
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
				UIInventorySelect inventorySelect = new UIInventorySelect();
				inventorySelect.runInventorySelect();
			}
		});

		JButton btnSelectElements = b.createButton("Next");
		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do something with selected Bucket
				
			}
		});

		pnlButtons.add(btnBack);
		pnlButtons.add(btnSelectElements);

		bucketFrame.add(pnlInstruction, BorderLayout.NORTH);
		bucketFrame.add(pnlBucketList, BorderLayout.CENTER);
		bucketFrame.add(pnlButtons, BorderLayout.SOUTH);
		bucketFrame.pack();
	}
	
	private static void getBucketData(){
		
		
		JSONArray bucketList = new APIProcess().bucketList(Data.targetURL, Data.sessionKey);
		System.out.println("Got entire bucket List");
		try {
			for (int i = 0; i < bucketList.length(); i++){
				JSONObject bucket = bucketList.getJSONObject(i);
				System.out.println(bucket.get("id").toString() + bucket.get("bucketName").toString());
				model.addElement(bucket.get("id") + " , " + bucket.get("bucketName"));
			}
			
			
			
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		getBucketData();
		System.out.println(Thread.currentThread().getName());
		Thread.yield();
	}

}
