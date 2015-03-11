package com.ws.smart.service;


import com.ws.model.OsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 23.11.13
 * Time: 23:00
 *
 * @author Vahur Kaar
 */
public class OsComparator implements Comparator<OsType> {

	private Logger logger = LoggerFactory.getLogger(OsComparator.class);

	private Map<String, Integer> osScoreTable = new HashMap<>();

	public OsComparator() {
		osScoreTable.put("WINDOWS ", 0);
		osScoreTable.put("WINDOWS XP", 1);
		osScoreTable.put("WINDOWS VISTA", 2);
		osScoreTable.put("WINDOWS 7", 3);
		osScoreTable.put("WINDOWS 8", 4);
	}

	@Override
	public int compare(OsType o1, OsType o2) {
		return Integer.compare(getScore(o1), getScore(o2));
	}

	private int getScore(OsType o) {
		String osFullName = o.getName() + " " + o.getVersion().trim();
		logger.debug(osFullName);
		return osScoreTable.get(osFullName);
	}
}
