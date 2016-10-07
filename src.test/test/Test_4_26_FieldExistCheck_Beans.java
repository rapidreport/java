package test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.IReportLogger;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.ReportSetting;
import jp.co.systembase.report.component.ContentDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.EvalException;
import jp.co.systembase.report.component.UnknownFieldException;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_26_FieldExistCheck_Beans {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_26_field_exist_check";

		ReportSetting setting = new ReportSetting();
		Logger logger = new Logger();
		setting.logger = logger;

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"), setting);
		report.fill(new ReportDataSource(getDataTable()));

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + "_beans.pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}

	private static List<Record> getDataTable() throws Exception {
		List<Record> ret = new ArrayList<Record>();
		{
			Record r = new Record();
			r.setField1("正常に文字列が出力 頁1");
			ret.add(r);
		}
		{
			Record r = new Record();
			r.setField1("正常に文字列が出力 頁2");
			ret.add(r);
		}
		return ret;
	}

	public static class Record {
		private String field1;
		public String getField1() {
			return field1;
		}
		public void setField1(String field1) {
			this.field1 = field1;
		}
	}

	public static class Logger implements IReportLogger {
		@Override
		public void evaluateError(String exp, EvalException ex) {
			System.out.println(ex.getMessage());
			if(ex.getCause() != null){
				System.out.println(" " + ex.getCause().getClass().toString() + ":" + ex.getCause().getMessage());
			}
		}
		@Override
		public void elementRenderingError(ContentDesign contentDesign,
				ElementDesign elementDesign, Throwable ex) {
		}
		@Override
		public void unknownFieldError(UnknownFieldException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
