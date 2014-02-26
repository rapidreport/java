package jp.co.systembase.report.renderer.xlsx.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;


public class CellStylePool {

	public XlsxRenderer renderer;
	public Map<CellStyle, XSSFCellStyle> map = new HashMap<CellStyle, XSSFCellStyle>();

	public CellStylePool(XlsxRenderer renderer){
		this.renderer = renderer;
	}

	public XSSFCellStyle get(CellStyle cellStyle){
		if (!this.map.containsKey(cellStyle)){
			this.map.put(cellStyle, this.createStyle(cellStyle));
		}
		return this.map.get(cellStyle);
	}

	private XSSFCellStyle createStyle(CellStyle cellStyle){
		XSSFCellStyle ret = this.renderer.workbook.createCellStyle();
		cellStyle.fill(ret, this.renderer);
		return ret;
	}

}
