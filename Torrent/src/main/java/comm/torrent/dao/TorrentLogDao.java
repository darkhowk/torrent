package comm.torrent.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value="torrentLogDao")
public class TorrentLogDao {

	@Autowired
	private SqlSession session;

	public int logInsert(HashMap<String, Object> param) {
		return session.insert("LOG.insert", param);
	}

}
