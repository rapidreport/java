package test;

import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

public class Test_5_4_RenderSheet {

	public static void main(String[] args) throws Throwable {
		String name = "test_5_4_rendersheet";
		
		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.fill(new ReportDataSource(getDataTable()));
		
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet(name);
				renderer.renderSheet(report);
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
				renderer.renderSheet(report);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
		
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + "_pages.xls");
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
			FileOutputStream fos = new FileOutputStream("out/" + name + "_pages.xlsx");
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

	public static DataTable getDataTable() throws Throwable
    {
        DataTable ret = new DataTable();
        ret.setFieldNames("col1", "col2", "col3", "col4"); 
        for (int i = 1; i <= 300; i++)
        {
        	Calendar cl = Calendar.getInstance();
        	cl.set(2020, 1, 1);
        	cl.add(Calendar.DATE, i);
        	ret.addRecord().puts("行" + i, cl.getTime(), i * 50, i * 15 + i * 0.01);
        }
        return ret;
    }
	
}
