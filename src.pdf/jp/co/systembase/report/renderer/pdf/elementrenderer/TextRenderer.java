package jp.co.systembase.report.renderer.pdf.elementrenderer;

import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.EvalException;
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
		_renderRect(renderer, reportDesign, region, design);
		String text = _getText(reportDesign, design, data);
		if (text == null){
			return;
		}
		if (renderer.setting.replaceBackslashToYen){
			text = text.replaceAll("\\\\", "\u00a5");
		}
		Region _region = _getRegion(reportDesign, design, region);
		PdfText pdfText = _getPdfText(renderer, reportDesign,  _region, design, text);
		pdfText.Initialize(renderer, reportDesign, _region, design, text);
		pdfText.draw();
	}

	protected void _renderRect(PdfRenderer renderer, ReportDesign reportDesign, Region region, ElementDesign design) throws Throwable {
		if (!design.isNull("rect")){
			renderer.setting.getElementRenderer("rect").render(
			renderer,
			reportDesign,
			region,
			design.child("rect"),
			null);
		}
	}

	protected String _getText(ReportDesign reportDesign, ElementDesign design, Object data) throws EvalException {
		String ret = (String)design.get("text");
		if (data != null){
			EmbeddedTextProcessor textProcessor = new EmbeddedTextProcessor();
			ret = textProcessor.embedData(reportDesign, design.child("formatter"), ret, (List<?>)data);
		}
		return ret;
	}

	protected Region _getRegion(ReportDesign reportDesign, ElementDesign design, Region region) {
		Region ret = region;
		if (!design.isNull("margin")){
			ElementDesign m = design.child("margin");
			float ml = 0;
			float mt = 0;
			float mr = 0;
			float mb = 0;
			if (!m.isNull("left")){
				ml = Cast.toFloat(m.get("left"));
			}
			if (!m.isNull("top")){
				mt = Cast.toFloat(m.get("top"));
			}
			if (!m.isNull("right")){
				mr = Cast.toFloat(m.get("right"));
			}
			if (!m.isNull("bottom")){
				mb = Cast.toFloat(m.get("bottom"));
			}
			ret = new Region(region, ml, mt, mr, mb);
		}
		return ret;
	}

	protected PdfText _getPdfText(PdfRenderer renderer, ReportDesign reportDesign, Region region, ElementDesign design, String text){
		return _getPdfText();
	}

	protected PdfText _getPdfText(){
		return new PdfText();
	}

}
