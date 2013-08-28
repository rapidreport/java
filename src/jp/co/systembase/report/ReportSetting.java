package jp.co.systembase.report;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.elementpreprocessor.DefaultPreprocessor;
import jp.co.systembase.report.elementpreprocessor.IElementPreprocessor;
import jp.co.systembase.report.elementpreprocessor.SubPagePreprocessor;
import jp.co.systembase.report.method.AverageAtMethod;
import jp.co.systembase.report.method.AverageMethod;
import jp.co.systembase.report.method.AveragePageMethod;
import jp.co.systembase.report.method.DummyMethod;
import jp.co.systembase.report.method.FieldMethod;
import jp.co.systembase.report.method.FieldSumAtMethod;
import jp.co.systembase.report.method.FieldSumMethod;
import jp.co.systembase.report.method.FieldSumPageMethod;
import jp.co.systembase.report.method.GlobalScopeMethod;
import jp.co.systembase.report.method.IMethod;
import jp.co.systembase.report.method.NextMethod;
import jp.co.systembase.report.method.PageCountMethod;
import jp.co.systembase.report.method.PageScopeMethod;
import jp.co.systembase.report.method.PreviousMethod;
import jp.co.systembase.report.method.RowCountAtMethod;
import jp.co.systembase.report.method.RowCountMethod;
import jp.co.systembase.report.method.RowCountPageMethod;
import jp.co.systembase.report.method.StateMethod;
import jp.co.systembase.report.method.ToggleMethod;
import jp.co.systembase.report.method.VariableMethod;
import jp.co.systembase.report.operator.AbsOperator;
import jp.co.systembase.report.operator.AddOperator;
import jp.co.systembase.report.operator.AndOperator;
import jp.co.systembase.report.operator.CatOperator;
import jp.co.systembase.report.operator.DayOperator;
import jp.co.systembase.report.operator.DivOperator;
import jp.co.systembase.report.operator.EqOperator;
import jp.co.systembase.report.operator.GreaterEqOperator;
import jp.co.systembase.report.operator.GreaterOperator;
import jp.co.systembase.report.operator.HourOperator;
import jp.co.systembase.report.operator.IOperator;
import jp.co.systembase.report.operator.IfOperator;
import jp.co.systembase.report.operator.LessEqOperator;
import jp.co.systembase.report.operator.LessOperator;
import jp.co.systembase.report.operator.MaxOperator;
import jp.co.systembase.report.operator.MinOperator;
import jp.co.systembase.report.operator.MinuteOperator;
import jp.co.systembase.report.operator.ModOperator;
import jp.co.systembase.report.operator.MonthOperator;
import jp.co.systembase.report.operator.MulOperator;
import jp.co.systembase.report.operator.NotEqOperator;
import jp.co.systembase.report.operator.NotOperator;
import jp.co.systembase.report.operator.NvlOperator;
import jp.co.systembase.report.operator.OrOperator;
import jp.co.systembase.report.operator.Round5EOperator;
import jp.co.systembase.report.operator.Round5Operator;
import jp.co.systembase.report.operator.Round6Operator;
import jp.co.systembase.report.operator.RoundDownOperator;
import jp.co.systembase.report.operator.RoundUp2Operator;
import jp.co.systembase.report.operator.RoundUpOperator;
import jp.co.systembase.report.operator.SecondOperator;
import jp.co.systembase.report.operator.StringLenOperator;
import jp.co.systembase.report.operator.SubOperator;
import jp.co.systembase.report.operator.SubStringOperator;
import jp.co.systembase.report.operator.WStringLenOperator;
import jp.co.systembase.report.operator.WSubStringOperator;
import jp.co.systembase.report.operator.YearOperator;
import jp.co.systembase.report.textformatter.DefaultTextFormatter;
import jp.co.systembase.report.textformatter.ITextFormatter;

public class ReportSetting  implements Cloneable{

	public Map<String, IMethod> methodMap = new HashMap<String, IMethod>();
	public Map<String, IOperator> operatorMap = new HashMap<String, IOperator>();
	public IElementPreprocessor defaultElementPreprocessor;
	public Map<String, IElementPreprocessor> elementPreprocessorMap = new HashMap<String, IElementPreprocessor>();
	public ITextFormatter defaultTextFormatter;
	public Map<String, ITextFormatter> textFormatterMap = new HashMap<String, ITextFormatter>();
	public IReportLogger logger = null;

	public ReportSetting(){
		this.methodMap.put("field", new FieldMethod());
		this.methodMap.put("global", new GlobalScopeMethod());
		this.methodMap.put("sum", new FieldSumMethod());
		this.methodMap.put("sum_at", new FieldSumAtMethod());
		this.methodMap.put("sum_page", new FieldSumPageMethod());
		this.methodMap.put("avr", new AverageMethod());
		this.methodMap.put("avr_at", new AverageAtMethod());
		this.methodMap.put("avr_page", new AveragePageMethod());
		this.methodMap.put("count", new RowCountMethod());
		this.methodMap.put("count_at", new RowCountAtMethod());
		this.methodMap.put("count_page", new RowCountPageMethod());
		this.methodMap.put("page_count", new PageCountMethod());
		this.methodMap.put("page", new PageScopeMethod());
		this.methodMap.put("state", new StateMethod());
		this.methodMap.put("toggle", new ToggleMethod());
		this.methodMap.put("prev", new PreviousMethod());
		this.methodMap.put("next", new NextMethod());
		this.methodMap.put("var", new VariableMethod());
		this.methodMap.put("debug", new DummyMethod());
		this.operatorMap.put("+", new AddOperator());
		this.operatorMap.put("-", new SubOperator());
		this.operatorMap.put("*", new MulOperator());
		this.operatorMap.put("/", new DivOperator());
		this.operatorMap.put("&", new CatOperator());
		this.operatorMap.put("mod", new ModOperator());
		this.operatorMap.put("%", new ModOperator());
		this.operatorMap.put("=", new EqOperator());
		this.operatorMap.put("eq", new EqOperator());
		this.operatorMap.put("in", new EqOperator());
		this.operatorMap.put("!=", new NotEqOperator());
		this.operatorMap.put("neq", new NotEqOperator());
		this.operatorMap.put("nin", new NotEqOperator());
		this.operatorMap.put(">", new GreaterOperator());
		this.operatorMap.put(">=", new GreaterEqOperator());
		this.operatorMap.put("<", new LessOperator());
		this.operatorMap.put("<=", new LessEqOperator());
		this.operatorMap.put("!", new NotOperator());
		this.operatorMap.put("not", new NotOperator());
		this.operatorMap.put("&&", new AndOperator());
		this.operatorMap.put("and", new AndOperator());
		this.operatorMap.put("||", new OrOperator());
		this.operatorMap.put("or", new OrOperator());
		this.operatorMap.put("if", new IfOperator());
		this.operatorMap.put("nvl", new NvlOperator());
		this.operatorMap.put("round5", new Round5Operator());
		this.operatorMap.put("round5e", new Round5EOperator());
		this.operatorMap.put("round6", new Round6Operator());
		this.operatorMap.put("roundup", new RoundUpOperator());
		this.operatorMap.put("roundup2", new RoundUp2Operator());
		this.operatorMap.put("rounddown", new RoundDownOperator());
		this.operatorMap.put("abs", new AbsOperator());
		this.operatorMap.put("max", new MaxOperator());
		this.operatorMap.put("min", new MinOperator());
		this.operatorMap.put("year", new YearOperator());
		this.operatorMap.put("month", new MonthOperator());
		this.operatorMap.put("day", new DayOperator());
		this.operatorMap.put("hour", new HourOperator());
		this.operatorMap.put("minute", new MinuteOperator());
		this.operatorMap.put("second", new SecondOperator());
		this.operatorMap.put("sub", new SubStringOperator());
		this.operatorMap.put("wsub", new WSubStringOperator());
		this.operatorMap.put("len", new StringLenOperator());
		this.operatorMap.put("wlen", new WStringLenOperator());
		this.defaultElementPreprocessor = new DefaultPreprocessor();
		this.elementPreprocessorMap.put("subpage", new SubPagePreprocessor());
		this.defaultTextFormatter = new DefaultTextFormatter();
		this.textFormatterMap.put("default", this.defaultTextFormatter);
	}

	public IMethod getMethod(String key){
		if (key == null){
			return this.methodMap.get("field");
		}else if(this.methodMap.containsKey(key)){
			return this.methodMap.get(key);
		}else{
			return null;
		}
	}

	public IOperator getOperator(String key){
		if (key != null && this.operatorMap.containsKey(key)){
			return this.operatorMap.get(key);
		}else{
			return null;
		}
	}

	public IElementPreprocessor getElementPreprocessor(String key){
		if (key != null && this.elementPreprocessorMap.containsKey(key)){
			return this.elementPreprocessorMap.get(key);
		}else{
			return this.defaultElementPreprocessor;
		}
	}

	public ITextFormatter getTextFormatter(String key){
		if (key != null && this.textFormatterMap.containsKey(key)){
			return this.textFormatterMap.get(key);
		}else{
			return this.defaultTextFormatter;
		}
	}

	@Override
	public ReportSetting clone() {
		try{
			ReportSetting ret = (ReportSetting)super.clone();
			ret.methodMap = new HashMap<String, IMethod>();
			for(String k: this.methodMap.keySet()){
				ret.methodMap.put(k, this.methodMap.get(k));
			}
			ret.operatorMap = new HashMap<String, IOperator>();
			for(String k: this.operatorMap.keySet()){
				ret.operatorMap.put(k, this.operatorMap.get(k));
			}
			ret.elementPreprocessorMap = new HashMap<String, IElementPreprocessor>();
			for(String k: this.elementPreprocessorMap.keySet()){
				ret.elementPreprocessorMap.put(k, this.elementPreprocessorMap.get(k));
			}
			ret.textFormatterMap = new HashMap<String, ITextFormatter>();
			for(String k: this.textFormatterMap.keySet()){
				ret.textFormatterMap.put(k, this.textFormatterMap.get(k));
			}
			return ret;
		}catch(CloneNotSupportedException e){
			throw new AssertionError();
		}
	}

}
