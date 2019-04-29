package torrent.comm.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import torrent.comm.service.AdminService;

@Controller
@RequestMapping(value ="/admin", method = {RequestMethod.GET, RequestMethod.POST})
public class AdminController {

	
	@Resource(name="adminService")
	private AdminService adminService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws JSONException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/comm_code", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView comm_code(Locale locale, Model model) throws IOException, JSONException {
		ModelAndView mv = new ModelAndView();
		mv = HomeController.TorrentCheck();
		mv.addObject("page", "admin/comm_code.jsp");
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		
		List<HashMap<String, Object>> data = adminService.getCommCode();
		System.out.println(data.size());
		mv.addObject("data", adminService.getCommCode());
		
		return mv;
	}
}
