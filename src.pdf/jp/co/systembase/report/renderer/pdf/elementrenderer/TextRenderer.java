package jp.co.systembase.report.renderer.pdf.elementrenderer;

import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.expression.EmbeddedTextProcessor;
import jp.co.systembase.report.renderer.pdf.PdfRenderUtil;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class TextRenderer implements IElementRenderer {

	public void render(
			PdfRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		String text = (String)design.get("text");
		if (data != null){
			EmbeddedTextProcessor textProcessor = new EmbeddedTextProcessor();
			text = textProcessor.embedData(reportDesign, design.child("formatter"), text, (List<?>)data);
		}
		if (text == null){
			return;
		}
		if (renderer.setting.replaceBackslashToYen){
			text = text.replaceAll("\\\\", "\u00a5");
		}
		Region _region = region.toPointScale(reportDesign);
		PdfRenderUtil.drawText(
				renderer,
				_region,
				new TextDesign(reportDesign, design),
				text);
	}
}
