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

				if (saveDir != null) {

					SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							try {
								api.getCSVSample(Data.URL, Data.sessionKey,
										saveDir);
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
								public void propertyChange(
										PropertyChangeEvent evt) {
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
			}
		});
		btnUpload = b.createButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
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
				return name.endsWith(".csv");
			}
		});

		btnBrowse = b.createButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
		try{
			String saveDir = fc.getSelectedFile().getAbsolutePath();
			System.out.println(saveDir);
			return saveDir;

		}catch(NullPointerException e){
			return null;
		}

	}

	private void closeFrame(ActionEvent e) {

		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				Data.mainFrame.uiInventorySelect.updateInventoryList();
				Data.mainFrame.uiFileUpload.setVisible(false);
				return null;
			}
		};
		Window win = SwingUtilities.getWindowAncestor((AbstractButton) e
				.getSource());
		final JDialog dialog = new JDialog(win, "Loading",
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
		panel.add(new JLabel("Refreshing Inventory......."),
				BorderLayout.PAGE_START);
		dialog.add(panel);
		dialog.pack();
		dialog.setBounds(50, 50, 300, 100);
		dialog.setLocationRelativeTo(Data.mainFrame.uiFileUpload);
		dialog.setVisible(true);
	}
}
