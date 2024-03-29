package jp.co.systembase.report.renderer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;

public class RenderUtil {

	private static Map<String, short[]> colorMap = null;
	public synchronized static Map<String, short[]> getColorMap(){
		if (colorMap == null){
			colorMap = new HashMap<String, short[]>();
			colorMap.put("black", new short[]{0x00, 0x00, 0x00});
			colorMap.put("aliceblue", new short[]{0xf0, 0xf8, 0xff});
			colorMap.put("darkcyan", new short[]{0x00, 0x8b, 0x8b});
			colorMap.put("lightyellow", new short[]{0xff, 0xff, 0xe0});
			colorMap.put("coral", new short[]{0xff, 0x7f, 0x50});
			colorMap.put("dimgray", new short[]{0x69, 0x69, 0x69});
			colorMap.put("lavender", new short[]{0xe6, 0xe6, 0xfa});
			colorMap.put("teal", new short[]{0x00, 0x80, 0x80});
			colorMap.put("lightgoldenrodyellow", new short[]{0xfa, 0xfa, 0xd2});
			colorMap.put("tomato", new short[]{0xff, 0x63, 0x47});
			colorMap.put("gray", new short[]{0x80, 0x80, 0x80});
			colorMap.put("lightsteelblue", new short[]{0xb0, 0xc4, 0xde});
			colorMap.put("darkslategray", new short[]{0x2f, 0x4, 0xf4});
			colorMap.put("lemonchiffon", new short[]{0xff, 0xfa, 0xcd});
			colorMap.put("orangered", new short[]{0xff, 0x45, 0x00});
			colorMap.put("darkgray", new short[]{0xa9	, 0xa9, 0xa9});
			colorMap.put("lightslategray", new short[]{0x77, 0x88, 0x99});
			colorMap.put("darkgreen", new short[]{0x00, 0x64, 0x00});
			colorMap.put("wheat", new short[]{0xf5, 0xde, 0xb3});
			colorMap.put("red", new short[]{0xff, 0x00, 0x00});
			colorMap.put("silver", new short[]{0xc0, 0xc0, 0xc0});
			colorMap.put("slategray", new short[]{0x70, 0x80, 0x90});
			colorMap.put("green", new short[]{0x00, 0x80, 0x00});
			colorMap.put("burlywood", new short[]{0xde, 0xb8, 0x87});
			colorMap.put("crimson", new short[]{0xdc, 0x14, 0x3c});
			colorMap.put("lightgray", new short[]{0xd3, 0xd3, 0xd3});
			colorMap.put("steelblue", new short[]{0x46, 0x82, 0xb4});
			colorMap.put("forestgreen", new short[]{0x22, 0x8b, 0x22});
			colorMap.put("tan", new short[]{0xd2, 0xb4, 0x8c});
			colorMap.put("mediumvioletred", new short[]{0xc7, 0x15, 0x85});
			colorMap.put("gainsboro", new short[]{0xdc, 0xdc, 0xdc});
			colorMap.put("royalblue", new short[]{0x41, 0x69, 0xe1});
			colorMap.put("seagreen", new short[]{0x2e, 0x8b, 0x57});
			colorMap.put("khaki", new short[]{0xf0, 0xe6, 0x8c});
			colorMap.put("deeppink", new short[]{0xff, 0x14, 0x93});
			colorMap.put("whitesmoke", new short[]{0xf5, 0xf5, 0xf5});
			colorMap.put("midnightblue", new short[]{0x19, 0x19, 0x70});
			colorMap.put("mediumseagreen", new short[]{0x3c, 0xb3, 0x71});
			colorMap.put("yellow", new short[]{0xff, 0xff, 0x00});
			colorMap.put("hotpink", new short[]{0xff, 0x69, 0xb4});
			colorMap.put("white", new short[]{0xff, 0xff, 0xff});
			colorMap.put("navy", new short[]{0x00, 0x00, 0x80});
			colorMap.put("mediumaquamarine", new short[]{0x66, 0xcd, 0xaa});
			colorMap.put("gold", new short[]{0xff, 0xd7, 0x00});
			colorMap.put("palevioletred", new short[]{0xdb, 0x70, 0x93});
			colorMap.put("snow", new short[]{0xff, 0xfa, 0xfa});
			colorMap.put("darkblue", new short[]{0x00, 0x00, 0x8b});
			colorMap.put("darkseagreen", new short[]{0x8f, 0xbc, 0x8f});
			colorMap.put("orange", new short[]{0xff, 0xa5, 0x00});
			colorMap.put("pink", new short[]{0xff, 0xc0, 0xcb});
			colorMap.put("ghostwhite", new short[]{0xf8, 0xf8, 0xff});
			colorMap.put("mediumblue", new short[]{0x00, 0x00, 0xcd});
			colorMap.put("aquamarine", new short[]{0x7f, 0xff, 0xd4});
			colorMap.put("sandybrown", new short[]{0xf4, 0xa4, 0x60});
			colorMap.put("lightpink", new short[]{0xff, 0xb6, 0xc1});
			colorMap.put("floralwhite", new short[]{0xff, 0xfa, 0xf0});
			colorMap.put("blue", new short[]{0x00, 0x00, 0xff});
			colorMap.put("palegreen", new short[]{0x98, 0xfb, 0x98});
			colorMap.put("darkorange", new short[]{0xff, 0x8c, 0x00});
			colorMap.put("thistle", new short[]{0xd8, 0xbf, 0xd8});
			colorMap.put("linen", new short[]{0xfa, 0xf0, 0xe6});
			colorMap.put("dodgerblue", new short[]{0x1e, 0x90, 0xff});
			colorMap.put("lightgreen", new short[]{0x90, 0xee, 0x90});
			colorMap.put("goldenrod", new short[]{0xda, 0xa5, 0x20});
			colorMap.put("magenta", new short[]{0xff, 0x00, 0xff});
			colorMap.put("antiquewhite", new short[]{0xfa, 0xeb, 0xd7});
			colorMap.put("cornflowerblue", new short[]{0x64, 0x95, 0xed});
			colorMap.put("springgreen", new short[]{0x00, 0xff, 0x7f});
			colorMap.put("peru", new short[]{0xcd, 0x85, 0x3f});
			colorMap.put("fuchsia", new short[]{0xff, 0x00, 0xff});
			colorMap.put("papayawhip", new short[]{0xff, 0xef, 0xd5});
			colorMap.put("deepskyblue", new short[]{0x00, 0xbf, 0xff});
			colorMap.put("mediumspringgreen", new short[]{0x00, 0xfa, 0x9a});
			colorMap.put("darkgoldenrod", new short[]{0xb8, 0x86, 0x0b});
			colorMap.put("violet", new short[]{0xee, 0x82, 0xee});
			colorMap.put("blanchedalmond", new short[]{0xff, 0xeb, 0xcd});
			colorMap.put("lightskyblue", new short[]{0x87, 0xce, 0xfa});
			colorMap.put("lawngreen", new short[]{0x7c, 0xfc, 0x00});
			colorMap.put("chocolate", new short[]{0xd2, 0x69, 0x1e});
			colorMap.put("plum", new short[]{0xdd, 0xa0, 0xdd});
			colorMap.put("bisque", new short[]{0xff, 0xe4, 0xc4});
			colorMap.put("skyblue", new short[]{0x87, 0xce, 0xeb});
			colorMap.put("chartreuse", new short[]{0x7f, 0xff, 0x00});
			colorMap.put("sienna", new short[]{0xa0, 0x52, 0x2d});
			colorMap.put("orchid", new short[]{0xda, 0x70, 0xd6});
			colorMap.put("moccasin", new short[]{0xff, 0xe4, 0xb5});
			colorMap.put("lightblue", new short[]{0xad, 0xd8, 0xe6});
			colorMap.put("greenyellow", new short[]{0xad, 0xff, 0x2f});
			colorMap.put("saddlebrown", new short[]{0x8b, 0x45, 0x13});
			colorMap.put("mediumorchid", new short[]{0xba, 0x55, 0xd3});
			colorMap.put("navajowhite", new short[]{0xff, 0xde, 0xad});
			colorMap.put("powderblue", new short[]{0xb0, 0xe0, 0xe6});
			colorMap.put("lime", new short[]{0x00, 0xff, 0x00});
			colorMap.put("maroon", new short[]{0x80, 0x00, 0x00});
			colorMap.put("darkorchid", new short[]{0x99, 0x32, 0xcc});
			colorMap.put("peachpuff", new short[]{0xff, 0xda, 0xb9});
			colorMap.put("paleturquoise", new short[]{0xaf, 0xee, 0xee});
			colorMap.put("limegreen", new short[]{0x32, 0xcd, 0x32});
			colorMap.put("darkred", new short[]{0x8b, 0x00, 0x00});
			colorMap.put("darkviolet", new short[]{0x94, 0x00, 0xd3});
			colorMap.put("mistyrose", new short[]{0xff, 0xe4, 0xe1});
			colorMap.put("lightcyan", new short[]{0xe0, 0xff, 0xff});
			colorMap.put("yellowgreen", new short[]{0x9a, 0xcd, 0x32});
			colorMap.put("brown", new short[]{0xa5, 0x2a, 0x2a});
			colorMap.put("darkmagenta", new short[]{0x8b, 0x00, 0x8b});
			colorMap.put("lavenderblush", new short[]{0xff, 0xf0, 0xf5});
			colorMap.put("cyan", new short[]{0x00, 0xff, 0xff});
			colorMap.put("darkolivegreen", new short[]{0x55, 0x6b, 0x2f});
			colorMap.put("firebrick", new short[]{0xb2, 0x22, 0x22});
			colorMap.put("purple", new short[]{0x80, 0x00, 0x80});
			colorMap.put("seashell", new short[]{0xff, 0xf5, 0xee});
			colorMap.put("aqua", new short[]{0x00, 0xff, 0xff});
			colorMap.put("olivedrab", new short[]{0x6b, 0x8e, 0x23});
			colorMap.put("indianred", new short[]{0xcd, 0x5c, 0x5c});
			colorMap.put("indigo", new short[]{0x4b, 0x00, 0x82});
			colorMap.put("oldlace", new short[]{0xfd, 0xf5, 0xe6});
			colorMap.put("turquoise", new short[]{0x40, 0xe0, 0xd0});
			colorMap.put("olive", new short[]{0x80, 0x80, 0x00});
			colorMap.put("rosybrown", new short[]{0xbc, 0x8f, 0x8f});
			colorMap.put("darkslateblue", new short[]{0x48, 0x3d, 0x8b});
			colorMap.put("ivory", new short[]{0xff, 0xff, 0xf0});
			colorMap.put("mediumturquoise", new short[]{0x48, 0xd1, 0xcc});
			colorMap.put("darkkhaki", new short[]{0xbd, 0xb7, 0x6b});
			colorMap.put("darksalmon", new short[]{0xe9, 0x96, 0x7a});
			colorMap.put("blueviolet", new short[]{0x8a, 0x2b, 0xe2});
			colorMap.put("honeydew", new short[]{0xf0, 0xff, 0xf0});
			colorMap.put("darkturquoise", new short[]{0x00, 0xce, 0xd1});
			colorMap.put("palegoldenrod", new short[]{0xee, 0xe8, 0xaa});
			colorMap.put("lightcoral", new short[]{0xf0, 0x80, 0x80});
			colorMap.put("mediumpurple", new short[]{0x93, 0x70, 0xdb});
			colorMap.put("mintcream", new short[]{0xf5, 0xff, 0xfa});
			colorMap.put("lightseagreen", new short[]{0x20, 0xb2, 0xaa});
			colorMap.put("cornsilk", new short[]{0xff, 0xf8, 0xdc});
			colorMap.put("salmon", new short[]{0xfa, 0x80, 0x72});
			colorMap.put("slateblue", new short[]{0x6a, 0x5a, 0xcd});
			colorMap.put("azure", new short[]{0xf0, 0xff, 0xff});
			colorMap.put("cadetblue", new short[]{0x5f, 0x9e, 0xa0});
			colorMap.put("beige", new short[]{0xf5, 0xf5, 0xdc});
			colorMap.put("lightsalmon", new short[]{0xff, 0xa0, 0x7a});
			colorMap.put("mediumslateblue", new short[]{0x7b, 0x68, 0xee});
		}
		return colorMap;
	}

	public static Color getColor(String v){
		short[] t = getColorTriplet(v);
		if (t != null) {
			return new Color(t[0], t[1], t[2]);
		}
		return null;
	}

	public static short[] getColorTriplet(String v){
		if (v != null) {
			try {
				if (v.startsWith("#")){
					String _v = v.substring(1).toLowerCase();
					if (_v.contains(",")){
						String l[] = _v.split(",");
						return new short[]{
								Short.valueOf(l[0]),
								Short.valueOf(l[1]),
								Short.valueOf(l[2])};
					}else {
						return new short[]{
								Short.parseShort(_v.substring(0, 2), 16),
								Short.parseShort(_v.substring(2, 4), 16),
								Short.parseShort(_v.substring(4, 6), 16)};
					}
				}else{
					Map<String, short[]> colorMap = getColorMap();
					if (colorMap.containsKey(v)){
						return colorMap.get(v);
					}
				}
			}catch(Exception e) {
			}
		}
		return null;
	}

	public static String format(ReportDesign reportDesign, ElementDesign formatterDesign, Object value){
		return reportDesign.setting.getTextFormatter((String)formatterDesign.get("type")).format(value, formatterDesign);
	}

}
