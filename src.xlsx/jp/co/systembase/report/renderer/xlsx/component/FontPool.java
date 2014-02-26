package jp.co.systembase.report.renderer.xlsx.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

import org.apache.poi.xssf.usermodel.XSSFFont;


public class FontPool {

	public XlsxRenderer renderer;
	public Map<FontStyle, XSSFFont> map = new HashMap<FontStyle, XSSFFont>();

	public FontPool(XlsxRenderer renderer){
		this.renderer = renderer;
	}

	public XSSFFont get(FontStyle fontStyle){
		if (!this.map.containsKey(fontStyle)){
			this.map.put(fontStyle, this.createFont(fontStyle));
		}
		return this.map.get(fontStyle);
	}

	private XSSFFont createFont(FontStyle fontStyle){
		XSSFFont ret = this.renderer.workbook.createFont();
		fontStyle.fill(ret, this.renderer);
		return ret;
	}

}
