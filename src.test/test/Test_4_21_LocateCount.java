package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test_4_21_LocateCount {


	public static void main(String[] args) throws Throwable {
		String name = "test_4_21_locate_count";
		
		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(new ReportDataSource(getDataTable()));
		
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet(name);
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".xlsx");
			try{
				XSSFWorkbook workBook = new XSSFWorkbook();
				XlsxRenderer renderer = new XlsxRenderer(workBook);
				renderer.newSheet(name);
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
	}
	
	private static DataTable getDataTable() throws Exception {
		DataTable ret = new DataTable();
		ret.setFieldNames("no");
		ret.addRecord().puts(1);
		ret.addRecord().puts(2);
		ret.addRecord().puts(3);
		ret.addRecord().puts(4);
		ret.addRecord().puts(5);
		ret.addRecord().puts(6);
		ret.addRecord().puts(7);
		ret.addRecord().puts(8);
		return ret;
	}

}
