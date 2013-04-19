package jp.co.systembase.report.renderer.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.PaperDesign;
import jp.co.systembase.report.component.PaperMarginDesign;
import jp.co.systembase.report.component.PaperSizeDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.IRenderer;
import jp.co.systembase.report.renderer.pdf.imageloader.IPdfImageLoader;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.RectangleReadOnly;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfWriter;

public class PdfRenderer implements IRenderer {

	public PdfRendererSetting setting;
	public Map<Object, IPdfImageLoader> imageLoaderMap = new HashMap<Object, IPdfImageLoader>();
	public Document document;
	public PdfWriter writer;
	public Trans trans;

	private Map<Map<?, ?>, Map<String, Image>> imageCache =
		new HashMap<Map<?, ?>, Map<String, Image>>();

	public static class Trans{
		public float paperHeight;
		public float marginLeft;
		public float marginTop;
		public Trans(float paperHeight, float marginLeft, float marginTop){
			this.paperHeight = paperHeight;
			this.marginLeft = marginLeft;
			this.marginTop = marginTop;
		}
		public float x(float _x){
			return _x + this.marginLeft;
		}
		public float y(float _y){
			return this.paperHeight - (_y + this.marginTop);
		}
	}

	public PdfRenderer(OutputStream os) throws DocumentException, IOException{
		this(os, new PdfRendererSetting());
	}

	public PdfRenderer(OutputStream os, PdfRendererSetting setting) throws DocumentException, IOException{
		this.setting = setting;
		this.document = new Document();
		this.writer = PdfWriter.getInstance(this.document, os);
		this.writer.addViewerPreference(PdfName.PRINTSCALING, PdfName.NONE);
	}

	public boolean clipMargin() {
		return false;
	}

	public void beginReport(ReportDesign reportDesign) throws Throwable {
		this.document.open();
	}

	public void endReport(ReportDesign reportDesign) throws Throwable {
		this.document.close();
	}

	public void beginPage(ReportDesign reportDesign, int pageIndex, Region paperRegion) throws Throwable {
		PaperDesign pd = reportDesign.paperDesign;
		PaperSizeDesign s = pd.getActualSize().toPoint(pd);
		PaperMarginDesign m = pd.margin.toPoint(pd);
		if (m.oddReverse && (pageIndex % 2 == 1)){
			this.trans = new Trans(s.height, m.right, m.top);
		}else{
			this.trans = new Trans(s.height, m.left, m.top);
		}
		this.document.setPageSize(new RectangleReadOnly(s.width, s.height));
		this.document.setMargins(0, 0, 0, 0);
		this.document.newPage();
	}

	public void endPage(ReportDesign reportDesign) throws Throwable {
	}

	public void renderElement(
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		this.setting.getElementRenderer(
				(String)design.get("type")).render(this, reportDesign, region, design, data);
	}

	public Image getImage(ReportDesign reportDesign, Map<?, ?> desc, String key){
		if (!this.imageCache.containsKey(desc) || !this.imageCache.get(desc).containsKey(key)){
			this.createImage(reportDesign, desc, key);
		}
		return this.imageCache.get(desc).get(key);
	}

	private void createImage(ReportDesign reportDesign, Map<?, ?> desc, String key){
		if (!this.imageCache.containsKey(desc)){
			this.imageCache.put(desc, new HashMap<String, Image>());
		}
		if (desc.containsKey(key)){
			try{
				Image image = Image.getInstance(reportDesign.getImage(desc, key), null);
				this.imageCache.get(desc).put(key, image);
			}catch(Exception e){
				this.imageCache.get(desc).put(key, null);
			}
		}else{
			this.imageCache.get(desc).put(key, null);
		}
	}

}
