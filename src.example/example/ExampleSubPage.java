package example;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.data.SubPageDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

public class ExampleSubPage {

	public static void main(String[] args) throws Throwable {
		Report subReport = new Report(ReadUtil.readJson("report/example_subpage2.rrpt"));
		subReport.fill(new ReportDataSource(getDataTable()));
		ReportPages subPages = subReport.getPages();
		Report report = new Report(ReadUtil.readJson("report/example_subpage1.rrpt"));
		report.addSubPages("subpage", subPages);
		report.fill(new SubPageDataSource(subPages, "group1", "page1", "page2"));
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/example_subpage.pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				pages.render(renderer);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example_subpage.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet("example_subpage");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example_subpage.xlsx");
			try{
				XSSFWorkbook workBook = new XSSFWorkbook();
				XlsxRenderer renderer = new XlsxRenderer(workBook);
				renderer.newSheet("example_subpage");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}		
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("g1", "g2", "num");
		ret.addRecord().puts("A", "A1", 123);
		ret.addRecord().puts("A", "A1", 456);
		ret.addRecord().puts("A", "A1", 200);
		ret.addRecord().puts("A", "A1", 100);
		ret.addRecord().puts("A", "A1", 99);
		ret.addRecord().puts("A", "A1", 88);
		ret.addRecord().puts("A", "A1", 77);
		ret.addRecord().puts("A", "A1", 230);
		ret.addRecord().puts("A", "A2", 109);
		ret.addRecord().puts("A", "A2", 10);
		ret.addRecord().puts("A", "A3", 120);
		ret.addRecord().puts("A", "A3", 63);
		ret.addRecord().puts("A", "A4", 30);
		ret.addRecord().puts("A", "A4", 97);
		ret.addRecord().puts("B", "B1", 10);
		ret.addRecord().puts("B", "B2", 22);
		ret.addRecord().puts("B", "B2", 44);
		return ret;
	}
}
