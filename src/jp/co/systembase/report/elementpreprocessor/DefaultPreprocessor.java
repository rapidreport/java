package jp.co.systembase.report.elementpreprocessor;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.expression.EmbeddedTextProcessor;
import jp.co.systembase.report.renderer.IRenderer;

public class DefaultPreprocessor implements IElementPreprocessor {

	public Object doProcess(
			IRenderer renderer,
			Region region,
			Evaluator evaluator,
			ElementDesign design) throws Throwable {
		if (!design.isNull("exp")){
			return evaluator.evalTry((String)design.get("exp"));
		}else if (!design.isNull("text")){
			EmbeddedTextProcessor textProcessor = new EmbeddedTextProcessor();
			List<String> expressions = textProcessor.extractExpressions((String)design.get("text"));
			if (expressions != null){
				List<Object> ret = new ArrayList<Object>();
				for(String exp: expressions){
					ret.add(evaluator.evalTry(exp));
				}
				return ret;
			}
		}
		return null;
	}

}
