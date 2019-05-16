package comm.util.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import ca.benow.transmission.TransmissionClient;

public class TorrentComm {
	
	public static void search(String name, String url) {
		
		if (url.contains("torrentboza")) {
			torrent_down(Torrentboza.Search(name, url));
		}
		else {
			
		}
		
	}
	
	public static  Boolean magnet_add(HashMap<String, Object> data) {
		try {
			URL transmission_url = new URL("http://m:9091/transmission/rpc/");
			TransmissionClient tc = new TransmissionClient(transmission_url);
			tc.addTorrent((String)data.get("magnet"));
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static Boolean file_down(HashMap<String, Object> data) {
		
		Boolean result = false;

		try {
			ByteBuffer buffer = ByteBuffer.wrap(Jsoup.connect((String)data.get("file_url")).header("User-Agent", "Mozilla/5.0").ignoreContentType(true).execute().bodyAsBytes());
			File tmpFile = new File("/DATA/Watch/"+data.get("title")+"."+data.get("ep")+".torrent");
			
			result = tmpFile.createNewFile();
			
			FileOutputStream fis = new FileOutputStream(tmpFile);
			FileChannel cin = fis.getChannel();
			cin.write(buffer);
			cin.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static void torrent_down(ArrayList<HashMap<String, Object>> search) {

		// 해당 리스트에서 magnet 이 있으면 magnet으로. 없으면 file 로.
		

		
		
		
	}
}

