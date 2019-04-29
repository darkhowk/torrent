package comm.torrent.service.impl;

import java.util.HashMap;
import java.util.List;

import comm.torrent.dao.TorrentDao;
import comm.torrent.service.TorrentService;

public class TorrentServiceImpl implements TorrentService{

	private TorrentDao dao;
	
	
	@Override
	public List<HashMap<String, Object>> getUrl() {
		return dao.getUrl();
	}


	@Override
	public void torrentLogInsert(HashMap<String, Object> data) {
		dao.torrentLogInsert(data);
	}

}
