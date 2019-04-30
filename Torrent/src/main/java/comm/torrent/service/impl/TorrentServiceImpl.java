package comm.torrent.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import comm.torrent.dao.TorrentDao;
import comm.torrent.service.TorrentService;

@Service(value="torrentService")
public class TorrentServiceImpl implements TorrentService{

	@Resource(name="torrentDao")
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
