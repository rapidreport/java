package example;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.GroupDataProvider;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

public class Example2 {

	public static void main(String[] args) throws Throwable {
		Report report = new Report(ReadUtil.readJson("report/example2.rrpt"));
		GroupDataProvider dataProvider = new GroupDataProvider();
		dataProvider.groupDataMap.put("group_shonin", new ReportDataSource(getSubDataTable()));
		report.fill(new ReportDataSource(getDataTable()), dataProvider);
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/example2.pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example2.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet("example2");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example2.xlsx");
			try{
				XSSFWorkbook workBook = new XSSFWorkbook();
				XlsxRenderer renderer = new XlsxRenderer(workBook);
				renderer.newSheet("example2");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}		
	}

	private static DataTable getDataTable() throws Throwable{
		DataTable ret = new DataTable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		ret.setFieldNames("HAT_ID", "TOKUI_NM", "TOKUI_TANTO_NM", "HIN_NM", "HIN_CD", "SURYO", "TANKA", "SHUKKABI");
		ret.addRecord().puts(1, "HOGE精密", "担当太郎", "パイロットパンチ", "AAA-BBB-CCC-DDD-1000", 1, 600, sdf.parse("2011/06/07"));
		ret.addRecord().puts(1, "HOGE精密", "担当太郎", "ガイドプレート", "AIUEO-999.999", 5, 1050, sdf.parse("2011/06/15"));
		ret.addRecord().puts(1, "HOGE精密", "担当太郎", "イジェクタピン", "1234-5678-9999", 1, 7340, sdf.parse("2011/06/13"));
		ret.addRecord().puts(2, "FUGA機械", "担当花子", "ブロックダイ", "9999-8888-7777", 10, 1600, sdf.parse("2011/06/10"));
		ret.addRecord().puts(2, "FUGA機械", "担当花子", "ブランジャ", "ZZZZZ-YYYYY-XXXXX", 5, 800, sdf.parse("2011/06/10"));
		return ret;
	}

	private static DataTable getSubDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("HAT_ID", "SHONIN_NM");
		ret.addRecord().puts(1, "承認一郎");
		ret.addRecord().puts(1, "承認次郎");
		ret.addRecord().puts(1, "承認三郎");
		ret.addRecord().puts(1, "承認四郎");
		ret.addRecord().puts(2, "承認花子");
		return ret;
	}

}
