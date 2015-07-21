package test;

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

public class Test_4_18_trim {

		public static void main(String[] args) throws Throwable {
			String name = "test_4_18_trim";
			
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
			ret.setFieldNames("A", "B", "C");
			ret.addRecord().puts("  a a  ", "b  ", "  c");
			return ret;
		}
}
