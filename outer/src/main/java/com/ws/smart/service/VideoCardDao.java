package com.ws.smart.service;

import com.ws.model.MemoryType;
import com.ws.model.VideoCardType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 24.11.13
 * Time: 13:48
 *
 * @author Vahur Kaar
 */
@Repository
public class VideoCardDao {

	private Map<String, Integer> scoreTable = new HashMap<>();

	public VideoCardDao() {
		scoreTable.put("Radeon 9500", 1);
		scoreTable.put("Radeon HD 2400 Series", 2);
		scoreTable.put("Radeon HD 5450", 3);
		scoreTable.put("Radeon X800 XL", 4);
		scoreTable.put("Radeon X1900", 5);
		scoreTable.put("Radeon X1950 Pro", 6);
	}

	public BigDecimal getVideoCardScore(VideoCardType videoCardType) {
		BigDecimal score = BigDecimal.ZERO;

		if (scoreTable.containsKey(videoCardType.getSeries())) {
			score = score.add(BigDecimal.valueOf(scoreTable.get(videoCardType.getSeries())));
			score = score.multiply(BigDecimal.valueOf(1000));

			if (videoCardType.getMemory() != null) {
				BigDecimal memoryValue = getMemoryValue(videoCardType.getMemory());
				score = score.add(memoryValue);
			}
		}

		return score;
	}

	private BigDecimal getMemoryValue(MemoryType memory) {
		BigDecimal memoryValue;
		switch (memory.getUnit()) {
			case GB:
				memoryValue = memory.getSize().multiply(BigDecimal.valueOf(1024));
				break;
			default:
				memoryValue = memory.getSize();
		}
		return memoryValue;
	}


}
