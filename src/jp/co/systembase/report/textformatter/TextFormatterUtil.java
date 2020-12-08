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

		String ret = "";
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		int i = 0;
		int j = 0;
		Calendar jcal = null;
		DateFormat jdf = null;
		if (format.indexOf("gg") != -1 ||
			format.indexOf("n") != -1 ||
			format.indexOf("N") != -1){
			Locale jlocale = new Locale("ja", "JP", "JP");
			jcal = Calendar.getInstance(jlocale);
			jdf = new SimpleDateFormat("GGGG", jlocale);
			jcal.setTime(date);
		}
		while(j < format.length()){
			String t = format.substring(j);
			String p = null;
			int w = 0;
			if (t.startsWith("yyyy")){
				p = Integer.toString(cal.get(Calendar.YEAR));
				w = 4;
			}else if (t.startsWith("yy")){
				p = Integer.toString(cal.get(Calendar.YEAR)).substring(2);
				w = 2;
			}else if (t.startsWith("MMMM")){
				p = getMonthEnL(cal.get(Calendar.MONTH) + 1);
				w = 4;
			}else if (t.startsWith("MMM")){
				p = getMonthEn(cal.get(Calendar.MONTH) + 1);
				w = 3;
			}else if (t.startsWith("MM")){
				p = padZero(Integer.toString(cal.get(Calendar.MONTH) + 1));
				w = 2;
			}else if (t.startsWith("M")){
				p = Integer.toString(cal.get(Calendar.MONTH) + 1);
				w = 1;
			}else if (t.startsWith("dddd")){
				p = getDayOfWeekL(cal.get(Calendar.DAY_OF_WEEK));
				w = 4;
			}else if (t.startsWith("ddd")){
				p = getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
				w = 3;
			}else if (t.startsWith("AAA")){
				p = getDayOfWeekJ(cal.get(Calendar.DAY_OF_WEEK));
				w = 3;
			}else if (t.startsWith("dd")){
				p = padZero(Integer.toString(cal.get(Calendar.DATE)));
				w = 2;
			}else if (t.startsWith("d")){
				p = Integer.toString(cal.get(Calendar.DATE));
				w = 1;
			}else if (t.startsWith("hh")){
				p = padZero(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
				w = 2;
			}else if (t.startsWith("h")){
				p = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
				w = 1;
			}else if (t.startsWith("mm")){
				p = padZero(Integer.toString(cal.get(Calendar.MINUTE)));
				w = 2;
			}else if (t.startsWith("m")){
				p = Integer.toString(cal.get(Calendar.MINUTE));
				w = 1;
			}else if (t.startsWith("ss")){
				p = padZero(Integer.toString(cal.get(Calendar.SECOND)));
				w = 2;
			}else if (t.startsWith("s")){
				p = Integer.toString(cal.get(Calendar.SECOND));
				w = 1;
			}else if (t.startsWith("nn")){
				p = padZero(Integer.toString(jcal.get(Calendar.YEAR)));
				w = 2;
			}else if (t.startsWith("n")){
				p = Integer.toString(jcal.get(Calendar.YEAR));
				w = 1;
			}else if (t.startsWith("NN")){
				int year = jcal.get(Calendar.YEAR);
				p = year == 1 ? "元" : padZero(Integer.toString(year));
				w = 2;
			}else if (t.startsWith("N")){
				int year = jcal.get(Calendar.YEAR);
				p = year == 1 ? "元" : Integer.toString(year);
				w = 1;
			}else if (t.startsWith("gg")){
				p = jdf.format(jcal.getTime());
				w = 2;
			}
			if (p != null){
				if (i < j){
					ret += format.substring(i, j);
				}
				ret += p;
				j += w;
				i = j;
			}else{
				j++;
			}
		}
		if (i < format.length()){
			ret += format.substring(i);
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

	private static String getMonthEn(int m){
		switch(m){
		case 1:
			return "Jan";
		case 2:
			return "Feb";
		case 3:
			return "Mar";
		case 4:
			return "Apr";
		case 5:
			return "May";
		case 6:
			return "Jun";
		case 7:
			return "Jul";
		case 8:
			return "Aug";
		case 9:
			return "Sep";
		case 10:
			return "Oct";
		case 11:
			return "Nov";
		case 12:
			return "Dec";
		}
		return null;
	}

	private static String getMonthEnL(int m){
		switch(m){
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
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
