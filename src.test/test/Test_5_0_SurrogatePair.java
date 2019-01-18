package test;

import java.io.FileOutputStream;

import com.lowagie.text.pdf.BaseFont;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_5_0_SurrogatePair {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_0_surrogate_pair";

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(DummyDataSource.getInstance());

		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				renderer.setting.fontMap.put("gothic", BaseFont.createFont("rrpt\\font\\ipamjm.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
				//renderer.setting.fontMap.put("gothic", BaseFont.createFont("MS-Gothic", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED));
				//renderer.setting.fontMap.put("mincho", BaseFont.createFont("MS-Mincho", "UniJIS-UCS2-H", BaseFont.NOT_EMBEDDED));
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}

}
