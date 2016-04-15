package httpUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import main.Data;

public class Upload {

	
	
	public String runUpload(String url, String filePath, String urlParameters){
		String charset = "UTF-8";
		String param = "value";
		File textFile = new File(filePath);
		String boundary = Long.toHexString(System.currentTimeMillis()); 
		String CRLF = "\r\n"; 
		URLConnection connection;
		try {
			connection = new URL(url).openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			
			OutputStream output = connection.getOutputStream();
		    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
		    
		    writer.append("--" + boundary).append(CRLF);
	        writer.append("Content-Disposition: form-data; name=\"session-key\"; ")
	                .append(CRLF);
	        writer.append("Content-Type: text/plain; charset=" + charset).append(
	        		CRLF);
	        writer.append(CRLF);
	        writer.append(Data.sessionKey).append(CRLF);
	        writer.flush();
		    
		   
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"csvFile\"; filename=\"" + textFile.getName() + "\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
		    writer.append(CRLF).flush();
		    Files.copy(textFile.toPath(), output);
		    output.flush(); 
		    writer.append(CRLF).flush();

		    writer.append("--" + boundary + "--").append(CRLF).flush();
		    int responseCode = ((HttpURLConnection) connection).getResponseCode();
			System.out.println(responseCode); 
			
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			System.out.println(response.toString());
			return response.toString();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
