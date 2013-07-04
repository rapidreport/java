package jp.co.systembase.report.renderer.pdf.barcode;

import java.util.Hashtable;

import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;

public class QRCode {

	public static Image getImage(
			PdfContentByte cb, 
			Region region, 
			ElementDesign design, 
			String code) throws Throwable{
		QRCodeWriter w = new QRCodeWriter();
		Hashtable<EncodeHintType, Object> h = new Hashtable<EncodeHintType, Object>();
		if (!design.isNull("qr_charset")){
			h.put(EncodeHintType.CHARACTER_SET, design.get("qr_charset"));
		}else{
			h.put(EncodeHintType.CHARACTER_SET, "shift_jis");
		}
		if (!design.isNull("qr_correction_level")){
			String l = (String)design.get("qr_correction_level");
			if (l.equals("L")){
				h.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			}else if(l.equals("Q")){
				h.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
			}else if(l.equals("H")){
				h.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			}else{
				h.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			}
		}else{
			h.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		}
		BitMatrix bm =  w.encode(code, BarcodeFormat.QR_CODE, 0, 0, h);
		PdfTemplate tmp = cb.createTemplate(bm.getWidth(), bm.getHeight());
		for(int y = 0;y < bm.getHeight();y++){
			for(int x = 0;x < bm.getWidth();x++){
				if (bm.get(x, y)){
					tmp.rectangle(x, bm.getHeight() - y, 1, 1);
				}
			}
		}
		tmp.fill();
		return Image.getInstance(tmp);
	}

}
