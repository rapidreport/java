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

public class RectRenderer implements IElementRenderer {

	public void render(
			PdfRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Region _region = region.toPointScale(reportDesign);
		PdfContentByte cb = renderer.writer.getDirectContent();
		float x1 = 0;
		float x2 = 0;
		float y1 = 0;
		float y2 = 0;
		{
			x1 = renderer.trans.x(_region.left);
			x2 = renderer.trans.x(_region.right);
			y1 = renderer.trans.y(_region.bottom);
			y2 = renderer.trans.y(_region.top);
		}
		if (x2 < x1 || y2 < y1){
			return;
		}
		float rd = 0;
		boolean t = !Cast.toBool(design.get("hide_top"));
		boolean b = !Cast.toBool(design.get("hide_bottom"));
		boolean l = !Cast.toBool(design.get("hide_left"));
		boolean r = !Cast.toBool(design.get("hide_right"));
		if (!design.isNull("round")){
			rd = Cast.toFloat(design.get("round"));
		}
		if (t && b && l && r){
			cb.saveState();
			try{
				boolean stroke = this.setupStroke(cb, design, reportDesign);
				boolean fill = this.setupFill(cb, design);
				if (stroke || fill){
					if (rd == 0){
						cb.rectangle(x1, y1, x2 - x1, y2 - y1);
					}else{
						cb.roundRectangle(x1, y1, x2 - x1, y2 - y1, rd);
					}
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
		}else{
			float _rd = rd * 0.4477f;
			cb.saveState();
			try{
				if (this.setupFill(cb, design)){
					cb.moveTo(x1 + ((b && l) ? rd : 0), y1);
					cb.lineTo(x2 - ((b && r) ? rd : 0), y1);
					if (rd > 0 && b && r){
						cb.curveTo(x2 - _rd, y1, x2, y1 + _rd, x2, y1 + rd);
					}
					cb.lineTo(x2, y2 - ((t && r) ? rd : 0));
					if (rd > 0 && t && r){
						cb.curveTo(x2, y2 - _rd, x2 - _rd, y2, x2 - rd, y2);
					}
					cb.lineTo(x1 + ((t && l) ? rd : 0), y2);
					if (rd > 0 && t && l){
						cb.curveTo(x1 + _rd, y2, x1, y2 - _rd, x1, y2 - rd);
					}
					cb.lineTo(x1, y1 + ((b && l) ? rd : 0));
					if (rd > 0 && b && l){
						cb.curveTo(x1, y1 + _rd, x1 + _rd, y1, x1 + rd, y1);
					}
					cb.fill();
				}
			}finally{
				cb.restoreState();
			}
			if (t || b || l || r){
				cb.saveState();
				try{
					if (this.setupStroke(cb, design, reportDesign)){
						float lw = 0;
						if (!design.isNull("line_width")){
							lw = Cast.toFloat(design.get("line_width")) / 2;
						}
						cb.moveTo(x1 + (l ? rd : -lw), y1);
						if (b){
							cb.lineTo(x2 + (r ? -rd : lw), y1);
						}else{
							cb.moveTo(x2, y1 - lw);
						}
						if (rd > 0 && b && r){
							cb.curveTo(x2 - _rd, y1, x2, y1 + _rd, x2, y1 + rd);
						}
						if (r){
							cb.lineTo(x2, y2 + (t ? -rd : lw));
						}else{
							cb.moveTo(x2 + lw, y2);
						}
						if (rd > 0 && t && r){
							cb.curveTo(x2, y2 - _rd, x2 - _rd, y2, x2 - rd, y2);
						}
						if (t){
							cb.lineTo(x1 + (l ? rd : -lw), y2);
						}else{
							cb.moveTo(x1, y2 + lw);
						}
						if (rd > 0 && t && l){
							cb.curveTo(x1 + _rd, y2, x1, y2 - _rd, x1, y2 - rd);
						}
						if (l){
							cb.lineTo(x1, y1 + (b ? rd : -lw));
						}else{
							cb.moveTo(x1, y1 - lw);
						}
						if (rd > 0 && b && l){
							cb.curveTo(x1, y1 + _rd, x1 + _rd, y1, x1 + rd, y1);
						}
						cb.stroke();
					}
				}finally{
					cb.restoreState();
				}
			}
		}
	}

	private boolean setupStroke(
			PdfContentByte cb,
			ElementDesign design,
			ReportDesign reportDesign){
		float lw = reportDesign.defaultLineWidth;
		if (!design.isNull("line_width")){
			lw = Cast.toFloat(design.get("line_width"));
			if (lw == 0){
				return false;
			}
		}
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
				cb.setLineDash(new float[]{3, 1 * lw}, 0);
			}else if (ls.equals("dashdot")){
				cb.setLineDash(new float[]{3 * lw, 1 * lw, 1 * lw, 1 * lw}, 0);
			}
		}
		return true;
	}

	private boolean setupFill(PdfContentByte cb, ElementDesign d){
		if (!d.isNull("fill_color")){
			Color c = RenderUtil.getColor((String)d.get("fill_color"));
			if (c != null){
				cb.setColorFill(c);
				return true;
			}
		}
		return false;
	}

}
