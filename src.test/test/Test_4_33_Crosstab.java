package test;

import java.io.FileOutputStream;

import jp.co.systembase.core.DataTable;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.ReportSetting;
import jp.co.systembase.report.data.ReportDataSource;
import jp.co.systembase.report.renderer.pdf.PdfRenderer;

public class Test_4_33_Crosstab {

	public static void main(String[] args) throws Exception {
		String name = "test_4_33_crosstab";

		ReportSetting setting = new ReportSetting();
		Report report = new Report(ReadUtil.readJson("rrpt/" + name + ".rrpt"), setting);
		report.fill(new ReportDataSource(getDataTable()));

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
	}
	
	private static DataTable getDataTable(){
		DataTable ret = new DataTable();
		String branchNms[] = 
				{"北上本店", "東京支店", "盛岡営業所", "秋田営業所",
	             "仙台営業所", "山形営業所", "福島営業所"};
		String periodNms[] = 
            {"2010年上期", "2010年下期", "2011年上期", "2011年下期",
	         "2012年上期", "2012年下期", "2013年上期", "2013年下期",
	         "2014年上期", "2014年下期", "2015年上期", "2015年下期",
	         "2016年上期", "2016年下期"};				
		ret.setFieldNames("branch_cd", "branch_nm", "period_cd", "period_nm", "amount");
		for(int i = 0;i < 14;i++){
			for(int j = 0;j < (i < 4 ? 5 : 7);j++){
				ret.addRecord().puts(j + 1, branchNms[j], i + 1, periodNms[i], 10000 + i * 100 + j * 10);
			}
		}
		return ret;
	}
	
}
