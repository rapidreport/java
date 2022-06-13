package test;

import java.io.FileOutputStream;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_5_14_Date {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_14_date";

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(DummyDataSource.getInstance());

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
	}
}
