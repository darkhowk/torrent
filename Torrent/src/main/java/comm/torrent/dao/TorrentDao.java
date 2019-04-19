package comm.torrent.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class TorrentDao {
	
	private SqlSession session;
	
	public List<HashMap<String, Object>> getUrl() {
		return session.selectList("TORRENT.getUrl");
	}

}
