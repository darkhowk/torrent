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
import org.jsoup.select.Elements;

import ca.benow.transmission.TransmissionClient;

public class torrentboza {
	
	public static void down(String title, String url) {
		
/*		String urlName = "torrentmap";
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

			torrent_comm.downList(urlName, contentList);
			
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
		*/
		
		
		
		/////////////////
	//	String[] URLList = {"https://torrentboza.com/bbs/board.php?bo_table=ent&page=","https://torrentboza.com/bbs/board.php?bo_table=daq&page=" };

		String urlName = "torrentboza";
		Connection conn = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
		
		Document doc;
		
		try {
			doc = conn.get();
		
			// get last page num
			ArrayList<Element> pageList = doc.select("ul.pagination li a[href]");
			String tmpPage = pageList.get(pageList.size()-1).toString();
			
			int LastPage = Integer.parseInt(tmpPage.substring(tmpPage.indexOf("page=")+5, tmpPage.indexOf("\"><i")));

			for (int i = 1 ; i < LastPage ; i ++) {
				
				Connection tmpListConn = Jsoup.connect(url+i).header("User-Agent", "Mozilla/5.0");
				Document tmpListDoc = tmpListConn.get();
				ArrayList<Element> tmpItemList = tmpListDoc.select("div.wr-subject a[href]");

				for (Element tmpitem : tmpItemList) {
					
					Pattern infoPattern = Pattern.compile(".*���� ��Ʋ �ڷ�����.*720.*");
					Matcher infoMatcher = infoPattern.matcher(tmpitem.text());
					
					if (infoMatcher.find()) {
						
						String[] tmpName = tmpitem.text().split("\\.");
						
						String ep = tmpName[1];
						String date = tmpName[2];
						
						String FileName = tmpitem.text();
		
						Connection tmpConn = Jsoup.connect(tmpitem.attr("href")).header("User-Agent", "Mozilla/5.0");
						Document tmpDoc = tmpConn.get();
						
						ArrayList<Element> torrentUrl = tmpDoc.select("div.list-group a.list-group-item");
						
						if (torrentUrl.size() > 1) {
							URL transmission = new URL("http://175.193.19.231:9091/transmission/rpc/");
							
							TransmissionClient tc = new TransmissionClient(transmission);
							
							Element tmpMagnet = (Element) torrentUrl.get(1);
							
							System.out.println("magnet text ::  " + tmpMagnet.text());
							
							String magnet = "";
							
				//			tc.addTorrent(tmpMagnet);
							
						}
						Element tmpTorrent = (Element) torrentUrl.get(0);
						
						byte[] bytes = Jsoup.connect(tmpTorrent.attr("href")).ignoreContentType(true).execute().bodyAsBytes();
						ByteBuffer buffer = ByteBuffer.wrap(bytes);
						
						File tmpFile = new File("C:/DATA/Watch/"+FileName);
						
						tmpFile.createNewFile();
						
						FileOutputStream fis = new FileOutputStream(tmpFile);
						FileChannel cin = fis.getChannel();
						cin.write(buffer);
						cin.close();
						fis.close();
							
					}// if end matcher.find 
				}
			} // for end
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}
