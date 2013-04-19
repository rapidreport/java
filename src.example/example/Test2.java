package example;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;


public class Test2 {

	public static void main(String[] args) throws Throwable {
		Report report = new Report(ReadUtil.readJson("report/test2.rrpt"));
		report.fill(new ReportDataSource(getDataTable()));
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/test2.pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/test2.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet("test2");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("CODE39", "CODE128", "EAN8", "EAN13", "CODABAR", "QRCODE");
        ret.addRecord().puts(
        		"12345678",
        		"12345abcABC",
        		"8765432",
        		"321098765432",
        		"12345678",
        		"ホームページ: http://www.systembase.co.jp/");
		return ret;
	}
}
