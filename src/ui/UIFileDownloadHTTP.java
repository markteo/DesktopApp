package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import api.APICall;
import main.Data;
import ui.components.Button;
import ui.components.FilePicker;
import ui.components.Label;
import ui.components.Panel;

public class UIFileDownloadHTTP extends JFrame implements PropertyChangeListener {

	Label l = new Label();
	Button b = new Button();
	Panel p = new Panel();

	private ui.components.FilePicker filePicker = new FilePicker("Pick a directory:	","Browse..");
	private JButton download = new JButton("Download");
	private JButton btnSkip = new JButton("I already have the template.");

	private JLabel labelFileName = new JLabel("File Name:");
	private JTextField tfFileName = new JTextField(20);

	private JLabel labelFileSize = new JLabel("File Size(Bytes):");
	private JTextField tfFileSize = new JTextField(20);

	private JLabel labelProgress = new JLabel("Progress");
	private JProgressBar progressBar = new JProgressBar(0, 100);

	public UIFileDownloadHTTP() {
		super("Download CSV File");
		// TODO Auto-generated constructor stub
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);
		
		filePicker.setMode(FilePicker.MODE_SAVE);
		filePicker.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				buttonDownloadActionPerformed(event);
				UIFileUploadHTTP uploadUI = new UIFileUploadHTTP();
				setVisible(false);
				uploadUI.runUpload();
			}
		});
		
		btnSkip.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				UIFileUploadHTTP uploadUI = new UIFileUploadHTTP();
				uploadUI.runUpload();
				
			}
		});
		
		tfFileName.setEditable(false);
		tfFileSize.setEditable(false);
		
		progressBar.setPreferredSize(new Dimension(200,30));
		progressBar.setStringPainted(true);
		
		
		constraints.gridx=0;
		constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        add(filePicker, constraints);
 
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.EAST;
        add(download, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.EAST;
        add(btnSkip, constraints);
         
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        add(labelFileName, constraints);
         
        constraints.gridx = 1;
        add(tfFileName, constraints);
         
        constraints.gridy = 4;
        constraints.gridx = 0;
        add(labelFileSize, constraints);
         
        constraints.gridx = 1;
        add(tfFileSize, constraints);
        
        pack();
        setLocationRelativeTo(null);    // center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);    
        setVisible(true);
	}

	private void buttonDownloadActionPerformed(ActionEvent event) {
		String saveDir = filePicker.getSelectedFilePath();	
		
		if(saveDir.equals("")){
			JOptionPane.showMessageDialog(this, "Please a choose destination folder", "Error",JOptionPane.ERROR_MESSAGE);	
			return;
		}
		
		try{
			progressBar.setValue(0);
			APICall api = new APICall();
			String response = api.getCSVSample(Data.URL, Data.sessionKey, saveDir, this);
		}catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error executing upload task: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }  
	}
	
	private void buttonSkipActionPerformed(ActionEvent event) {
		UIFileUploadHTTP upload = new UIFileUploadHTTP();
		upload.runUpload();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getPropertyName().equals("progress")){
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	public void setFileInfo(String fileName, int size) {
		// TODO Auto-generated method stub
		tfFileName.setText(fileName);
		tfFileSize.setText(String.valueOf(size));
	}
	
	public void setProgressBar( int value){
		progressBar.setValue(value);
	}
}

