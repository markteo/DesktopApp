package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.json.JSONException;
import org.json.JSONObject;

import ui.UILicenseDetail;
import ui.UILogin;

public class DesktopAppMain{
	
	public static JFrame frame;

	public static void main(String args[]) {
		frame = new JFrame("KAI Tool QR Generator");
		JButton showWaitBtn = new JButton(
				new ShowWaitAction("Start"));
	    frame.add(showWaitBtn, BorderLayout.CENTER);
	    frame.setSize(300, 95);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);

//		Data.loadingFrame.setVisible(true);
//		Data.loadingScreen = new JDialog(Data.loadingFrame, "Busy", ModalityType.DOCUMENT_MODAL);
//		Data.loadingScreen.setSize(200, 150);
//		Data.loadingScreen.setLocationRelativeTo(Data.loadingFrame);
		

	}
	
	public static boolean checkResult (String response){
		boolean result = false;
		
		JSONObject responseObject;
		try {
			responseObject = new JSONObject(response);
			if(responseObject.get("result").equals("ok")){
				result = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
}
class ShowWaitAction extends AbstractAction {

	public ShowWaitAction(String name) {
		super(name);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {

				Data.uiLogin = new UILogin();
				Data.uiLogin.runLogin();
				DesktopAppMain.frame.setVisible(false);
				
				return null;
			}
		};

		Window win = SwingUtilities.getWindowAncestor((AbstractButton) evt
				.getSource());
		final JDialog dialog = new JDialog(win, "Loading",
				ModalityType.APPLICATION_MODAL);

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
		panel.add(new JLabel("Initializing......."), BorderLayout.PAGE_START);
		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(win);
		dialog.setVisible(true);
	}
}
