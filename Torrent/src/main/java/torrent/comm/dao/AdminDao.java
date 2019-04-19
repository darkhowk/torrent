package torrent.comm.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value="adminDao")
public class AdminDao {
	
	@Autowired
	private SqlSession session;
	
	public List<HashMap<String, Object>> getCommCode() {
		return session.selectList("ADMIN.getCommCode");
	}
}
