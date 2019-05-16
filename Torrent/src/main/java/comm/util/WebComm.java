package comm.util;

import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.web.servlet.ModelAndView;

import ca.benow.transmission.TransmissionClient;
import ca.benow.transmission.model.TorrentStatus;

public class WebComm {
	@SuppressWarnings("static-access")
	public static ModelAndView TorrentCheck() {

		ModelAndView mv = new ModelAndView("main");
		
		try {
			int down = 0;
			int all = 0;
			
			URL url = new URL("http://memorandum.iptime.org:9091/transmission/rpc/");
			TransmissionClient tc = new TransmissionClient(url);
			List<TorrentStatus> list = tc.getAllTorrents();
			ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for(TorrentStatus item : list) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("NAME", item.getName());
				
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
			
			Date date = new Date();
			Locale locale = new Locale("ko");
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			
			String formattedDate = dateFormat.format(date);
			
			mv.addObject("serverTime", formattedDate );
			mv.addObject("data", data);
			mv.addObject("down", down);
			mv.addObject("all", all);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
}
