package jp.co.systembase.report;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.renderer.IRenderer;
import jp.co.systembase.report.renderer.RenderException;

public class ReportPages extends ArrayList<ReportPage> {
	private static final long serialVersionUID = -1655861914307614398L;

	public Report report;

	public ReportPages(Report report){
		this.report = report;
	}

	public void render(IRenderer renderer) throws RenderException{
		if (this.size() == 0){
			throw new RenderException("ページがありません");
		}
		try{
			this.setUpCountingPages();
			renderer.beginReport(this.report.design);
			for(ReportPage page: this){
				page.render(renderer, this);
			}
			renderer.endReport(this.report.design);
		}catch(Throwable ex){
			if (ex instanceof RenderException){
				throw (RenderException)ex;
			}else{
				String message = "帳票のレンダリング中にエラーが発生しました";
				throw new RenderException(message, ex);
			}
		}
	}

	public void setUpCountingPages(){
		List<ReportPage> countingPages = null;
		for(ReportPage p: this){
			if (countingPages == null || p.resetPageCount){
				countingPages = new ArrayList<ReportPage>();
			}
			countingPages.add(p);
			p.countingPages = countingPages;
		}
	}

}
