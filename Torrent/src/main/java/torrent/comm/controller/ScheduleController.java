package torrent.comm.controller;

import java.io.IOException;
import java.util.Locale;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import comm.util.WebComm;
import torrent.comm.service.ScheduleService;

@Controller
@RequestMapping(value="/Schedule")
public class ScheduleController {

	@Resource(name="scheduleService")
	private ScheduleService scheduleService;
	
	
	//@Scheduled(cron="00 00 * * * *")
	@RequestMapping(value = "/Regular", method = RequestMethod.GET)
	public ModelAndView Regular(Locale locale, Model model) throws IOException, JSONException {
		
		ModelAndView mv = WebComm.TorrentCheck();

		scheduleService.regular();

		mv.addObject("page", "/torrent/regular.jsp");
		
		return mv;
	}
	
}
