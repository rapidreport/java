package jp.co.systembase.report.renderer.pdf.elementrenderer;

import java.awt.Color;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

import com.lowagie.text.pdf.PdfContentByte;

public class DummyRenderer implements IElementRenderer {

	public void render(
			PdfRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Region _region = region.toPointScale(reportDesign);
		PdfContentByte cb = renderer.writer.getDirectContent();
		cb.saveState();
		cb.setColorFill(Color.RED);
		{
			String t = (String)design.get("type");
			if (t != null){
				cb.setFontAndSize(renderer.setting.defaultFont, 10);
				cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
				cb.beginText();
				cb.setTextMatrix(
						renderer.trans.x(_region.left),
						renderer.trans.y(_region.top + 10));
				cb.showText(t);
				cb.endText();
			}
		}
		cb.setLineDash(new float[]{1, 2}, 0);
		cb.setColorStroke(Color.RED);
		cb.rectangle(
				renderer.trans.x(_region.left),
				renderer.trans.y(_region.bottom),
				_region.getWidth(),
				_region.getHeight());
		cb.stroke();
		cb.moveTo(
				renderer.trans.x(_region.left),
				renderer.trans.y(_region.top));
		cb.lineTo(
				renderer.trans.x(_region.right),
				renderer.trans.y(_region.bottom));
		cb.stroke();
		cb.moveTo(
				renderer.trans.x(_region.left),
				renderer.trans.y(_region.bottom));
		cb.lineTo(
				renderer.trans.x(_region.right),
				renderer.trans.y(_region.top));
		cb.stroke();
		cb.restoreState();
	}

}
