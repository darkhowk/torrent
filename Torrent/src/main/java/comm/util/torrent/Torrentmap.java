package comm.util.torrent;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Torrentmap {
	public static ArrayList<HashMap<String, Object>> Search(String name, String url ) {
		
		Connection conn = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
		
		Document doc;
		
		ArrayList<HashMap<String, Object>> contentList = new ArrayList<HashMap<String, Object>>();
		
		try {
			doc = conn.get();
		
			// 1. get last Page
			Elements last_page = doc.select(".pg_end");
			String tmpPage = last_page.attr("href");
			int lastPage = Integer.parseInt(tmpPage.substring(tmpPage.indexOf("page=")+5, tmpPage.length()));

			// 2. page Search
			for (int i = 1; i <= lastPage; i++) {
				
					conn = Jsoup.connect(url+i).header("User-Agent", "Mozilla/5.0");
					doc = conn.get();
					
					// get item from pageitem with name
			//		ArrayList<Element> tmpItemList = TorrentComm.list_find_name(doc.select(".td_subject"), name);
					
					// make item list-map
				/*	for (Element tmpItem : tmpItemList) {
						String tmp = tmpItem.text().substring(tmpItem.text().indexOf(".E")+1);
						String ep = tmp.substring(0, tmp.indexOf("."));
						HashMap<String, Object> content = new HashMap<String, Object>();
						content.put("name", tmpItem.text());
						content.put("title", name);
						content.put("ep", ep);
						contentList.add(content);*/
				/*	} // make item end
				*/
			}// page Search end
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
		return contentList;
	}
	
	public static int downList(ArrayList<HashMap<String, Object>> contentList ) {
		
		int result = 0;
		for (HashMap<String, Object> content : contentList) {
			String url = (String) content.get("url");
			String wr_id = url.substring(url.indexOf("wr_id=")+6, url.indexOf("&page="));
			Connection conn = Jsoup.connect((String) content.get("url")).header("User-Agent", "Mozilla/5.0");
			Document doc;
			
			try {
				doc = conn.get();
				
				String magnet = "";
				for (Element el : doc.select("a")) {
					if (el.text().equals("마그넷주소")) {
						magnet = el.attr("href");
					}
				}
				
				if (magnet.equals("")) {
					/*result++;*/
				/*	String file_url = "https://www.torrentmap.com/skin/board/torrent/get_filetender2.php?bo_table=kr_ent&wr_id="+wr_id+"&bf_no=0";
					content.put("file_url", file_url);
					TorrentComm.file_down(content);*/
				}
				else {
					result++;
					content.put("magnet", magnet);
					TorrentComm.magnet_add(content);
				}
				
			}
			catch (Exception e1) {
			}
		}
		
		return result;
		
	}
}
