package jp.co.systembase.report;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.component.ContentDesign;
import jp.co.systembase.report.component.ContentHistory;
import jp.co.systembase.report.component.CustomField;
import jp.co.systembase.report.component.DataCache;
import jp.co.systembase.report.component.GroupDesign;
import jp.co.systembase.report.component.GroupRange;
import jp.co.systembase.report.component.Groups;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.customizer.IReportCustomizer;
import jp.co.systembase.report.data.IGroupDataProvider;
import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.internal.WrapperDataSource;
import jp.co.systembase.report.renderer.IRenderer;
import jp.co.systembase.report.scanner.PagingScanner;

public class Report {

	public static final float PAPER_WIDTH_MAX = 4999f;
	public static final float PAPER_HEIGHT_MAX = 4999f;

	public enum EDirection{
		VERTICAL,
		HORIZONTAL
	}

	public enum EPaperType{
		A3,
		A4,
		A5,
		B4,
		B5,
		CUSTOM
	}

	public enum EScaleUnit{
		POINT,
		MM,
		INCH
	}

	public enum EEvalContext{
		ANY,
		CONTENT,
		PAGE
	}

	public enum EHAlign{
		LEFT,
		CENTER,
		RIGHT
	}

	public enum EVAlign{
		TOP,
		CENTER,
		BOTTOM
	}

	public enum EGroupDataMode{
		DEFAULT_BLANK,
		DEFAULT_DUMMY,
		NO_SPLIT
	}

	public static class Compatibility{
		public static boolean _4_6_PdfFontBold = false;
	}

	public static Map<String, ContentDesign> sharedContents = new HashMap<String, ContentDesign>();
	
	public ReportDesign design;
	public IReportCustomizer customizer;
	public ReportData data = null;
	public IGroupDataProvider groupDataProvider = null;
	public Groups groups = null;
	public Map<String, Object> globalScope = new HashMap<String, Object>();
	public DataCache dataCache = new DataCache();
	public boolean filled = false;
	public CustomField.Stack customFieldStack = new CustomField.Stack();

	public Map<GroupDesign, WrapperDataSource> wrapperDataSourceMap =
		new HashMap<GroupDesign, WrapperDataSource>();

	private Map<String, ReportPages> _subPageMap = new HashMap<String, ReportPages>();

	public Report(Map<?, ?> desc){
		this(desc, (IReportCustomizer)null);
	}

	public Report(Map<?, ?> desc, ReportSetting setting){
		this(desc, setting, null);
	}

	public Report(ReportDesign design){
		this(design, null);
	}

	public Report(Map<?, ?> desc, IReportCustomizer customizer){
		this(new ReportDesign(desc), customizer);
	}

	public Report(Map<?, ?> desc, ReportSetting setting, IReportCustomizer customizer){
		this(new ReportDesign(desc, setting), customizer);
	}

	public Report(ReportDesign design, IReportCustomizer customizer){
		this.design = design;
		this.customizer = customizer;
	}

	public void fill(IReportDataSource dataSource){
    	this.fill(dataSource, null);
    }

    public void fill(IReportDataSource dataSource, IGroupDataProvider groupDataProvider){
    	this.groupDataProvider = groupDataProvider;
		this.data = new ReportData(dataSource, this, null);
		this.groups = new Groups(this.design.groupDesign, this, null);
		this.groups.fill(this.data);
		this.filled = true;
	}

	public ReportPages getPages() {
		ReportPages ret = new ReportPages(this);
		ContentHistory fch = this.groups.getNextContentHistory(null);
		Region paperRegion = this.design.paperDesign.getRegion();
		while(fch != null){
			GroupRange range = null;
			PagingScanner scanner = null;
			GroupRange rangeAux = new GroupRange(this.groups, fch, fch);
			PagingScanner scannerAux = new PagingScanner();
			this.groups.scan(scannerAux, rangeAux, paperRegion);
			ContentHistory lch = this.groups.getNextContentHistory(fch);
			while(lch != null){
				GroupRange _range = new GroupRange(this.groups, fch, lch);
				PagingScanner _scanner = new PagingScanner();
				this.groups.scan(_scanner, _range, paperRegion);
				if (_scanner.broken){
					break;
				}
				if (this.design.pageCapacity > 0 && this.design.pageCapacity < _scanner.weight){
					break;
				}
				lch = this.groups.getNextContentHistory(lch);
				rangeAux = _range;
				scannerAux = _scanner;
				if (!_range.unbreakable() || lch == null){
					range = rangeAux;
					scanner = scannerAux;
				}
			}
			if (range == null){
				range = rangeAux;
				scanner = scannerAux;
			}
			ReportPage page = new ReportPage(this, range, scanner);
			if (ret.isEmpty() && page.report.design.resetPageCount){
				page.resetPageCount = true;
			}else{
				for(GroupDesign gd: scanner.startedGroups.keySet()){
					if (gd.resetPageCount){
						page.resetPageCount = true;
						break;
					}
				}
			}
			ret.add(page);
			if (this.customizer != null){
				this.customizer.pageAdded(this, ret, page);
			}
			if (range.last == null){
				fch = null;
			}else{
				fch = this.groups.getNextContentHistory(range.last);
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		if (this.design.caption != null){
			return this.design.caption;
		}else if (this.design.id != null){
			return this.design.id;
		}else{
			return "(report)";
		}
	}

	public void addSubPages(String key, ReportPages pages){
		pages.setUpCountingPages();
		this._subPageMap.put(key, pages);
	}

	public void renderSubPage(IRenderer renderer, Region region, String key, int index) throws Throwable{
		if (!this._subPageMap.containsKey(key)){
			return;
		}
		ReportPages pages = this._subPageMap.get(key);
		pages.get(index).renderSubPage(renderer, pages, region);
	}
	
	public static void addSharedContent(String id, ReportDesign reportDesign){
		ContentDesign cd = reportDesign.findContentDesign(id);
		sharedContents.put(id, cd);
	}

}
