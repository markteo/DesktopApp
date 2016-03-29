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
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.components.table.model.UneditableModel;

public class UIInventorySelect extends JPanel {
	public static APICall api = new APICall();

	private JPanel pnlInstruction;
	private JPanel pnlButton;

	private JLabel lblInstruction;

	private JButton btnAddElements;
	private JButton btnSelectElements;

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	private JTable listInventory;

	public UIInventorySelect() {
		setBackground(CustomColor.NavyBlue.returnColor());
		setLayout(new BorderLayout());

		// fetch list from server
		listInventory = getInventoryData();
		listInventory.setShowHorizontalLines(false);
		listInventory.setAutoCreateRowSorter(true);

		// listInventory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//
		// for (int column = 0; column < listInventory.getColumnCount();
		// column++) {
		// TableColumn tableColumn =
		// listInventory.getColumnModel().getColumn(column);
		// int preferredWidth = tableColumn.getMinWidth();
		// int maxWidth = tableColumn.getMaxWidth();
		//
		// for (int row = 0; row < listInventory.getRowCount(); row++) {
		// TableCellRenderer cellRenderer = listInventory.getCellRenderer(row,
		// column);
		// Component c = listInventory.prepareRenderer(cellRenderer, row,
		// column);
		// int width = c.getPreferredSize().width +
		// listInventory.getIntercellSpacing().width;
		// preferredWidth = Math.max(preferredWidth, width);
		//
		// // We've exceeded the maximum width, no need to check other rows
		//
		// if (preferredWidth >= maxWidth) {
		// preferredWidth = maxWidth;
		// break;
		// }
		// }
		//
		// tableColumn.setPreferredWidth(preferredWidth);
		// }

		// start of ui
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
		scrollInventory.setPreferredSize(new Dimension(400,200));
		pnlInventoryList.add(scrollInventory, BorderLayout.CENTER);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnAddElements = b.createButton("Add Inventory Item");
		JButton btnSelectElements = b.createButton("Next");

		pnlButtons.add(btnAddElements);
		pnlButtons.add(btnSelectElements);

		add(pnlInstruction, BorderLayout.NORTH);
		add(pnlInventoryList, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
		btnAddElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Data.mainFrame.addPanel(Data.mainFrame.uiFileUpload = new UIFileUploadHTTP() , "upload");
				Data.mainFrame.pack();
				Data.mainFrame.showPanel("upload");
			}
		});

		btnSelectElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {

						// do something with selected inventory
						int selected = listInventory.getSelectedRow();
						Data.registrationNumber = (String) listInventory.getModel().getValueAt(selected, 1);
						setVisible(false);

						if (Data.mainFrame.uiBucketSelect != null) {
							Data.mainFrame.showPanel("bucket");
						} else {
							Data.mainFrame.uiBucketSelect = new UIBucketSelect();
							Data.mainFrame.addPanel(Data.mainFrame.uiBucketSelect,"bucket");
							Data.mainFrame.pack();
							Data.mainFrame.showPanel("bucket");
						}
						return null;
					}
				};

				Window win = SwingUtilities.getWindowAncestor((AbstractButton) e.getSource());
				final JDialog dialog = new JDialog(win, "Bucket", ModalityType.APPLICATION_MODAL);

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
				panel.add(new JLabel("Getting Buckets......."), BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setLocationRelativeTo(win);
				dialog.setBounds(50, 50, 300, 100);
				dialog.setVisible(true);

			}
		});
	}

	public JTable getInventoryData() {
		JSONArray inventoryList = new APIProcess().inventoryList(Data.targetURL, Data.sessionKey);
		try {
			Object rowData[][] = new Object[inventoryList.length()][3];
			Object columnNames[] = { "ID", "Registration Number", "MAC Address" };
			for (int i = 0; i < inventoryList.length(); i++) {

				JSONObject inventoryItem = inventoryList.getJSONObject(i);
				rowData[i][0] = inventoryItem.get("id");
				rowData[i][1] = inventoryItem.get("registrationNumber");
				rowData[i][2] = inventoryItem.get("macAddress");
			}
			JTable model = new JTable(new UneditableModel(rowData, columnNames));
			return model;

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}

	public void updateInventoryList() {
		JTable inventoryList = getInventoryData();
	}
}
