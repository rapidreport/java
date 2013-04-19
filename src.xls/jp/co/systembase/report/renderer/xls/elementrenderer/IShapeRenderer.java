package jp.co.systembase.report.renderer.xls.elementrenderer;

import jp.co.systembase.report.renderer.xls.component.Page;
import jp.co.systembase.report.renderer.xls.component.Shape;

public interface IShapeRenderer {
	void render(Page page, Shape shape);
}
