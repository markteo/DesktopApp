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
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APIProcess;
import customColor.CustomColor;

public class UIBucketSelect{
	Thread buckets;
	public JTable listBucket = new JTable();
	private DefaultTableModel model;
	private JFrame bucketFrame;
	public UIBucketSelect(){
		getBucketData();
		runBucketSelect();
	}
	public void runBucketSelect(){
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();
		bucketFrame = new JFrame("Bucket");		
		
		// start of ui
		bucketFrame.setLayout(new BorderLayout());

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("Bucket List");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlBucketList = p.createPanel(Layouts.flow);
		
		
		listBucket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBucket.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scrollBucket = new JScrollPane(listBucket);
		scrollBucket.setPreferredSize(new Dimension(300, 150));
		Component[] BucketListComponents = { scrollBucket };
		p.addComponentsToPanel(pnlBucketList, BucketListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnBack = b.createButton("Back");
		JButton btnSelectElements = b.createButton("Next");
		JButton btnRefresh = b.createButton("Refresh Bucket List");

		pnlButtons.add(btnBack);
		pnlButtons.add(btnSelectElements);
		pnlButtons.add(btnRefresh);

		bucketFrame.add(pnlInstruction, BorderLayout.NORTH);
		bucketFrame.add(pnlBucketList, BorderLayout.CENTER);
		bucketFrame.add(pnlButtons, BorderLayout.SOUTH);
		bucketFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bucketFrame.setVisible(true);
		bucketFrame.pack();
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
						Data.bucketID = (int) listBucket.getModel().getValueAt(selected, 0);
						bucketFrame.setVisible(false);
						Data.uiLicenseDetail = new UILicenseDetail();
						return null;
					}
				};
		
				Window win = SwingUtilities.getWindowAncestor((AbstractButton) e
						.getSource());
				final JDialog dialog = new JDialog(win, "License Details",
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
				panel.add(new JLabel("Retrieving Licenses......."), BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setLocationRelativeTo(win);
				dialog.setBounds(50,50,300,100);
				dialog.setVisible(true);					
				
			}
		});
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bucketFrame.setVisible(false);
				Data.uiInventorySelect.setFrameVisible();
			}	
		});
	}
	
	private void getBucketData(){
		
		
		JSONArray bucketList = new APIProcess().bucketList(Data.targetURL, Data.sessionKey);
		try {
			Object columnName[] = new Object[] {"Bucket ID", "Bucket Name"};
			Object[][] rowData = new Object[bucketList.length()][columnName.length];
			for (int i = 0; i < bucketList.length(); i++){
				JSONObject bucket = bucketList.getJSONObject(i);
				rowData[i][0] = bucket.get("bucketID");
				rowData[i][1] = bucket.get("bucketName");
			}	
			model = new DefaultTableModel(rowData, columnName){
				@Override
			    public boolean isCellEditable(int row, int column) {
			        return false;
			    }
			};
			listBucket.setModel(model);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void setFrameVisible(){
		bucketFrame.setVisible(true);
	}

}
