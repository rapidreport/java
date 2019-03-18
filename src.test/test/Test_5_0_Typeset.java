package test;

import java.io.FileOutputStream;

import com.lowagie.text.pdf.BaseFont;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_5_0_Typeset {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_0_typeset";

		// Report.Compatibility._4_37_Typeset = true;

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(DummyDataSource.getInstance());

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				renderer.setting.gaijiFont = BaseFont.createFont("rrpt/font/eudc.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}
}
