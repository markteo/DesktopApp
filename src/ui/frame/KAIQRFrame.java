package ui.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

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

	Panel p = new Panel();
	Button b = new Button();
	Label l = new Label();

	public KAIQRFrame() {
		// init frames
		super("KAI QR Code Generator");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		cardPanel = p.createPanel(Layouts.card);
		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);
		//
		// cardPanel.add(uiInventorySelect, "inventory");
		// cardPanel.add(uiFileUpload, "upload");
		// cardPanel.add(uiBucketSelect, "bucket");
		// cardPanel.add(uiLicenseDetail, "license");
		// cardPanel.add(uiAccessKeySelect, "access");
		// cardPanel.add(uiGenerateKey, "generate");
		// cardPanel.add(qrGenerator, "qr");

		getContentPane().setBackground(CustomColor.NavyBlue.returnColor());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
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
