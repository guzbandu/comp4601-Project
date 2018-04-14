package edu.carleton.comp4601.dao;

import java.util.HashSet;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import edu.carleton.comp4601.model.Skill;
import edu.carleton.comp4601.utility.DatabaseSingleton;

public class Skills {
	private static Skills instance;
	
	public static Skills getInstance() {
		if (instance == null)
			instance = new Skills();
		return instance;
	}

	public Skills() {

	}

	public HashSet<String> getSkills() {
		HashSet<String> skills = new HashSet<String>();
		try {
			DBCollection skillsCollection = DatabaseSingleton.getInstance().getCollection("skills");
			skillsCollection.setObjectClass(Skill.class);
			DBCursor skillEntries = skillsCollection.find();
			try {
				while(skillEntries.hasNext()) {
					Skill skillEntry = (Skill) skillEntries.next();
					skills.add(skillEntry.getSkill());
				}
			} finally {
				skillEntries.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return skills;
	}

}
