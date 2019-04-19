package comm.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import comm.torrent.service.TorrentService;

@SuppressWarnings("unused")
public class TorrentSearch {

	private TorrentService service;
	
	private void search(HashMap<String, Object> programs) {
		
		// 1. 토렌트 주소 가져오기. 현재 완성된 것들만.
		List<HashMap<String, Object>> urlList = service.getUrl();
		
		// 주소별로 돌림.
		for (HashMap<String, Object> urlMap : urlList) {
			search((String)urlMap.get("url"));
		}
		
	}
	
		
	public String search(String url) {
		
		// 예능, 다큐 주소
	    // String[] URLList = {"https://torrentboza.com/bbs/board.php?bo_table=ent&page=","https://torrentboza.com/bbs/board.php?bo_table=daq&page=" };
		// String url = "https://torrentboza.com/bbs/board.php?bo_table=ent&page=";
		
		// 예능으로 접속 
		Connection conn = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
		Document doc;
		try {
			doc = conn.get();
		
			// get last page num
			ArrayList<Element> pageList = doc.select("ul.pagination li a[href]");
			String tmpPage = pageList.get(pageList.size()-1).toString();
			
			// 마지막 페이지 숫자로 구함
			int LastPage = Integer.parseInt(tmpPage.substring(tmpPage.indexOf("page=")+5, tmpPage.indexOf("\"><i")));

			// 검색할 것들 종류
	//		List<String> searchList = dao.getSearchList();
			
			
			// 페이지별로 해당 리스트를 가져온다.
			for (int i = 1 ; i < LastPage ; i ++) {
				
				Connection tmpListConn = Jsoup.connect(url+i).header("User-Agent", "Mozilla/5.0");
				Document tmpListDoc = tmpListConn.get();
				ArrayList<Element> tmpItemList = tmpListDoc.select("div.wr-subject a[href]");
				
				// 각 페이지의 글들을 돌린다.
				for (Element tmpitem : tmpItemList) {
					
					// 글들의 제목중, 찾고 있는 제목만 추린다.
					Pattern infoPattern = Pattern.compile(".*문제적 남자.*720.*");
					Matcher infoMatcher = infoPattern.matcher(tmpitem.text());
					
					// 찾고있는 녀석이면
					if (infoMatcher.find()) {
						
						System.out.println(tmpitem.text()); // 제목

						String[] tmpName = tmpitem.text().split("\\.");
						
						String ep = tmpName[1];
						String date = tmpName[2];
						
						System.out.println(ep);
						System.out.println(date);
						
						// 이름을 이름.EXXX.YYMMDD 으로 변경, DB저장
						
						String FileName = tmpitem.text();
		
						Connection tmpConn = Jsoup.connect(tmpitem.attr("href")).header("User-Agent", "Mozilla/5.0");
						Document tmpDoc = tmpConn.get();
						
						ArrayList<Element> torrentUrl = tmpDoc.select("div.list-group a.list-group-item");
						
						// 0번 토렌트 파일, 1번 마그넷 주소
						Element tmpTorrent = (Element) torrentUrl.get(0);
						
						// 해당 파일을 Watch폴더에 다운받는다.
						byte[] bytes = Jsoup.connect(tmpTorrent.attr("href")).ignoreContentType(true).execute().bodyAsBytes();
						ByteBuffer buffer = ByteBuffer.wrap(bytes);
						 
						File tmpFile = new File("/DATA/Watch/"+FileName);
						
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
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return "main";
		
	}
}
