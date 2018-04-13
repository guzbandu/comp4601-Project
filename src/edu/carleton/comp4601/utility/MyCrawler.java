package edu.carleton.comp4601.utility;

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
	boolean repeat = false;
	static int count = 0;
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                                                           + "|png|mp3|mp4|zip|gz))$");

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
    	 //System.out.println("should: " + url);
    	 int pagenumber;
         String href = url.getURL().toLowerCase();
        	 
        	 if((href.startsWith("https://www.workopolis.com/jobsearch/find-jobs?ak=java&l=canada&lg=en&pn="))){
        		 
        		 pagenumber = Integer.valueOf(href.substring(href.indexOf("pn=")+3));
        		 
        		 //System.out.println("MEGA HIT FAM: " + pagenumber);
        		 
        		 
             	if(set.contains(pagenumber)){
             		repeat = true;
             	} else{
             		repeat = false;
             		set.add(pagenumber);
             	}
        	 } else if (href.startsWith("https://www.monster.ca/jobs/search/?q=")) {
        		 
        	 } else if (href.startsWith("https://www.indeed.ca/jobs?q=")) {
        		 
        	 }
         
         return !FILTERS.matcher(href).matches()
        		&& !repeat
                && (href.startsWith("https://www.workopolis.com/jobsearch/find-jobs?ak=java&l=canada&lg=en&pn="));
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
         String url = page.getWebURL().getURL();
         //System.out.println("URL: " + url);

         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();
             String html = htmlParseData.getHtml();
             Set<WebURL> links = htmlParseData.getOutgoingUrls();
             
             //Regex code to find links for the jobs
             Document document = Jsoup.parse(htmlParseData.getHtml());
             String expression = document.html().toString().replace("\n", "");
          	 Matcher p = Pattern.compile("(schema.org/JobPosting.>.*?class)").matcher(expression);
          	//schema.org/JobPosting"><a href=
          	 
          	
          	 
          	 
          	//loop threw each regex link and connect to it using JSoup
          	while (p.find()) {
          		//System.out.println("hit");
          		//trim and polish regex link
				String jobLink = p.group(1);
				int startIndex = jobLink.indexOf("f=\"")+3;
				int endIndex = jobLink.indexOf(" class")-1;
				jobLink = jobLink.substring(startIndex, endIndex);
				jobLink = "https://www.workopolis.com" + jobLink;
				//System.out.println("REGEX BABY: " + jobLink);
				
				
				//Connect+Parse using jsoup
				
				try {
					//Connect
					//System.out.println();
					//System.out.println("job link: " + jobLink + " url: " + url);
					//System.out.println(count);
					
					Document jobPage = Jsoup.connect(jobLink).get();
					//--get location:
					
					//Parse
					//--Add Page
					Pages pages = Pages.getInstance();
					pages.addPage(jobLink, "");
					
					/*
					String pattern = "https://www.workopolis.com/jobsearch/(.*)-jobs/.*";
					Pattern r = Pattern.compile(pattern);
					Matcher m = r.matcher(url);
					String searchSkill = "";
					if(m.find()) {
						//System.out.println("match"+m.group(1));
						searchSkill = m.group(1);
					}
					if(!searchSkill.equals(""))
						Pages.getInstance().addSkill(jobLink, searchSkill);
					else {
						pattern = "https://www.workopolis.com/jobsearch/find-jobs\\?st=RELEVANCE\\&ak=(.*)\\&l=canada&pn=1";
						r = Pattern.compile(pattern);
						m = r.matcher(url);
						searchSkill = "";
						if(m.find()) {
							//System.out.println("match"+m.group(1));
							searchSkill = m.group(1);
						}
						if(!searchSkill.equals(""))
							Pages.getInstance().addSkill(jobLink, searchSkill);
						else
							System.out.println(searchSkill + " did not match!!:"+url);
					}
					*/
					
					count++;
					HashMap<String, Boolean> skills = pages.getSkills(jobLink);
					
					//--look for each skill in page and make a note if it exists
					String jobExpression = jobPage.html().toString().replace("\n", " ").replace(",", " ").replace(".", " ").replaceAll(";", " ");
					for(String skill: skills.keySet()){
						if(jobExpression.toLowerCase().indexOf(" " + skill + " ") != -1){
							//System.out.println("we gotta skill boy: " + skill);
							Pages.getInstance().addSkill(jobLink, skill);
						}
					}
				} catch (Exception e) {
					System.out.println("job link: " + jobLink + " url: " + url);
					System.out.println("Unable to reach url");
					//e.printStackTrace();
				}				
          	}
          	 
         }
         
        
    }
}
