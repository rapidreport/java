package jp.co.systembase.report.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPage;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.ReportDataRecord;
import jp.co.systembase.report.expression.IExpression;
import jp.co.systembase.report.expression.Parser;
import jp.co.systembase.report.scanner.RenderingScanner;

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

	public Evaluator(ReportData data){
		this(data.report, data, data.getRecord());
	}
	
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
			ContentInstance contentInstance,
			RenderingScanner scanner){
		this(page, pages, contentInstance.content, contentInstance.contentState, scanner.dataContainer);
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
			if (!this.basicContext.data.context.dataCache.expression.containsKey(_exp)){
				Parser parser = new Parser(this.basicContext.report.design.setting);
				this.basicContext.data.context.dataCache.expression.put(_exp, parser.parse(_exp));
			}
			return this.eval(this.basicContext.data.context.dataCache.expression.get(_exp));
		}catch(Throwable ex){
			EvalException _ex;
			if (ex instanceof EvalException){
				_ex = (EvalException)ex;
			}else{
				_ex = new EvalException("式の評価中にエラーが発生しました : " + exp, ex);
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
			throw new IllegalArgumentException("集計範囲が不正です" + (!scope.equals("") ? ": " + scope : ""));
		}
		if (unit == null){
			return scopeData;
		}else{
			GroupDesign unitGroupDesign = scopeData.findUnit(unit);
			if (unitGroupDesign == null){
				throw new IllegalArgumentException("集計単位が不正です" + (!unit.equals("") ? ": " + unit : ""));
			}
			IReportDataSource dataSource = scopeData.getWrapperDataSource(unitGroupDesign);
			IndexRange indexRange = scopeData.getDataIndexRange(unitGroupDesign);
			Report.Context context = this.basicContext.report.context;
			if (indexRange != null){
				return new ReportData(
						dataSource,
						indexRange.beginIndex,
						indexRange.endIndex,
						context);
			}else{
				return new ReportData(dataSource, context);
			}
		}
	}

	public ReportData getPageData(String scope, String unit){
		return this.pageContext.dataContainer.getPageData(this.contentContext.content, scope);
	}

	public ReportData getPresentData(String scope, String unit){
		return this.pageContext.dataContainer.getPresentData(this.contentContext.content, scope);
	}

	public void ValidateParamCount(List<IExpression> params, int count){
		if (params.size() < count){
			throw new IllegalArgumentException("引数が足りません");
		}
	}

}
