package jp.co.systembase.report.renderer.xls.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.renderer.xls.XlsRenderer;

import org.apache.poi.hssf.usermodel.HSSFFont;


public class FontPool {

	public XlsRenderer renderer;
	public Map<FontStyle, HSSFFont> map = new HashMap<FontStyle, HSSFFont>();

	public FontPool(XlsRenderer renderer){
		this.renderer = renderer;
	}

	public HSSFFont get(FontStyle fontStyle){
		if (!this.map.containsKey(fontStyle)){
			this.map.put(fontStyle, this.createFont(fontStyle));
		}
		return this.map.get(fontStyle);
	}

	private HSSFFont createFont(FontStyle fontStyle){
		HSSFFont ret = this.renderer.workbook.createFont();
		fontStyle.fill(ret, this.renderer);
		return ret;
	}

}
