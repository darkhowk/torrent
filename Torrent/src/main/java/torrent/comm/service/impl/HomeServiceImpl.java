package torrent.comm.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import torrent.comm.dao.HomeDao;
import torrent.comm.service.HomeService;

@Service( value="homeService")
public class HomeServiceImpl implements HomeService{

	@Resource(name="homeDao")
	private HomeDao homeDao;

	@Override
	public List<HashMap<String, Object>> getMenuList() {
		return homeDao.getMenuList();
	}

	@Override
	public List<HashMap<String, Object>> getSiteList() {
		return homeDao.getSIteList();
	}

	@Override
	public HashMap<String, Object> checkThread() {
		return homeDao.checkThread();
	}

	@Override
	public void insertThread(HashMap<String, Object> data) {
		 homeDao.insertThread(data);		
	}

	@Override
	public void updateThread(HashMap<String, Object> data) {
		 homeDao.updateThread(data);		
	}
	
	
	
}
