package jp.co.systembase.report.renderer.xlsx.component;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFColor;

import jp.co.systembase.report.renderer.RenderUtil;

public class ColorUtil {

	public static byte[] getTriplet(String v){
		if (v.startsWith("#") && v.length() == 7){
			String _v = v.substring(1).toLowerCase();
			for(int i = 0;i < 6;i++){
				if ("0123456789abcdef".indexOf(_v.charAt(i)) < 0){
					return null;
				}
			}
			return new byte[] {
					Byte.parseByte(_v.substring(0, 2), 16),
					Byte.parseByte(_v.substring(2, 4), 16),
					Byte.parseByte(_v.substring(4, 6), 16)};
		}else{
			Map<String, short[]> colorMap = RenderUtil.getColorMap();
			if (colorMap.containsKey(v)){
				return new byte[] {
						(byte)colorMap.get(v)[0],
						(byte)colorMap.get(v)[1],
						(byte)colorMap.get(v)[2]};
			}
		}
		return null;
	}

	public static XSSFColor getColor(String v){
		byte t[] = getTriplet(v);
		if (t != null){
			return new XSSFColor(t);
		}else{
			return null;
		}
	}

}
