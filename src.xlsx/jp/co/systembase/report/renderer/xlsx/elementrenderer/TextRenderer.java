package jp.co.systembase.report.renderer.xlsx.elementrenderer;

import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.expression.EmbeddedTextProcessor;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;
import jp.co.systembase.report.renderer.xlsx.component.Field;
import jp.co.systembase.report.renderer.xlsx.component.FieldStyle;

public class TextRenderer implements IElementRenderer {

	public void collect(
			XlsxRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Region _region = region.toPointScale(reportDesign);
		if (_region.getWidth() <= 0 || _region.getHeight() <= 0){
			return;
		}
		Field field = new Field();
		field.region = _region;
		field.style = new FieldStyle(new TextDesign(reportDesign, design));
		String text = (String)design.get("text");
		if (data != null){
			EmbeddedTextProcessor textProcessor = new EmbeddedTextProcessor();
			text = textProcessor.embedData(reportDesign, design.child("formatter"), text, (List<?>)data);
		}
		field.data = text;
		renderer.currentPage.fields.add(field);
	}

}
