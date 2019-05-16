package torrent.comm.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import comm.util.WebComm;
import comm.util.torrent.TorrentComm;
import torrent.comm.service.HomeService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Resource(name="homeService")
	private HomeService homeService;
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView home() throws IOException, JSONException {
		ModelAndView mv = WebComm.TorrentCheck();

		mv.addObject("page", "home.jsp");
		return mv;
	}
	
	@RequestMapping(value = "/torrentSearch", method = {RequestMethod.GET, RequestMethod.POST} ,  produces = "application/json")
	@ResponseBody 
	public HashMap<String, Object> torrentSearch(@RequestParam HashMap<String, Object> data) throws IOException, JSONException  {
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
					
					HashMap<String, Object> data1 = new HashMap<String, Object>();
					data.put("NAME", name);
					data.put("STAT", "SEARCH");
					homeService.updateThread(data1);
					TorrentComm.search(name, (String) site.get("URL"));
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
	
}
