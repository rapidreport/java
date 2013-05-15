package jp.co.systembase.report.textformatter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import jp.co.systembase.core.Round;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.ElementDesign;

public class TextFormatterUtil {

	public static String format(Object v, ElementDesign design){
		Object _v = ReportUtil.regularize(v);
		if (_v == null){
			return null;
		}else if (v instanceof Boolean){
			if ((Boolean)v){
				return "true";
			}else{
				return "false";
			}
		}else if (_v instanceof Date){
			String format = null;
			if (!design.isNull("format")){
				format = (String)design.get("format");
			}
			return formatDate((Date)_v, format);
		}else if (_v instanceof BigDecimal){
			String format = null;
			if (!design.isNull("format")){
				format = (String)design.get("format");
			}
			return formatNumber((BigDecimal)_v, format);
		}
		return _v.toString();
	}

	public static String formatList(List<?> list, String separator, ElementDesign design){
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < list.size(); i++){
			if (i != 0){
				result.append(separator);
			}
			result.append(TextFormatterUtil.format(list.get(i), design));
		}
		return result.toString();
	}

	public static String formatDate(Date date, String format){
		if (format == null){
			return formatDate(date, "yyyy/MM/dd");
		}

		String ret = format;
		{
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			ret = ret.replaceAll("yyyy", Integer.toString(cal.get(Calendar.YEAR)));
			ret = ret.replaceAll("yy", Integer.toString(cal.get(Calendar.YEAR)).substring(2));
			ret = ret.replaceAll("MM", padZero(Integer.toString(cal.get(Calendar.MONTH) + 1)));
			ret = ret.replaceAll("M", Integer.toString(cal.get(Calendar.MONTH) + 1));
			ret = ret.replaceAll("ddd", getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)));
			ret = ret.replaceAll("dddd", getDayOfWeekL(cal.get(Calendar.DAY_OF_WEEK)));
			ret = ret.replaceAll("AAA", getDayOfWeekJ(cal.get(Calendar.DAY_OF_WEEK)));
			ret = ret.replaceAll("dd", padZero(Integer.toString(cal.get(Calendar.DATE))));
			ret = ret.replaceAll("d", Integer.toString(cal.get(Calendar.DATE)));
			ret = ret.replaceAll("hh", padZero(Integer.toString(cal.get(Calendar.HOUR_OF_DAY))));
			ret = ret.replaceAll("h", Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
			ret = ret.replaceAll("mm", padZero(Integer.toString(cal.get(Calendar.MINUTE))));
			ret = ret.replaceAll("m", Integer.toString(cal.get(Calendar.MINUTE)));
			ret = ret.replaceAll("ss", padZero(Integer.toString(cal.get(Calendar.SECOND))));
			ret = ret.replaceAll("s", Integer.toString(cal.get(Calendar.SECOND)));
		}
		if (ret.indexOf("gg") != -1 ||
				ret.indexOf("n") != -1 ||
				ret.indexOf("N") != -1){
			Locale locale = new Locale("ja", "JP", "JP");
			Calendar cal = Calendar.getInstance(locale);
			DateFormat df = new SimpleDateFormat("GGGG", locale);
			cal.setTime(date);

			int year = cal.get(Calendar.YEAR);
			ret = ret.replaceAll("nn", padZero(Integer.toString(year)));
			ret = ret.replaceAll("n", Integer.toString(year));
			ret = ret.replaceAll("NN", year == 1 ? "元" : padZero(Integer.toString(year)));
			ret = ret.replaceAll("N",  year == 1 ? "元" : Integer.toString(year));
			ret = ret.replaceAll("gg", df.format(cal.getTime()));
		}
		return ret;
	}

	private static String getDayOfWeek(int d){
		switch(d){
		case Calendar.SUNDAY:
			return "Sun";
		case Calendar.MONDAY:
			return "Mon";
		case Calendar.TUESDAY:
			return "Tue";
		case Calendar.WEDNESDAY:
			return "Wed";
		case Calendar.THURSDAY:
			return "Thu";
		case Calendar.FRIDAY:
			return "Fri";
		case Calendar.SATURDAY:
			return "Sat";
		}
		return null;
	}

	private static String getDayOfWeekL(int d){
		switch(d){
		case Calendar.SUNDAY:
			return "Sunday";
		case Calendar.MONDAY:
			return "Monday";
		case Calendar.TUESDAY:
			return "Tuesday";
		case Calendar.WEDNESDAY:
			return "Wednesday";
		case Calendar.THURSDAY:
			return "Thursday";
		case Calendar.FRIDAY:
			return "Friday";
		case Calendar.SATURDAY:
			return "Saturday";
		}
		return null;
	}

	private static String getDayOfWeekJ(int d){
		switch(d){
		case Calendar.SUNDAY:
			return "日";
		case Calendar.MONDAY:
			return "月";
		case Calendar.TUESDAY:
			return "火";
		case Calendar.WEDNESDAY:
			return "水";
		case Calendar.THURSDAY:
			return "木";
		case Calendar.FRIDAY:
			return "金";
		case Calendar.SATURDAY:
			return "土";
		}
		return null;
	}

	private static String padZero(String v){
		String t = "0" + v;
		return t.substring(t.length() - 2);
	}

	public static String formatNumber(BigDecimal v, String format){
		if (format == null || format.equals("")){
			return v.toPlainString();
		}
		{
			int i = format.indexOf(";");
			if (i == -1){
				return _formatNumber(v, format);
			}else{
				if (v.compareTo(BigDecimal.ZERO) >= 0){
					return _formatNumber(v, format.substring(0, i));
				}else{
					return _formatNumber(v.abs(), format.substring(i + 1));
				}
			}
		}
	}

	private static String _formatNumber(BigDecimal v, String format){
		if (format == null || format.equals("")){
			return v.toPlainString();
		}
		String ret = "";
		int b = -1;
		int e = -1;
		for(int i = 0;i < format.length();i++){
			if (b == -1){
				if ("0#,.".indexOf(format.charAt(i)) >= 0){
					b = i;
					if (b > 0){
						ret = format.substring(0, b);
					}
				}
			}else if (e == -1){
				if ("0#,.".indexOf(format.charAt(i)) == -1){
					e = i;
					ret += _formatNumber_aux(v, format.substring(b, e));
				}
			}
		}
		if (b == -1){
			ret = format;
		}else if (e == -1){
			ret += _formatNumber_aux(v, format.substring(b));
		}else if (e < format.length()){
			ret += format.substring(e);
		}
		return ret;
	}

	private static String _formatNumber_aux(BigDecimal v, String format){
		int zeroLength = 0;
		int decZeroLength = 0;
		int decLength = 0;
		int camma = 0;
		boolean negative = false;
		String intValue = null;
		String decValue = null;
		{
			int i = format.indexOf(".");
			String _intFormat;
			if (i >= 0){
				String _decFormat = format.substring(i + 1);
				boolean z = true;
				for(int j = 0;j < _decFormat.length(); j++){
					char c = _decFormat.charAt(j);
					if (c == '#'){
						z = false;
					}
					if (c == '#' || c == '0'){
						decLength++;
						if (z){
							decZeroLength++;
						}
					}
				}
				_intFormat = format.substring(0, i);
			}else{
				_intFormat = format;
			}
			{
				boolean z = true;
				for(int j = 0;j < _intFormat.length();j++){
					char c = _intFormat.charAt(_intFormat.length() - j - 1);
					if (c == ',' && camma == 0){
						camma = j;
					}
					if (c == '#'){
						z = false;
					}
					if (z && c == '0'){
						zeroLength++;
					}
				}
			}
		}
		{
			String _intValue = null;
			{
				BigDecimal __v = Round.round5(v, -decLength);
				negative = (__v.compareTo(BigDecimal.ZERO) < 0);
				String _v = __v.abs().toPlainString();
				int i = _v.indexOf(".");
				if (i >= 0){
					_intValue = _v.substring(0, i);
					decValue = _v.substring(i + 1);
				}else{
					_intValue = _v;
					decValue = "";
				}
				if (_intValue.equals("0")){
					_intValue = "";
				}
			}
			while(decValue.length() < decZeroLength){
				decValue += '0';
			}
			{
				while(_intValue.length() < zeroLength){
					_intValue = '0' + _intValue;
				}
				if (camma > 0){
					int l = _intValue.length();
					int i = l % camma;
					if (i > 0){
						intValue = _intValue.substring(0, i);
					}else{
						intValue = "";
					}
					while(i + camma <= l){
						if (intValue.length() > 0){
							intValue += ",";
						}
						intValue += _intValue.substring(i, i + camma);
						i += camma;
					}
				}else{
					intValue = _intValue;
				}
			}
		}
		String ret = "";
		if (negative){
			ret = "-";
		}
		if (intValue != null){
			ret += intValue;
		}
		if (decValue != null && decValue.length() > 0){
			ret += "." + decValue;
		}
		return ret;
	}

}
