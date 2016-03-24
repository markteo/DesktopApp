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
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APICall;
import api.APIProcess;
import customColor.CustomColor;

public class UIInventorySelect {
	public static APICall api = new APICall();
	private JFrame inventoryFrame;
	private JTable listInventory = new JTable();
	private DefaultTableModel model;

	public void runInventorySelect() {
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();

		// fetch list from server
		getInventoryData();
		listInventory.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		for (int column = 0; column < listInventory.getColumnCount(); column++) {
			TableColumn tableColumn = listInventory.getColumnModel().getColumn(column);
			int preferredWidth = tableColumn.getMinWidth();
			int maxWidth = tableColumn.getMaxWidth();

			for (int row = 0; row < listInventory.getRowCount(); row++) {
				TableCellRenderer cellRenderer = listInventory.getCellRenderer(row, column);
				Component c = listInventory.prepareRenderer(cellRenderer, row, column);
				int width = c.getPreferredSize().width + listInventory.getIntercellSpacing().width;
				preferredWidth = Math.max(preferredWidth, width);

				// We've exceeded the maximum width, no need to check other rows

				if (preferredWidth >= maxWidth) {
					preferredWidth = maxWidth;
					break;
				}
			}

			tableColumn.setPreferredWidth(preferredWidth);
		}

		// start of ui
		inventoryFrame = new JFrame("Inventory");
		inventoryFrame.setLayout(new BorderLayout());

		JPanel pnlInstruction = p.createPanel(Layouts.flow);
		JLabel lblInstruction = l.createLabel("Inventory List");
		pnlInstruction.setBackground(CustomColor.LightBlue.returnColor());
		lblInstruction.setForeground(Color.white);
		lblInstruction.setFont(new Font("San Serif", Font.PLAIN, 18));
		pnlInstruction.add(lblInstruction);

		JPanel pnlInventoryList = p.createPanel(Layouts.flow);
		listInventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollInventory = new JScrollPane(listInventory);
		scrollInventory.setPreferredSize(
				new Dimension(listInventory.getPreferredSize().width + 20, listInventory.getPreferredSize().height));
		Component[] inventoryListComponents = { scrollInventory };
		p.addComponentsToPanel(pnlInventoryList, inventoryListComponents);

		JPanel pnlButtons = p.createPanel(Layouts.flow);
		JButton btnAddElements = b.createButton("Add Inventory Item");
		JButton btnSelectElements = b.createButton("Next");

		pnlButtons.add(btnAddElements);
		pnlButtons.add(btnSelectElements);

		inventoryFrame.add(pnlInstruction, BorderLayout.NORTH);
		inventoryFrame.add(pnlInventoryList, BorderLayout.CENTER);
		inventoryFrame.add(pnlButtons, BorderLayout.SOUTH);
		inventoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inventoryFrame.pack();
		inventoryFrame.setVisible(true);
		btnAddElements.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Data.uiFileUpload = new UIFileUploadHTTP();
				Data.uiFileUpload.runUpload();
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
						inventoryFrame.setVisible(false);

						if (Data.uiBucketSelect != null) {
							Data.uiBucketSelect.setFrameVisible();
						} else {
							Data.uiBucketSelect = new UIBucketSelect();
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

	public void getInventoryData() {
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
			model = new DefaultTableModel(rowData, columnNames){
				@Override
			    public boolean isCellEditable(int row, int column) {
			        //all cells false
			        return false;
			    }
			};
			listInventory.setModel(model);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void setFrameVisible() {
		inventoryFrame.setVisible(true);
	}

	public void updateInventoryList() {
		getInventoryData();
	}
}
