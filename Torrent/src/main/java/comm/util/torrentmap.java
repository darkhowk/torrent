package comm.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class torrentmap {
	public static Boolean down(String title, String url) {
		
		Boolean result = false;
		
		String urlName = "torrentmap";
		//String url = "https://www.torrentmap.com/bbs/board.php?bo_table="+type+"&page=";
		
		Connection conn = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
		
		Document doc;
		
		ArrayList<HashMap<String, Object>> contentList = new ArrayList<HashMap<String, Object>>();
		
		try {
			doc = conn.get();
		
			// get last page num
			Elements last_page = doc.select(".pg_end");

			String tmpPage = last_page.attr("href");
			
			int lastPage = Integer.parseInt(tmpPage.substring(tmpPage.indexOf("page=")+5, tmpPage.length()));

			for (int i = 1; i < lastPage; i++) {
				
					conn = Jsoup.connect(url+i).header("User-Agent", "Mozilla/5.0");
					doc = conn.get();
					
					ArrayList<Element> tmpItemList = torrent_comm.list_find_name(doc.select(".td_subject"), title);
					
					for (Element tmpItem : tmpItemList) {
	
						String tmp = tmpItem.text().substring(tmpItem.text().indexOf(".E")+1);
						
						String ep = tmp.substring(0, tmp.indexOf("."));
						
						String tmpUrl = "";
						for (Element tmpEl : tmpItem.select("a")) {
							if (tmpEl.text().contains(title)) {
								tmpUrl = tmpEl.attr("href");
							}
						}
						
						HashMap<String, Object> content = new HashMap<String, Object>();
						content.put("name", tmpItem.text());
						content.put("title", title);
						content.put("url",tmpUrl);
						content.put("ep", ep);
						contentList.add(content);
						
					}
				
			}// for end

			result = torrent_comm.downList(urlName, contentList);
			
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return result;
	}
}
