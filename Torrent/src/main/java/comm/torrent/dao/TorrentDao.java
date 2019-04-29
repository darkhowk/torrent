package comm.torrent.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class TorrentDao {
	
	private SqlSession session;
	
	public List<HashMap<String, Object>> getUrl() {
		return session.selectList("TORRENT.getUrl");
	}

	public void torrentLogInsert(HashMap<String, Object> data) {
		session.insert("TORRENT.logInsert", data);
	}

}
