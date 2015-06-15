package jp.co.systembase.report.renderer.xlsx.component;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFColor;

import jp.co.systembase.report.renderer.RenderUtil;

public class ColorUtil {

	public static short[] getTriplet(String v){
		if (v.startsWith("#") && v.length() == 7){
			String _v = v.substring(1).toLowerCase();
			for(int i = 0;i < 6;i++){
				if ("0123456789abcdef".indexOf(_v.charAt(i)) < 0){
					return null;
				}
			}
			return new short[] {
					Short.parseShort(_v.substring(0, 2), 16),
					Short.parseShort(_v.substring(2, 4), 16),
					Short.parseShort(_v.substring(4, 6), 16)};
		}else{
			Map<String, short[]> colorMap = RenderUtil.getColorMap();
			if (colorMap.containsKey(v)){
				return new short[] {
						colorMap.get(v)[0],
						colorMap.get(v)[1],
						colorMap.get(v)[2]};
			}
		}
		return null;
	}

	public static XSSFColor getColor(String v){
		short t[] = getTriplet(v);
		if (t != null){
			if (t[0] == 255 && t[1] == 255 && t[2] == 255){
				return new XSSFColor(new byte[] {-2, -2, -2});
			}else{
				return new XSSFColor(new byte[] {(byte)t[0], (byte)t[1], (byte)t[2]});
			}
		}else{
			return null;
		}
	}

}
