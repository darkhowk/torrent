package comm.util.torrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Torrentboza {

	public static ArrayList<HashMap<String, Object>>  Search(String name, String url, String ep, String season) {
		String tmpName = name.replace(" ", "+");
		
		ArrayList<HashMap<String, Object>> content = new ArrayList<HashMap<String, Object>>();
		
		Connection conn = Jsoup.connect(url + tmpName).header("User-Agent", "Mozilla/5.0");
		try {
			Document doc = conn.get();
			Elements els = doc.select(".wr-subject");
			
			for (Element el : els) {
				/* 제목 패턴
				 * Pattern namePattern = Pattern.compile("^*+"+name+"+.*$");
				 * Matcher nameMatcher = namePattern.matcher(el.text());
				 * 
				 * 시즌 패턴	
				 * Pattern sePattern = Pattern.compile("^*+.+"+season+"+.*$");
				 *	Matcher seMatcher = sePattern.matcher(el.text());
				 */
				
				
				// 에피소드를 지정하지 않으면, 해당 모든 것을 다운받느다.
				if(ep.equals("")) {
					HashMap<String , Object> item = new HashMap<String, Object>();
					Connection conn2 = Jsoup.connect(el.select(".item-subject").attr("href")).header("User-Agent", "Mozilla/5.0");
					Document doc2 = conn2.get();
					Elements tmpElts = doc2.select(".list-group-item");
					
					for (Element tmpElt : tmpElts) {
						if (tmpElt.text().contains("magnet:?")) {
							item.put("magnet", tmpElt.text());
						}
						else {
							item.put("file", tmpElt.select(".list-group-item").attr("href"));
						}
						
						item.put("name", el.text());
						
						content.add(item);
					}
				}
				
				// 에피소드를 선택하면, 해당 에피소드만 받는다.
				else {
					// 에피소드 패턴
					Pattern epPattern = Pattern.compile("^*+.+"+ep+"+.*$");
					Matcher epMatcher = epPattern.matcher(el.text());
					
					// 에피소드 패턴에 맞으면, 
					if (epMatcher.find()) {
						
						HashMap<String , Object> item = new HashMap<String, Object>();
						item.put("title", name);
						item.put("ep", ep);
						
						Connection conn2 = Jsoup.connect(el.select(".item-subject").attr("href")).header("User-Agent", "Mozilla/5.0");
						Document doc2 = conn2.get();
						Elements tmpElts = doc2.select(".list-group-item");
						
						for (Element tmpElt : tmpElts) {
							if (tmpElt.text().contains("magnet:?")) {
								item.put("magnet", tmpElt.text());
							}
							else {
								item.put("file", tmpElt.select(".list-group-item").attr("href"));
							}
							
							item.put("name", el.text());
							
							content.add(item);
						}
					}
				}
			}
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
		return content;
	}
}
