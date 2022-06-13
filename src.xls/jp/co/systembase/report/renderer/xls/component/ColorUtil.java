package jp.co.systembase.report.renderer.xls.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;

import jp.co.systembase.report.renderer.RenderUtil;

public class ColorUtil {

	public static short getIndex(String v){
		short[] t = RenderUtil.getColorTriplet(v);
		if (t != null){
			return getIndex(t);
		}else{
			return -1;
		}
	}

	public static short getIndex(short[] triplet){
		short ret = 0;
		int diff = 0xffffff;
		for(_ColorData c: getColorTable()){
			int _diff = c.getDiff(triplet);
			if (_diff < diff){
				ret = c.index;
				diff = _diff;
			}
		}
		return ret;
	}

	private static class _ColorData{
		public short index;
		public short[] triplet;
		public _ColorData(short index, short[] triplet){
			this.index = index;
			this.triplet = triplet;
		}
		public int getDiff(short[] triplet){
			int r = this.triplet[0] - triplet[0];
			int g = this.triplet[1] - triplet[1];
			int b = this.triplet[2] - triplet[2];
			return r * r + g * g + b * b;
		}
	}

	private static List<_ColorData> colorTable = null;
	private static List<_ColorData> getColorTable(){
		if (colorTable == null){
			colorTable = new ArrayList<_ColorData>();
			colorTable.add(new _ColorData(HSSFColor.AQUA.index, HSSFColor.AQUA.triplet));
			colorTable.add(new _ColorData(HSSFColor.BLACK.index, HSSFColor.BLACK.triplet));
			colorTable.add(new _ColorData(HSSFColor.BLUE.index, HSSFColor.BLUE.triplet));
			colorTable.add(new _ColorData(HSSFColor.BLUE_GREY.index, HSSFColor.BLUE_GREY.triplet));
			colorTable.add(new _ColorData(HSSFColor.BRIGHT_GREEN.index, HSSFColor.BRIGHT_GREEN.triplet));
			colorTable.add(new _ColorData(HSSFColor.BROWN.index, HSSFColor.BROWN.triplet));
			colorTable.add(new _ColorData(HSSFColor.CORAL.index, HSSFColor.CORAL.triplet));
			colorTable.add(new _ColorData(HSSFColor.CORNFLOWER_BLUE.index, HSSFColor.CORNFLOWER_BLUE.triplet));
			colorTable.add(new _ColorData(HSSFColor.DARK_BLUE.index, HSSFColor.DARK_BLUE.triplet));
			colorTable.add(new _ColorData(HSSFColor.DARK_GREEN.index, HSSFColor.DARK_GREEN.triplet));
			colorTable.add(new _ColorData(HSSFColor.DARK_RED.index, HSSFColor.DARK_RED.triplet));
			colorTable.add(new _ColorData(HSSFColor.DARK_TEAL.index, HSSFColor.DARK_TEAL.triplet));
			colorTable.add(new _ColorData(HSSFColor.DARK_YELLOW.index, HSSFColor.DARK_YELLOW.triplet));
			colorTable.add(new _ColorData(HSSFColor.GOLD.index, HSSFColor.GOLD.triplet));
			colorTable.add(new _ColorData(HSSFColor.GREEN.index, HSSFColor.GREEN.triplet));
			colorTable.add(new _ColorData(HSSFColor.GREY_25_PERCENT.index, HSSFColor.GREY_25_PERCENT.triplet));
			colorTable.add(new _ColorData(HSSFColor.GREY_40_PERCENT.index, HSSFColor.GREY_40_PERCENT.triplet));
			colorTable.add(new _ColorData(HSSFColor.GREY_50_PERCENT.index, HSSFColor.GREY_50_PERCENT.triplet));
			colorTable.add(new _ColorData(HSSFColor.GREY_80_PERCENT.index, HSSFColor.GREY_80_PERCENT.triplet));
			colorTable.add(new _ColorData(HSSFColor.INDIGO.index, HSSFColor.INDIGO.triplet));
			colorTable.add(new _ColorData(HSSFColor.LAVENDER.index, HSSFColor.LAVENDER.triplet));
			colorTable.add(new _ColorData(HSSFColor.LEMON_CHIFFON.index, HSSFColor.LEMON_CHIFFON.triplet));
			colorTable.add(new _ColorData(HSSFColor.LIGHT_BLUE.index, HSSFColor.LIGHT_BLUE.triplet));
			colorTable.add(new _ColorData(HSSFColor.LIGHT_CORNFLOWER_BLUE.index, HSSFColor.LIGHT_CORNFLOWER_BLUE.triplet));
			colorTable.add(new _ColorData(HSSFColor.LIGHT_GREEN.index, HSSFColor.LIGHT_GREEN.triplet));
			colorTable.add(new _ColorData(HSSFColor.LIGHT_ORANGE.index, HSSFColor.LIGHT_ORANGE.triplet));
			colorTable.add(new _ColorData(HSSFColor.LIGHT_TURQUOISE.index, HSSFColor.LIGHT_TURQUOISE.triplet));
			colorTable.add(new _ColorData(HSSFColor.LIGHT_YELLOW.index, HSSFColor.LIGHT_YELLOW.triplet));
			colorTable.add(new _ColorData(HSSFColor.LIME.index, HSSFColor.LIME.triplet));
			colorTable.add(new _ColorData(HSSFColor.MAROON.index, HSSFColor.MAROON.triplet));
			colorTable.add(new _ColorData(HSSFColor.OLIVE_GREEN.index, HSSFColor.OLIVE_GREEN.triplet));
			colorTable.add(new _ColorData(HSSFColor.ORANGE.index, HSSFColor.ORANGE.triplet));
			colorTable.add(new _ColorData(HSSFColor.ORCHID.index, HSSFColor.ORCHID.triplet));
			colorTable.add(new _ColorData(HSSFColor.PALE_BLUE.index, HSSFColor.PALE_BLUE.triplet));
			colorTable.add(new _ColorData(HSSFColor.PINK.index, HSSFColor.PINK.triplet));
			colorTable.add(new _ColorData(HSSFColor.PLUM.index, HSSFColor.PLUM.triplet));
			colorTable.add(new _ColorData(HSSFColor.RED.index, HSSFColor.RED.triplet));
			colorTable.add(new _ColorData(HSSFColor.ROSE.index, HSSFColor.ROSE.triplet));
			colorTable.add(new _ColorData(HSSFColor.ROYAL_BLUE.index, HSSFColor.ROYAL_BLUE.triplet));
			colorTable.add(new _ColorData(HSSFColor.SEA_GREEN.index, HSSFColor.SEA_GREEN.triplet));
			colorTable.add(new _ColorData(HSSFColor.SKY_BLUE.index, HSSFColor.SKY_BLUE.triplet));
			colorTable.add(new _ColorData(HSSFColor.TAN.index, HSSFColor.TAN.triplet));
			colorTable.add(new _ColorData(HSSFColor.TEAL.index, HSSFColor.TEAL.triplet));
			colorTable.add(new _ColorData(HSSFColor.TURQUOISE.index, HSSFColor.TURQUOISE.triplet));
			colorTable.add(new _ColorData(HSSFColor.VIOLET.index, HSSFColor.VIOLET.triplet));
			colorTable.add(new _ColorData(HSSFColor.WHITE.index, HSSFColor.WHITE.triplet));
			colorTable.add(new _ColorData(HSSFColor.YELLOW.index, HSSFColor.YELLOW.triplet));
		}
		return colorTable;
	}

}
