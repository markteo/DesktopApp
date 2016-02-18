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

import main.Data;
import api.APICall;
import ui.components.*;

public class UIFileDownloadHTTP extends JFrame implements PropertyChangeListener {

	Label l = new Label();
	Button b = new Button();
	Panel p = new Panel();

	private ui.components.FilePicker filePicker = new FilePicker("Pick a directory:	","Browse..");
	private JButton download = new JButton("Download");

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
 
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(download, constraints);
         
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
			api.getCSVSample(Data.URL, Data.sessionKey, saveDir, this);
			
		}catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error executing upload task: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }  
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
