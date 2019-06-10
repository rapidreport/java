package test;

import java.text.SimpleDateFormat;

import jp.co.systembase.core.DataTable;

public class Test_0_5_Data {

	public static DataTable getDataTable() throws Throwable
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        DataTable ret = new DataTable();
        ret.setFieldNames("bumonCd", "bumon", "uriageDate", "denpyoNo", 
        		"shohinCd", "shohin", "tanka", "suryo");
        for (int i = 1; i <= 100; i++)
        {
            for (int j = 1; j <= 50; j++)
            {
                ret.addRecord().puts(i, "部門" + i, sdf.parse("2013/02/01"), 
                		j, "PC00001", "ノートパソコン", 70000, 10);
                ret.addRecord().puts(i, "部門" + i, sdf.parse("2013/02/01"), 
                		j, "DP00002", "モニター", 25000, 10);
                ret.addRecord().puts(i, "部門" + i, sdf.parse("2013/02/01"),
                		j, "PR00003", "プリンタ", 20000, 2);
                ret.addRecord().puts(i, "部門" + i, sdf.parse("2013/02/10"), 
                		j, "PR00003", "プリンタ", 20000, 3);
            }
        }
        return ret;
    }
    
}
