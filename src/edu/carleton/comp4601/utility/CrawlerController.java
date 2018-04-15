package edu.carleton.comp4601.utility;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import edu.carleton.comp4601.dao.Equivalencies;
import edu.carleton.comp4601.dao.Skills;
import edu.carleton.comp4601.model.Pages;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerController {
	
	DatabaseSingleton db; 
	
	
	public CrawlerController() {		
		try { db = DatabaseSingleton.getInstance();}
		catch (UnknownHostException e) {e.printStackTrace();}
	}
	
	public static String crawlSearchWord = "default";
	
	public Map<String, Double> getResults(String searchTerm) {
		System.out.println("getResults");
		Map<String, Double> results = new LinkedHashMap<String, Double>();
		Map<String, Double> ratios = new LinkedHashMap<String, Double>();
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for(String skill : Skills.getInstance().getSkills()) {
			counts.put(skill, 0);
		}
		createPages(searchTerm); //TODO this is a stub where the actual crawl structure would have been created
		for(String url : Pages.getInstance().getPages().keySet()) {
			HashMap<String, Boolean> skills = Pages.getInstance().getPages().get(url).getSkills();
			for(String skill : skills.keySet()) {
				if(skills.get(skill)) {
					//System.out.println("skill:"+skill);
					counts.put(skill, counts.get(skill)+1);
				}
			}
		}
		
		//Deal with equivalencies
		HashMap<String, String> equivalencies = Equivalencies.getInstance().getEquivalencies();
		for(String skill1 : equivalencies.keySet()) {
			//System.out.println("skill1:"+skill1+" skill2:"+equivalencies.get(skill1));
			counts.put(skill1, counts.get(skill1)+counts.get(equivalencies.get(skill1)));
			counts.remove(equivalencies.get(skill1));
		}
		
		System.out.println("Number of search term hits"+counts.get(searchTerm));

		for(String skill : counts.keySet()) {
			if(Pages.getInstance().getPages().size()==0) {
				ratios.put(skill, 0d);
			} else {
				ratios.put(skill, (double)counts.get(skill)/Pages.getInstance().getPages().size());
			}
		}
		//Find the top 10 ratios
		Map<String, Double> sortedRatios = sortByComparator(ratios);
		int i=0;
		for(String skill : sortedRatios.keySet()) {
			results.put(skill, sortedRatios.get(skill));
			i++;
			if(i>=11)
				break;
		}
		
		results.remove(searchTerm);
		
		//DATABASE CODE
		//Add SearchWord-skill to DB with Map of corresponding skills
		BasicDBObject objectToAdd = new BasicDBObject();
		objectToAdd.append("skill", crawlSearchWord);
		objectToAdd.append("associates", results.toString());
		db.addToCollection("relatedSkills", objectToAdd);
				
		return results;
	}

	private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap) {

		List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> o1,
					Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
	
	//Decr: starts the craw
	//Input: the keyword you want to search into each job website
	private void crawl(String searchword) throws Exception{
		crawlSearchWord = searchword;
		
		String crawlStorageFolder = System.getProperty("user.home")+"/.project/";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(1000);
        config.setMaxPagesToFetch(10);

       
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);      
        
        if(searchword.equals("c++")){
        	controller.addSeed("https://www.workopolis.com/jobsearch/find-jobs?&st=RELEVANCE&ak=" + "c%2B%2B" + "&l=canada&&pn=1");
        	controller.addSeed("https://www.jobboom.com/en/job/" + "c%2B%2B" + "_canada/_k-1?dk=" + "c%2B%2B" + "&location=canada&defaultDistance=true");
        	//controller.addSeed("https://www.jobboom.com/en/job/c_canada/_k-1?dk=" + searchword + "&location=canada");
        	controller.addSeed("https://www.monster.ca/jobs/search/?q=" + "c__2B__2B" + "&where=canada");
        }else{
        	controller.addSeed("https://www.workopolis.com/jobsearch/find-jobs?&st=RELEVANCE&ak=" + searchword + "&l=canada&&pn=1");
        //	controller.addSeed("https://www.jobboom.com/en/job/" + searchword + "_canada/_k-1?dk=" + searchword + "&location=canada&defaultDistance=true");
        	controller.addSeed("https://www.jobboom.com/en/job/c_canada/_k-1?dk=" + searchword + "&location=canada");
        	controller.addSeed("https://www.monster.ca/jobs/search/?q=" + searchword + "&where=canada");
        }
        
        controller.start(MyCrawler.class, numberOfCrawlers);
	}
	
	
	private void createPages(String searchTerm) {
		//System.out.println("createPage");
		//Step One: crawl to get the links of all the jobs pages
		try {
			crawl(searchTerm);
		} catch (Exception e) {e.printStackTrace();}
		
		
		//Step Two: Parse each job site with JSoup
		
	}
	
	
	public static void main(String[] args) {
		//System.out.println("!!!!!!!!!!LETS GO");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date startdate = new Date();
		System.out.println(dateFormat.format(startdate));	
		CrawlerController cc = new CrawlerController();
		//cc.getResults("java");
		//List of skills
		//
		
		
		List<String> automate = new ArrayList<String>();
		automate.add("natural language processing");

		//c++ everything that follows must be recoded and then redone
		//sql server
		//objective c 
		//chart js
		//tcp/ip
		//web/db
		//crystal reports
		//angular js
		//google protocol buffers
		//visual studio
		//android studio
		//apache spark
		//unreal engine
		//natural language processing
		//netconf/yang
		//asp net
		//vb net
		//pl-sql
		//fraud managment
		//handlebar - why is this giving a bad result?
		//dom ?? - html - 104.545454545%
		//bootstrap - html - 107.3%
		//macos - windows 140%
		//scipy -python 109%
		//yocto - linux 128.6%
		//j2ee - java 105%
		//maven - java 116%
		//jquery - css 104%
		//grunt - javascript 182%
		//qml - c++ 154.5%
		//jpa - java 135.3%
		//ceph - html 240%
		//scrum - agile 102.9%
		//servlets - java 222.2%
		//scikit - python 600%
		
		for(String searchSk : automate) {
			Pages.getInstance().reset();
			MyCrawler.urls = new HashSet<String>();
			Map<String, Double> results = cc.getResults(searchSk); //TODO this is a stub until the actual crawling gets done
			System.out.println("Searching: "+searchSk);
			System.out.println("The top ten skills are:");
			for(String skill : results.keySet()) {
				System.out.println("Skill: "+skill+" Percent:"+results.get(skill)*100);
			}		
			Date date = new Date();
			System.out.println(dateFormat.format(date));				
		}
		
	}
}
