package jp.co.systembase.report.renderer.pdf.elementrenderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.pdf.PdfContentByte;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.RenderUtil;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class CircleRenderer implements IElementRenderer {

	public void render(
			PdfRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Region _region = region.toPointScale(reportDesign);
		PdfContentByte cb = renderer.writer.getDirectContent();
		cb.saveState();
		try{
			boolean stroke = true;
			boolean fill = false;
			float lw = reportDesign.defaultLineWidth;
			if (!design.isNull("line_width")){
				lw = Cast.toFloat(design.get("line_width"));
				if (lw == 0){
					stroke = false;
				}
			}
			if (stroke){
				cb.setLineWidth(lw);
				if (!design.isNull("color")){
					Color c = RenderUtil.getColor((String)design.get("color"));
					if (c != null){
						cb.setColorStroke(c);
					}
				}
				if (!design.isNull("line_pattern")){
					List<Float> pl = new ArrayList<Float>();
					for(String p: ((String)design.get("line_pattern")).split(",")){
						float v = Float.valueOf(p);
						if (v > 0){
							pl.add(v);
						}
					}
					if (pl.size() > 0){
						if (pl.size() % 2 == 1){
							pl.add(0f);
						}
						float l[] = new float[pl.size()];
						for(int i = 0;i < pl.size();i++){
							l[i] = pl.get(i);
						}
						cb.setLineDash(l, 0);
					}
				}else if (!design.isNull("line_style")){
					String ls = (String)design.get("line_style");
					if (ls.equals("dot")){
						cb.setLineDash(new float[]{1 * lw, 1 * lw}, 0);
					}else if (ls.equals("dash")){
						cb.setLineDash(new float[]{3 * lw, 1 * lw}, 0);
					}else if (ls.equals("dashdot")){
						cb.setLineDash(new float[]{3 * lw, 1 * lw, 1 * lw, 1 * lw}, 0);
					}
				}
			}
			if (!design.isNull("fill_color")){
				Color c = RenderUtil.getColor((String)design.get("fill_color"));
				if (c != null){
					fill = true;
					cb.setColorFill(c);
				}
			}
			if (stroke || fill){
				cb.ellipse(
					renderer.trans.x(_region.left),
					renderer.trans.y(_region.bottom),
					renderer.trans.x(_region.right),
					renderer.trans.y(_region.top));
				if (stroke && fill){
					cb.fillStroke();
				}else if (stroke){
					cb.stroke();
				}else if (fill){
					cb.fill();
				}
			}
		}finally{
			cb.restoreState();
		}
	}

}
