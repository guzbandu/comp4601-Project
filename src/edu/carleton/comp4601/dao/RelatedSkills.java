package edu.carleton.comp4601.dao;

import java.util.HashMap;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import edu.carleton.comp4601.model.Equivalence;
import edu.carleton.comp4601.utility.DatabaseSingleton;

public class RelatedSkills {
	private static RelatedSkills instance;
	
	public static RelatedSkills getInstance() {
		if (instance == null)
			instance = new RelatedSkills();
		return instance;
	}
	
	public RelatedSkills() {

	}
	
	public HashMap<String, Double> getRelatedSkills(String skill) {
		HashMap<String, Double> relatedSkills = new HashMap<String, Double>();
		try {
			DBCollection relatedSkillsCollection = DatabaseSingleton.getInstance().getCollection("relatedSkills");
			DBCursor relatedSkillsEntries = relatedSkillsCollection.find();
			try {
				while(relatedSkillsEntries.hasNext()) {
					relatedSkillsEntries.next();
					//relatedSkills.put(equivEntry.getSkill1(), equivEntry.getSkill2());
				}
			} finally {
				relatedSkillsEntries.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return relatedSkills;
	}

}
