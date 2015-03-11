package com.ws.smart.service;


import com.ws.model.VideoGameType;

import java.util.Comparator;

/**
 * Date: 23.11.13
 * Time: 22:39
 *
 * @author Vahur Kaar
 */
public class VideoGameComparator implements Comparator<VideoGameType> {

	@Override
	public int compare(VideoGameType game1, VideoGameType game2) {
		int ratingDiff = game1.getRating().compareTo(game2.getRating());

		if (ratingDiff == 0) {
			int yearDiff = game1.getYear().toGregorianCalendar().getTime()
					.compareTo(game2.getYear().toGregorianCalendar().getTime());
			if (yearDiff == 0) {
				return game1.getName().compareTo(game2.getName());
			}

			return yearDiff;
		}

		return ratingDiff;
	}
}
