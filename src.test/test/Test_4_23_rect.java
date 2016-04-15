package test;

import java.io.FileOutputStream;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_23_rect {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_23_rect";
		
		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
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
