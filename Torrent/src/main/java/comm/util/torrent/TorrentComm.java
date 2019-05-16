package comm.util.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;

import ca.benow.transmission.TransmissionClient;

public class TorrentComm {
	
	public static String name;
	public static String url;
	public static String ep;
	public static String season;
	
	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		TorrentComm.name = name;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		TorrentComm.url = url;
	}

	public static String getEp() {
		return ep;
	}

	public static void setEp(String ep) {
		TorrentComm.ep = ep;
	}

	public static String getSeason() {
		return season;
	}

	public static void setSeason(String season) {
		TorrentComm.season = season;
	}

	public static ArrayList<HashMap<String, Object>> search(String name, String url, String ep) {
		setName(name);
		setUrl(url);
		setEp(ep);
		setSeason("");
		return search();
	}
	
	public static ArrayList<HashMap<String, Object>> search(String name, String url) {
		setName(name);
		setUrl(url);
		setEp(ep);
		setSeason("");
		return search();
	}
	public static ArrayList<HashMap<String, Object>> search() {
		ArrayList<HashMap<String, Object>> result = null;
		if (url.contains("torrentboza")) {
			result = Torrentboza.Search(getName(), getUrl(), getEp(), getSeason());
		}
		else {
			
		}
		
		return result;
		
	}
	

	public static Boolean torrent_down(HashMap<String, Object> item) {

		Boolean result = false;
		
		// 마그넷이 비어있으면 파일 다운.
		if (item.get("magnet").equals("")) {
			
			// 파일도 비어있으면 ??
			if (item.get("file").equals("")) {
				System.out.println("??");
			}
			else {
				file_down(item);
			}
		}
		else {
			magnet_add(item);
		}
		
		return result;
	}
	
	public static  Boolean magnet_add(HashMap<String, Object> data) {
		try {
			URL transmission_url = new URL("http://memorandum.iptime.org:9091/transmission/rpc/");
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

}

