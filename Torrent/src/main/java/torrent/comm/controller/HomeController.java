package torrent.comm.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ca.benow.transmission.TransmissionClient;
import ca.benow.transmission.model.TorrentStatus;
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
	
	@RequestMapping(value = "/torrentSearch", method = {RequestMethod.GET, RequestMethod.POST} ,  produces = "application/json")
	@ResponseBody 
	public HashMap<String, Object> torrentSearch(Model model,@RequestParam HashMap<String, Object> data) throws IOException, JSONException  {
		HashMap<String, Object>  result = new HashMap<String, Object> ();
		// COMM CODE PRGRAM TYPE ADD
		String name = (String) data.get("search");

		// DB 스레드 확인후 시작
		int count = ((BigDecimal) ((Object) homeService.checkThread().get("COUNT"))).intValue();
		
		if (count <= 0) {
			Thread thread = new SearchThread(name);
			
			thread.start();
			result.put("result", "start");
		}
		else {
			result.put("result", "wait");
			
		}
		return result;
	}
	
	@RequestMapping(value = "/torrentAuto", method = {RequestMethod.GET, RequestMethod.POST} ,  produces = "application/json")
	@ResponseBody 
	public HashMap<String, Object> torrentAuto(Model model,@RequestParam HashMap<String, Object> data) throws IOException, JSONException  {
		HashMap<String, Object>  result = new HashMap<String, Object> ();
		// COMM CODE PRGRAM TYPE ADD
		String name = (String) data.get("search");
		
		// DB 스레드 확인후 시작
	//	int count = ((BigDecimal) ((Object) homeService.checkAutoThread().get("COUNT"))).intValue();
		
		// DB에서 검색 목록 가져옴. EP 도 가져옴.
		
		List<HashMap<String, Object>> searchList = homeService.getMenuList();
	//	if (count <= 0) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					Thread auto = new AutoThread(searchList);
					auto.start();
				}
			};
			
			//timer.schedule(task, 1000, 1000);
			timer.schedule(task, 1000, 360000);
		
	//	}
	//	else {
	//		result.put("result", "wait");
			
	//	}
		return result;
	}

	class AutoThread extends Thread{
		
		List<HashMap<String, Object>> searchList;
		
		AutoThread(List<HashMap<String, Object>> searchList){
			this.searchList = searchList;
		}
		
		public void run() {
			
			for (HashMap<String, Object> search : searchList) {

				// DB에 시작 기록
				HashMap<String, Object> data = new HashMap<String, Object>();
				
				data.put("NAME", search.get("NAME"));
				data.put("STAT", "START");
				homeService.insertThread(data);
				for (HashMap<String, Object> site : homeService.getSiteList()) {
					webScrollingSearch((String) search.get("NAME"), (String) site.get("URL"));
				}
	
				// DB 업데이트
				
				HashMap<String, Object> data2 = new HashMap<String, Object>();
				
				data2.put("NAME", search.get("NAME"));
				data2.put("STAT", "END");
				homeService.updateThread(data2);
			}
		}
	
	}
	
	class SearchThread extends Thread{
		
		String name ;
		
		SearchThread(String name){
			this.name = name;
		}
		
		public void run() {
			try {
				// DB에 시작 기록
				HashMap<String, Object> data = new HashMap<String, Object>();
				
				data.put("NAME", name);
				data.put("STAT", "START");
				homeService.insertThread(data);
				for (HashMap<String, Object> site : homeService.getSiteList()) {
					webScrollingSearch(name, (String) site.get("URL"));
				}

				// DB 업데이트
				
				HashMap<String, Object> data2 = new HashMap<String, Object>();
				
				data2.put("NAME", name);
				data2.put("STAT", "END");
				homeService.updateThread(data2);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public Boolean webScrollingSearch(String name, String url) {

		Boolean result = false;
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("NAME", name);
		data.put("STAT", "SEARCH");
		homeService.updateThread(data);

		if (url.contains("torrentboza")) {
			result = homeService.torrentbozadown(name, url);
		}
		else if (url.contains("torrentmap")) {
			result = homeService.torrentmapdown(name, url);
		}
		
		return result;
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
				if (item.getStatus() == item.STATUS_CHECKING) {
					state = "STATUS_CHECKING";
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
