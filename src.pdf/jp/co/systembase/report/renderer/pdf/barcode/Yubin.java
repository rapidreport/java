package jp.co.systembase.report.renderer.pdf.barcode;

import java.awt.Color;
import java.util.List;

import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;

public class Yubin {

	public static Image getImage(
			PdfContentByte cb, 
			Region region, 
			ElementDesign design, 
			String code) throws Throwable{
		jp.co.systembase.barcode.Yubin barcode = 
				new jp.co.systembase.barcode.Yubin();
		List<Byte> codes = barcode.encode(code);
		if (codes == null){
			return null;
		}
		float uw = region.getWidth() / (codes.size() * 2);
		float x = 0;
		float y = region.getHeight() / 2;
		PdfTemplate tmp = cb.createTemplate(region.getWidth(), region.getHeight());
		tmp.setColorFill(Color.BLACK);
		for(Byte c: codes){
			float by = 0;
			float bh = 0;
			switch(c){
			case 1:
				by = y - uw * 3;
				bh = uw * 6;
				break;
			case 2:
				by = y - uw;
				bh = uw * 4;
				break;
			case 3:
				by = y - uw * 3;
				bh = uw * 4;
				break;
			case 4:
				by = y - uw;
				bh = uw * 2;
				break;
			}
			tmp.rectangle(x, by, uw, bh);
			x += uw * 2;
		}
		tmp.fill();
		return Image.getInstance(tmp);
	}

}
