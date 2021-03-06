package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.IReportLogger;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.ReportSetting;
import jp.co.systembase.report.component.ContentDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.EvalException;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.data.UnknownFieldException;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_26_UnknownField {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_26_unknownfield";

		ReportSetting setting = new ReportSetting();
		Logger logger = new Logger();
		setting.logger = logger;

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"), setting);
		report.fill(new ReportDataSource(getDataTable()));

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}

	private static DataTable getDataTable() throws Exception {
		DataTable ret = new DataTable();
		ret.setFieldNames("field1");
		ret.addRecord().puts("ページ１");
		ret.addRecord().puts("ページ２");
		return ret;
	}

	public static class Logger implements IReportLogger {
		@Override
		public void evaluateError(String exp, EvalException ex) {
		}
		@Override
		public void elementRenderingError(ContentDesign contentDesign, ElementDesign elementDesign, Throwable ex) {
		}
		@Override
		public void unknownFieldError(UnknownFieldException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
