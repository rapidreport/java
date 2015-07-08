package test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.renderer.ImageMap;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;
import jp.co.systembase.report.renderer.pdf.imageloader.PdfImageLoader;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xls.imageloader.XlsImageLoader;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;
import jp.co.systembase.report.renderer.xlsx.imageloader.XlsxImageLoader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test_0_4 {
	
	private static ImageMap _getImageMap() throws Throwable{
		ImageMap ret = new ImageMap();
		ret.put("bmp", ImageIO.read(new File("./img/logo.bmp")));
		ret.put("gif", ImageIO.read(new File("./img/logo.gif")));
		ret.put("jpg", ImageIO.read(new File("./img/logo.jpg")));
		ret.put("png", ImageIO.read(new File("./img/logo.png")));
		return ret;
	}

	public static void main(String[] args) throws Throwable {
		String name = "test_0_4";
		
		ImageMap imageMap = _getImageMap();
		
		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"));
		report.globalScope.put("time", new Date());
        report.globalScope.put("lang", "java");
		report.fill(DummyDataSource.getInstance());
		
		ReportPages pages = report.getPages();
		{
			FileOutputStream fos = new FileOutputStream("out/" + name + ".pdf");
			try{
				PdfRenderer renderer = new PdfRenderer(fos);
				renderer.imageLoaderMap.put("image", new PdfImageLoader(imageMap));
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
				renderer.imageLoaderMap.put("image", new XlsImageLoader(imageMap));
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
				renderer.imageLoaderMap.put("image", new XlsxImageLoader(imageMap));
				pages.render(renderer);
				workBook.write(fos);
			}finally{
				fos.close();
			}
		}		
	}

}
