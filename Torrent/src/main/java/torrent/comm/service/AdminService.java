package torrent.comm.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

public interface AdminService {

	List<HashMap<String,Object>> getCommCode();

}
