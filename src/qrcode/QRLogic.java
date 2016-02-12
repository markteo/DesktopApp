package qrcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRLogic {
	public void generateQR(JSONObject jsonObject, String fileLocation) {
		String dataText = jsonObject.toString();
		Date date = new Date();
		String newDate = new SimpleDateFormat("yyyy-MM-dd h-m-a").format(date);
		String filePath = fileLocation + "/qrcode-" + newDate + ".png";
		int size = 175;
		String fileType = "png";
		File myFile = new File(filePath);
		try {
			Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(dataText,BarcodeFormat.QR_CODE, size, size, hintMap);
			int Width = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(Width, Width,
					BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, Width, Width);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < Width; i++) {
				for (int j = 0; j < Width; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			
			Graphics g = image.getGraphics();
			g.setFont(g.getFont().deriveFont(15f));
			g.setColor(Color.BLACK);
			try {
				g.drawString(jsonObject.getString("registrationNumber"), 20, 170);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			g.dispose();
			
			ImageIO.write(image, fileType, myFile);

		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
