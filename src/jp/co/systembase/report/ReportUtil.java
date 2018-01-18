package jp.co.systembase.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

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
		int b = begin;
		if (b < 0){
			b = str.length() + b;
			if (b < 0){
				b = 0;
			}
		}
		if (b >= str.length()){
			return null;
		}else{
			return str.substring(b);
		}
	}

	public static String subString(String str, int begin, int len){
		int b = begin;
		int l = len;
		if (b < 0){
			b = str.length() + b;
			if (b < 0){
				l += b;
				b = 0;
			}
		}
		if (l <= 0 || b >= str.length()){
			return null;
		}else if(b + l > str.length()){
			return str.substring(b);
		}else{
			return str.substring(b, b + l);
		}
	}

	public static int wStringLen(String str){
		int ret = 0;
		for(int i = 0;i < str.length();i++){
			char c = str.charAt(i);
			if (SINGLE_CHARS.indexOf(c) >= 0){
				ret += 1;
			}else{
				ret += 2;
			}
		}
		return ret;
	}

	public static String wSubString(String str, int begin){
		int b;
		if (begin >= 0){
			b = getWIndex(str, 0, begin);
		}else{
			b = getWRevIndex(str, str.length(), -begin);
		}
		if (b >= str.length()){
			return null;
		}else{
			return str.substring(b);
		}
	}

	public static String wSubString(String str, int begin, int len){
		int b;
		int e;
		if (begin >= 0){
			b = getWIndex(str, 0, begin);
			e = getWIndex(str, b, len);
		}else{
			int _len = Math.min(-begin, len);
			e = getWRevIndex(str, str.length(), -(begin + _len));
			b = getWRevIndex(str, e, _len);
		}
		if (e <= b || b >= str.length()){
			return null;
		}else if (e >= str.length()){
			return str.substring(b);
		}else{
			return str.substring(b, e);
		}
	}

	public static int getWIndex(String str, int base, int w){
		int _w = 0;
		for(int i = base;i < str.length();i++){
			char c = str.charAt(i);
			if (SINGLE_CHARS.indexOf(c) >= 0){
				_w += 1;
			}else{
				_w += 2;
			}
			if (_w > w){
				return i;
			}
		}
		return str.length();
	}

	public static int getWRevIndex(String str, int base, int w){
		int _w = 0;
		for(int i = base - 1;i >= 0;i--){
			char c = str.charAt(i);
			if (SINGLE_CHARS.indexOf(c) >= 0){
				_w += 1;
			}else{
				_w += 2;
			}
			if (_w > w){
				return i + 1;
			}
		}
		return 0;
	}
	
	public static Iterable<String> splitLines(String str){
		return new _Lines(str);
	}
	
	private static class _Lines implements Iterable<String>, Iterator<String>{
		private String _lines[];
		private int _i;
		public _Lines(String str){
			this._lines = str.split("\n");
			this._i = 0;
		}
		@Override
		public Iterator<String> iterator() {
			return this;
		}
		@Override
		public boolean hasNext() {
			return this._i < this._lines.length;
		}
		@Override
		public String next() {
			return this._lines[this._i++].replace("\r", "");
		}
		@Override
		public void remove() {}
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

}
