package cz.mpelant.droidmotepc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

/**
 * The Class QRGenerator.
 */
public class QRGenerator {

	/** The scale of output image. */
	private int scale;

	/**
	 * Instantiates a new qR generator.
	 * 
	 * @param scale the scale
	 */
	public QRGenerator(int scale) {
		this.scale = scale;
	}

	/**
	 * Encode.
	 * 
	 * @param text the text to encode
	 * @return the image
	 */
	public Image encode(String text) {
		// Encode the text
		QRCode qrcode = new QRCode();
		try {
			Encoder.encode(text, ErrorCorrectionLevel.H, qrcode);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		byte[][] matrix = qrcode.getMatrix().getArray();
		int size = qrcode.getMatrixWidth() * scale;

		// Make the BufferedImage that are to hold the QRCode
		BufferedImage im = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		im.createGraphics();
		Graphics2D g = (Graphics2D) im.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, size, size);

		// BitMatrix for validation
		BitMatrix bm = new BitMatrix(qrcode.getMatrixWidth());

		// paint the image using the ByteMatrik
		for (int h = 0; h < qrcode.getMatrixWidth(); h++) {
			for (int w = 0; w < qrcode.getMatrixWidth(); w++) {
				// Find the colour of the dot
				if (matrix[h][w] == 0)
					g.setColor(Color.WHITE);
				else {
					g.setColor(Color.BLACK);
					bm.set(w, h);// build the BitMatrix
				}

				// Paint the dot
				g.fillRect(w * scale, h * scale, scale, scale);
			}
		}
		return im;
	}
}
