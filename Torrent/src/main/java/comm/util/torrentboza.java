package comm.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class torrentboza {
	
	public static ArrayList<HashMap<String, Object>> down(String title, String url) {
		
	class MultiThread extends Thread{
			
			int pageStart;
			int pageEnd;
			String title;
			String url;
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			
			MultiThread(int pageStart, int pageEnd, String url, String title){
				this.pageStart = pageStart;
				this.pageEnd = pageEnd;
				this.title = title;
				this.url = url;
			}
			
			public void run() {
				try {
					System.out.println("Thread "+ pageStart + "  Start");
					for (int i = pageStart ; i < pageEnd ; i ++) {
						Connection tmpListConn = Jsoup.connect(url+i).header("User-Agent", "Mozilla/5.0");
						Document tmpListDoc = tmpListConn.get();
						
						ArrayList<Element> tmpItemList = torrent_comm.list_find_name(tmpListDoc.select(".wr-subject"), title);
						
						for (Element tmpItem : tmpItemList) {
							
							String tmp = tmpItem.text().substring(tmpItem.text().indexOf(".E")+1);
							
							String ep = tmp.substring(0, tmp.indexOf("."));
							
							String tmpUrl = tmpItem.select("a").attr("href");
		
							HashMap<String, Object> content = new HashMap<String, Object>();
							content.put("name", tmpItem.text());
							content.put("title", title);
							content.put("url",tmpUrl);
							content.put("ep", ep);
							list.add(content);
							
						}
					} // for end
		
					System.out.println("Thread "+ pageStart + "  End");
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("torrentboza down start ");
		ArrayList<HashMap<String, Object>> contentList = new ArrayList<HashMap<String, Object>>();
		
		Connection conn = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0");
		
		Document doc;
		
		try {
			doc = conn.get();
			// get last page num
			ArrayList<Element> pageList = doc.select("ul.pagination li a[href]");
			String tmpPage = pageList.get(pageList.size()-1).toString();
			
			int LastPage = Integer.parseInt(tmpPage.substring(tmpPage.indexOf("page=")+5, tmpPage.indexOf("\"><i")));

			System.out.println(LastPage + " :: LastPage");
			
			int dev = 10;
			int tmpMax = (LastPage/dev)+1;
			MultiThread[] mt = new MultiThread[tmpMax];
			
	        for(int i = 0 ; i < tmpMax ; i ++){
	        	
	        	int pageStart = (i*dev)+1;
	        	int pageEnd = (i*dev) + dev;
	        	
	            mt[i] = new MultiThread(pageStart, pageEnd, url, title);
	            mt[i].start();
	            
	        }
	        Boolean thread = true;
	      
	        int thread_count = 0;
	        while(thread) {
	        	thread_count = 0;
	        	for(int i = 0 ; i < tmpMax ; i ++){
		            if (mt[i].getState() == Thread.State.TERMINATED) {
		            	thread_count++;
		            }
		        }
	        	if (thread_count == tmpMax) {
	        		thread = false;
	        	}
	        }
	        
	        for(int i = 0 ; i < tmpMax ; i ++){
	        	contentList.addAll(mt[i].list);
	        }
	        
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}

		return contentList;
	
	}
}
