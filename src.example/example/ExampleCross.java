package example;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

public class ExampleCross {

	public static void main(String[] args) throws Throwable {
		Report report = new Report(ReadUtil.readJson("report/example_cross.rrpt"));
		report.fill(new ReportDataSource(getDataTable()));
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/example_cross.pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example_cross.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet("example_cross");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example_cross.xlsx");
			try{
				XSSFWorkbook workBook = new XSSFWorkbook();
				XlsxRenderer renderer = new XlsxRenderer(workBook);
				renderer.newSheet("example_cross");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}		
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("ITEM", "ITEM_NAME", "OFFICE", "OFFICE_NAME", "NUM");
		ret.addRecord().puts(1, "科目1", 1, "事業所1", 0);
		ret.addRecord().puts(1, "科目1", 2, "事業所2", 10);
		ret.addRecord().puts(1, "科目1", 3, "事業所3", 0);
		ret.addRecord().puts(1, "科目1", 4, "事業所4", 100);
		ret.addRecord().puts(2, "科目2", 1, "事業所1", 999);
		ret.addRecord().puts(2, "科目2", 2, "事業所2", 555);
		ret.addRecord().puts(2, "科目2", 3, "事業所3", 0);
		ret.addRecord().puts(2, "科目2", 4, "事業所4", 0);
		ret.addRecord().puts(3, "科目3", 1, "事業所1", 12345);
		ret.addRecord().puts(3, "科目3", 2, "事業所2", 3456);
		ret.addRecord().puts(3, "科目3", 3, "事業所3", 4567);
		ret.addRecord().puts(3, "科目3", 4, "事業所4", 56789);
		return ret;
	}

}
