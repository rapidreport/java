package jp.co.systembase.report.renderer.xlsx.elementrenderer;

import java.awt.image.BufferedImage;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;
import jp.co.systembase.report.renderer.xlsx.component.Page;
import jp.co.systembase.report.renderer.xlsx.component.Shape;

import org.apache.poi.xssf.usermodel.XSSFDrawing;

public class ImageRenderer implements IElementRenderer {

	public void collect(
			XlsxRenderer renderer,
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
			if (index >= 0){
				XSSFDrawing p = page.renderer.sheet.createDrawingPatriarch();
				p.createPicture(shape.getXSSFClientAnchor(page.topRow), index);
			}
		}
	}

}
