package jp.co.systembase.report.renderer.pdf.elementrenderer;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BarcodeCodabar;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.BarcodeInter25;
import com.lowagie.text.pdf.PdfContentByte;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.RenderUtil;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.barcode.Gs1_128;
import jp.co.systembase.report.renderer.pdf.barcode.QRCode;
import jp.co.systembase.report.renderer.pdf.barcode.Yubin;

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
			if (type != null && type.equals("ean8")){
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
			}else if (type != null && type.equals("code39")){
				Barcode39 barcode = new Barcode39();
				if (Cast.toBool(design.get("without_text"))){
					barcode.setFont(null);
				}
				if (Cast.toBool(design.get("generate_checksum"))){
					barcode.setGenerateChecksum(true);
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
				String startCode = "A";
				String stopCode = "A";
				if (!design.isNull("codabar_startstop_code")){
					String ss = (String)design.get("codabar_startstop_code");
					if (ss.length() == 1){
						startCode = ss;
						stopCode = ss;
					}else if (ss.length() > 1){
						startCode = ss.substring(0, 1);
						stopCode = ss.substring(1, 2);
					}
				}
				if (Cast.toBool(design.get("codabar_startstop_show"))){
					barcode.setStartStopText(true);
				}
				barcode.setCode(startCode + code + stopCode);
				image = barcode.createImageWithBarcode(cb, null, null);
			}else if (type != null && type.equals("itf")){
				BarcodeInter25 barcode = new BarcodeInter25();
				if (Cast.toBool(design.get("without_text"))){
					barcode.setFont(null);
				}
				if (Cast.toBool(design.get("generate_checksum"))){
					barcode.setGenerateChecksum(true);
				}
				String _code = code;
				if (barcode.isGenerateChecksum()){
					if (code.length() % 2 == 0){
						_code = "0" + _code;
					}
				}else{
					if (code.length() % 2 == 1){
						_code = "0" + _code;
					}
				}
				barcode.setCode(_code);
				image = barcode.createImageWithBarcode(cb, null, null);
			}else if (type != null && type.equals("code128")){
				Barcode128 barcode = new Barcode128();
				if (Cast.toBool(design.get("without_text"))){
					barcode.setFont(null);
				}
				barcode.setCode(code);
				image = barcode.createImageWithBarcode(cb, null, null);
			}else if (type != null && type.equals("gs1_128")){
				image = Gs1_128.getImage(cb, _region, design, code);
			}else if (type != null && type.equals("yubin")){
				image = Yubin.getImage(cb, _region, design, code);
			}else if (type != null && type.equals("qrcode")){
				image = QRCode.getImage(cb, _region, design, code);
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
			if (!Report.Compatibility._5_13_PdfBarcode) {
				image.scaleAbsolute(_region.getWidth() - 4f, _region.getHeight() - 4f);
				image.setAbsolutePosition(
						renderer.trans.x(_region.left + 2),
						renderer.trans.y(_region.bottom - 2));
			}else {
				image.scaleAbsolute(_region.getWidth() - 2f, _region.getHeight() - 2f);
				image.setAbsolutePosition(
						renderer.trans.x(_region.left + 1),
						renderer.trans.y(_region.bottom + 1));
			}
			cb.addImage(image);
		}
	}
}
