package torrent.comm.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value="adminDao")
public class AdminDao {
	
	@Autowired
	 protected SqlSessionTemplate session;

	public List<HashMap<String, Object>> getCommCode() {
		return session.selectList("ADMIN.getCommCode");
	}
}
