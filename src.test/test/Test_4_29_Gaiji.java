package test;

import java.io.FileOutputStream;

import com.lowagie.text.pdf.BaseFont;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_29_Gaiji {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_29_gaiji";
			
		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(DummyDataSource.getInstance());
		
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				renderer.setting.gaijiFont = BaseFont.createFont("rrpt/font/eudc.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				renderer.setting.gaijiFontMap.put("gothic", BaseFont.createFont("rrpt/font/msgothic.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
	}

}
