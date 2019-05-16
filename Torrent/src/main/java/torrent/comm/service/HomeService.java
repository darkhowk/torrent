package torrent.comm.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

public interface HomeService {

	List<HashMap<String, Object>> getMenuList();

	List<HashMap<String, Object>> getSiteList();

	HashMap<String, Object> checkThread();

	void insertThread(HashMap<String, Object> data);

	void updateThread(HashMap<String, Object> data);

}
