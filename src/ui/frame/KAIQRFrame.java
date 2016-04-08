package ui.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Data;

import org.json.JSONException;
import org.json.JSONObject;

import api.APICall;
import customColor.CustomColor;
import qrcode.JavaQR;
import ui.components.Button;
import ui.components.Label;
import ui.components.Layouts;
import ui.components.Panel;
import ui.panel.UIAccessKeySelect;
import ui.panel.UIBucketSelect;
import ui.panel.UIGenerateKey;
import ui.panel.UIInventorySelect;
import ui.panel.UILicenseAdd;
import ui.panel.UILicenseDetail;

public class KAIQRFrame extends JFrame {
	public UIFileUploadHTTP uiFileUpload;
	public JavaQR qrGenerator;
	public UILicenseDetail uiLicenseDetail;
	public UILicenseAdd uiLicenseAdd;
	public UIAccessKeySelect uiAccessKeySelect;
	public UIGenerateKey uiGenerateKey;
	public UIBucketSelect uiBucketSelect;
	public UIInventorySelect uiInventorySelect;

	public JPanel cardPanel;
	public CardLayout cardLayout;

	private JPanel menuPanel;
	private JButton btnLogOut;

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	static Point mouseDownCompCoords;

	public KAIQRFrame() {
		// init frames
		super("KAI QR Code Generator");
		mouseDownCompCoords = null;
		addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				mouseDownCompCoords = null;
			}

			public void mousePressed(MouseEvent e) {
				mouseDownCompCoords = e.getPoint();
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
			}

			public void mouseDragged(MouseEvent e) {
				Point currCoords = e.getLocationOnScreen();
				setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
			}
		});

		//setUndecorated(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		menuPanel = p.createPanel(Layouts.grid, 5, 1);
		btnLogOut = b.createButton("Logout");
		btnLogOut.setPreferredSize(new Dimension(100, 50));
		btnLogOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				APICall api = new APICall();
				try {
					JSONObject response = new JSONObject(api.logout(Data.targetURL, Data.sessionKey));
					if (response.getString("result").equals("ok")) {
						UILogin login = new UILogin();
						Data.mainFrame.setVisible(false);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menuPanel.add(btnLogOut);

		cardPanel = p.createPanel(Layouts.card);
		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);

		getContentPane().setBackground(CustomColor.NavyBlue.returnColor());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		add(menuPanel, BorderLayout.WEST);
		add(cardPanel, BorderLayout.CENTER);
		pack();
	}

	public void showPanel(String name) {
		cardLayout.show(cardPanel, name);
	}

	public void addPanel(JPanel panel, String pnlName) {
		cardPanel.add(panel, pnlName);
	}
}
