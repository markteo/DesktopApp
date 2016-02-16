package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import uiComponents.Button;
import uiComponents.Label;
import uiComponents.Layouts;
import uiComponents.Panel;

public class UI_login {
	
	public static void runLogin(String apiUrl){
		JFrame loginFrame = new JFrame("Login");
		loginFrame.setLayout(new BorderLayout());
		loginFrame.setPreferredSize(new Dimension(400, 400));
		loginFrame.setResizable(false);
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		File pathToImage = new File("image/KaiSquare_logoFA.png");
		Image myPicture = null;
		try {
			myPicture = ImageIO.read(pathToImage);
			myPicture = myPicture.getScaledInstance(250, 150, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
	
		Panel p = new Panel();
		JPanel loginPanel = p.createPanel(Layouts.grid,3,3);
		loginPanel.setBorder(new EmptyBorder(25,25,0,25));
		JLabel lblUser = new Label().createLabel("Username:");
		JLabel lblPassword = new Label().createLabel("Password:");
		JTextField tfUser = new JTextField("Enter username here..");
		JPasswordField pfPassword = new JPasswordField("password");
		
		JPanel buttonPanel = p.createPanel(Layouts.flow);
		buttonPanel.setBorder(new EmptyBorder(0, 0, 25, 0));
		JButton btnLogin = Button.createButton("Login");
		JButton btnExit = Button.createButton("Exit");
		btnLogin.setPreferredSize(new Dimension(150,50));
		btnExit.setPreferredSize(new Dimension(150,50));
		Component[] arrayBtn = {btnLogin,btnExit};
		p.addComponentsToPanel(buttonPanel,arrayBtn);
		
		btnLogin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(apiUrl);
			}
		});
		
		btnExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		Component[] arrayComponents = {lblUser,tfUser,lblPassword,pfPassword};
		picLabel.setBounds(50, 50, 50, 50);
		p.addComponentsToPanel(loginPanel, arrayComponents);
		loginFrame.add(picLabel,BorderLayout.NORTH);
		loginFrame.add(loginPanel,BorderLayout.CENTER);
		loginFrame.add(buttonPanel,BorderLayout.SOUTH);
		loginFrame.pack();
	}

}
