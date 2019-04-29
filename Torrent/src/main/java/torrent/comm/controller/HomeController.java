package torrent.comm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ca.benow.transmission.TransmissionClient;
import ca.benow.transmission.model.TorrentStatus;
import comm.util.torrentmap;
import torrent.comm.service.HomeService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Resource(name="homeService")
	private HomeService homeService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView home(Locale locale, Model model) throws IOException, JSONException {
		
		ModelAndView mv = new ModelAndView();

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );

		mv = TorrentCheck();
		mv.addObject("page", "home.jsp");
		
		return mv;
	}
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/torrent", method = RequestMethod.GET)
	public String torrent(Model model,@RequestParam HashMap<String, Object> data) {
		
		// 예능, 다큐 주소
	//	String[] URLList = {"https://torrentboza.com/bbs/board.php?bo_table=ent&page=","https://torrentboza.com/bbs/board.php?bo_table=daq&page=" };
		String url = "https://torrentboza.com/bbs/board.php?bo_table=ent&page=";
		
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
	
	@RequestMapping(value = "/torrentState", method = RequestMethod.GET)
	public String torrentState(Model model,@RequestParam HashMap<String, Object> data) throws IOException, JSONException  {

		URL url = new URL("http://memorandum.tk:9091/transmission/rpc/");

		TransmissionClient tc = new TransmissionClient(url);
		
		List<TorrentStatus> list = tc.getAllTorrents();
		
		
		for (TorrentStatus item : list) {
			System.out.println(item.getName());
		}
		
		return "main";
	}
	
	@RequestMapping(value = "/torrentSearch", method = RequestMethod.GET)
	public String torrentSearch(Model model,@RequestParam HashMap<String, Object> data) throws IOException, JSONException  {
	
		// COMM CODE PRGRAM TYPE ADD
		
		String name = (String) data.get("search");

		/*
		 * 1. 사용 가능 사이트 목록 가져오기
		 * 2. 해당 사이트에 이름으로 검색 ( 0 페이지부터 )
		 * 3. 찾는 것이 있으면 break , torrent add, log insert
		 * 4. 없으면 alert
		 * 5. 
		 * */
		List<HashMap<String, Object>> site_list = homeService.getSiteList();
		
		
		
		for (HashMap<String, Object> site : site_list) {
			webScrollingSearch(name, (String) site.get("URL"));
		}
		
		return "main";
	}
	
	public void webScrollingSearch(String name, String url) {

		if (url.contains("torrentboza")) {
			torrentboza(name, url);
		}
		else if (url.contains("torrentmap")) {
			torrentmap.down(name, url);
		}
	}
	
	
	private void torrentboza(String name, String url) {
		
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

			// 페이지별로 해당 리스트를 가져온다.
			for (int i = 1 ; i < LastPage ; i ++) {
				
				Connection tmpListConn = Jsoup.connect(url+i).header("User-Agent", "Mozilla/5.0");
				Document tmpListDoc = tmpListConn.get();
				ArrayList<Element> tmpItemList = tmpListDoc.select("div.wr-subject a[href]");
				
				// 각 페이지의 글들을 돌린다.
				for (Element tmpitem : tmpItemList) {
					
					// 글들의 제목중, 찾고 있는 제목만 추린다.
					Pattern infoPattern = Pattern.compile(".*"+name+".*720.*");
					Matcher infoMatcher = infoPattern.matcher(tmpitem.text());
					
					// 찾고있는 녀석이면
					if (infoMatcher.find()) {
						
						String[] tmpName = tmpitem.text().split("\\.");
						
						String ep = tmpName[1];
						String date = tmpName[2];
						
						// 이름을 이름.EXXX.YYMMDD 으로 변경, DB저장
						String FileName = tmpitem.text();
		
						Connection tmpConn = Jsoup.connect(tmpitem.attr("href")).header("User-Agent", "Mozilla/5.0");
						Document tmpDoc = tmpConn.get();
						
						ArrayList<Element> torrentUrl = tmpDoc.select("div.list-group a.list-group-item");
						
						
						
						// 0번 토렌트 파일, 1번 마그넷 주소
						Element tmpTorrent = (Element) torrentUrl.get(0);
						
						// 마그넷 주소가 있으면 마그넷으로 바로 토렌트로 추가
						
						// 마그넷 주소가 없으면 해당 파일을 Watch폴더에 다운받는다.
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

		
	}
	
	@SuppressWarnings("static-access")
	public static ModelAndView TorrentCheck() {

		ModelAndView mv = new ModelAndView("main");
		
		try {
			int down = 0;
			int all = 0;
			
			
			URL url = new URL("http://175.193.19.231:9091/transmission/rpc/");
	
			TransmissionClient tc = new TransmissionClient(url);
			
			List<TorrentStatus> list = tc.getAllTorrents();
			
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for(TorrentStatus item : list) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("NAME", item.getName());
				// item.STATUS_CHECK_WAIT == 1
				// item.STATUS_CHECKING == 2
				// item.STATUS_DOWNLOADING == 4	
				// item.STATUS_FINISHED == 16
				// item.STATUS_SEEDING == 16
				// SUCC === 6
				String state = "";
				
				
				
				if (item.getStatus() == item.STATUS_CHECK_WAIT) {
					state = "STATUS_CHECK_WAIT";
				}
				if (item.getStatus() == item.STATUS_CHECK_WAIT) {
					state = "STATUS_CHECK_WAIT";
				}
				if (item.getStatus() == item.STATUS_DOWNLOADING) {
					state = "STATUS_DOWNLOADING";
					down++;
				}
				if (item.getStatus() == item.STATUS_FINISHED) {
					state = "STATUS_FINISHED";
				}
				if (item.getStatus() == item.STATUS_SEEDING) {
					state = "STATUS_SEEDING";
				}
				if (item.getStatus() == 6) {
					state = "SUCC";
				}
				
				map.put("STATE", state);
				data.add(map);
				
				all++;
			}
			
			mv.addObject("data", data);
			mv.addObject("down", down);
			mv.addObject("all", all);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
}
