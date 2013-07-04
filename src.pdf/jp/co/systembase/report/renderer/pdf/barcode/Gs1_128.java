package jp.co.systembase.report.renderer.pdf.barcode;

import java.awt.Color;
import java.util.List;

import jp.co.systembase.barcode.Code128.ECodeType;
import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;

public class Gs1_128 {

	public static Image getImage(
			PdfContentByte cb,
			Region region,
			ElementDesign design,
			String code) throws Throwable{
		jp.co.systembase.barcode.Gs1_128 barcode =
				new jp.co.systembase.barcode.Gs1_128();
		if (Cast.toBool(design.get("without_text"))) {
			barcode.withText = false;
		}
		if (Cast.toBool(design.get("gs1_conveni"))) {
			barcode.conveniFormat = true;
		}
		if (code == null || code.length() == 0){
			return null;
		}
		float w = region.getWidth();
		float h = region.getHeight();
		float _h = h;
		if (barcode.withText){
			if (barcode.conveniFormat){
				_h *= 0.5f;
			}else{
				_h *= 0.7f;
			}
		}
		if (w <= 0 || h <= 0){
			return null;
		}
		String _data = code;
		barcode.validate(_data);
		if (barcode.conveniFormat){
			_data = barcode.preprocessConveniData(_data);
		}
		List<Integer> ps = barcode.getCodePoints(barcode.trimData(_data), ECodeType.C);
		PdfTemplate tmp = cb.createTemplate(region.getWidth(), region.getHeight());
		tmp.setColorFill(Color.BLACK);
		float mw = w / ((ps.size() + 1) * 11 + 13);
		boolean draw = true;
		float x = 0;
		for(Byte c: barcode.encode(ps)){
			float dw = c * mw;
			if (draw){
				tmp.rectangle(x, tmp.getHeight() - _h, dw, _h);
			}
			draw = !draw;
			x += dw;
		}
		tmp.fill();
		if (barcode.withText){
			if (barcode.conveniFormat){
				String t = barcode.conveniDisplayFormat(_data);
				String t1 = t.substring(0, 33);
				String t2 = t.substring(33);
				tmp.beginText();
				float fs = BarcodeUtil.getFontSize(tmp, t1);
				BarcodeUtil.setFont(tmp, fs);
				tmp.setTextMatrix(0, tmp.getHeight() - (_h + fs));
				tmp.showText(t1);
				tmp.setTextMatrix(0, tmp.getHeight() - (_h + fs * 2));
				tmp.showText(t2);
				tmp.endText();
			}else{
				String t = barcode.displayFormat(_data);
				BarcodeUtil.renderText(tmp, t);
			}
		}
		return Image.getInstance(tmp);
	}

}
