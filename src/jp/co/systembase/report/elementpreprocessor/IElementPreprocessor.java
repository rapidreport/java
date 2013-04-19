package jp.co.systembase.report.elementpreprocessor;

import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.IRenderer;

public interface IElementPreprocessor {
	Object doProcess(
			IRenderer renderer,
			Region region,
			Evaluator evaluator,
			ElementDesign design) throws Throwable;
}
