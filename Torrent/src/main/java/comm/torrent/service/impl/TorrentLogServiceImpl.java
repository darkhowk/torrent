package comm.torrent.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import comm.torrent.dao.TorrentLogDao;
import comm.torrent.service.TorrentLogService;

public class TorrentLogServiceImpl implements TorrentLogService{

	@Resource(name="torrentLogDao")
	private TorrentLogDao dao;
	
	@Override
	public int logInsert(HashMap<String, Object> param) {
		return dao.logInsert(param);
		
	}

}
