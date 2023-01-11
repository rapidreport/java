package jp.co.systembase.report.renderer.xlsx.elementrenderer;

import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.EvalException;
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
		_renderRect(renderer, reportDesign, region, design);
		Region _region = region.toPointScale(reportDesign);
		if (_region.getWidth() <= 0 || _region.getHeight() <= 0){
			return;
		}
		Field field = new Field();
		field.region = _region;
		field.style = new FieldStyle(new TextDesign(reportDesign, design));
		field.data = _getText(reportDesign, design, data);
		renderer.currentPage.fields.add(field);
	}

	protected void _renderRect(XlsxRenderer renderer, ReportDesign reportDesign, Region region, ElementDesign design) throws Throwable {
		if (!design.isNull("rect")){
			renderer.setting.getElementRenderer("rect").collect(
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

}
