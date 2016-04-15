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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.APICall;
import api.APIProcess;
import customColor.CustomColor;
import main.Data;
import ui.panel.UIBucketSelect;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.table.model.UneditableModel;
import ui.frame.UIFileUploadHTTP;

public class UIInventorySelect extends JPanel {
	public static APICall api = new APICall();

	private JPanel pnlInstruction;
	private JPanel pnlButton;

	private JLabel lblInstruction;

	private JButton btnAddElements;
	private JButton btnSelectElements;

	private DefaultTableModel model;

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	private JTable listInventory = new JTable();

	public UIInventorySelect() {
		setBackground(CustomColor.NavyBlue.returnColor());
		setLayout(new BorderLayout());

		getInventoryData();
		listInventory.setShowHorizontalLines(false);
		listInventory.setAutoCreateRowSorter(true);

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("Inventory List");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlInventoryList = p.createPanel(Layouts.border);
		listInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listInventory.setOpaque(false);
		JScrollPane scrollInventory = new JScrollPane(listInventory);
		scrollInventory.setPreferredSize(new Dimension(400, 200));
		pnlInventoryList.add(scrollInventory, BorderLayout.CENTER);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnAddElements = b.createButton("Add Inventory Item");
		JButton btnSelectElements = b.createButton("Next");

		pnlButtons.add(btnAddElements);
		pnlButtons.add(btnSelectElements);

		add(pnlInstruction, BorderLayout.NORTH);
		add(pnlInventoryList, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);

		setVisible(true);

		btnAddElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						if(Data.mainFrame.uiFileUpload == null){
							Data.mainFrame.uiFileUpload = new UIFileUploadHTTP();
							Data.mainFrame.uiFileUpload.pack();
						}else{
							Data.mainFrame.uiFileUpload.setVisible(true);
						}
						
						return null;
					}
				};

				Window win = SwingUtilities
						.getWindowAncestor((AbstractButton) e.getSource());
				final JDialog dialog = new JDialog(win, "Loading",
						ModalityType.APPLICATION_MODAL);

				mySwingWorker
						.addPropertyChangeListener(new PropertyChangeListener() {

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
				panel.add(new JLabel("Loading......."),
						BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setBounds(50, 50, 300, 100);
				dialog.setLocationRelativeTo(Data.mainFrame);
				dialog.setVisible(true);

			}
		});

		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {

						int selected = listInventory.getSelectedRow();
						if (selected == -1) {
							JOptionPane.showMessageDialog(Data.mainFrame,
									"Please Select an Inventory Item", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							String status = (String) listInventory.getModel().getValueAt(selected, 3);
							if(status.equals("Registered")){
								JOptionPane.showMessageDialog(Data.mainFrame,
										"Select an Unregistered Inventory Item", "Error",
										JOptionPane.ERROR_MESSAGE);
							}else{
								Data.registrationNumber = (String) listInventory
										.getModel().getValueAt(selected, 1);

								if (Data.mainFrame.uiBucketSelect != null) {
									Data.mainFrame.showPanel("bucket");
								} else {
									Data.mainFrame
											.addPanel(
													Data.mainFrame.uiBucketSelect = new ui.panel.UIBucketSelect(),
													"bucket");
									Data.mainFrame.showPanel("bucket");
								}
							}
						}
						return null;
					}
				};

				Window win = SwingUtilities
						.getWindowAncestor((AbstractButton) e.getSource());
				final JDialog dialog = new JDialog(win, "Loading",
						ModalityType.APPLICATION_MODAL);

				mySwingWorker
						.addPropertyChangeListener(new PropertyChangeListener() {

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
				panel.add(new JLabel("Getting Buckets......."),
						BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setBounds(50, 50, 300, 100);
				dialog.setLocationRelativeTo(Data.mainFrame);
				dialog.setVisible(true);

			}
		});

	}

	public void getInventoryData() {
		JSONObject inventoryList = new APIProcess().inventoryList(
				Data.targetURL, Data.sessionKey);

		try {
			JSONArray used = inventoryList.getJSONArray("used");
			JSONArray unused = inventoryList.getJSONArray("unused");
			Object columnNames[] = { "ID", "Registration Number", "MAC Address", "Status" };
			Object rowData[][] = new Object[used.length() + unused.length()][columnNames.length];
			for (int i = 0; i < used.length(); i++) {

				JSONObject inventoryItem = used.getJSONObject(i);
				rowData[i][0] = inventoryItem.get("id");
				rowData[i][1] = inventoryItem.get("registrationNumber");
				rowData[i][2] = inventoryItem.get("macAddress");
				rowData[i][3] = "Registered";
			}
			
			for(int i = used.length(), x = 0; x < unused.length(); i ++, x++ ){
				JSONObject inventoryItem = unused.getJSONObject(x);
				rowData[i][0] = inventoryItem.get("id");
				rowData[i][1] = inventoryItem.get("registrationNumber");
				rowData[i][2] = inventoryItem.get("macAddress");
				rowData[i][3] = "Unregistered";
			}
			model = new DefaultTableModel(rowData, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			listInventory.setModel(model);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	public void updateInventoryList() {
		getInventoryData();
	}
}
