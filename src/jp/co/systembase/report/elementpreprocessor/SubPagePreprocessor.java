package jp.co.systembase.report.elementpreprocessor;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.IRenderer;

public class SubPagePreprocessor implements IElementPreprocessor {

	public Object doProcess(
			IRenderer renderer,
			Region region,
			Evaluator evaluator,
			ElementDesign design) throws Throwable {
		String k;
		Object i;
		if (design.isNull("key") || design.isNull("exp")){
			return null;
		}
		k = (String)design.get("key");
        i = evaluator.evalTry((String)design.get("exp"));
        if (i == null){
        	return null;
        }
        evaluator.basicContext.report.renderSubPage(renderer,region, k, Cast.toInt(i));
		return null;
	}

}
