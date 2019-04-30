package comm.torrent.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import comm.torrent.dao.TorrentLogDao;
import comm.torrent.service.TorrentLogService;

@Service(value="torrentLogService")
public class TorrentLogServiceImpl implements TorrentLogService{

	@Resource(name="torrentLogDao")
	private TorrentLogDao dao;
	
	@Override
	public int logInsert(HashMap<String, Object> param) {
		return dao.logInsert(param);
		
	}

}
