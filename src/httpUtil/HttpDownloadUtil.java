package httpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloadUtil {
	private HttpURLConnection httpConn;
	private InputStream inputStream;
	private String fileName;
	private int contentLength;

	public void downloadFile(String fileURL) throws IOException {
		URL url = new URL(fileURL);
		httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header
				int index = disposition.indexOf("filename");
				if (index > 0) {
					fileName = disposition.substring(index+9, disposition.length());
				}
			} else {
				fileName = fileURL.substring(fileURL.lastIndexOf("/"), fileURL.length());
			}

			inputStream = httpConn.getInputStream();
		} else {
			throw new IOException("No file to download, Server replied with HTTP Code:" + responseCode);
		}
	}

	public void disconnect() throws IOException {
		inputStream.close();
		httpConn.disconnect();
	}

	public String getFileName() {
		return this.fileName;
	}

	public int getContentLength() {
		return this.contentLength;
	}

	public InputStream getInputStream() {
		return this.inputStream;
	}
}
