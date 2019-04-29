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
import comm.util.torrentboza;
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
	
	@RequestMapping(value = "/torrentState", method = RequestMethod.GET)
	public String torrentState(Model model,@RequestParam HashMap<String, Object> data) throws IOException, JSONException  {

		URL url = new URL("http://175.193.19.231:9091/transmission/rpc/");

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

		for (HashMap<String, Object> site : homeService.getSiteList()) {
			webScrollingSearch(name, (String) site.get("URL"));
		}
		
		return "main";
	}
	
	public void webScrollingSearch(String name, String url) {

		if (url.contains("torrentboza")) {
			torrentboza.down(name, url);
		}
		else if (url.contains("torrentmap")) {
			torrentmap.down(name, url);
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
