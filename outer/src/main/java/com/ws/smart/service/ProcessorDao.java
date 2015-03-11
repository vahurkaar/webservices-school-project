package com.ws.smart.service;

import com.ws.model.ClockSpeedType;
import com.ws.model.ProcessorType;
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
public class ProcessorDao {

	private Map<String, Integer> scoreTable = new HashMap<>();

	public ProcessorDao() {
		scoreTable.put("Pentium 4", 1);
		scoreTable.put("Celeron D 365", 2);
		scoreTable.put("Core 2 Duo E4400", 3);
		scoreTable.put("Core 2 Duo E4700", 4);
		scoreTable.put("Core 2 Quad", 5);
	}

	public BigDecimal getProcessorScore(ProcessorType processorType) {
		BigDecimal score = BigDecimal.ZERO;

		if (scoreTable.containsKey(processorType.getNumber())) {
			score = score.add(BigDecimal.valueOf(scoreTable.get(processorType.getNumber())));
			score = score.multiply(BigDecimal.valueOf(1000));

			if (processorType.getClockSpeed() != null) {
				BigDecimal clockSpeedValue = getClockSpeedValue(processorType.getClockSpeed());
				score = score.add(clockSpeedValue);
			}
		}

		return score;
	}

	private BigDecimal getClockSpeedValue(ClockSpeedType clockSpeed) {
		BigDecimal clockSpeedValue;
		switch (clockSpeed.getUnit()) {
			case G_HZ:
				clockSpeedValue = clockSpeed.getValue().multiply(BigDecimal.valueOf(1024));
				break;
			default:
				clockSpeedValue = clockSpeed.getValue();
		}

		return clockSpeedValue;
	}

}
