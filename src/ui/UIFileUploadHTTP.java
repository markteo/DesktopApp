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

	public static void runUpload() {
		Panel p = new Panel();
		Button b = new Button();
		Label l = new Label();
		
		JFrame uploadInventoryFrame = new JFrame("Upload Inventory");
		uploadInventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		uploadInventoryFrame.setLayout(new BorderLayout());
		uploadInventoryFrame.setSize(600, 70);

		JPanel pnlUploadForm = p.createPanel(Layouts.flow);
		JLabel lblCSVFile = l.createLabel("Upload CSV File:");
		JTextField tfCSVFilePath = new JTextField();
		PromptSupport.setPrompt("Choose a file", tfCSVFilePath);
		tfCSVFilePath.setEnabled(false);
		JButton btnAddCSV = b.createButton(" Choose File ");
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

		JButton btnUpload = b.createButton("Upload");
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
							Data.uiInventorySelect.setFrameVisible();
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

		Component[] elementPnlForms = { lblCSVFile, tfCSVFilePath, btnAddCSV, btnUpload };

		p.addComponentsToPanel(pnlUploadForm, elementPnlForms);

		uploadInventoryFrame.add(pnlUploadForm, BorderLayout.CENTER);
		uploadInventoryFrame.add(pnlUploadForm, BorderLayout.NORTH);
		uploadInventoryFrame.setVisible(true);
	}

}
