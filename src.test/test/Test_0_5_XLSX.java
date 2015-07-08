package test;

import java.io.FileOutputStream;
import java.util.Date;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test_0_5_XLSX {

	public static void main(String[] args) throws Throwable {
		String name = "test_0_5";

		long t = System.currentTimeMillis();
		
		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.globalScope.put("time", new Date());
        report.globalScope.put("lang", "java");
        report.fill(new ReportDataSource(Test_0_5_Data.getDataTable()));
		
		ReportPages pages = report.getPages();
		
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
		
		System.out.println((System.currentTimeMillis() - t));
	}
}
