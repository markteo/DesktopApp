package ui.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.jdesktop.xswingx.PromptSupport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.APICall;
import main.Data;
import main.DesktopAppMain;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.frame.KAIQRFrame;

public class UILogin extends JFrame {

	public static APICall api = new APICall();

	private JFrame frame = Data.mainFrame;

	private JPanel loginPanel;
	private JPanel buttonPanel;

	private JLabel lblUser;
	private JLabel lblPassword;
	private JLabel lblURL;
	private JLabel lblBucket;

	private JTextField tfURL;
	private JTextField tfBucket;
	private JTextField tfUser;
	private JPasswordField pfPassword;

	public UILogin() {
		super("login");
		Label l = new Label();

		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(400, 300));

		Panel p = new Panel();
		loginPanel = p.createPanel(Layouts.grid, 4, 2);
		loginPanel.setBorder(new EmptyBorder(25, 25, 0, 25));
		lblUser = l.createLabel("Username:");
		lblPassword = l.createLabel("Password:");
		lblURL = l.createLabel("Server URL");
		lblBucket = l.createLabel("Bucket");

		tfURL = new JTextField();
		tfURL.setText("kaiup.kaisquare.com");
		tfBucket = new JTextField();
		PromptSupport.setPrompt("BucketName", tfBucket);
		tfUser = new JTextField();
		PromptSupport.setPrompt("Username", tfUser);
		pfPassword = new JPasswordField();
		PromptSupport.setPrompt("Password", pfPassword);

		buttonPanel = p.createPanel(Layouts.flow);
		buttonPanel.setBorder(new EmptyBorder(0, 0, 25, 0));
		Button b = new Button();
		JButton btnLogin = b.createButton("Login");
		JButton btnExit = b.createButton("Exit");
		btnLogin.setPreferredSize(new Dimension(150, 50));
		btnExit.setPreferredSize(new Dimension(150, 50));
		Component[] arrayBtn = { btnLogin, btnExit };
		p.addComponentsToPanel(buttonPanel, arrayBtn);

		Component[] arrayComponents = { lblURL, tfURL, lblBucket, tfBucket, lblUser, tfUser, lblPassword, pfPassword };
		// picLabel.setBounds(50, 50, 50, 50);
		p.addComponentsToPanel(loginPanel, arrayComponents);
		// loginFrame.add(picLabel,BorderLayout.NORTH);
		add(loginPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {

						String username = tfUser.getText();
						String password = String.valueOf(pfPassword.getPassword());
						Data.URL = Data.protocol + tfURL.getText();
						Data.targetURL = Data.protocol + tfURL.getText() + "/api/" + tfBucket.getText() + "/";

						String response = api.loginBucket(Data.targetURL, username, password);

						try {

							if (DesktopAppMain.checkResult(response)) {
								JSONObject responseJSON = new JSONObject(response);
								Data.sessionKey = responseJSON.get("session-key").toString();
								response = api.getUserFeatures(Data.targetURL, Data.sessionKey);
								if (checkFeatures(response)) {
									Data.mainFrame = new KAIQRFrame();
									Data.mainFrame.addPanel(new UIInventorySelect(), "inventory");
									Data.mainFrame.showPanel("inventory");
									Data.mainFrame.pack();
									Data.mainFrame.setVisible(true);
									Data.loginFrame.setVisible(false);
								} else {
									JOptionPane.showMessageDialog(frame, "User does not have necessary features",
											"Error", JOptionPane.ERROR_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(frame, "Wrong username/password", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						return null;
					}
				};

				Window win = SwingUtilities.getWindowAncestor((AbstractButton) e.getSource());
				final JDialog dialog = new JDialog(win, "Login", ModalityType.APPLICATION_MODAL);

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
				panel.add(new JLabel("Logging in .........."), BorderLayout.PAGE_START);
				dialog.add(panel);
				dialog.pack();
				dialog.setLocationRelativeTo(win);
				dialog.setBounds(50, 50, 300, 100);
				dialog.setVisible(true);

			}
		});

		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pack();
		setVisible(true);
	}

	public static boolean checkFeatures(String response) {
		boolean result = false;
		HashMap<String, String> featureList = new HashMap<String, String>();
		JSONObject responseObject;
		try {
			responseObject = new JSONObject(response);
			if (responseObject.get("result").equals("ok")) {
				JSONArray features = responseObject.getJSONArray("features");

				for (int i = 0; i < features.length(); i++) {
					JSONObject feature = features.getJSONObject(i);

					String featureName = feature.getString("name");

					if (featureName.equals("bucket-management") || featureName.equals("inventory-management")
							|| featureName.equals("access-key-management")) {
						System.out.println("Feature: " + feature.getString("name"));
						featureList.put(featureName, feature.getString("name"));

					}
				}

				if (featureList.size() == 3) {
					return true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
