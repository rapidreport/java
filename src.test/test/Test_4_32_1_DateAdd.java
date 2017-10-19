package test;
import java.io.FileOutputStream;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.ReportSetting;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_32_1_DateAdd {

	public static void main(String[] args) throws Exception {
		String name = "test_4_32_1_dateadd";

		ReportSetting setting = new ReportSetting();
		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"), setting);
		report.fill(DummyDataSource.getInstance());

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
		
}
