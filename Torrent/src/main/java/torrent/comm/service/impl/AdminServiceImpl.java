package torrent.comm.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import torrent.comm.dao.AdminDao;
import torrent.comm.service.AdminService;

@Service( value="adminService")
public class AdminServiceImpl implements AdminService{

	@Resource(name="adminDao")
	private AdminDao adminDao;

	@Override
	public List<HashMap<String, Object>> getCommCode() {
		return adminDao.getCommCode();
	}

}
