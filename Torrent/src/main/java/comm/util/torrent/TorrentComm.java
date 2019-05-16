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

	private static  ArrayList<Element> tmpList;
	private static  String name;
	private static  String view;
	private static  String rill;

	public static  ArrayList<Element> list_find_name(ArrayList<Element> tmpList, String name) {
			setTmpList(tmpList);
			setName(name);
			setView("");
			setRill("");
			return list_find_name();
	}
	
	public ArrayList<Element> list_find_name(ArrayList<Element> tmpList, String name, String view) {
			setTmpList(tmpList);
			setName(name);
			setView(view);
			setRill("");
		return list_find_name();
	}
	
	
	public ArrayList<Element> list_find_name(ArrayList<Element> tmpList, String name, String view, String rill) {
		setTmpList(tmpList);
		setName(name);
		setView(view);
		setRill(rill);
		return list_find_name();
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
	
	private static  ArrayList<Element> list_find_name() {
		
		ArrayList<Element> itemList = new ArrayList<Element>();
		
		if (view.equals("")) {
			setView("720");
		}
		
		// find item with name
		for (Element tmpitem : tmpList) {
			
			Pattern infoPattern = Pattern.compile(".*"+name+".*"+view+".*"+rill);
			Matcher infoMatcher = infoPattern.matcher(tmpitem.text());
			
			if (infoMatcher.find()) {
				itemList.add(tmpitem);
			}// if end matcher.find 
		}
		return itemList;
	}

	public ArrayList<Element> getTmpList() {
		return tmpList;
	}

	public static  void setTmpList(ArrayList<Element> tmpList1) {
		tmpList = tmpList1;
	}

	public String getName() {
		return name;
	}

	public static  void setName(String name1) {
		name = name1;
	}

	public String getView() {
		return view;
	}

	public static  void setView(String view1) {
		view = view1;
	}

	public String getRill() {
		return rill;
	}

	public static  void setRill(String rill1) {
		rill = rill1;
	}

	public static int webScrollingSearch(String name, String url) {

		int result = 0;
	
		if (url.contains("torrentboza")) {
			result = result + Torrentboza.downList(Torrentboza.Search(name, url));
		}
		else if (url.contains("torrentmap")) {
			result = result + Torrentmap.downList(Torrentmap.Search(name, url));
		}
		
		return result;
	}
}

