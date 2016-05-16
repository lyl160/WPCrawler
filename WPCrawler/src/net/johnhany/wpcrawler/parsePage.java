package net.johnhany.wpcrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.mysql.jdbc.StringUtils;

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
 * @parseFromString
 * extract link from <a> tags in a web page
 */
public class parsePage {
	public static String savePath ="C:\\data\\";
	public static Map<String, String> parseFromString(String title, String content) {
		Map<String, String> result = new HashMap<String, String>();
		Parser parser = createParser(content);
		HasAttributeFilter filter = new HasAttributeFilter("class","search-item");
		NodeList spanList = null;
		NodeFilter spanFilter = new TagNameFilter ("span");
		String name = null;
		String size = null;
		String link = null;
		try {
			NodeList itemList = parser.parse(filter);
			for(int i=0; i<itemList.size(); i++) {
				Node item = itemList.elementAt(i);
				spanList = new NodeList();
				item.collectInto(spanList, spanFilter);
				title = spanList.elementAt(0).getFirstChild().toPlainTextString();
				size = spanList.elementAt(5).toPlainTextString();
				link = spanList.elementAt(8).getFirstChild().toHtml();
				link = link.substring(link.indexOf("\"")+1, link.indexOf("\">"));
				result.put(link, title+"   "+size);
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
	
	public static void parseIMGFromString(String content,String title) throws Exception {
		Parser parser = createParser(content);
		HasAttributeFilter filter = new HasAttributeFilter("src");
		
		try {
			NodeList list = parser.parse(filter);
			int count = list.size();
			
			//process every link on this page
			for(int i=0; i<count; i++) {
				Node node = list.elementAt(i);
				
				if(node instanceof ImageTag) {
					ImageTag link = (ImageTag)node;
					String imageURL = link.getImageURL();
					System.out.println("imageURL:"+imageURL);
					try {
						downloadFile(imageURL, title.substring(title.lastIndexOf("/"),title.lastIndexOf("."))+"_"+i+imageURL.substring(imageURL.lastIndexOf(".")), savePath);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void downloadFile(String urlString, String filename,String savePath) throws Exception {  
        // 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();  
      
        // 1K的数据缓冲  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
        // 开始读取  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
        is.close();  
    }   
	
	
	public static Parser createParser(String inputHTML) {
        Lexer mLexer = new Lexer(new Page(inputHTML));
        return new Parser(mLexer,
                          new DefaultParserFeedback(DefaultParserFeedback.QUIET));
    }
	
	
	public static void writeToFile(String fanhao, Map<String , String> dataMap) {
		try{
		     BufferedWriter writer = new BufferedWriter(new FileWriter(new File(savePath+fanhao+".txt"),true));
		     for (String key : dataMap.keySet()) {
	    	   System.out.println("key= "+ key + " and value= " + dataMap.get(key));
	    	   writer.append(dataMap.get(key)+"\r\n");
	    	   writer.append(key+"\r\n");
	    	 }
		     writer.flush();
		     writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}