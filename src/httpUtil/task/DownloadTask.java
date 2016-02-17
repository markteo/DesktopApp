package httpUtil.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import httpUtil.HttpDownloadUtil;
import testUI.UIFileDownloadHTTP;

public class DownloadTask extends SwingWorker<Void, Void> {

	private static final int BUFFER_SIZE = 4096;
	private String downloadURL;
	private String saveDirectory;
	private UIFileDownloadHTTP gui;

	public DownloadTask(UIFileDownloadHTTP gui, String downloadURL, String saveDirectory) {
		// TODO Auto-generated constructor stub
		this.gui = gui;
		this.downloadURL = downloadURL;
		this.saveDirectory = saveDirectory;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		try {
			HttpDownloadUtil util = new HttpDownloadUtil();
			util.downloadFile(downloadURL);
			
			gui.setFileInfo(util.getFileName(),util.getContentLength());
			
			String saveFilePath = saveDirectory + File.separator + util.getFileName();

			InputStream is = util.getInputStream();
			// Opens output stream to save file
			FileOutputStream os = new FileOutputStream(saveFilePath);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			long totalBytesRead = 0;
			int percentCompleted = 0;
			long fileSize = util.getContentLength();

			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
				percentCompleted = (int) (totalBytesRead * 100 / fileSize);

				setProgress(percentCompleted);
			}

			os.close();
			util.disconnect();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(gui, "Error downloading file: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			setProgress(0);
			cancel(true);
		}
		return null;
	}

	@Override
	protected void done() {
		if (!isCancelled()) {
			JOptionPane.showMessageDialog(gui, "File has been downloaded successfully!", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
