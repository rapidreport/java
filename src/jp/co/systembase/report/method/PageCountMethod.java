package jp.co.systembase.report.method;

import jp.co.systembase.report.ReportPage;
import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.Evaluator;

public class PageCountMethod implements IMethod {

	public EEvalContext getAvailableContext() {
		return EEvalContext.PAGE;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		ReportPage p = evaluator.pageContext.page;
		if (param != null && param.equals("entire")){
			return evaluator.pageContext.pages.indexOf(p) + 1;
		}else if (param != null && param.equals("entire_total")){
			return evaluator.pageContext.pages.size();
		}else if (param != null && param.equals("total")){
			return p.countingPages.size();
		}else{
			return p.countingPages.indexOf(p) + 1;
		}
	}

}
