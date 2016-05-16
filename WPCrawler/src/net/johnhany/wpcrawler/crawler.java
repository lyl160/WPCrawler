package net.johnhany.wpcrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/*
 * WPsiteCrawler
 * a web crawler for single WordPress site
 * Author:	John Hany
 * Site:	http://johnhany.net/
 * Source code updates:	https://github.com/johnhany/WPCrawler
 * 
 * Using:	Apache HttpComponents 4.3 -- http://hc.apache.org/
 * 			HTML Parser 2.0 -- http://htmlparser.sourceforge.net/
 * 			MySQL Connector/J 5.1.27 -- http://dev.mysql.com/downloads/connector/j/
 * Thanks for their work!
 */

/*
 * @main
 * start from here
 */
public class crawler {
	
	public static void main(String args[]) throws Exception {
		String frontpage = "http://www.breadsearch.com/search/";
		//bubing
//		String[] fanhaos = { "Caribbeancom", "SKY", "RHJ", "SMD", "SMDV",
//				"SMBD", "Tokyo%20Hot", "heyzo", "CWP", "S2M", "SSKJ", "SSKP",
//				"MESUBUTA", "MURAMURA", "10MUSUME", "1000GIRL", "PACOPACOMAMA" };
		//qibi
		String[] fanhaos = { "1Pondo","IPTD", "IPZ", "SUPD", "PGD", "PJD",
				"KAWD", "KAPD", "YRZ", "INU", "EVO", "GYD", "HYK","KIRD","BLK","KISD",
				"MILD", "MIDD", "MIAD", "MIGD", "MIBD","SOE","SPS","ONSD","UPSM","BBI","BEB" };
		 
		//connect the MySQL database
		for (String fanhao : fanhaos) {
			Map<String , String> dataMap = new HashMap<String, String>();
			String responseBody = null;
			for (int i = 2; i <= 50; i++) {
				String url = frontpage+fanhao+"/"+i;
				try {
					responseBody =  httpGet.getByString(url);
					dataMap = parsePage.parseFromString(fanhao,responseBody);
					parsePage.writeToFile(fanhao,dataMap);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				System.out.println(fanhao+" "+i);
			}
			
		}
		
		System.out.println("Done.");
	}
}
