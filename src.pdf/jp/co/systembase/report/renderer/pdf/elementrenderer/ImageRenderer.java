package jp.co.systembase.report.renderer.pdf.elementrenderer;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class ImageRenderer implements IElementRenderer {

	public void render(
			PdfRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Image img = null;
		if (!design.isNull("key") && data != null){
			String key = (String)design.get("key");
			if (renderer.imageLoaderMap.containsKey(key)){
				img = renderer.imageLoaderMap.get(key).getImage(data);
			}
		}
		if (img == null){
			img = renderer.getImage(reportDesign, design.base, "image");
		}
		if (img == null){
			return;
		}
		Region _region = region.toPointScale(reportDesign);
		PdfContentByte cb = renderer.writer.getDirectContent();
		float w = img.getWidth();
		float h = img.getHeight();
		float r = 1.0f;
		if (w > _region.getWidth() || h > _region.getHeight()){
			float rw = _region.getWidth() / w;
			float rh = _region.getHeight() / h;
			r = (rw < rh) ? rw : rh;
		}
		if (Cast.toBool(design.get("fit")) &&
			(w < _region.getWidth() && h < _region.getHeight())){
			float rw = _region.getWidth() / w;
			float rh = _region.getHeight() / h;
			r = (rw < rh) ? rw : rh;
		}
		w *= r;
		h *= r;
		float t = _region.top;
		float l = _region.left;
		if (!design.isNull("valign")){
			String align = (String)design.get("valign");
			if (align.equals("center")){
				t = _region.top + (_region.getHeight() - h) / 2;
			}
			if (align.equals("bottom")){
				t = _region.bottom - h;
			}
		}
		if (!design.isNull("halign")){
			String align = (String)design.get("halign");
			if (align.equals("center")){
				l = _region.left + (_region.getWidth() - w) / 2;
			}
			if (align.equals("right")){
				l = _region.right - w;
			}
		}
		cb.addImage(img, w, 0, 0, h,
				renderer.trans.x(l),
				renderer.trans.y(t + h));
	}

}
