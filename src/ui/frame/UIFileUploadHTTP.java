package ui.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import main.Data;

import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APICall;
import customColor.CustomColor;

public class UIFileUploadHTTP extends JFrame {
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

	private JLabel lblFileName;

	private JTextField tfFileName;

	private JButton btnDownload;
	private JButton btnUpload;
	private JButton btnBack;
	private JButton btnCancel;
	private JButton btnBrowse;
	private JFileChooser fc = new JFileChooser();

	private FileDialog fd;

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public UIFileUploadHTTP() {
		super("Upload Inventory");
		setBackground(CustomColor.NavyBlue.returnColor());

		setLayout(new BorderLayout());

		add(createButtonPanel(), BorderLayout.SOUTH);
		add(createBrowsePanel(), BorderLayout.CENTER);

		setPreferredSize(new Dimension(500, 300));
		setVisible(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public JPanel createButtonPanel() {
		JPanel panel = p.createPanel(Layouts.flow);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		btnDownload = b.createButton("Download Template");
		btnDownload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				APICall api = new APICall();
				String saveDir = runFileChooser();
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						try {
							api.getCSVSample(Data.URL, Data.sessionKey, saveDir);
						} catch (IOException e1) {
							e1.printStackTrace();
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
				panel.add(new JLabel("Downloading......."),
						BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setBounds(50, 50, 300, 100);
				dialog.setLocationRelativeTo(Data.mainFrame.uiFileUpload);
				dialog.setVisible(true);

			}
		});
		btnUpload = b.createButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// insert api for upload
						String fileURL = tfFileName.getText();

						if (fileURL.isEmpty()) {
							final JPanel panel = new JPanel();
							JOptionPane.showMessageDialog(panel,
									"No file selected", "Error",
									JOptionPane.ERROR_MESSAGE);
							return null;
						} else {
							APICall api = new APICall();
							String response = api.uploadInventory(
									Data.targetURL, fileURL, Data.sessionKey);
							try {
								JSONObject responseObject = new JSONObject(
										response);
								if (responseObject.get("result").equals("ok")) {
									JOptionPane.showMessageDialog(panel,
											"File uploaded", "Success",
											JOptionPane.INFORMATION_MESSAGE);
									closeFrame(e);
									
								} else {
									JOptionPane.showMessageDialog(
											Data.mainFrame,
											"Please check the file", "Error",
											JOptionPane.ERROR_MESSAGE);
								}

							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
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
				panel.add(new JLabel("Uploading......."),
						BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setBounds(50, 50, 300, 100);
				dialog.setLocationRelativeTo(Data.mainFrame.uiFileUpload);
				dialog.setVisible(true);
			}
		});
		btnBack = b.createButton("Cancel");
		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		panel.add(btnDownload);
		panel.add(btnUpload);
		panel.add(btnBack);

		return panel;
	}

	public JPanel createBrowsePanel() {
		JPanel panel = p.createPanel(Layouts.gridbag);
		GridBagConstraints g = new GridBagConstraints();
		g.weightx = 0.5;

		fd = new FileDialog(Data.mainFrame, "Choose a file", FileDialog.LOAD);
		fd.setFile("*.csv");
		fd.setFilenameFilter(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".csv");
			}
		});

		btnBrowse = b.createButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fd.setVisible(true);
				String filename = fd.getDirectory() + fd.getFile();
				if (fd.getDirectory() == null || fd.getFile() == null) {
					filename = "";
				}

				tfFileName.setText(filename);
			}
		});
		lblFileName = l.createLabel("File to upload:");
		l.addPadding(lblFileName, 10);
		tfFileName = new JTextField();
		tfFileName.setEditable(false);
		tfFileName.setPreferredSize(new Dimension(225, tfFileName
				.getPreferredSize().height));

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

	public String runFileChooser() {
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showSaveDialog(null);
		String saveDir = fc.getSelectedFile().getAbsolutePath();

		return saveDir;
	}
	
	private void closeFrame(ActionEvent e){
		
		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				Data.mainFrame.uiInventorySelect.updateInventoryList();
				Data.mainFrame.uiFileUpload.setVisible(false);
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
		panel.add(new JLabel("Refreshing Inventory......."),
				BorderLayout.PAGE_START);
		dialog.add(panel);
		dialog.pack();
		dialog.setBounds(50, 50, 300, 100);
		dialog.setLocationRelativeTo(Data.mainFrame.uiFileUpload);
		dialog.setVisible(true);
	}
}
