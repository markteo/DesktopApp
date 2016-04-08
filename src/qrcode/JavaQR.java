
package qrcode;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main.Data;
import ui.frame.UILogin;

import org.jdesktop.xswingx.PromptSupport;
import org.json.JSONException;
import org.json.JSONObject;

import api.APICall;

public class JavaQR extends JPanel implements Runnable {

	private Thread t;

	public JavaQR() {
		start();
	}

	@Override
	public void run() {
		// Initial Config

		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();

		// Top Panel
		topPanel.setLayout(new GridLayout(0, 1));
		topPanel.setBorder(BorderFactory.createTitledBorder("Input Data"));

		JPanel rowTopPanel = new JPanel();
		rowTopPanel.setLayout(new GridLayout(0, 2));

		JLabel accKey = new JLabel("Access Key");
		JTextField accField = new JTextField(5);
		
		accField.setEditable(false);
		accField.setText(Data.accessKey);

		JLabel regNo = new JLabel("Registration Number");
		JTextField regField = new JTextField(5);

		
		regField.setEditable(false);
		regField.setText(Data.registrationNumber);

		JLabel licNo = new JLabel("License Number");
		JFormattedTextField licField = new JFormattedTextField();
		
		licField.setEditable(false);
		licField.setText(Data.licenseNumber);


		rowTopPanel.add(accKey);
		rowTopPanel.add(accField);
		rowTopPanel.add(regNo);
		rowTopPanel.add(regField);
		rowTopPanel.add(licNo);
		rowTopPanel.add(licField);

		topPanel.add(rowTopPanel);

		// Center Panel
		centerPanel.setLayout(new GridLayout(0, 1));
		centerPanel.setBorder(BorderFactory.createTitledBorder("QR Code"));
		// centerPanel.setPreferredSize(new Dimension(100,60));

		JPanel rowCenPanel = new JPanel();
		rowCenPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton genBtn = new JButton("Download QR Code");
		JButton homeBtn = new JButton("Back to Start");

		String accessKey = accField.getText().toString();
		String regKey = regField.getText().toString();
		String licKey = licField.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("accessKey", accessKey);
			jsonObject.put("registrationNumber", regKey);
			jsonObject.put("licenseNumber", licKey);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}


		QRLogic qrGen = new QRLogic();
		BufferedImage image = qrGen.generateQR(jsonObject);
		centerPanel.add(new JLabel(new ImageIcon(image)));

		bottomPanel.setLayout(new GridLayout(2, 1));
		
		rowCenPanel.add(homeBtn);
		rowCenPanel.add(genBtn);
		bottomPanel.add(rowCenPanel);
		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
		add(centerPanel, BorderLayout.CENTER);
		Data.mainFrame.setSize(1000, 500);

		// if(accessKey.trim().equalsIgnoreCase("") == true ||
		// regKey.trim().equalsIgnoreCase("") == true) {
		// JOptionPane.showMessageDialog(frame, "Some fields are missing!");
		// } else if(licKey.trim().length() < 21) {
		// JOptionPane.showMessageDialog(frame, "License Key is incomplete!");
		// } else if(dlDir.equalsIgnoreCase("Choose Download Folder") != true )
		// {

		// JOptionPane.showMessageDialog(frame, "QR Code Generated in " +
		// fileLocation);
		// } else {
		// JOptionPane.showMessageDialog(frame, "Choose a directory!");
		// System.out.println("No QR Generated");
		// }

		// Buttons Listener
		genBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();
				String newDate = new SimpleDateFormat("yyyy-MM-dd h-m-a").format(date);
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				File myFile = new File(Data.registrationNumber + ".png");
				fileChooser.setSelectedFile(myFile);
				fileChooser.showSaveDialog(null);
				String dlDir = fileChooser.getSelectedFile().getPath();
				System.out.println(dlDir);
				String fileName = fileChooser.getSelectedFile().getName();
				String filePath = "";
				if(fileName != null){
					filePath =  dlDir + ".png";
				}else{
					filePath = dlDir + "/" + Data.registrationNumber + ".png";
				}
				
				String fileType = "png";
				myFile = new File(filePath);
				
				if (dlDir != null) {

					try {
						ImageIO.write(image, fileType, myFile);
						JOptionPane.showMessageDialog(Data.mainFrame, "QR Code Saved in " + dlDir);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});
	

		homeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Data.mainFrame.showPanel("inventory");
			}
		});

		

		// Frame Config
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

	}

	public void start() {
		t = new Thread(this);
		t.start();
	}
}
