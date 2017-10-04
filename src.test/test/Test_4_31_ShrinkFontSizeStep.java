package test;

import java.io.FileOutputStream;
import java.util.Date;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_31_ShrinkFontSizeStep {

	public static void main(String[] args) throws Exception {
		String name = "test_4_31_shrink_font_size_step";

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.globalScope.put("time", new Date());
        report.globalScope.put("lang", "java");
		report.fill(DummyDataSource.getInstance());

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + "_0.1.pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				renderer.setting.shrinkFontSizeStep = 0.1f;
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + "_0.5.pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				renderer.setting.shrinkFontSizeStep = 0.5f;
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}

}
