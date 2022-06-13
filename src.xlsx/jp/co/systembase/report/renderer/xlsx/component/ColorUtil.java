package jp.co.systembase.report.renderer.xlsx.component;

import org.apache.poi.xssf.usermodel.XSSFColor;

import jp.co.systembase.report.renderer.RenderUtil;

public class ColorUtil {

	public static XSSFColor getColor(String v){
		short t[] = RenderUtil.getColorTriplet(v);
		if (t != null){
			return new XSSFColor(new byte[] {(byte)t[0], (byte)t[1], (byte)t[2]});
		}else{
			return null;
		}
	}

	public static XSSFColor getFontColor(String v){
		short t[] = RenderUtil.getColorTriplet(v);
		if (t != null){
			if (t[0] == 255 && t[1] == 255 && t[2] == 255){
				return new XSSFColor(new byte[] {-2, -2, -2});
			}else if (t[0] == 0 && t[1] == 0 && t[2] == 0){
				return null;
			}else{
				return new XSSFColor(new byte[] {(byte)t[0], (byte)t[1], (byte)t[2]});
			}
		}else{
			return null;
		}
	}
}
