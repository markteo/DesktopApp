package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Data;

import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APICall;
import customColor.CustomColor;

public class UIFileUploadHTTP {
	//
	// public static JFrame uploadInventoryFrame;
	//
	// public static void runUpload() {
	// Panel p = new Panel();
	// Button b = new Button();
	// Label l = new Label();
	// uploadInventoryFrame = new JFrame("Upload Inventory File");
	// uploadInventoryFrame.setLayout(new BorderLayout());
	// uploadInventoryFrame.setSize(600, 150);
	// JPanel pnlUploadForm = p.createPanel(Layouts.flow);
	// JLabel lblCSVFile = l.createLabel("Upload CSV File:");
	// JTextField tfCSVFilePath = new JTextField();
	// PromptSupport.setPrompt("Choose a file", tfCSVFilePath);
	// tfCSVFilePath.setEnabled(false);
	// JButton btnAddCSV = b.createButton(" Choose File ");
	// JButton btnUpload = b.createButton("Upload");
	// JButton btnCancel = b.createButton("Cancel");
	//
	// JPanel pnlDownloadForm = p.createPanel(Layouts.flow);
	// JLabel lblDownload = l.createLabel("Download CSV File: ");
	// JButton btnBrowse = b.createButton("Choose download location");
	// JButton btnDownload = b.createButton("Download");
	// JFileChooser fileChooser = new JFileChooser();
	//
	// Component[] elementPnlForms = { lblCSVFile, tfCSVFilePath, btnAddCSV,
	// btnUpload, btnCancel };
	// Component[] elementPnlDownload = { lblDownload, btnBrowse, btnDownload };
	//
	// p.addComponentsToPanel(pnlUploadForm, elementPnlForms);
	// p.addComponentsToPanel(pnlDownloadForm, elementPnlDownload);
	//
	// uploadInventoryFrame.add(pnlUploadForm, BorderLayout.CENTER);
	// uploadInventoryFrame.add(pnlDownloadForm, BorderLayout.SOUTH);
	// uploadInventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	// uploadInventoryFrame.setVisible(true);
	// btnAddCSV.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// JFileChooser fileChooser = new JFileChooser();
	// FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files",
	// "csv");
	// fileChooser.setFileFilter(filter);
	// int returnValue = fileChooser.showOpenDialog(null);
	// if (returnValue == JFileChooser.APPROVE_OPTION) {
	// File selectedFile = fileChooser.getSelectedFile();
	// tfCSVFilePath.setText(selectedFile.getAbsolutePath());
	// }
	// }
	// });
	// btnCancel.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// uploadInventoryFrame.setVisible(false);
	// Data.uiInventorySelect.setFrameVisible();
	//
	// }
	// });
	// btnUpload.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// // insert api for upload
	// String fileURL = tfCSVFilePath.getText();
	//
	// if (fileURL.isEmpty()) {
	// final JPanel panel = new JPanel();
	// JOptionPane.showMessageDialog(panel, "No file selected", "Error",
	// JOptionPane.ERROR_MESSAGE);
	// return;
	// } else {
	// APICall api = new APICall();
	// String response = api.uploadInventory(Data.targetURL, fileURL,
	// Data.sessionKey);
	// try {
	// JSONObject responseObject = new JSONObject(response);
	// if (responseObject.get("result").equals("ok")) {
	// uploadInventoryFrame.setVisible(false);
	// Data.uiInventorySelect.updateInventoryList();
	// } else {
	// final JPanel panel = new JPanel();
	// JOptionPane.showMessageDialog(panel, "Please check file", "Error",
	// JOptionPane.ERROR_MESSAGE);
	// }
	//
	// } catch (JSONException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	// }
	// });
	//
	// btnBrowse.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// // TODO Auto-generated method stub
	//
	// fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	// int returnValue = fileChooser.showOpenDialog(null);
	// if (returnValue == JFileChooser.APPROVE_OPTION) {
	// String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
	// btnBrowse.setText(selectedFile);
	// }
	// }
	// });
	// btnDownload.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// String saveDir = btnBrowse.getText();
	//
	// boolean result = false;
	// if (saveDir.equals("")) {
	// JOptionPane.showMessageDialog(uploadInventoryFrame, "Please a choose
	// destination folder", "Error",
	// JOptionPane.ERROR_MESSAGE);
	// }
	//
	// try {
	// APICall api = new APICall();
	// String response = api.getCSVSample(Data.URL, Data.sessionKey, saveDir,
	// Data.uiFileUpload);
	// JOptionPane.showMessageDialog(uploadInventoryFrame, "Successfully
	// downloaded CSV Sample File",
	// "Success", JOptionPane.INFORMATION_MESSAGE);
	// } catch (Exception ex) {
	// JOptionPane.showMessageDialog(uploadInventoryFrame,
	// "Error executing upload task: " + ex.getMessage(), "Error",
	// JOptionPane.ERROR_MESSAGE);
	// }
	//
	// }
	// });
	// }

	private JFrame frame;

	private JLabel lblFileName;

	private JTextField tfFileName;

	private JButton btnDownload;
	private JButton btnUpload;
	private JButton btnCancel;
	private JButton btnBrowse;
	private JFileChooser fc = new JFileChooser();

	private String saveDir;
	private FileDialog fd;
	private APICall api = new APICall();

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public void runUpload() {
		frame = new JFrame("License Details");
		frame.getContentPane().setBackground(CustomColor.NavyBlue.returnColor());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().setLayout(new BorderLayout());

		frame.add(createButtonPanel(), BorderLayout.SOUTH);
		frame.add(createBrowsePanel(), BorderLayout.CENTER);

		frame.setPreferredSize(new Dimension(500, 300));
		frame.pack();
		frame.setVisible(true);
	}

	public JPanel createButtonPanel() {
		JPanel panel = p.createPanel(Layouts.flow);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		btnDownload = b.createButton("Download Template");
		btnDownload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				runFileChooser();
				downloadFile();
			}
		});
		btnUpload = b.createButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// insert api for upload
				String fileURL = tfFileName.getText();

				if (fileURL.isEmpty()) {
					final JPanel panel = new JPanel();
					JOptionPane.showMessageDialog(panel, "No file selected",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					APICall api = new APICall();
					String response = api.uploadInventory(Data.targetURL,
							fileURL, Data.sessionKey);
					try {
						JSONObject responseObject = new JSONObject(response);
						if (responseObject.get("result").equals("ok")) {
							JOptionPane.showMessageDialog(panel,
									"File uploaded", "Success",
									JOptionPane.INFORMATION_MESSAGE);
							frame.setVisible(false);
							Data.uiInventorySelect.updateInventoryList();
						} else {
							final JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel,
									"Please check the file", "Error",
									JOptionPane.ERROR_MESSAGE);
						}

					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		btnCancel = b.createButton("Cancel");
		
		panel.add(btnCancel);
		panel.add(btnDownload);
		panel.add(btnUpload);
		

		return panel;
	}

	public JPanel createBrowsePanel() {
		JPanel panel = p.createPanel(Layouts.gridbag);
		GridBagConstraints g = new GridBagConstraints();

		fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
		fd.setFile("*.csv");

		btnBrowse = b.createButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fd.setVisible(true);
				String filename = fd.getDirectory() + fd.getFile();
				if(fd.getDirectory() == null || fd.getFile() == null){
					filename = "";
				}

				tfFileName.setText(filename);
			}
		});
		lblFileName = l.createLabel("File to upload:");
		l.addPadding(lblFileName, 10);
		tfFileName = new JTextField();
		tfFileName.setEditable(false);
		tfFileName.setPreferredSize(new Dimension(225, tfFileName.getPreferredSize().height));

		g.gridx = 0;
		g.gridy = 0;
		g.fill = g.BOTH;
		panel.add(lblFileName, g);

		g.gridx = 1;
		g.gridy = 0;
		panel.add(tfFileName, g);

		g.gridx = 3;
		g.gridy = 0;
		g.insets = new Insets(0, 10, 0, 0);
		panel.add(btnBrowse, g);

		return panel;
	}
	public void runFileChooser() {
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showSaveDialog(null);
		saveDir = fc.getSelectedFile().getAbsolutePath();

	}
	private void downloadFile() {
		// validate input first
		if (saveDir.equals("")) {
			JOptionPane.showMessageDialog(frame, "Please choose a directory", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}else {
			try {
				api.getCSVSample(Data.URL, Data.sessionKey, saveDir, this);
				
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error executing upload task: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	public JFrame getFrame(){
		return frame;
	}
}
