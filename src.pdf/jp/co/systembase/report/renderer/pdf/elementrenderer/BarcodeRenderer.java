package jp.co.systembase.report.renderer.pdf.elementrenderer;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BarcodeCodabar;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.RenderUtil;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class BarcodeRenderer implements IElementRenderer {

	public void render(
			PdfRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		String code = RenderUtil.format(reportDesign, design.child("formatter"), data);
		if (code == null){
			return;
		}
		Region _region = region.toPointScale(reportDesign);
		Image image = null;
		PdfContentByte cb = renderer.writer.getDirectContent();
		String type = (String)design.get("barcode_type");
		try{
			if (type != null && type.equals("code39")){
				Barcode39 barcode = new Barcode39();
				if (Cast.toBool(design.get("without_text"))){
					barcode.setFont(null);
				}
				if (Cast.toBool(design.get("generate_checksum"))){
					barcode.setGenerateChecksum(true);
				}
				barcode.setCode(code);
				image = barcode.createImageWithBarcode(cb, null, null);
			}else if (type != null && type.equals("ean8")){
				BarcodeEAN barcode = new BarcodeEAN();
				barcode.setCodeType(Barcode.EAN8);
				if (Cast.toBool(design.get("without_text"))){
					barcode.setFont(null);
				}
				if(code.length() == 7){
					barcode.setCode(code + BarcodeEAN.calculateEANParity(code));
					image = barcode.createImageWithBarcode(cb, null, null);
				}else if (code.length() == 8){
					barcode.setCode(code);
					image = barcode.createImageWithBarcode(cb, null, null);
				}
				image = barcode.createImageWithBarcode(cb, null, null);
			}else if (type != null && type.equals("code128")){
				Barcode128 barcode = new Barcode128();
				if (Cast.toBool(design.get("without_text"))){
					barcode.setFont(null);
				}
				barcode.setCode(code);
				image = barcode.createImageWithBarcode(cb, null, null);
			}else if (type != null && type.equals("codabar")){
				BarcodeCodabar barcode = new BarcodeCodabar();
				if (Cast.toBool(design.get("without_text"))){
					barcode.setFont(null);
				}
				if (Cast.toBool(design.get("generate_checksum"))){
					barcode.setGenerateChecksum(true);
				}
				String ss = "A";
				if (!design.isNull("codabar_startstop_code")){
					ss = (String)design.get("codabar_startstop_code");
				}
				barcode.setCode(ss + code + ss);
				image = barcode.createImageWithBarcode(cb, null, null);
			}else if (type != null && type.equals("qrcode")){
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
				image = Image.getInstance(tmp);
			}else{
				BarcodeEAN barcode = new BarcodeEAN();
				barcode.setCodeType(Barcode.EAN13);
				if (Cast.toBool(design.get("without_text"))){
					barcode.setFont(null);
				}
				if(code.length() == 12){
					barcode.setCode(code + BarcodeEAN.calculateEANParity(code));
					image = barcode.createImageWithBarcode(cb, null, null);
				}else if (code.length() == 13){
					barcode.setCode(code);
					image = barcode.createImageWithBarcode(cb, null, null);
				}
			}
		}catch(Exception ex){}
		if (image != null){
			image.scaleAbsolute(_region.getWidth() - 2, _region.getHeight() - 2);
			image.setAbsolutePosition(
					renderer.trans.x(_region.left + 1),
					renderer.trans.y(_region.bottom + 1));
			cb.addImage(image);
		}
	}
}
