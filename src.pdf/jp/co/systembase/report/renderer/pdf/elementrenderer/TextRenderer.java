package jp.co.systembase.report.renderer.pdf.elementrenderer;

import java.math.BigDecimal;
import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.expression.EmbeddedTextProcessor;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.PdfText;

public class TextRenderer implements IElementRenderer {

	public void render(
			PdfRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		if (!design.isNull("rect")){
			renderer.setting.getElementRenderer("rect").render(
			  renderer,
			  reportDesign,
			  region,
			  design.child("rect"),
			  null);
		}
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
		Region _region = region;
		if (!design.isNull("margin")){
			ElementDesign m = design.child("margin");
			float ml = 0;
			float mt = 0;
			float mr = 0;
			float mb = 0;
			if (!m.isNull("left")){
				ml = ((BigDecimal)m.get("left")).floatValue();
			}
			if (!m.isNull("top")){
				mt = ((BigDecimal)m.get("top")).floatValue();
			}
			if (!m.isNull("right")){
				mr = ((BigDecimal)m.get("right")).floatValue();
			}
			if (!m.isNull("bottom")){
				mb = ((BigDecimal)m.get("bottom")).floatValue();
			}
			_region = new Region(region, ml, mt, mr, mb);
		}
		PdfText pdfText = new PdfText();
		pdfText.Initialize(renderer, reportDesign, _region, design, text);
		pdfText.draw();
	}
}
