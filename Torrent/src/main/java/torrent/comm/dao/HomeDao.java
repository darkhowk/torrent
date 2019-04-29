package torrent.comm.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value="homeDao")
public class HomeDao {
	
	@Autowired
	 protected SqlSessionTemplate session;

	
    public List<HashMap<String, Object>> getMenuList() {
		return session.selectList("HOME.getMenuList");
	}

	public HashMap<String, Object> getSearchType(HashMap<String, Object> param) {
		return session.selectOne("HOME.getSearchType", param);
	}

	public List<HashMap<String, Object>> getSIteList() {
		return session.selectList("HOME.getSiteList");
	}

	public HashMap<String, Object> checkThread() {
		return session.selectOne("HOME.checkThread");
	}

	public void insertThread(HashMap<String, Object> data) {
		session.insert("HOME.insertThread", data);
	}

	public void updateThread(HashMap<String, Object> data) {
		session.update("HOME.updateThread", data);
	}
}
