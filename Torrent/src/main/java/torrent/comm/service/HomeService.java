package torrent.comm.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

@Service(value="homeService")
public interface HomeService {

	List<HashMap<String, Object>> getMenuList();

/*	HashMap<String, Object> getSearchType(HashMap<String, Object> praram);*/

	List<HashMap<String, Object>> getSiteList();

	HashMap<String, Object> checkThread();

	void insertThread(HashMap<String, Object> data);

	void updateThread(HashMap<String, Object> data);

	Boolean torrentbozadown(String name, String url);

	Boolean torrentmapdown(String name, String url);

}
