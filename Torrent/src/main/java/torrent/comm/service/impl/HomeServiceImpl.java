package torrent.comm.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import comm.util.torrent_comm;
import comm.util.torrentboza;
import comm.util.torrentmap;
import torrent.comm.dao.HomeDao;
import torrent.comm.service.HomeService;

@Service(value="homeService")
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

	@Override
	public Boolean torrentbozadown(String title1, String url1) {
		Boolean result = false;
		
		ArrayList<HashMap<String, Object>> contentList = torrentboza.down(title1, url1);
			
			for (HashMap<String, Object> content : contentList) {
				
				Boolean db = true;
				
				String url = (String) content.get("url");
				String title = (String) content.get("title");
				String ep = (String) content.get("ep");
				String wr_id = "";
				
				Connection conn = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
				
				Document doc;
				
				try {
					doc = conn.get();

					String magnet = "";

					String tmpMagnet = doc.select(".list-group-item").text();
					
					magnet = tmpMagnet.substring(tmpMagnet.indexOf("magnet:?"), tmpMagnet.length());

					String file = doc.select(".view_file_download").attr("href");
					
					wr_id = file.substring(file.indexOf("wr_id=")+6, file.indexOf("&page="));
					String page = file.substring(file.indexOf("&page=")+6);
					
					HashMap<String, Object> ep_data = new HashMap<String, Object>();
					ep_data.put("TITLE", title);
					ep_data.put("EP", ep);
					
					int check_ep = Integer.parseInt((String) homeDao.check_ep(ep_data));
					
					if (check_ep <= 0) {
						System.out.println("다운 시작 ::" +ep);
						if (magnet.equals("")) {
							String file_url = "https://torrentboza.com/bbs/download.php?bo_table=ent&wr_id="+wr_id+"&no=1&page="+page;
	
							byte[] bytes = Jsoup.connect(file_url).header("User-Agent", "Mozilla/5.0").ignoreContentType(true).execute().bodyAsBytes();
							ByteBuffer buffer = ByteBuffer.wrap(bytes);
							
							File tmpFile = new File("C:/DATA/Watch/"+title+ep+".torrent");
							
							db = tmpFile.createNewFile();
							
							FileOutputStream fis = new FileOutputStream(tmpFile);
							FileChannel cin = fis.getChannel();
							cin.write(buffer);
							cin.close();
							fis.close();
						}
						else {
							db = torrent_comm.magnet_add(magnet);
						}
						
						
						db = true;
						if (db) {
							
							HashMap<String, Object> data = new HashMap<String, Object>();
							
							data.put("NAME", title);
							data.put("EP", ep);
							data.put("SITE", "torrentboza");
							data.put("ID", wr_id);
							data.put("STATE", "DOWN");
							homeDao.torrentLogInsert(data);
						}
					}
					else {
						System.out.println("이미 다운 시작 :: " +ep);
					}
					
				}
				catch (Exception e1) {
					e1.printStackTrace();
					db = false;
					result = false;
				}
				result = true;
				
			}
			return result;
		}
	
	@Override
	public Boolean torrentmapdown(String title1, String url1) {
		Boolean result = false;
		
		ArrayList<HashMap<String, Object>> contentList  = torrentmap.down(title1, url1);
		
			for (HashMap<String, Object> content : contentList) {
				
				Boolean db = true;
				
				String url = (String) content.get("url");
				String title = (String) content.get("title");
				String ep = (String) content.get("ep");
				
				String wr_id = url.substring(url.indexOf("wr_id=")+6, url.indexOf("&page="));
				
				Connection conn = Jsoup.connect((String) content.get("url")).header("User-Agent", "Mozilla/5.0");
				
				Document doc;
				
				try {
					doc = conn.get();
					
					String magnet = "";
					for (Element el : doc.select("a")) {
						if (el.text().equals("마그넷주소")) {
							magnet = el.attr("href");
						}
					}
					
					magnet = "";
					if (magnet.equals("")) {
						String file_url = "https://www.torrentmap.com/skin/board/torrent/get_filetender2.php?bo_table=kr_ent&wr_id="+wr_id+"&bf_no=0";
						
						byte[] bytes = Jsoup.connect(file_url).ignoreContentType(true).execute().bodyAsBytes();
						ByteBuffer buffer = ByteBuffer.wrap(bytes);
						
						File tmpFile = new File("C:/DATA/Watch/"+title+ep+".torrent");
						
						db = tmpFile.createNewFile();
						
						FileOutputStream fis = new FileOutputStream(tmpFile);
						FileChannel cin = fis.getChannel();
						cin.write(buffer);
						cin.close();
						fis.close();
						
					}
					else {
						db = torrent_comm.magnet_add(url);
					}
					
				}
				catch (Exception e1) {
					db = false;
				}
				
				if (db) {
					
					HashMap<String, Object> data = new HashMap<String, Object>();
					
					data.put("NAME", title);
					data.put("EP", ep);
					data.put("SITE", "torrentmap");
					data.put("ID", wr_id);
					data.put("STATE", "DOWN");
					
					homeDao.torrentLogInsert(data);
					result = true;
				}
			}
			
			return result;
			
		}
	
}

