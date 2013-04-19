package example;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Test6 {

	public static void main(String[] args) throws Throwable {
		Report report = new Report(ReadUtil.readJson("report/test6_page.rrpt"));
		report.fill(new ReportDataSource(getDataTable()));
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/test6.pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/test6.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet("test6");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("group1", "group2", "group3", "num");
		ret.addRecord().puts(1, 1, 1, 1);
		ret.addRecord().puts(1, 1, 1, 1);
		ret.addRecord().puts(1, 2, 1, 1);
		ret.addRecord().puts(1, 2, 1, 1);
		ret.addRecord().puts(2, 1, 1, 1);
		ret.addRecord().puts(2, 1, 1, 1);
		return ret;
	}
}
