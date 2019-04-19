package comm.torrent.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;

public class TorrentLogDao {

	private SqlSession session;

	public int logInsert(HashMap<String, Object> param) {
		return session.insert("LOG.insert", param);
	}

}
