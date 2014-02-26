package jp.co.systembase.report.renderer.xlsx.elementrenderer;

import jp.co.systembase.report.renderer.xlsx.component.Page;
import jp.co.systembase.report.renderer.xlsx.component.Shape;

public interface IShapeRenderer {
	void render(Page page, Shape shape);
}
