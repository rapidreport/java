package jp.co.systembase.report.renderer.xls.elementrenderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import jp.co.systembase.barcode.Codabar;
import jp.co.systembase.barcode.Code128;
import jp.co.systembase.barcode.Code39;
import jp.co.systembase.barcode.Ean13;
import jp.co.systembase.barcode.Ean8;
import jp.co.systembase.barcode.Gs1128;
import jp.co.systembase.barcode.Itf;
import jp.co.systembase.barcode.YubinCustomer;
import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xls.component.Page;
import jp.co.systembase.report.renderer.xls.component.Shape;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class BarcodeRenderer implements IElementRenderer {

	public void collect(
			XlsRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable{
		Region _region = region.toPointScale(reportDesign);
		ElementDesign fd = design.child("formatter");
		String code = renderer.setting.getTextFormatter((String)fd.get("type")).format(data, fd);
		Shape shape = new Shape();
		shape.region = _region;
		shape.renderer = new BarcodeShapeRenderer(design, code);
		renderer.currentPage.shapes.add(shape);
	}

	public static class BarcodeShapeRenderer implements IShapeRenderer{
		public String code;
		public ElementDesign design;
		public BarcodeShapeRenderer(ElementDesign design, String code){
			this.design = design;
			this.code = code;
		}
		public void render(Page page, Shape shape) {
			if (this.code == null){
				return;
			}
			int scale = 5;
			int width = (int)(shape.region.getWidth() * scale);
			int height = (int)(shape.region.getHeight() * scale);
			if (width == 0 || height == 0) {
				return;
			}
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
			g.setColor(Color.BLACK);
			String type = (String)design.get("barcode_type");
			try{
				if (type != null && type.equals("code39")){
					Code39 barcode = new Code39();
					if (Cast.toBool(design.get("without_text"))){
						barcode.withText = false;
					}
					if (Cast.toBool(design.get("generate_checksum"))){
						barcode.generateCheckSum = true;
					}
					barcode.render(g, 0, 0, image.getWidth(), image.getHeight(), this.code);
				}else if (type != null && type.equals("ean8")){
					Ean8 barcode = new Ean8();
					if (Cast.toBool(design.get("without_text"))){
						barcode.withText = false;
					}
					barcode.render(g, 0, 0, image.getWidth(), image.getHeight(), this.code);
				}else if (type != null && type.equals("code128")){
					Code128 barcode = new Code128();
					if (Cast.toBool(design.get("without_text"))){
						barcode.withText = false;
					}
					barcode.render(g, 0, 0, image.getWidth(), image.getHeight(), this.code);
				}else if (type != null && type.equals("codabar")){
					Codabar barcode = new Codabar();
					if (Cast.toBool(design.get("without_text"))){
						barcode.withText = false;
					}
					if (Cast.toBool(design.get("generate_checksum"))){
						barcode.generateCheckSum = true;
					}
					String ss = "A";
					if (!design.isNull("codabar_startstop_code")){
						ss = (String)design.get("codabar_startstop_code");
					}
					barcode.render(g, 0, 0, image.getWidth(), image.getHeight(), ss + this.code + ss);
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

					BitMatrix bm =  w.encode(this.code, BarcodeFormat.QR_CODE, 0, 0, h);
					int mw = image.getWidth() / bm.getWidth();
					int mh = image.getHeight() / bm.getHeight();
					for(int y = 0;y < bm.getHeight();y++){
						for(int x = 0;x < bm.getWidth();x++){
							if (bm.get(x, y)){
								g.fillRect(x * mw, y * mh, mw, mh);
							}
						}
					}
				} else if (type != null && type.equals("yubin")) {
					YubinCustomer barcode = new YubinCustomer();
					float pt = 10.0f;
					if (!design.isNull("point")) {
						pt = Cast.toFloat(design.get("point"));
					}
					final int dpi = 72 * scale;
					barcode.render(g, 0, 0, image.getWidth(), image.getHeight(), pt, dpi, code);
				} else if (type != null && type.equals("itf")) {
					Itf barcode = new Itf();
					if (Cast.toBool(design.get("without_text"))) {
						barcode.withText = false;
					}
					if (Cast.toBool(design.get("generate_checksum"))) {
						barcode.generateCheckSum = true;
					}
					final int dpi = 72 * scale;
					barcode.render(g, 0, 0, (int)shape.region.getWidth(), (int)shape.region.getHeight(), dpi, code);
				} else if (type != null && type.equals("gs1128")) {
					Gs1128 barcode = new Gs1128();
					if (Cast.toBool(design.get("without_text"))) {
						barcode.withText = false;
					}
					if (Cast.toBool(design.get("convenience_format"))) {
						barcode.isConvenienceFormat = true;
					}
					final int dpi = 72 * scale;
					barcode.render(g, 0, 0, (int)shape.region.getWidth(), (int)shape.region.getHeight(), dpi, code);
				}else{
					Ean13 barcode = new Ean13();
					if (Cast.toBool(design.get("without_text"))){
						barcode.withText = false;
					}
					barcode.render(g, 0, 0, image.getWidth(), image.getHeight(), this.code);
				}
				HSSFPatriarch p = page.renderer.sheet.getDrawingPatriarch();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				BufferedOutputStream os = new BufferedOutputStream(bos);
				image.flush();
				ImageIO.write(image, "png", os);
				os.flush();
				int index = page.renderer.workbook.addPicture(
						bos.toByteArray(),
						HSSFWorkbook.PICTURE_TYPE_PNG);
				p.createPicture(shape.getHSSFClientAnchor(page.topRow), index);
			}catch(Exception e){
			}
		}
	}

}
