package jp.co.systembase.report.renderer.xls.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.renderer.xls.XlsRenderer;

import org.apache.poi.hssf.usermodel.HSSFPalette;

public class ColorPool {

	public XlsRenderer renderer;
	public Map<String, Short> pool = new HashMap<String, Short>();

	private HSSFPalette palette;
	private short index = 10;

	public ColorPool(XlsRenderer renderer){
		this.renderer = renderer;
		this.palette = this.renderer.workbook.getCustomPalette();
	}

	public short getIndex(String color){
		short[] t = ColorUtil.getTriplet(color);
		if (t != null){
			return this.getIndex(t);
		}else{
			return -1;
		}

	}

	public short getIndex(short[] triplet){
		if (this.renderer.setting.customPalette){
			String key = this.tripretToStr(triplet);
			if (!this.pool.containsKey(key)){
				if (this.index == 64){
					return -1;
				}
				this.palette.setColorAtIndex(
						this.index,
						(byte)triplet[0],
						(byte)triplet[1],
						(byte)triplet[2]);
				this.pool.put(key, index);
				this.index++;
			}
			return this.pool.get(key);
		}else{
			return ColorUtil.getIndex(triplet);
		}
	}

	private String tripretToStr(short[] triplet){
		return String.valueOf(triplet[0]) + ":" +
		       String.valueOf(triplet[1]) + ":" +
		       String.valueOf(triplet[2]);
	}
}
