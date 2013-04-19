package jp.co.systembase.report.renderer.pdf.elementrenderer;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.pdf.PdfContentByte;
import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.pdf.PdfRenderUtil;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class LineRenderer implements IElementRenderer {

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
			float lw = reportDesign.defaultLineWidth;
			if (!design.isNull("line_width")){
				lw = ((BigDecimal)design.get("line_width")).floatValue();
				if (lw == 0){
					return;
				}
			}
			cb.setLineWidth(lw);
			if (!design.isNull("color")){
				Color c = PdfRenderUtil.getColor((String)design.get("color"));
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
					cb.setLineDash(new float[]{3, 1 * lw}, 0);
				}else if (ls.equals("dashdot")){
					cb.setLineDash(new float[]{3 * lw, 1 * lw, 1 * lw, 1 * lw}, 0);
				}
			}
			if (Cast.toBool(design.get("doublet"))){
				double r = Math.atan2(_region.getHeight(), _region.getWidth()) + Math.PI / 2;
				float dx = (float)Math.cos(r) * Math.max(lw, 0.7f);
				float dy = (float)Math.sin(r) * Math.max(lw, 0.7f);
				cb.moveTo(
						renderer.trans.x(_region.left + dx),
						renderer.trans.y(_region.top + dy));
				cb.lineTo(
						renderer.trans.x(_region.right + dx),
						renderer.trans.y(_region.bottom + dy));
				cb.moveTo(
						renderer.trans.x(_region.left - dx),
						renderer.trans.y(_region.top - dy));
				cb.lineTo(
						renderer.trans.x(_region.right - dx),
						renderer.trans.y(_region.bottom - dy));
				cb.stroke();
			}else{
				boolean startArrow = false;
				boolean endArrow = false;
				if (!design.isNull("start_cap")){
					String cap = (String)design.get("start_cap");
					if (cap.equals("arrow")){
						startArrow = true;
					}
				}
				if (!design.isNull("end_cap")){
					String cap = (String)design.get("end_cap");
					if (cap.equals("arrow")){
						endArrow = true;
					}
				}
				double r = Math.atan2(_region.getHeight(), _region.getWidth());
				float w = lw + 2;
				if (startArrow){
					cb.moveTo(
							renderer.trans.x(_region.left + tx(r, w * 2, 0)),
							renderer.trans.y(_region.top + ty(r, w * 2, 0)));
				}else{
					cb.moveTo(
							renderer.trans.x(_region.left),
							renderer.trans.y(_region.top));
				}
				if (endArrow){
					cb.lineTo(
							renderer.trans.x(_region.right + tx(r, -w * 2, 0)),
							renderer.trans.y(_region.bottom + ty(r, -w * 2, 0)));
				}else{
					cb.lineTo(
							renderer.trans.x(_region.right),
							renderer.trans.y(_region.bottom));
				}
				cb.stroke();
				if (startArrow){
					cb.moveTo(
							renderer.trans.x(_region.left + tx(r, w * 2, -w)),
							renderer.trans.y(_region.top + ty(r, w * 2, -w)));
					cb.lineTo(
							renderer.trans.x(_region.left),
							renderer.trans.y(_region.top));
					cb.lineTo(
							renderer.trans.x(_region.left + tx(r, w * 2, w)),
							renderer.trans.y(_region.top + ty(r, w * 2, w)));
					cb.fill();
				}
				if (endArrow){
					cb.moveTo(
							renderer.trans.x(_region.right + tx(r, -w * 2, -w)),
							renderer.trans.y(_region.bottom + ty(r, -w * 2, -w)));
					cb.lineTo(
							renderer.trans.x(_region.right),
							renderer.trans.y(_region.bottom));
					cb.lineTo(
							renderer.trans.x(_region.right + tx(r, -w * 2, w)),
							renderer.trans.y(_region.bottom + ty(r, -w * 2, w)));
					cb.fill();
				}
			}
		}finally{
			cb.restoreState();
		}
	}

	private float tx(double r, float x, float y){
		if (x != 0 || y != 0){
			return (float)(x * Math.cos(r) - y * Math.sin(r));
		}else{
			return 0;
		}
	}

	private float ty(double r, float x, float y){
		if (x != 0 || y != 0){
			return (float)(x * Math.sin(r) + y * Math.cos(r));
		}else{
			return 0;
		}
	}

}
