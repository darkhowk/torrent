package comm.torrent;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.annotation.Resource;

import ca.benow.transmission.TransmissionClient;
import ca.benow.transmission.model.TorrentStatus;

public class TorrentDel {
	
	@Resource(name="torrentLog")
	private static TorrentLog log;
	
	public static void main(String[] args) {

		log.Log(TorrentDel.class, log.START);
		
		URL url;
		try {
			url = new URL("http://175.193.19.231:9091/transmission/rpc/");

			TransmissionClient tc = new TransmissionClient(url);

			List<TorrentStatus> list = tc.getAllTorrents();

			Object[] obj = new Object[list.size()];
			int idx = 0;
			for (TorrentStatus item : list) {
				if (item.getStatus() == 6) {
					obj[idx] = item.getId();
					idx++;
					
					// 해당 파일의 type등 파일 옮기기 위해 필요한 자료 가져오기
					
					// 해당 파일을 실제로 옮기기. 
					File dataFolder = new File("/DATA");
					for (String name : dataFolder.list()) {
						if (name.equals(item.getId())) {
							
						}
					}
							
				}
			}
			if (idx != 0) {
			
				tc.removeTorrents(obj, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	log.Log(TorrentDel.class, log.END);
	}

}
