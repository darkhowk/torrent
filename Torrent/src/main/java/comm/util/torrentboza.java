package comm.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class torrentboza {

	public static Boolean down(String title, String url) {
		
		Boolean result = false;
		
		String urlName = "torrentboza";
		
		Connection conn = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
		
		Document doc;
		
		ArrayList<HashMap<String, Object>> contentList = new ArrayList<HashMap<String, Object>>();
		
		try {
			doc = conn.get();
			// get last page num
			ArrayList<Element> pageList = doc.select("ul.pagination li a[href]");
			String tmpPage = pageList.get(pageList.size()-1).toString();
			
			int LastPage = Integer.parseInt(tmpPage.substring(tmpPage.indexOf("page=")+5, tmpPage.indexOf("\"><i")));

			LastPage = 4;
			
			for (int i = 1 ; i < LastPage ; i ++) {
				Connection tmpListConn = Jsoup.connect(url+i).header("User-Agent", "Mozilla/5.0");
				Document tmpListDoc = tmpListConn.get();
				
				ArrayList<Element> tmpItemList = torrent_comm.list_find_name(tmpListDoc.select(".wr-subject"), title);
				
				for (Element tmpItem : tmpItemList) {
					
					String tmp = tmpItem.text().substring(tmpItem.text().indexOf(".E")+1);
					
					String ep = tmp.substring(0, tmp.indexOf("."));
					
					String tmpUrl = tmpItem.select("a").attr("href");

					HashMap<String, Object> content = new HashMap<String, Object>();
					content.put("name", tmpItem.text());
					content.put("title", title);
					content.put("url",tmpUrl);
					content.put("ep", ep);
					contentList.add(content);
					
				}
			} // for end
			
			System.out.println("down start");
			result = torrent_comm.downList(urlName, contentList);
			
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}

		return result;
	}
}
