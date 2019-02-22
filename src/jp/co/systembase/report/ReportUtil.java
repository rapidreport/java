package jp.co.systembase.report;

import java.math.BigDecimal;
import java.text.BreakIterator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.Report.EScaleUnit;

public class ReportUtil {

	private static final String SINGLE_CHARS =
		"0123456789" +
		"abcdefghijklmnopqrstuvwxyz" +
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
		"ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝｧｨｩｪｫｯｬｭｮﾞﾟｰ｢｣･､｡" +
		" !@#$%^&*()_+|~-=\\`{}[]:\";'<>?,./\n\r\t";

	private ReportUtil(){};

	public static Object regularize(Object v){
		BigDecimal _v = Cast.toBigDecimal(v);
		if (_v != null){
			return _v;
		}else{
			return v;
		}
	}

	public static Object eqRegularize(Object v){
		Object o = regularize(v);
		if (o instanceof BigDecimal){
			BigDecimal d = (BigDecimal)o;
			if (d.scale() < 0){
				return d.setScale(0);
			}else if (d.scale() > 0){
				return d.stripTrailingZeros();
			}else{
				return d;
			}
		}else{
			return o;
		}
	}

	public static boolean eq(Object v1, Object v2){
		if (v1 == null){
			return (v2 == null);
		}else{
			return eqRegularize(v1).equals(eqRegularize(v2));
		}
	}

	public static boolean condition(Object v){
		if (v instanceof Boolean){
			return (Boolean)v;
		}else{
			Object _v = regularize(v);
			if (_v instanceof BigDecimal){
				return !_v.equals(BigDecimal.ZERO);
			}
			return false;
		}
	}

	public static int compareTo(Object v1, Object v2) throws Exception{
		if (v1 != null && v2 != null){
			if (v1 instanceof Date && v2 instanceof Date){
				Date d1 = (Date)v1;
				Date d2 = (Date)v2;
				return d1.compareTo(d2);
			}else{
				BigDecimal d1 = Cast.toBigDecimal(v1);
				BigDecimal d2 = Cast.toBigDecimal(v2);
				if (d1 != null && d2 != null){
					return d1.compareTo(d2);
				}
			}
			throw new Exception("cannot compare " + v1.toString() + " to " + v2.toString());
		}else if (v1 != null){
			return 1;
		}else if (v2 != null){
			return -1;
		}else{
			return 0;
		}
	}

	public static float toPoint(EScaleUnit scaleUnit, float v){
		switch(scaleUnit){
		case MM:
			return v * 2.835f;
		case INCH:
			return v * 72f;
		default:
		}
		return v;
	}

	public static float pointTo(EScaleUnit scaleUnit, float v){
		switch(scaleUnit){
		case MM:
			return v * 0.3528f;
		case INCH:
			return v * 0.01389f;
		default:
		}
		return v;
	}

	public static String subString(String str, int begin){
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int _begin = (begin < 0 ? ReportUtil.stringLen(str) + begin : begin);
		int b = bi.first();
		int count = 0;
		while (bi.next() != BreakIterator.DONE) {
			if (count >= _begin){
				break;
			}
			b = bi.current();
			count++;
		}
		return str.substring(b);
	}

	public static String subString(String str, int begin, int len){
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int _begin = Math.max(begin < 0 ? ReportUtil.stringLen(str) + begin : begin, 0);
		int b = bi.first();
		int e = 0;
		int count = 0;
		while (bi.next() != BreakIterator.DONE) {
			if (count >= _begin){
				if (count < _begin + len){
					e = bi.current();
				}else{
					break;
				}
			}else{
				b = bi.current();
			}
			count++;
		}
		return str.substring(b, e);
	}

	public static int stringLen(String str){
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int count = 0;
		bi.first();
		while (bi.next() != BreakIterator.DONE){
			count++;
		}
		return count;
	}

	public static int wStringLen(String str){
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int count = 0;
		int last = bi.first();
		while (bi.next() != BreakIterator.DONE){
			if (isSingleChar(str.substring(last, bi.current()))){
				count += 1;
			}else{
				count += 2;
			}
			last = bi.current();
		}
		return count;
	}

	public static String wSubString(String str, int begin){
		if (begin >= 0){
			return subString(str, getWIndex(str, begin));
		}else{
			return subString(str, getWRevIndex(str, -begin));
		}
	}

	public static String wSubString(String str, int begin, int len){
		if (begin >= 0){
			String s = subString(str, getWIndex(str, begin));
			return subString(s, 0, getWIndex(s, len));
		}else{
			String s = subString(str, getWRevIndex(str, -begin));
			return subString(s, 0, getWIndex(s, len));
		}
	}

	public static int getWIndex(String str, int w){
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int last = bi.first();
		int _w = 0;
		int ret = 0;
		while (bi.next() != BreakIterator.DONE){
			if (isSingleChar(str.substring(last, bi.current()))){
				_w += 1;
			}else{
				_w += 2;
			}
			if (_w > w){
				break;
			}
			last = bi.current();
			ret++;
		}
		return ret;
	}

	public static int getWRevIndex(String str, int w){
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int last = bi.last();
		int _w = 0;
		int ret = 0;
		while (bi.previous() != BreakIterator.DONE){
			if (isSingleChar(str.substring(bi.current(), last))){
				_w += 1;
			}else{
				_w += 2;
			}
			last = bi.current();
			if (_w > w){
				ret++;
			}
		}
		return ret;
	}

	public static String objectToString(Object o){
		if (o instanceof String){
			return (String)o;
		}else{
			Object _o = regularize(o);
			if (_o instanceof BigDecimal){
				return ((BigDecimal)_o).toPlainString();
			}
		}
		return null;
	}

	public static String trimLeft(String str){
		String ret = str;
		if (ret != null){
			int i = 0;
			while(i < ret.length()){
				if (ret.charAt(i) != ' ' && ret.charAt(i) != '　'){
					break;
				}
				i++;
			}
			ret = ret.substring(i);
		}
		return ret;
	}

	private static Map<String, Boolean> _SingleCharsMap = null;
	
	synchronized public static boolean isSingleChar(String c){
		if (_SingleCharsMap == null){
			_SingleCharsMap = new HashMap<String, Boolean>();
			for(int i = 0;i < SINGLE_CHARS.length();i++){
				_SingleCharsMap.put(SINGLE_CHARS.substring(i, i + 1), true);
			}
		}
		return _SingleCharsMap.containsKey(c);
	}
}
