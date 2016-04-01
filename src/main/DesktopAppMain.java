package main;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

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

import ui.frame.UILogin;

public class DesktopAppMain {

	public static JFrame initFrame;

	public static void main(String args[]) {
		
		Data.fieldNames = new HashMap<String, String>();
		Data.reverseNames = new HashMap<String, String>();
        String fileName = "messages.en";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	String[] lineSplit = line.split("\\=");
            	if(lineSplit.length == 2){
            		Data.fieldNames.put(lineSplit[0].trim(), lineSplit[1].trim());
            		Data.reverseNames.put(lineSplit[1].trim(), lineSplit[0].trim());
            	}
            	
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }

		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		initFrame = new JFrame("KAI Tool QR Generator");
		JButton showWaitBtn = new JButton(new ShowWaitAction("Start"));
		initFrame.add(showWaitBtn, BorderLayout.CENTER);
		initFrame.setSize(150, 150);
		initFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initFrame.setLocation(dim.width / 2 - initFrame.getSize().width / 2,
				dim.height / 2 - initFrame.getSize().height / 2);
		initFrame.setVisible(true);

		// Data.loadingFrame.setVisible(true);
		// Data.loadingScreen = new JDialog(Data.loadingFrame, "Busy",
		// ModalityType.DOCUMENT_MODAL);
		// Data.loadingScreen.setSize(200, 150);
		// Data.loadingScreen.setLocationRelativeTo(Data.loadingFrame);
	}

	public static boolean checkResult(String response) {
		boolean result = false;

		JSONObject responseObject;
		try {
			responseObject = new JSONObject(response);
			if (responseObject.get("result").equals("ok")) {
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
				Data.loginFrame = new UILogin();
				Data.loginFrame.setVisible(true);
				DesktopAppMain.initFrame.setVisible(false);
				return null;
			}
		};

		Window win = SwingUtilities.getWindowAncestor((AbstractButton) evt.getSource());
		final JDialog dialog = new JDialog(win, "Loading", ModalityType.APPLICATION_MODAL);

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
		dialog.setBounds(50, 50, 300, 100);
		dialog.setLocationRelativeTo(Data.loginFrame);
		dialog.setVisible(true);
	}
}
