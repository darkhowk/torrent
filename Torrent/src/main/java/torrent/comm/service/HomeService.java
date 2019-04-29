package torrent.comm.service;

import java.util.HashMap;
import java.util.List;

public interface HomeService {

	List<HashMap<String, Object>> getMenuList();

/*	HashMap<String, Object> getSearchType(HashMap<String, Object> praram);*/

	List<HashMap<String, Object>> getSiteList();

	HashMap<String, Object> checkThread();

	void insertThread(HashMap<String, Object> data);

	void updateThread(HashMap<String, Object> data);

}
