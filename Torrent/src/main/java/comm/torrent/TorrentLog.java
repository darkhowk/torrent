package comm.torrent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TorrentLog {

	public final String ERROR = "0";
	public final String START = "1";
	public final String END = "2";
	public final String DELETE = "3";
	
	
	public TorrentLogService logService;
	
	long time = System.currentTimeMillis(); 
	SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	private String now = dayTime.format(new Date(time));
	
	
	public String Log(Class<TorrentDel> class1, String stat) {
		return Log(class1, stat, "");
	}
	
	
	public String Log(Class<TorrentDel> class1, String stat, String name) {
		
		String resultLog = "";
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("type", class1.getName());
		param.put("time", now);
		param.put("stat", stat);
		param.put("name", name);
		
		int result = logService.logInsert(param);
		
		
		if (result == 1) {
			resultLog = "SUCC :: " + class1.getName() ;
		}
		else {
			resultLog = "ERROR :: " + class1.getName();
		}
			
		return resultLog;
	}
	
}
