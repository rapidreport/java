package example;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import jp.co.systembase.core.Cast;
import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.component.Content;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.ElementDesigns;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.customizer.DefaultCustomizer;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.xls.XlsRenderer;

public class ExampleRender {

	public static void main(String[] args) throws Throwable {
		Report report = new Report(
				ReadUtil.readJson("report/example_render.rrpt"),
				new Customizer());
		report.fill(new ReportDataSource(getDataTable()));
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("output/example_render.pdf");
			try{
				pages.render(new PdfRenderer(fos));
			}finally{
				fos.close();
			}
		}
		{
			FileOutputStream fos = new FileOutputStream("output/example_render.xls");
			try{
				HSSFWorkbook workBook = new HSSFWorkbook();
				XlsRenderer renderer = new XlsRenderer(workBook);
				renderer.newSheet("example_render");
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}
	}

	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		ret.setFieldNames("NUM");
		ret.addRecord().puts(50);
		ret.addRecord().puts(40);
		ret.addRecord().puts(30);
		ret.addRecord().puts(20);
		ret.addRecord().puts(10);
		ret.addRecord().puts(0);
		ret.addRecord().puts(-10);
		ret.addRecord().puts(-20);
		ret.addRecord().puts(-30);
		ret.addRecord().puts(-40);
		ret.addRecord().puts(-50);
		return ret;
	}

	private static class Customizer extends DefaultCustomizer{
		public void renderContent(
				Content content,
				Evaluator evaluator,
				Region region,
				ElementDesigns elementDesigns) {
			// id = "content_example" となっているcontentに対して処理する
			if ("content_example".equals(content.design.id)){
				// graphのelementを得る
				ElementDesign e = elementDesigns.find("graph");
				float num = Cast.toFloat(evaluator.evalTry(".NUM"));
				if (num >= 0){
					e.child("layout").put("x1", 100);
					e.child("layout").put("x2", 100 + num);
					e.put("fill_color", "lightblue");
				}else{
					e.child("layout").put("x1", 100 + num);
					e.child("layout").put("x2", 100);
					e.put("fill_color", "pink");
				}
			}
		}
	}

}
