package jp.co.systembase.report.renderer.xls.elementrenderer;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.renderer.RenderUtil;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xls.component.Field;
import jp.co.systembase.report.renderer.xls.component.FieldStyle;

public class FieldRenderer implements IElementRenderer {

	public void collect(
			XlsRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		if (!design.isNull("rect")){
			renderer.setting.getElementRenderer("rect").collect(
			  renderer, 
			  reportDesign, 
			  region, 
			  design.child("rect"), 
			  null);
		}
		Region _region = region.toPointScale(reportDesign);
		if (_region.getWidth() <= 0 || _region.getHeight() <= 0){
			return;
		}
		Field field = new Field();
		field.region = _region;
		field.style = new FieldStyle(new TextDesign(reportDesign, design));
		if (data != null){
			if (field.style.textDesign.xlsFormat != null){
				field.data = data;
			}else{
				field.data = RenderUtil.format(reportDesign, design.child("formatter"), data);
			}
		}

		renderer.currentPage.fields.add(field);
	}

}
