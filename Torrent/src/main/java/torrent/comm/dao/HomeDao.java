package torrent.comm.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import comm.util.CommSession;

@Repository(value="homeDao")
public class HomeDao extends CommSession {

	
	@Autowired
	private SqlSession session;
	
    public List<HashMap<String, Object>> getMenuList() {
		return selectList("HOME.getMenuList");
	}

	public HashMap<String, Object> getSearchType(HashMap<String, Object> param) {
		return selectOne("HOME.getSearchType", param);
	}

	public List<HashMap<String, Object>> getSIteList() {
		return selectList("HOME.getSiteList");
	}
}
