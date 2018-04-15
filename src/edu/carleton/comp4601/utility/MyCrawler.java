package edu.carleton.comp4601.utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;
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
	
	static Set<String> urls = new HashSet<String>();
	
	boolean repeat = false;
	static int count = 0;
	
	long startCrawlTime = 0;
	long endCrawlTime   = 0;
	
	static int htmlcount = 0;
	static int jobCount = 0;
	
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
    	 //if(detail) {System.out.println("should: " + url);}
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
        	 else if (href.startsWith("https://www.monster.ca/jobs/search/?q=" + searchWord + "&where=canada&page=")) {
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

             /* Jobboom */
        	 if((href.startsWith("https://www.jobboom.com/en/job/" + searchWord + "_canada/_k-"))){
        		 if(href.indexOf("?")!=-1) {
        			if(detail==true){System.out.println("Job boom HIT: " + (href.indexOf("_k-")+3) + " " + href.indexOf("?") );}
        			pagenumber = Integer.valueOf(href.substring((href.indexOf("_k-")+3),href.indexOf("?")));
        		 } else {
        			 if(detail==true){System.out.println("Job boom HIT: " + (href.indexOf("_k-")+3) + " " + href.indexOf("?") );}
        			 pagenumber = Integer.valueOf(href.substring((href.indexOf("_k-")+3)));
        		 }
        		 if(detail==true){System.out.println("Job boom HIT: " + pagenumber);}
        		 
             	if(set.contains(pagenumber)){
             		repeat = true;
             	} else{
             		repeat = false;
             		set.add(pagenumber);
             	}
             
        	 }
         	         	 
         return !FILTERS.matcher(href).matches()
        		&& !repeat
                && (href.startsWith("https://www.workopolis.com/jobsearch/find-jobs?ak=" + searchWord + "&l=canada&lg=en&pn=")
                		|| href.startsWith("https://www.monster.ca/jobs/search/?q=" + searchWord + "&where=canada&page=")
         				|| href.startsWith("https://www.jobboom.com/en/job/" + searchWord + "_canada/_k-"));
     }
     
     
     
     @Override
     public boolean shouldFollowLinksIn(WebURL url){
    	 startCrawlTime = System.currentTimeMillis();
    	 return true;
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
    	 endCrawlTime = (System.currentTimeMillis() - startCrawlTime) * 10;
    	 this.getMyController().getConfig().setPolitenessDelay(new Long(endCrawlTime).intValue());
    	 
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
              	 int workopolisJobsPrePage = 0;
              	 while (p.find() && workopolisJobsPrePage < 26) {
              		workopolisJobsPrePage++;
              		
               		//trim and polish regex link
    				String jobLink = p.group(1);
    				int startIndex = jobLink.indexOf("f=\"")+3;
    				int endIndex = jobLink.indexOf(" class")-1;
    				jobLink = jobLink.substring(startIndex, endIndex);
    				jobLink = "https://www.workopolis.com" + jobLink;
    				
    				if(urls.contains(jobLink)){if(detail==true){System.out.println("repeat monster job");}}
    				else{
    					urls.add(jobLink);
    					
    			 //Step THREE: Prase for skills (pre job page)
        			    try { htmlParseForSkills(jobLink); }
        				catch (Exception e) {
        					System.out.println("\n ============================== \n Unable to reach \n url job link: " + jobLink + " url: " + url +"\n ========================== \n");
        				}
    				}
              	}
              	if(detail==true){System.out.println("jobsperpage: " + workopolisJobsPrePage);}
             
            	 
             }
                         
             /*Monster*/
             else if(url.startsWith("https://www.monster.ca")){
            	 //Step ONE: Regex Search for job postings
            	 Matcher m = Pattern.compile("(ListItem.\\,.position.\\:.*?\\,.url.\\:.*?\\})").matcher(expression);
            	 
            	 //Step TWO: Connect to each job link (pre search page)
              	 int monsterJobsPrePage = 0;
              	 while (m.find() && monsterJobsPrePage<26) {              		 
              		monsterJobsPrePage++;

               		//trim and polish regex link
    				String jobLink = m.group(1);
    				int startIndex = jobLink.indexOf("url\":\"")+6;
    				int endIndex = jobLink.indexOf("}")-1;
    				jobLink = jobLink.substring(startIndex, endIndex);
    				if(detail==true){System.out.println("monster joblink: " + jobLink);}
    				
    				//ensure the link has not been already added.... trust me it happens
    				if(urls.contains(jobLink)){if(detail==true){System.out.println("repeat monster job");}}
    				else{
    					urls.add(jobLink);
    					
    			 //Step THREE: Prase for skills (pre job page)
        			    try { htmlParseForSkills(jobLink); }
        				catch (Exception e) {
        					System.out.println("\n ============================== \n Unable to reach \n url job link: " + jobLink + " url: " + url +"\n ========================== \n");
        				}
    				}
    				jobCount++;				
              	}
              	//Debugging prints
              	if(detail==true){System.out.println("Total Monster at page " + url + ": " + monsterJobsPrePage);}
              	monsterJobsPrePage = 0; 
              	if(detail==true){System.out.println("urls set size: " + urls.size() + " totalCount: " + jobCount);}
             }
             
             /* Jobboom */
             if(url.startsWith("https://www.jobboom.com")){
            	 
            	 //Step ONE: Regex Search for job postings
                 Matcher p = Pattern.compile("href=\\\"\\/en\\/job-description\\/(.*?)\\\"").matcher(expression);
              	 
              	 
                 //Step TWO: Connect to each job link (pre search page)
              	 int jobboomJobsPrePage = 0;
              	 while (p.find() && jobboomJobsPrePage<26) {
              		jobboomJobsPrePage++;
              		
               		//trim and polish regex link
    				String jobLink = p.group(1);
    				jobLink = "https://www.jobboom.com/en/job-description/" + jobLink;
    				
    				if(urls.contains(jobLink)) {
    					if(detail==true) {System.out.println("repeat jobboom job");}
    				} else {
    					urls.add(jobLink);
    	    		     //Step THREE: Prase for skills (pre job page)
        				try { htmlParseForSkillsJobBoom(jobLink); } 
        				catch (Exception e) {
        					System.out.println("\n ============================== \n Unable to reach \n url job link: " + jobLink + " url: " + url +"\n ========================== \n");
        				}				

    				}    				
              	}
              	if(detail==true){System.out.println("jobsperpage: " + jobboomJobsPrePage);}
             }
            	 
         	if(detail==true){System.out.println("urls set size: " + urls.size());}
         
         }
     }
   
     
     
     public void htmlParseForSkillsJobBoom(String linkOfJobPost) throws IOException{
    	 
    	 //Step One: Connect
		Document jobPage = Jsoup.connect(linkOfJobPost).get();
		
		//Step Two: Pre-Parse
		Pages pages = Pages.getInstance();
		pages.addPage(linkOfJobPost, "");
		
		//Step Three: Parsing
		 //--setup
		count++;
		HashMap<String, Boolean> skills = pages.getSkills(linkOfJobPost);
		
		String jobExpression = jobPage.html().toString().replace("\r", " ").replace("\n", " ").replace(",", " ").replace(".", " ");
		//--look for each skill in page and make a note if it exists
		for(String skill: skills.keySet()){
			if(jobExpression.toLowerCase().indexOf(" " + skill + " ") != -1){
				if(((skill.equals("html")||skill.equals("javascript")))&&(StringUtils.countMatches(jobExpression.toLowerCase(), " " + skill + " ")>=2)) {
					if(detail==true){System.out.println("count: "+StringUtils.countMatches(jobExpression.toLowerCase(), " " + skill + " "));}
					if(detail==true){System.out.println("we gotta skill boy: " + skill);}
					Pages.getInstance().addSkill(linkOfJobPost, skill);
				} else if (((skill.equals("node")||skill.equals("css")))&&(StringUtils.countMatches(jobExpression.toLowerCase(), " " + skill + " ")>=3)) {
					if(detail==true){System.out.println("count: "+StringUtils.countMatches(jobExpression.toLowerCase(), " " + skill + " "));}
					if(detail==true){System.out.println("we gotta skill boy: " + skill);}
					Pages.getInstance().addSkill(linkOfJobPost, skill);					
				} else if (!(skill.equals("html"))&&!(skill.equals("css"))&&!(skill.equals("javascript"))&&!(skill.equals("node"))) {
					if(detail==true){System.out.println("we gotta skill boy: " + skill);}
					Pages.getInstance().addSkill(linkOfJobPost, skill);
				}
			}
			if(skill.equals(searchWord)) { //Always add the searchWord for every page
				Pages.getInstance().addSkill(linkOfJobPost, skill);
			}
		}
			
     }
     
     public void htmlParseForSkills(String linkOfJobPost) throws IOException {
    	 //Step One: Connect
		Document jobPage = Jsoup.connect(linkOfJobPost).get();
		
		//Step Two: Pre-Parse
		Pages pages = Pages.getInstance();
		pages.addPage(linkOfJobPost, "");
		
		//Step Three: Parsing
		 //--setup
		count++;
		HashMap<String, Boolean> skills = pages.getSkills(linkOfJobPost);
   
		String jobExpression = jobPage.html().toString();
		for(String skill: skills.keySet()){

			//These are the only natural language occurences we care about
			if((jobExpression.toLowerCase().indexOf(" " + skill + " ")  != -1) ||
					(jobExpression.toLowerCase().indexOf(" " + skill + ", ") != -1) ||
					(jobExpression.toLowerCase().indexOf(" " + skill + ". ") != -1) ||
					(jobExpression.toLowerCase().indexOf(" " + skill + "\n") != -1)){

				Pages.getInstance().addSkill(linkOfJobPost, skill);

				//debug print
				if(detail==true){if(skill.equals("html")){htmlcount++;}}

			}

		}

		//debug print
		if(detail==true){System.out.println("html count: " + htmlcount);}
	}

}
