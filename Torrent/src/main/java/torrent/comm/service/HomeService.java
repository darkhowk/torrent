package torrent.comm.service;

import java.util.HashMap;
import java.util.List;

public interface HomeService {

	List<HashMap<String, Object>> getMenuList();

	HashMap<String, Object> getSearchType(HashMap<String, Object> praram);

}
