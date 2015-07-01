package jp.co.systembase.report.search.searchobject;

import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.expression.EmbeddedTextProcessor;

public class TextSearchObject implements ISearchObject {

	@Override
	public String getText(
			ReportDesign reportDesign, 
			ElementDesign design,
			Object data) throws Throwable {
		String ret = (String)design.get("text");
		if (data != null){
			EmbeddedTextProcessor textProcessor = new EmbeddedTextProcessor();
			ret = textProcessor.embedData(reportDesign, design.child("formatter"), ret, (List<?>) data);
		}
		return ret;
	}

}
