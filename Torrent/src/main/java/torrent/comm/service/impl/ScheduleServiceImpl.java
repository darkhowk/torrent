package torrent.comm.service.impl;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ca.benow.transmission.TransmissionClient;
import ca.benow.transmission.model.TorrentStatus;
import comm.util.torrent.TorrentComm;
import comm.util.torrent.Torrentboza;
import comm.util.torrent.Torrentmap;
import torrent.comm.dao.ScheduleDao;
import torrent.comm.service.ScheduleService;

@Service(value="scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
	
		@Resource(name="scheduleDao")
		private ScheduleDao scheduleDao;

		@Override
		//@Scheduled(cron="00 00 * * * *")
		@Scheduled(cron="00 00 * * * *") // 1시간마다
		public void regular() {
			
			// 1. DB에서 사이트 url 가져옴.
			List<HashMap<String, Object>> sites = scheduleDao.getTorrentSite();
			// 2. DB에서 찾을 녀석들 가져옴
			List<HashMap<String, Object>> items = scheduleDao.getItems();

			// 3. 사이트 뒤짐
			for (HashMap<String, Object> site : sites) {
				
				for (HashMap<String, Object> item : items) {
					
					String url = (String) site.get("URL");
					String name = (String) item.get("NAME");
					String ep = (String) item.get("EP");
					if (url.contains("torrentboza")) {
						TorrentComm.torrent_down(Torrentboza.Search(name, url, ep ));
					}
					else if (url.contains("torrentmap")) {
						Torrentmap.Search(name, url);
					}
				}
			}
			// 4. 찾은것중에 받지 않은것 다운 받고 DB 입력
			
		}
		
		
		//@Scheduled(cron="00 00 * * * *")
		@Scheduled(cron="00 */10 * * * *") // 10분마다
		@SuppressWarnings({ "unused", "null" })
		public void torrentDelete() {
			
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mi:ss");
			String now = sdf.format(today);
					
			System.out.println("torrent Delete Start" + now);
			
			try {
				URL url = new URL("http://memorandum.tk:9091/transmission/rpc/");
			
				TransmissionClient tc = new TransmissionClient(url);
				List<TorrentStatus> list = tc.getAllTorrents();
				ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
				List<Integer> tmpIds = null;
				
				for(TorrentStatus item : list) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("NAME", item.getName());
					
					if (item.getStatus() == 6) {
						tmpIds.add(item.getId());
						scheduleDao.deleteTorrent(item.getName());
					}
				}
				
				if (tmpIds.size() > 0) {
					
					Object[] ids = new Object[tmpIds.size()];
					for (int i = 0 ; i < tmpIds.size() ; i ++) {
						ids[i] = tmpIds.get(i);
					}
					
					tc.removeTorrents(ids, false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
}
