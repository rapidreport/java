package jp.co.systembase.report.renderer.xls.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.renderer.xls.XlsRenderer;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;


public class CellStylePool {

	public XlsRenderer renderer;
	public Map<CellStyle, HSSFCellStyle> map = new HashMap<CellStyle, HSSFCellStyle>();

	public CellStylePool(XlsRenderer renderer){
		this.renderer = renderer;
	}

	public HSSFCellStyle get(CellStyle cellStyle){
		if (!this.map.containsKey(cellStyle)){
			this.map.put(cellStyle, this.createStyle(cellStyle));
		}
		return this.map.get(cellStyle);
	}

	private HSSFCellStyle createStyle(CellStyle cellStyle){
		HSSFCellStyle ret = this.renderer.workbook.createCellStyle();
		cellStyle.fill(ret, this.renderer);
		return ret;
	}

}
