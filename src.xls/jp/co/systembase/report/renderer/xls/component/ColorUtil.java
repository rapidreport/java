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
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.AQUA.getIndex(), HSSFColor.HSSFColorPredefined.AQUA.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.BLACK.getIndex(), HSSFColor.HSSFColorPredefined.BLACK.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.BLUE.getIndex(), HSSFColor.HSSFColorPredefined.BLUE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.BLUE_GREY.getIndex(), HSSFColor.HSSFColorPredefined.BLUE_GREY.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.BRIGHT_GREEN.getIndex(), HSSFColor.HSSFColorPredefined.BRIGHT_GREEN.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.BROWN.getIndex(), HSSFColor.HSSFColorPredefined.BROWN.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.CORAL.getIndex(), HSSFColor.HSSFColorPredefined.CORAL.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.CORNFLOWER_BLUE.getIndex(), HSSFColor.HSSFColorPredefined.CORNFLOWER_BLUE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.DARK_BLUE.getIndex(), HSSFColor.HSSFColorPredefined.DARK_BLUE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.DARK_GREEN.getIndex(), HSSFColor.HSSFColorPredefined.DARK_GREEN.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex(), HSSFColor.HSSFColorPredefined.DARK_RED.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.DARK_TEAL.getIndex(), HSSFColor.HSSFColorPredefined.DARK_TEAL.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.DARK_YELLOW.getIndex(), HSSFColor.HSSFColorPredefined.DARK_YELLOW.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.GOLD.getIndex(), HSSFColor.HSSFColorPredefined.GOLD.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.GREEN.getIndex(), HSSFColor.HSSFColorPredefined.GREEN.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex(), HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.getIndex(), HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex(), HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex(), HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.INDIGO.getIndex(), HSSFColor.HSSFColorPredefined.INDIGO.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LAVENDER.getIndex(), HSSFColor.HSSFColorPredefined.LAVENDER.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LEMON_CHIFFON.getIndex(), HSSFColor.HSSFColorPredefined.LEMON_CHIFFON.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex(), HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex(), HSSFColor.HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex(), HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex(), HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LIGHT_TURQUOISE.getIndex(), HSSFColor.HSSFColorPredefined.LIGHT_TURQUOISE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex(), HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.LIME.getIndex(), HSSFColor.HSSFColorPredefined.LIME.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.MAROON.getIndex(), HSSFColor.HSSFColorPredefined.MAROON.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.OLIVE_GREEN.getIndex(), HSSFColor.HSSFColorPredefined.OLIVE_GREEN.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.ORANGE.getIndex(), HSSFColor.HSSFColorPredefined.ORANGE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.ORCHID.getIndex(), HSSFColor.HSSFColorPredefined.ORCHID.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex(), HSSFColor.HSSFColorPredefined.PALE_BLUE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.PINK.getIndex(), HSSFColor.HSSFColorPredefined.PINK.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.PLUM.getIndex(), HSSFColor.HSSFColorPredefined.PLUM.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.RED.getIndex(), HSSFColor.HSSFColorPredefined.RED.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.ROSE.getIndex(), HSSFColor.HSSFColorPredefined.ROSE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex(), HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.SEA_GREEN.getIndex(), HSSFColor.HSSFColorPredefined.SEA_GREEN.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex(), HSSFColor.HSSFColorPredefined.SKY_BLUE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.TAN.getIndex(), HSSFColor.HSSFColorPredefined.TAN.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.TEAL.getIndex(), HSSFColor.HSSFColorPredefined.TEAL.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.TURQUOISE.getIndex(), HSSFColor.HSSFColorPredefined.TURQUOISE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.VIOLET.getIndex(), HSSFColor.HSSFColorPredefined.VIOLET.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.WHITE.getIndex(), HSSFColor.HSSFColorPredefined.WHITE.getTriplet()));
			colorTable.add(new _ColorData(HSSFColor.HSSFColorPredefined.YELLOW.getIndex(), HSSFColor.HSSFColorPredefined.YELLOW.getTriplet()));
		}
		return colorTable;
	}

}
