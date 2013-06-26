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
	private static final Pattern PATTERN_METHOD = Pattern.compile("^([^.#@]*)?(\\.([^.#@]*))?(@([^.#@]*))?(#([^.#@]*))?$");

	private int index;
	private ReportSetting setting;

	public Parser(ReportSetting setting){
		this.setting = setting;
	}

	public IExpression parse(String source) throws EvalException{
		if (source == null){
			return null;
		}
		String _source = source.trim();
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
		}else if (source.charAt(this.index) == '\''){
			return this._parse_text(source);
		}else{
			String token = this.nextToken(source);
			if (token.equals(LITERAL_NULL)){
				return new ImmediateExpression(null);
			}else if (token.equals(LITERAL_TRUE)){
				return new ImmediateExpression(true);
			}else if (token.equals(LITERAL_FALSE)){
				return new ImmediateExpression(false);
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
					String param = m.group(3);
					String scope = m.group(5);
					String unit = m.group(7);
					if (methodKey != null && methodKey.length() == 0){
						methodKey = null;
					}
					method = this.setting.getMethod(methodKey);
					if (method == null){
						throw new EvalException("メソッド '" + methodKey + "' は見つかりません : " + source);
					}
					if (param != null && param.length() == 0){
						param = null;
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
		if (operatorKey.startsWith("(")){
			throw new EvalException("'(' は予期せぬ文字です : " + source);
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

	private IExpression _parse_text(String source) throws EvalException{
		this.index += 1;
		int i = this.index;
		StringBuilder sb = new StringBuilder();
		boolean escaped = false;
		while(true){
			if (i >= source.length()){
				throw new EvalException("文字列が閉じられていません : " + source);
			}
			char c = source.charAt(i);
			if (c == '\'' && !escaped){
				if (i > this.index){
					sb.append(source.substring(this.index, i));
					this.index = i;
				}
				break;
			}
			if (escaped){
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
					throw new EvalException("不正なエスケープ文字です: \\" + c + " (有効なもの \\' \\\\ \\n) : " + source);
				}
				escaped = false;
				this.index = i + 1;
			}else if (c == '\\'){
				if (i > this.index){
					sb.append(source.substring(this.index, i));
					this.index = i;
				}
				escaped = true;
			}
			i += 1;
		}
		this.index += 1;
		return new ImmediateExpression(sb.toString());
	}

	private void skipSpace(String source){
		while(this.index < source.length() && source.charAt(this.index) == ' '){
			this.index += 1;
		}
	}

	private String nextToken(String source){
		int i = this.index;
		while(i < source.length() &&
				source.charAt(i) != ' ' && source.charAt(i) != ')'){
			i += 1;
		}
		String ret = "";
		if (i > this.index){
			ret = source.substring(this.index, i);
			this.index = i;
		}
		return ret;
	}

}
