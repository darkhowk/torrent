package comm.torrent;

import java.util.HashMap;

import javax.annotation.Resource;

public class TorrentLogServiceImpl implements TorrentLogService{

	@Resource(name="torrentLogDao")
	private TorrentLogDao dao;
	
	@Override
	public int logInsert(HashMap<String, Object> param) {
		return dao.logInsert(param);
		
	}

}
