package jp.co.systembase.report.renderer.pdf.elementrenderer;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.renderer.RenderUtil;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.PdfText;

public class FieldRenderer implements IElementRenderer {

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
		String text = RenderUtil.format(reportDesign, design.child("formatter"), data);
		if (text == null){
			return;
		}
		if (renderer.setting.replaceBackslashToYen){
			text = text.replaceAll("\\\\", "\u00a5");
		}
		Region _region = region.toPointScale(reportDesign);
		PdfText pdfText = new PdfText(
				renderer,
				_region,
				new TextDesign(reportDesign, design),
				text);
		pdfText.draw();
	}
}
