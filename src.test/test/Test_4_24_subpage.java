package test;

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

public class Test_4_24_subpage {

	public static void main(String[] args) throws Throwable {
		String name = "test_4_24_subpage";

		Report subReport = new Report(ReadUtil.readJson("rrpt/" + name + "2.rrpt"));
		subReport.fill(new ReportDataSource(getDataTable()));
		subReport.fill(new ReportDataSource(getDataTable()));
		ReportPages subPages = subReport.getPages();

		Report report = new Report(ReadUtil.readJson("rrpt/" + name + "1.rrpt"));
		report.addSubPages("subpage", subPages);
		report.fill(new SubPageDataSource(subPages, "group1", "page1", "page2"));
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

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("g1", "g2", "num");
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A1", 10);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("A", "A2", 100);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B1", 20);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		ret.addRecord().puts("B", "B2", 200);
		return ret;
	}
}
