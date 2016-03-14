
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

import org.jdesktop.xswingx.PromptSupport;
import org.json.JSONException;
import org.json.JSONObject;

import ui.UILogin;
import api.APICall;


public class JavaQR implements Runnable {

	private Thread t;
	private JFrame frame;
	
	public JavaQR(){
		start();
	}
	
	@Override
	public void run() {
		//		Initial Config
		frame = new JFrame("QR Code Generator");
		frame.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		//		Top Panel
		topPanel.setLayout(new GridLayout(0,1));
		topPanel.setBorder(BorderFactory.createTitledBorder("Input Data"));

		JPanel rowTopPanel = new JPanel();
		rowTopPanel.setLayout(new GridLayout(0,2));

		JLabel accKey = new JLabel("Access Key");
		JTextField accField = new JTextField(5);
		PromptSupport.setPrompt("E.G Z76GU", accField);
		PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, accField);
		PromptSupport.setFontStyle(Font.BOLD, accField);
		accField.setEditable(false);
		accField.setText(Data.accessKey);

		JLabel regNo = new JLabel("Register Number");
		JTextField regField = new JTextField(5);
		
		PromptSupport.setPrompt("E.G THF11200054160106", regField);
		PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, regField);
		PromptSupport.setFontStyle(Font.BOLD, regField);
		regField.setEditable(false);
		regField.setText(Data.registrationNumber);
		
		JLabel licNo = new JLabel("License Number");
		JFormattedTextField licField = new JFormattedTextField();
		PromptSupport.setPrompt("E.G 2DAJS - 3J8SS - 9H8HS", licField);
		PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, licField);
		PromptSupport.setFontStyle(Font.BOLD, licField);
		licField.setEditable(false);
		licField.setText(Data.licenseNumber);
		
		JLabel downloadLoc = new JLabel("Download Location");
		JButton dlBtn = new JButton("Choose Download Folder");

		rowTopPanel.add(accKey);
		rowTopPanel.add(accField);
		rowTopPanel.add(regNo);
		rowTopPanel.add(regField);
		rowTopPanel.add(licNo);
		rowTopPanel.add(licField);
		
		topPanel.add(rowTopPanel);

		//		Center Panel
		centerPanel.setLayout(new GridLayout(0,1));
		centerPanel.setBorder(BorderFactory.createTitledBorder("QR Code"));
//		centerPanel.setPreferredSize(new Dimension(100,60));

		JPanel rowCenPanel = new JPanel();
		rowCenPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton genBtn = new JButton("Download QR Code");
		JButton homeBtn = new JButton("Back to Start");
		JButton logoutBtn = new JButton("Logout");
		
		String accessKey = accField.getText().toString();
		String regKey = regField.getText().toString();
		String licKey = licField.getText().toString();
		String dlDir = dlBtn.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("accessKey", accessKey);
			jsonObject.put("registrationNumber", regKey);
			jsonObject.put("licenseNumber", licKey);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String fileLocation = dlBtn.getText().toString();
		fileLocation = "./";
		QRLogic qrGen = new QRLogic();
		BufferedImage image = qrGen.generateQR(jsonObject);
		centerPanel.add(new JLabel(new ImageIcon(image)));
		
		bottomPanel.setLayout(new GridLayout(2, 1));
		JPanel rowBottom1 = new JPanel();
		rowBottom1.setLayout(new FlowLayout(FlowLayout.CENTER));

		rowBottom1.add(downloadLoc);
		rowBottom1.add(dlBtn);
		rowCenPanel.add(genBtn);
		rowCenPanel.add(homeBtn);
		rowCenPanel.add(logoutBtn);
		bottomPanel.add(rowBottom1);
		bottomPanel.add(rowCenPanel);
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setSize(1000, 500);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.setVisible(true);
		
		

//		if(accessKey.trim().equalsIgnoreCase("") == true || regKey.trim().equalsIgnoreCase("") == true) {
//			JOptionPane.showMessageDialog(frame, "Some fields are missing!");
//		} else if(licKey.trim().length() < 21) {
//			JOptionPane.showMessageDialog(frame, "License Key is incomplete!");
//		} else if(dlDir.equalsIgnoreCase("Choose Download Folder") != true ) {
			
			//JOptionPane.showMessageDialog(frame, "QR Code Generated in " + fileLocation);
//		} else {
//			JOptionPane.showMessageDialog(frame, "Choose a directory!");
//			System.out.println("No QR Generated");
//		}

//		Buttons Listener
		genBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();
				String newDate = new SimpleDateFormat("yyyy-MM-dd h-m-a").format(date);
				String dlDir = dlBtn.getText().toString();

				String filePath = dlDir + "/qrcode-" + newDate + ".png";
				String fileType = "png";

				if(dlDir.equalsIgnoreCase("Choose Download Folder") != true ) {

					String fileLocation = dlBtn.getText().toString();
					File myFile = new File(filePath);
					try {
						ImageIO.write(image, fileType, myFile);
						JOptionPane.showMessageDialog(frame, "QR Code Saved in " + fileLocation);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
					
				} else {
					JOptionPane.showMessageDialog(frame, "Choose a directory!");
					System.out.println("No QR Generated");
				}
			}
		});

		dlBtn.addActionListener(new ActionListener() {;
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
				dlBtn.setText(selectedFile);
			}
		}
		});
		
		homeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Data.uiInventorySelect.setFrameVisible();
				frame.setVisible(false);
				
			}
		});
		
		logoutBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				APICall api = new APICall();
				try {
					JSONObject response = new JSONObject(api.logout(Data.targetURL, Data.sessionKey));
					if(response.getString("result").equals("ok")){
						Data.uiLogin = new UILogin();
						frame.setVisible(false);
						Data.uiLogin.runLogin();
						
					}
						
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//      Frame Config
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
	public void setFrameVisible(){
		frame.setVisible(true);
	}
	
}
