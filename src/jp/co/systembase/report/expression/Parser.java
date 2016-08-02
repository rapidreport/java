package jp.co.systembase.report.expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.systembase.report.ReportSetting;
import jp.co.systembase.report.component.EvalException;
import jp.co.systembase.report.method.IMethod;
import jp.co.systembase.report.operator.IOperator;

public class Parser {

	private static final String LITERAL_NULL = "null";
	private static final String LITERAL_TRUE = "true";
	private static final String LITERAL_FALSE = "false";

	private static final Pattern PATTERN_NUMBER = Pattern.compile("-?[0-9.]+");
	private static final Pattern PATTERN_DATE = Pattern.compile("^#([0-9]{4})([0-9]{2})([0-9]{2})$");
	private static final Pattern PATTERN_DATETIME = Pattern.compile("^#([0-9]{4})([0-9]{2})([0-9]{2})([0-9]{2})([0-9]{2})([0-9]{2})$");
	private static final Pattern PATTERN_METHOD = Pattern.compile("^([^.#@]*)?(\\.((\'.*\')|([^.#@]*)))?(@([^.#@]*))?(#([^.#@]*))?$");

	private int index;
	private ReportSetting setting;

	public Parser(ReportSetting setting){
		this.setting = setting;
	}

	public IExpression parse(String source) throws EvalException{
		if (source == null){
			return null;
		}
		String _source = source.replaceAll("\r\n",  " ").trim();
		if (_source.length() == 0){
			return null;
		}
		this.index = 0;
        IExpression ret = this._parse_aux(_source);
        if (this.index < _source.length()){
            throw new EvalException("'" + _source.substring(this.index) + "' は予期せぬ文字です : " + source);
        }
		return ret;
	}

	private IExpression _parse_aux(String source) throws EvalException{
		if (source.charAt(this.index) == '('){
			return this._parse_operator(source);
		}else{
			String token = this.nextToken(source);
			if (token.equals(LITERAL_NULL)){
				return new ImmediateExpression(null);
			}else if (token.equals(LITERAL_TRUE)){
				return new ImmediateExpression(true);
			}else if (token.equals(LITERAL_FALSE)){
				return new ImmediateExpression(false);
			}else if (token.startsWith("\'")){
				return new ImmediateExpression(evalString(token));
			}
			{
				Matcher m = PATTERN_NUMBER.matcher(token);
				if (m.matches()){
					return new ImmediateExpression(new BigDecimal(token));
				}
			}
			{
				Matcher m = PATTERN_DATE.matcher(token);
				if (m.matches()){
					int year = Integer.valueOf(m.group(1));
					int month = Integer.valueOf(m.group(2));
					int day = Integer.valueOf(m.group(3));
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, year);
					c.set(Calendar.MONTH, month - 1);
					c.set(Calendar.DAY_OF_MONTH, day);
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					return new ImmediateExpression(c.getTime());
				}
			}
			{
				Matcher m = PATTERN_DATETIME.matcher(token);
				if (m.matches()){
					int year = Integer.valueOf(m.group(1));
					int month = Integer.valueOf(m.group(2));
					int day = Integer.valueOf(m.group(3));
					int hour = Integer.valueOf(m.group(4));
					int minute = Integer.valueOf(m.group(5));
					int second = Integer.valueOf(m.group(6));
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, year);
					c.set(Calendar.MONTH, month - 1);
					c.set(Calendar.DAY_OF_MONTH, day);
					c.set(Calendar.HOUR_OF_DAY, hour);
					c.set(Calendar.MINUTE, minute);
					c.set(Calendar.SECOND, second);
					c.set(Calendar.MILLISECOND, 0);
					return new ImmediateExpression(c.getTime());
				}
			}
			{
				Matcher m = PATTERN_METHOD.matcher(token);
				if (m.matches()){
					String methodKey = m.group(1);
					IMethod method = null;
					String param = null;
					String param1 = m.group(4);
					String param2 = m.group(5);
					String scope = m.group(7);
					String unit = m.group(9);
					if (methodKey != null && methodKey.length() == 0){
						methodKey = null;
					}
					if (param1 != null && param1.length() != 0){
						param1 = param1.toString().trim();
					}
					if (param2 != null && param2.length() != 0){
						param2 = param2.toString().trim();
					}
					if (param1 != null && param1.isEmpty() == false){
						param = evalString(param1);
					}else if(param2 != null && param2.isEmpty() == false){
						param = param2;
					}
					if (param != null && param.length() == 0){
						param = null;
					}
					method = this.setting.getMethod(methodKey);
					if (method == null){
						throw new EvalException("メソッド '" + methodKey + "' は見つかりません : " + source);
					}
					return new MethodExpression(method, param, scope, unit);
				}
			}
			return null;
		}
	}

	private IExpression _parse_operator(String source) throws EvalException{
		this.index += 1;
		this.skipSpace(source);
		String operatorKey = nextToken(source);
		if (operatorKey.length() == 0){
			throw new EvalException("オペレータがありません : " + source);
		}
		IOperator operator = this.setting.getOperator(operatorKey);
		if (operator == null){
			throw new EvalException("オペレータ '" + operatorKey + "' は見つかりません : " + source);
		}
		List<IExpression> params = new ArrayList<IExpression>();
		while(true){
			this.skipSpace(source);
			if (this.index == source.length()){
				throw new EvalException("')' がありません");
			}
			if (source.charAt(this.index) == ')'){
				this.index += 1;
				return new OperatorExpression(operator, params);
			}
			params.add(this._parse_aux(source));
		}
	}

	private void skipSpace(String source){
		while(this.index < source.length() && source.charAt(this.index) == ' '){
			this.index += 1;
		}
	}

	private String nextToken(String source){
		int i = this.index;
		boolean escaped = false;
		boolean quoted = false;
		while(i < source.length()){
			char c = source.charAt(i);
			if (escaped == false && quoted == false){
				if (c == ' ' || c == ')'){
					break;
				}
			}
			if (escaped == false){
				if (c == '\\'){
					escaped = true;
				}else{
					if (quoted == false){
						if (c == '\''){
							quoted = true;
						}
					}else{
						if (c == '\''){
							quoted = false;
						}
					}
				}
			}else{
				escaped = false;
			}
			i += 1;
		}
		String ret = "";
		if (i > this.index){
			ret = source.substring(this.index, i);
			this.index = i;
		}
		return ret;
	}

	private String evalString(String token) throws EvalException{
		int i = 1;
		int j = 1;
		StringBuilder sb = new StringBuilder();
		boolean escaped = false;

		while(true){
			if (j >= token.length()){
				throw new EvalException("文字列が閉じられていません : " + token);
			}
			char c = token.charAt(j);
			if (escaped == false){
				if (c == '\''){
					if (j > i){
						sb.append(token.substring(i, j));
						i = j;
					}
					break;
				}else if(c == '\\'){
					if (j > i){
						sb.append(token.substring(i, j));
						i = j;
					}
					escaped = true;
				}
			}else{
				switch(c){
				case '\'':
					sb.append('\'');
					break;
				case '\\':
					sb.append('\\');
					break;
				case 'n':
					sb.append('\n');
					break;
				default:
					throw new EvalException("不正なエスケープ文字です: \\" + c + " (有効なもの \\' \\\\ \\n) : " + token);
				}
				escaped = false;
				i = j + 1;
			}
			j += 1;
		}
		if (i + 1 < token.length()){
			throw new EvalException("'" + token.subSequence(i + 1, 1) + "'は予期せぬ文字です : " + token);
		}
		return sb.toString();
	}
}
