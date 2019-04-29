package comm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ca.benow.transmission.TransmissionClient;
import comm.torrent.service.TorrentService;

public class torrent_comm {

	private static ArrayList<Element> tmpList;
	private static String name;
	private static String view;
	private static String rill;

	private static TorrentService service;
	
	public static ArrayList<Element> list_find_name(ArrayList<Element> tmpList, String name) {
			setTmpList(tmpList);
			setName(name);
			setView("");
			setRill("");
			return list_find_name();
	}
	
	public static ArrayList<Element> list_find_name(ArrayList<Element> tmpList, String name, String view) {
			setTmpList(tmpList);
			setName(name);
			setView(view);
			setRill("");
		return list_find_name();
	}
	
	
	public static ArrayList<Element> list_find_name(ArrayList<Element> tmpList, String name, String view, String rill) {
		setTmpList(tmpList);
		setName(name);
		setView(view);
		setRill(rill);
		return list_find_name();
	}
	
	public static void downList(String urlName, ArrayList<HashMap<String, Object>> contentList ) {
		
		switch(urlName) {
		
			case "torrentmap" :
				torrentmap_downList(contentList);
				break;
			
			default:
		
		}
	}
	
	private static void torrentmap_downList(ArrayList<HashMap<String, Object>> contentList ) {
		
		CommSession session = null;
		
		for (HashMap<String, Object> content : contentList) {
			
			Boolean db = true;
			
			String url = (String) content.get("url");
			String title = (String) content.get("title");
			String ep = (String) content.get("ep");
			
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
				
				magnet = "";
				if (magnet.equals("")) {
					String file_url = "https://www.torrentmap.com/skin/board/torrent/get_filetender2.php?bo_table=kr_ent&wr_id="+wr_id+"&bf_no=0";
					
					byte[] bytes = Jsoup.connect(file_url).ignoreContentType(true).execute().bodyAsBytes();
					ByteBuffer buffer = ByteBuffer.wrap(bytes);
					
					File tmpFile = new File("C:/DATA/Watch/"+title+ep+".torrent");
					
					db = tmpFile.createNewFile();
					
					FileOutputStream fis = new FileOutputStream(tmpFile);
					FileChannel cin = fis.getChannel();
					cin.write(buffer);
					cin.close();
					fis.close();
					
				}
				else {
					db = magnet_add(url);
				}
				
			}
			catch (Exception e1) {
				db = false;
			}
			
			if (db) {
				
				HashMap<String, Object> data = new HashMap<String, Object>();
				
				service.torrentLogInsert(data);
				
			}
		}
		
	}
	
	private static Boolean magnet_add(String url) {
		try {
			URL transmission_url = new URL("http://175.193.19.231:9091/transmission/rpc/");
			TransmissionClient tc = new TransmissionClient(transmission_url);
			tc.addTorrent(url);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	private static ArrayList<Element> list_find_name() {
		
		ArrayList<Element> itemList = new ArrayList<Element>();
		
		if (view.equals("")) {
			setView("720");
		}
		
		for (Element tmpitem : tmpList) {
			
			Pattern infoPattern = Pattern.compile(".*"+name+".*"+view+".*"+rill);
			Matcher infoMatcher = infoPattern.matcher(tmpitem.text());
			
			if (infoMatcher.find()) {
				itemList.add(tmpitem);
			}// if end matcher.find 
		}
		return itemList;
	}

	public ArrayList<Element> getTmpList() {
		return tmpList;
	}

	public static void setTmpList(ArrayList<Element> tmpList1) {
		tmpList = tmpList1;
	}

	public String getName() {
		return name;
	}

	public static void setName(String name1) {
		name = name1;
	}

	public static String getView() {
		return view;
	}

	public static void setView(String view1) {
		view = view1;
	}

	public String getRill() {
		return rill;
	}

	public static void setRill(String rill1) {
		rill = rill1;
	}
	
}
