package jp.co.systembase.report.renderer.xls.elementrenderer;

import java.awt.image.BufferedImage;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xls.component.Page;
import jp.co.systembase.report.renderer.xls.component.Shape;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;

public class ImageRenderer implements IElementRenderer {

	public void collect(
			XlsRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Region _region = region.toPointScale(reportDesign);
		BufferedImage image = null;
		if (!design.isNull("key") && data != null){
			String key = (String)design.get("key");
			if (renderer.imageLoaderMap.containsKey(key)){
				image = renderer.imageLoaderMap.get(key).getImage(data);
			}
		}
		if (image == null){
			image = renderer.getImage(reportDesign, design.base, "image");
		}
		Shape shape = new Shape();
		shape.region = _region;
		shape.renderer = new ImageShapeRenderer(image);
		renderer.currentPage.shapes.add(shape);
	}

	public static class ImageShapeRenderer implements IShapeRenderer{
		public BufferedImage image;
		public ImageShapeRenderer(BufferedImage image){
			this.image = image;
		}
		public void render(Page page, Shape shape) {
			int index = page.renderer.getImageIndex(this.image);
			if (index > 0){
				HSSFPatriarch p = page.renderer.sheet.getDrawingPatriarch();
				p.createPicture(shape.getHSSFClientAnchor(page.topRow), index);
			}
		}
	}

}
