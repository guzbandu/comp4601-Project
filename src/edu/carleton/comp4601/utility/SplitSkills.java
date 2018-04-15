package edu.carleton.comp4601.utility;

import java.util.HashSet;
import java.util.Set;

import edu.carleton.comp4601.dao.Skills;

public class SplitSkills {

	public static void main(String[] args) {
		int size = Skills.getInstance().getSkills().size();
		int count = 0;
		Set<String> firstHalf = new HashSet<String>();
		Set<String> secondHalf = new HashSet<String>();
		for(String skill : Skills.getInstance().getSkills()) {
			if(count<Math.floor(size/2)) {
				firstHalf.add(skill);
			} else {
				secondHalf.add(skill);
			}
			count++;
		}
		
		System.out.println("First half:");
		for(String skill : firstHalf) {
			System.out.println(skill);
		}
		
		System.out.println("");
		System.out.println("Second half:");
		for(String skill : secondHalf) {
			System.out.println(skill);
		}
	}

}
