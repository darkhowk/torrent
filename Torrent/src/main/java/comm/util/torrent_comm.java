package comm.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import ca.benow.transmission.TransmissionClient;

public class torrent_comm {

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
	
	public static  Boolean magnet_add(String url) {
		try {
			URL transmission_url = new URL("http://175.193.19.231:9091/transmission/rpc/");
			TransmissionClient tc = new TransmissionClient(transmission_url);
			tc.addTorrent(url);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	private static  ArrayList<Element> list_find_name() {
		
		ArrayList<Element> itemList = new ArrayList<Element>();
		
		if (view.equals("")) {
			setView("720");
		}
		
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
	
}

