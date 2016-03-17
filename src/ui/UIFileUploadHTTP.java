package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.Data;

import org.jdesktop.xswingx.PromptSupport;
import org.json.JSONException;
import org.json.JSONObject;

import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import api.APICall;

public class UIFileUploadHTTP {
	
	public static JFrame uploadInventoryFrame;
	public static void runUpload() {
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();
		uploadInventoryFrame = new JFrame("Upload Inventory File");
		uploadInventoryFrame.setLayout(new BorderLayout());
		uploadInventoryFrame.setSize(600, 100);

		JPanel pnlUploadForm = p.createPanel(Layouts.flow);
		JLabel lblCSVFile = l.createLabel("Upload CSV File:");
		JTextField tfCSVFilePath = new JTextField();
		PromptSupport.setPrompt("Choose a file", tfCSVFilePath);
		tfCSVFilePath.setEnabled(false);
		JButton btnAddCSV = b.createButton(" Choose File ");
		JButton btnUpload = b.createButton("Upload");
		JButton btnCancel = b.createButton("Cancel");
		
		
		JPanel pnlDownloadForm = p.createPanel(Layouts.flow);
		JLabel lblDownload = l.createLabel("Download CSV File: ");
		JButton btnBrowse = b.createButton("Choose download location");
		JButton btnDownload = b.createButton("Download");
		JFileChooser fileChooser = new JFileChooser();
		

		Component[] elementPnlForms = { lblCSVFile, tfCSVFilePath, btnAddCSV, btnUpload, btnCancel };
		Component[] elementPnlDownload = {lblDownload, btnBrowse, btnDownload};

		p.addComponentsToPanel(pnlUploadForm, elementPnlForms);
		p.addComponentsToPanel(pnlDownloadForm, elementPnlDownload);

		uploadInventoryFrame.add(pnlUploadForm, BorderLayout.CENTER);
		uploadInventoryFrame.add(pnlDownloadForm, BorderLayout.SOUTH);
		uploadInventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		uploadInventoryFrame.setVisible(true);
		btnAddCSV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
				fileChooser.setFileFilter(filter);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					tfCSVFilePath.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				uploadInventoryFrame.setVisible(false);
				Data.uiInventorySelect.setFrameVisible();
				
			}
		});
		btnUpload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// insert api for upload
				String fileURL = tfCSVFilePath.getText();
				
				if(fileURL.isEmpty()){
					final JPanel panel = new JPanel();
					 JOptionPane.showMessageDialog(panel, "No file selected", "Error", JOptionPane.ERROR_MESSAGE);
					 return;
				}else{
					APICall api = new APICall();
					String response = api.uploadInventory(Data.targetURL, fileURL, Data.sessionKey);
					try {
						JSONObject responseObject = new JSONObject(response);
						if(responseObject.get("result").equals("ok")){
							uploadInventoryFrame.setVisible(false);
							Data.uiInventorySelect.updateInventoryList();
						}else{
							 final JPanel panel = new JPanel();
							 JOptionPane.showMessageDialog(panel, "Please check file", "Error", JOptionPane.ERROR_MESSAGE);
						}
						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		btnBrowse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
					btnBrowse.setText(selectedFile);
				}
			}
		});
		btnDownload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String saveDir = btnBrowse.getText();
				
				boolean result = false;
				if(saveDir.equals("")){
					JOptionPane.showMessageDialog(uploadInventoryFrame, "Please a choose destination folder", "Error",JOptionPane.ERROR_MESSAGE);	
				}
				
				try{
					APICall api = new APICall();
					String response = api.getCSVSample(Data.URL, Data.sessionKey, saveDir, Data.uiFileUpload);
					JOptionPane.showMessageDialog(uploadInventoryFrame,
		                    "Successfully downloaded CSV Sample File", "Success",
		                    JOptionPane.INFORMATION_MESSAGE);
				}catch (Exception ex) {
		            JOptionPane.showMessageDialog(uploadInventoryFrame,
		                    "Error executing upload task: " + ex.getMessage(), "Error",
		                    JOptionPane.ERROR_MESSAGE);
		        }  
				
			}
		});
	}

}
