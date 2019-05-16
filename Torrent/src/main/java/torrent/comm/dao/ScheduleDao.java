package torrent.comm.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value="scheduleDao")
public class ScheduleDao {
		
		@Autowired
		 protected SqlSessionTemplate session;
		
		
		public void deleteTorrent(String name) {
			session.update("TORRNET.DeleteTorrent", name);
		}


		public List<HashMap<String, Object>> getTorrentSite() {
			return session.selectList("TORRENT.getUrl");
		}


		public List<HashMap<String, Object>> getItems() {
			return session.selectList("TORRENT.getItems");
		}
}
