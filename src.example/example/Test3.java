package example;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

//import com.lowagie.text.pdf.BaseFont;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

public class Test3 {

	public static void main(String[] args) throws Throwable {
		Report report = new Report(ReadUtil.readJson("report/test3.rrpt"));
		report.fill(DummyDataSource.getInstance());
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/test3.pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				//renderer.setting.gaijiFont = BaseFont.createFont("EUDC.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/test3.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.setting.customPalette = true;
				renderer.newSheet("test3");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/test3.xlsx");
			try{
				XSSFWorkbook workBook = new XSSFWorkbook();
				XlsxRenderer renderer = new XlsxRenderer(workBook);
				renderer.newSheet("test3");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}		
	}

}
