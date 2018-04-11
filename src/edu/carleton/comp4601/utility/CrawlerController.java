package edu.carleton.comp4601.utility;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.carleton.comp4601.dao.Skills;
import edu.carleton.comp4601.model.Pages;

public class CrawlerController {
	public CrawlerController() {
		Map<String, Double> results = getResults("java"); //TODO this is a stub until the actual crawling gets done
		System.out.println("The top ten skills are:");
		for(String skill : results.keySet()) {
			System.out.println("Skill: "+skill+" Percent:"+results.get(skill)*100);
		}
	}
	
	private Map<String, Double> getResults(String searchTerm) {
		Map<String, Double> results = new LinkedHashMap<String, Double>();
		Map<String, Double> ratios = new LinkedHashMap<String, Double>();
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for(String skill : Skills.getInstance().getSkills()) {
			counts.put(skill, 0);
		}
		createPages(); //TODO this is a stub where the actual crawl structure would have been created
		for(String url : Pages.getInstance().getPages().keySet()) {
			HashMap<String, Boolean> skills = Pages.getInstance().getPages().get(url).getSkills();
			for(String skill : skills.keySet()) {
				if(skills.get(skill)) {
					counts.put(skill, counts.get(skill)+1);
				}
			}
		}
		
		for(String skill : counts.keySet()) {
			ratios.put(skill, (double)counts.get(skill)/(double)counts.get(searchTerm));
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
	
	private void createPages() {
		Pages.getInstance().addPage("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "Toronto, Ontario");
		Pages.getInstance().addPage("/jobsearch/job/18051436?uc=E2&amp;sc=2.8286&amp;sp=2", "Mississauga, Ontario");
		Pages.getInstance().addPage("/jobsearch/job/17731346?uc=E12&amp;sc=5.4716&amp;sp=1", "Toronto, Ontario");
		Pages.getInstance().addPage("/jobsearch/job/17915328?uc=E12&amp;sc=5.3884&amp;sp=3", "Toronto, Ontario");
		Pages.getInstance().addPage("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "Toronto, Ontario");
		Pages.getInstance().addPage("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "Toronto, Ontario");
		Pages.getInstance().addPage("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "Mississauga, Ontario");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "angularjs");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "java");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "restful");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "unix");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "linux");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "sql");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "oracle");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "pl-sql");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "agile");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "git");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "jenkins");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "oracle");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "subversion");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "jira");
		Pages.getInstance().addSkill("/jobsearch/job/18058641?uc=E6&amp;sc=4.4176&amp;sp=1", "confluence");
		Pages.getInstance().addSkill("/jobsearch/job/18051436?uc=E2&amp;sc=2.8286&amp;sp=2", "java");
		Pages.getInstance().addSkill("/jobsearch/job/18051436?uc=E2&amp;sc=2.8286&amp;sp=2", "html5");
		Pages.getInstance().addSkill("/jobsearch/job/18051436?uc=E2&amp;sc=2.8286&amp;sp=2", "css3");
		Pages.getInstance().addSkill("/jobsearch/job/18051436?uc=E2&amp;sc=2.8286&amp;sp=2", "javascript");
		Pages.getInstance().addSkill("/jobsearch/job/18051436?uc=E2&amp;sc=2.8286&amp;sp=2", "angularjs");
		Pages.getInstance().addSkill("/jobsearch/job/18051436?uc=E2&amp;sc=2.8286&amp;sp=2", "sql server");
		Pages.getInstance().addSkill("/jobsearch/job/18051436?uc=E2&amp;sc=2.8286&amp;sp=2", "oracle");
		Pages.getInstance().addSkill("/jobsearch/job/17731346?uc=E12&amp;sc=5.4716&amp;sp=1", "java");
		Pages.getInstance().addSkill("/jobsearch/job/17731346?uc=E12&amp;sc=5.4716&amp;sp=1", "j2ee");
		Pages.getInstance().addSkill("/jobsearch/job/17731346?uc=E12&amp;sc=5.4716&amp;sp=1", "servlets");
		Pages.getInstance().addSkill("/jobsearch/job/17731346?uc=E12&amp;sc=5.4716&amp;sp=1", "jsp");
		Pages.getInstance().addSkill("/jobsearch/job/17731346?uc=E12&amp;sc=5.4716&amp;sp=1", "jdbc");
		Pages.getInstance().addSkill("/jobsearch/job/17731346?uc=E12&amp;sc=5.4716&amp;sp=1", "ejb");
		Pages.getInstance().addSkill("/jobsearch/job/17731346?uc=E12&amp;sc=5.4716&amp;sp=1", "jms");
		Pages.getInstance().addSkill("/jobsearch/job/17915328?uc=E12&amp;sc=5.3884&amp;sp=3", "java");
		Pages.getInstance().addSkill("/jobsearch/job/17915328?uc=E12&amp;sc=5.3884&amp;sp=3", "jquery");
		Pages.getInstance().addSkill("/jobsearch/job/17915328?uc=E12&amp;sc=5.3884&amp;sp=3", "spring");
		Pages.getInstance().addSkill("/jobsearch/job/17915328?uc=E12&amp;sc=5.3884&amp;sp=3", "html");
		Pages.getInstance().addSkill("/jobsearch/job/17915328?uc=E12&amp;sc=5.3884&amp;sp=3", "css");
		Pages.getInstance().addSkill("/jobsearch/job/17915328?uc=E12&amp;sc=5.3884&amp;sp=3", "agile");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "java");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "jee");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "jms");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "jsp");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "jsf");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "sql");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "xml");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "html");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "hibernate");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "ejb");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "ejb");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "ejb");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "agile");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "jboss");
		Pages.getInstance().addSkill("/jobsearch/job/17836867?uc=E8&amp;sc=5.3567&amp;sp=4", "junit");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "java");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "jee");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "spring");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "javascript");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "junit");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "angular js");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "sql");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "jms");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "git");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "maven");
		Pages.getInstance().addSkill("/jobsearch/job/18022161?uc=E14&amp;sc=5.3283&amp;sp=5", "jenkins");
		Pages.getInstance().addSkill("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "java");
		Pages.getInstance().addSkill("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "node");
		Pages.getInstance().addSkill("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "j2ee");
		Pages.getInstance().addSkill("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "rest");
		Pages.getInstance().addSkill("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "wsdl");
		Pages.getInstance().addSkill("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "xml");
		Pages.getInstance().addSkill("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "soap");
		Pages.getInstance().addSkill("/jobsearch/job/18055767?uc=E12&amp;sc=5.3226&amp;sp=6", "xsd");
	}
	
	public static void main(String[] args) {
		CrawlerController cc = new CrawlerController();
	}
}
