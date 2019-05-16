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

	private static String name;
	private static String url;
	private static String ep;
	private static String season;

	
	public static String getEp() {
		return ep;
	}
	public static void setEp(String ep) {
		Torrentboza.ep = ep;
	}
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		Torrentboza.name = name;
	}
	public static String getUrl() {
		return url;
	}
	public static void setUrl(String url) {
		Torrentboza.url = url;
	}
	public static String getSeason() {
		return season;
	}
	public static void setSeason(String season) {
		Torrentboza.season = season;
	}
	
	public static ArrayList<HashMap<String, Object>>  Search(String name, String url) {
		setName(name);
		setUrl(url);
		setEp("");
		setSeason("");
		return Search();			
	}
	
	public static ArrayList<HashMap<String, Object>>  Search(String name, String url, String ep) {
		setName(name);
		setUrl(url);
		setEp(ep);
		setSeason("");
		return Search();			
	}
	
	public static ArrayList<HashMap<String, Object>>  Search(String name, String url, String ep, String season) {
		setName(name);
		setUrl(url);
		setEp(ep);
		setSeason(season);
		return Search();			
	}
	
	
	public static ArrayList<HashMap<String, Object>>  Search() {
		
		String url = getUrl();
		String name = getName();
		String tmpName = name.replace(" ", "+");
		String ep = getEp();
		
		Boolean ep_yn = false;
		
		ArrayList<HashMap<String, Object>> content = new ArrayList<HashMap<String, Object>>();
		
		Connection conn = Jsoup.connect(url + tmpName).header("User-Agent", "Mozilla/5.0");
		try {
			Document doc = conn.get();
			Elements els = doc.select(".wr-subject");
			
			for (Element el : els) {
				if (ep.equals("")) {
					ep_yn = true;
				}
				else {
					
				}
				/* Pattern namePattern = Pattern.compile("^*+"+name+"+.*$");
				Matcher nameMatcher = namePattern.matcher(el.text());
				 */
				Pattern epPattern = Pattern.compile("^*+.+"+ep+"+.*$");
				Matcher epMatcher = epPattern.matcher(el.text());
				
			/*	Pattern sePattern = Pattern.compile("^*+.+"+season+"+.*$");
				Matcher seMatcher = sePattern.matcher(el.text());
			*/
				if (epMatcher.find() || ep_yn) {
					
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
			}
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
		return content;
	}
}
