package edu.carleton.comp4601.utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.carleton.comp4601.model.Pages;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.regex.Matcher;

public class MyCrawler extends WebCrawler {

	static Set<Integer> set = new HashSet<Integer>();
	static Set<Integer> setM = new HashSet<Integer>();
	static Set<Integer> setI = new HashSet<Integer>();
	boolean repeat = false;
	static int count = 0;
	
	String searchWord = CrawlerController.crawlSearchWord;
	
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                                                           + "|png|mp3|mp4|zip|gz))$");

   //turns testing prints on/off
   boolean detail = true;
    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
    	 int pagenumber;
         String href = url.getURL().toLowerCase();
        
        	 
             /*Workopolis*/
        	 if((href.startsWith("https://www.workopolis.com/jobsearch/find-jobs?ak=" + searchWord + "&l=canada&lg=en&pn="))){
        		 
        		 pagenumber = Integer.valueOf(href.substring(href.indexOf("pn=")+3));
        		 
             	if(set.contains(pagenumber)){
             		repeat = true;
             	} else{
             		repeat = false;
             		set.add(pagenumber);
             	}
             
        	 }
        	 
        	 /*Monster*/
        	                         //https://www.monster.ca/jobs/search/?q=java&where=canada&page=2
        	 else if (href.startsWith("https://www.monster.ca/jobs/search/?q=java&where=canada&page=")) {
        		 if(detail==true){System.out.println("INITAL HIT");}
        		 String pgnum = href.substring(href.indexOf("page=")+5);
        		 if(detail==true){System.out.println("INITAL HIT: " + pgnum);}
        		 pagenumber = Integer.valueOf(pgnum);
        		 if(detail==true){System.out.println(">>>>>>>>>>>HIT: " + pagenumber);}
        		 if(detail==true){System.out.println("should visit: " + url.getURL());}
             	if(setM.contains(pagenumber)){
             		repeat = true;
             	} else{
             		repeat = false;
             		setM.add(pagenumber);
             	}
             		
        	 } 
        	 
        	 /*Indeed*/
        	 else if (href.startsWith("https://www.indeed.ca/jobs?q=")) {
        		 
        	 }
         
         return !FILTERS.matcher(href).matches()
        		&& !repeat
                && 
                ((href.startsWith("https://www.workopolis.com/jobsearch/find-jobs?ak=" + searchWord + "&l=canada&lg=en&pn=")) ||
                 (href.startsWith("https://www.monster.ca/jobs/search/?q=" + searchWord + "&where=canada&page=")));
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
         String url = page.getWebURL().getURL();
         if(detail==true){System.out.println("Search page URL: " + url);}

         //Step ZERO: Basic Parsing...
         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();
             String html = htmlParseData.getHtml();
             Set<WebURL> links = htmlParseData.getOutgoingUrls();
             Document document = Jsoup.parse(htmlParseData.getHtml());
             String expression = document.html().toString().replace("\n", "");
             
             
             
             /*Workopolis*/
             if(url.startsWith("https://www.workopolis.com")){
            	 
            	 //Step ONE: Regex Search for job postings
                 Matcher p = Pattern.compile("(schema.org/JobPosting.>.*?class)").matcher(expression);
              	 
              	 
                 //Step TWO: Connect to each job link (pre search page)
              	 int jobsPrePage = 0;
              	 while (p.find()) {
              		jobsPrePage++;
              		
               		//trim and polish regex link
    				String jobLink = p.group(1);
    				int startIndex = jobLink.indexOf("f=\"")+3;
    				int endIndex = jobLink.indexOf(" class")-1;
    				jobLink = jobLink.substring(startIndex, endIndex);
    				jobLink = "https://www.workopolis.com" + jobLink;
    			
    				
    				
    		     //Step THREE: Prase for skills (pre job page)
    				try { htmlParseForSkills(jobLink); } 
    				catch (Exception e) {
    					if(detail==true){System.out.println("job link: " + jobLink + " url: " + url);}
    					if(detail==true){System.out.println("Unable to reach url");}
    				}				
              	}
              	if(detail==true){System.out.println("jobsperpage: " + jobsPrePage);}
             
            	 
             }
             
             /*Indeed*/
             else if(url.startsWith("https://www.indeed.ca")){
            	//Step ONE: Regex Search for job postings
            	 
                //Step TWO: Connect to each job link (pre search page)
            	 
            	//Step THREE: Prase for skills (pre job page)
            	 
             }
             
             /*Monster*/
             else if(url.startsWith("https://www.monster.ca")){
            	 //Step ONE: Regex Search for job postings

            	 //@type":"ListItem","position":2,"url":"
            	 //(type.:.ListItem.,.position.:.,.url.:.*?})
            	 Matcher m = Pattern.compile("(ListItem.\\,.position.\\:.\\,.url.\\:.*?\\})").matcher(expression);
            	 
            	 //Step TWO: Connect to each job link (pre search page)
              	 int jobsPrePage = 0;
              	 while (m.find()) {
              		jobsPrePage++;
              		
               		//trim and polish regex link
    				String jobLink = m.group(1);
    				int startIndex = jobLink.indexOf("url\":\"")+5;
    				int endIndex = jobLink.indexOf("}")-1;
    				jobLink = jobLink.substring(startIndex, endIndex);
    				jobLink = "https://www.monster.ca" + jobLink;
    				if(detail==true){System.out.println("monster joblink: " + jobLink);}
    				
    				
    				
    		     //Step THREE: Prase for skills (pre job page)
    								
              	}
              	if(detail==true){System.out.println("Total Monster: " + jobsPrePage);}
              	 
             }
             
          
             
        }
         
        
    }
     
     
     public void htmlParseForSkills(String linkOfJobPost) throws IOException{
    	 
    	 //Step One: Connect
		Document jobPage = Jsoup.connect(linkOfJobPost).get();
		
		//Step Two: Pre-Parse
		Pages pages = Pages.getInstance();
		pages.addPage(linkOfJobPost, "");
		
		//Step Three: Parsing
		 //--setup
		count++;
		HashMap<String, Boolean> skills = pages.getSkills(linkOfJobPost);
		
		 //--look for each skill in page and make a note if it exists
		String jobExpression = jobPage.html().toString().replace("\n", " ").replace(",", " ").replace(".", " ").replaceAll(";", " ");
		for(String skill: skills.keySet()){
			if(jobExpression.toLowerCase().indexOf(" " + skill + " ") != -1){
				if(detail==true){System.out.println("we gotta skill boy: " + skill);}
				Pages.getInstance().addSkill(linkOfJobPost, skill);
			}
		}
		
		
		
		
    	 
    	 
    	 
     }
}
