package jp.co.systembase.report.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPage;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.ReportDataRecord;
import jp.co.systembase.report.expression.IExpression;
import jp.co.systembase.report.expression.Parser;

public class Evaluator {

	public BasicContext basicContext = null;
	public ContentContext contentContext = null;
	public PageContext pageContext = null;

	public static class BasicContext{
		public Report report;
		public ReportData data;
		public ReportDataRecord dataRecord;
	}

	public static class ContentContext{
		public Content content;
		public ContentState contentState;
	}

	public static class PageContext{
		public ReportPage page;
		public ReportPages pages;
		public DataContainer dataContainer;
		public Map<String, Object> variables = new HashMap<String, Object>();
	}

	public Evaluator(){}

	public Evaluator(Report report, ReportData data, ReportDataRecord dataRecord){
		this.basicContext = new BasicContext();
		this.basicContext.report = report;
		this.basicContext.data = data;
		this.basicContext.dataRecord = dataRecord;
	}

	public Evaluator(Content content, ContentState contentState){
		this.basicContext = new BasicContext();
		this.basicContext.report = content.getReport();
		this.basicContext.data = content.getData();
		this.basicContext.dataRecord = content.getData().getRecord();
		this.contentContext = new ContentContext();
		this.contentContext.content = content;
		this.contentContext.contentState = contentState;
	}

	public Evaluator(
			ReportPage page,
			ReportPages pages,
			Content content,
			ContentState contentState,
			DataContainer dataContainer){
		this.basicContext = new BasicContext();
		this.basicContext.report = content.getReport();
		this.basicContext.data = content.getData();
		this.basicContext.dataRecord = content.getData().getRecord();
		this.contentContext = new ContentContext();
		this.contentContext.content = content;
		this.contentContext.contentState = contentState;
		this.pageContext = new PageContext();
		this.pageContext.page = page;
		this.pageContext.pages = pages;
		this.pageContext.dataContainer = dataContainer;
		{
			ContentDesign cd = this.contentContext.content.design;
			if (cd.variables != null){
				for(String k: cd.variables.keySet()){
					this.pageContext.variables.put(k, this.evalTry(cd.variables.get(k)));
				}
			}
		}
	}

	public Object evalTry(String exp){
		try{
			return this.eval(exp);
		}catch(Throwable ex){
			return null;
		}
	}

	public Object eval(String exp) throws EvalException{
		try{
			if (exp == null){
				return null;
			}
			String _exp = exp.trim();
			if (_exp.length() == 0){
				return null;
			}
			if (!this.basicContext.data.dataCache.expression.containsKey(_exp)){
				Parser parser = new Parser(this.basicContext.report.design.setting);
				this.basicContext.data.dataCache.expression.put(_exp, parser.parse(_exp));
			}
			return this.eval(this.basicContext.data.dataCache.expression.get(_exp));
		}catch(Throwable ex){
			EvalException _ex;
			if (ex instanceof EvalException){
				_ex = (EvalException)ex;
			}else{
				_ex = new EvalException("an error occurred while evaluating exp : " + exp, ex);
			}
			if (this.basicContext.report.design.setting.logger != null){
				this.basicContext.report.design.setting.logger.evaluateError(exp, _ex);
			}
			throw _ex;
		}
	}

	public Object eval(IExpression exp) throws Throwable{
		return exp.eval(this);
	}

	public ReportData getData(String scope, String unit){
		ReportData scopeData = this.basicContext.data.findScope(scope);
		if (scopeData == null){
			throw new IllegalArgumentException("invalid scope" + (!scope.equals("") ? ": " + scope : ""));
		}
		if (unit == null){
			return scopeData;
		}else{
			GroupDesign unitGroupDesign = scopeData.findUnit(unit);
			if (unitGroupDesign == null){
				throw new IllegalArgumentException("invalid unit" + (!unit.equals("") ? ": " + unit : ""));
			}
			IReportDataSource dataSource = scopeData.getWrapperDataSource(unitGroupDesign);
			IndexRange indexRange = scopeData.getDataIndexRange(unitGroupDesign);
			DataCache dataCache = this.basicContext.report.dataCache;
			if (indexRange != null){
				return new ReportData(
						dataSource,
						indexRange.beginIndex,
						indexRange.endIndex,
						dataCache);
			}else{
				return new ReportData(dataSource, dataCache);
			}
		}
	}

	public ReportData getPageData(String scope, String unit){
		return this.pageContext.dataContainer.getPageData(this.contentContext.content, scope);
	}

	public ReportData getPresentData(String scope, String unit){
		return this.pageContext.dataContainer.getPresentData(this.contentContext.content, scope);
	}

}
