package jp.co.systembase.report.renderer.xls;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PageMargin;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.ReportPage;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.GroupRange;
import jp.co.systembase.report.component.PaperDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.IRenderer;
import jp.co.systembase.report.renderer.RenderException;
import jp.co.systembase.report.renderer.xls.component.Cell;
import jp.co.systembase.report.renderer.xls.component.CellMap;
import jp.co.systembase.report.renderer.xls.component.CellStylePool;
import jp.co.systembase.report.renderer.xls.component.ColorPool;
import jp.co.systembase.report.renderer.xls.component.FontPool;
import jp.co.systembase.report.renderer.xls.component.Page;
import jp.co.systembase.report.renderer.xls.component.RowColUtil;
import jp.co.systembase.report.renderer.xls.component.Shape;
import jp.co.systembase.report.renderer.xls.imageloader.IXlsImageLoader;
import jp.co.systembase.report.scanner.PagingScanner;

public class XlsRenderer implements IRenderer {

	public XlsRendererSetting setting;
	public Map<Object, IXlsImageLoader> imageLoaderMap = new HashMap<Object, IXlsImageLoader>();
	public HSSFWorkbook workbook;
	public HSSFSheet sheet = null;
	public List<Page> pages = null;
	public Page currentPage = null;

	public Map<BufferedImage, Integer> imagePool;
	public CellStylePool cellStylePool;
	public FontPool fontPool;
	public ColorPool colorPool;

	private boolean _sheetMode = false;

	private Map<Map<?, ?>, Map<String, BufferedImage>> imageCache =
			new HashMap<Map<?, ?>, Map<String, BufferedImage>>();

	public XlsRenderer(HSSFWorkbook workbook){
		this(workbook, new XlsRendererSetting());
	}

	public XlsRenderer(HSSFWorkbook workbook, XlsRendererSetting setting){
		this.setting = setting;
		this.workbook = workbook;
		this.cellStylePool = new CellStylePool(this);
		this.fontPool = new FontPool(this);
		this.colorPool = new ColorPool(this);
	}

	public boolean clipMargin() {
		return true;
	}

	public void newSheet(String sheetName){
		this.sheet = this.workbook.createSheet(sheetName);
		this.pages = new ArrayList<Page>();
		this.currentPage = null;
		this.imagePool = new HashMap<BufferedImage, Integer>();
		this.sheet.createDrawingPatriarch();
	}

	public void beginReport(ReportDesign reportDesign) throws Throwable {
		HSSFPrintSetup ps = this.sheet.getPrintSetup();
		switch(reportDesign.paperDesign.paperType){
		case A3:
			ps.setPaperSize(HSSFPrintSetup.A3_PAPERSIZE);
			break;
		case A4:
			ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
			break;
		case A5:
			ps.setPaperSize(HSSFPrintSetup.A5_PAPERSIZE);
			break;
		case B4:
			ps.setPaperSize(HSSFPrintSetup.B4_PAPERSIZE);
			break;
		case B5:
			ps.setPaperSize(HSSFPrintSetup.B5_PAPERSIZE);
			break;
		default:
		}
		ps.setLandscape(reportDesign.paperDesign.landscape);
		ps.setHeaderMargin(0);
		ps.setFooterMargin(0);
		ps.setVResolution(this.setting.vResolution);
		ps.setHResolution(this.setting.hResolution);
		{
			PaperDesign pd = reportDesign.paperDesign;
			this.sheet.setMargin(PageMargin.TOP, pd.toPoint(pd.margin.top) / 72);
			this.sheet.setMargin(PageMargin.BOTTOM, pd.toPoint(pd.margin.bottom) / 72);
			this.sheet.setMargin(PageMargin.LEFT, pd.toPoint(pd.margin.left) / 72);
			this.sheet.setMargin(PageMargin.RIGHT, pd.toPoint(pd.margin.right) / 72);
		}
	}

	public void endReport(ReportDesign reportDesign) throws Throwable {
		List<Float> cols = RowColUtil.createCols(reportDesign, this);
		List<Integer> colWidths = RowColUtil.createColWidths(cols, 1.26f * this.setting.colWidthCoefficient);
		for(int i = 0;i < colWidths.size();i++){
			this.sheet.setColumnWidth(i, colWidths.get(i));
		}
		{
			int topRow = 0;
			for(Page page: this.pages){
				page.topRow = topRow;
				List<Float> rows = RowColUtil.createRows(reportDesign, page);
				List<Short> rowHeights = RowColUtil.createRowHeights(rows, 1.36f * this.setting.rowHeightCoefficient);
				for(int i = 0;i < rowHeights.size();i++){
					this.sheet.createRow(topRow + i).setHeight(rowHeights.get(i));
				}
				CellMap cellMap = new CellMap(rowHeights.size(), colWidths.size() , page);
				for(int r = 0;r < rowHeights.size();r++){
					for(int c = 0;c < colWidths.size();c++){
						Cell cell = cellMap.map.get(r).get(c);
						if (cell != null){
							cell.render(page);
						}
					}
				}
				for(Shape shape: page.shapes){
					shape.renderer.render(page, shape);
				}
				topRow += rowHeights.size();
				if (!_sheetMode){
					this.sheet.setRowBreak(topRow - 1);
				}
			}
			{
				HSSFWorkbook w = this.sheet.getWorkbook();
				w.setPrintArea(w.getSheetIndex(this.sheet), 0, colWidths.size() - 1, 0, topRow - 1);
				this.sheet.setFitToPage(false);
			}
		}
	}

	public void beginPage(ReportDesign reportDesign, int pageIndex, Region paperRegion) throws Throwable {
		this.currentPage = new Page(this, reportDesign, paperRegion);
		this.pages.add(this.currentPage);
	}

	public void endPage(ReportDesign reportDesign) throws Throwable {
	}

	public void renderElement(
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		this.setting.getElementRenderer(
				(String)design.get("type")).collect(this, reportDesign, region, design, data);
	}

	public int getImageIndex(BufferedImage image){
		if (image == null){
			return 0;
		}
		if (!this.imagePool.containsKey(image)){
			try{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(image, "png", bos);
				int index = workbook.addPicture(
						bos.toByteArray(),
						HSSFWorkbook.PICTURE_TYPE_PNG);
				this.imagePool.put(image, index);
			}catch(Exception e){
				return 0;
			}
		}
		return this.imagePool.get(image);
	}

	public BufferedImage getImage(ReportDesign reportDesign, Map<?, ?> desc, String key){
		if (!this.imageCache.containsKey(desc) || !this.imageCache.get(desc).containsKey(key)){
			this.createImage(reportDesign, desc, key);
		}
		return this.imageCache.get(desc).get(key);
	}

	private void createImage(ReportDesign reportDesign, Map<?, ?> desc, String key){
		if (!this.imageCache.containsKey(desc)){
			this.imageCache.put(desc, new HashMap<String, BufferedImage>());
		}
		BufferedImage image = null;
		if (desc.containsKey(key)){
			try{
				byte[] b = reportDesign.getImageBytes(desc, key);
				if (b != null){
					image = ImageIO.read(new ByteArrayInputStream(b));
				}
			}catch(Exception e){}
		}
		this.imageCache.get(desc).put(key, image);
	}

	public void renderSheet(Report report) throws RenderException{
		PagingScanner scanner = new PagingScanner();
		GroupRange range = new GroupRange(report.groups);
		report.groups.scan(scanner, range, report.design.paperDesign.getRegion());
		ReportPage page = new ReportPage(report, range, scanner);
		ReportPages pages = new ReportPages(report);
		pages.add(page);
		try{
			_sheetMode = true;
			pages.render(this);
		}finally{
			_sheetMode = false;
		}
	}

}
