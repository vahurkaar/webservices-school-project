package com.ws.smart.service;

import com.ws.model.*;
import com.ws.smart.model.*;
import com.ws.smart.model.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.xml.transform.StringResult;

import javax.xml.transform.Result;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Date: 23.11.13
 * Time: 22:07
 *
 * @author Vahur Kaar
 */
@Endpoint
public class SmartVideoGamesServiceImpl implements SmartVideoGamesService {

	private Logger logger = LoggerFactory.getLogger(SmartVideoGamesServiceImpl.class);

	@Autowired
	private WebServiceTemplate webServiceTemplate;

	@Autowired
	private Marshaller marshaller;

	@Autowired
	private ProcessorDao processorDao;

	@Autowired
	private VideoCardDao videoCardDao;

	private ObjectFactory of = new ObjectFactory();
	private com.ws.model.ObjectFactory of2 = new com.ws.model.ObjectFactory();

	@PayloadRoot(namespace = NAMESPACE, localPart = GET_PLAYABLE_GAMES_REQUEST)
	@ResponsePayload
	@Override
	public GetPlayableGamesResponse getPlayableGames(@RequestPayload GetPlayableGamesRequest getPlayableGamesRequest) {
		GetPlayableGamesResponse response = new GetPlayableGamesResponse();
		response.setVideoGamesList(of2.createVideoGamesList());
		List<VideoGameType> videoGames = getVideoGames();
		logger.debug(Arrays.toString(videoGames.toArray()));

		Result result = new StringResult();
		try {
			marshaller.marshal(getPlayableGamesRequest, result);
			logger.debug(result.toString());
		} catch (IOException e) {
			logger.error("error", e);
		}

		for (VideoGameType game : videoGames) {
			if (gameCanBePlayed(game, getPlayableGamesRequest.getSpecsFilter())) {
				response.getVideoGamesList().getVideoGame().add(game);
			}
		}

		Collections.sort(videoGames, new VideoGameComparator());

		return response;
	}

	private boolean gameCanBePlayed(VideoGameType game, GetPlayableGamesRequest.SpecsFilter specsFilter) {
		GetVideoGameSpecsRequest getVideoGameSpecsRequest = of2.createGetVideoGameSpecsRequest();
		getVideoGameSpecsRequest.setVideoGameID(game.getID());

		GetVideoGameSpecsResponse specsResponse =
				(GetVideoGameSpecsResponse) webServiceTemplate.marshalSendAndReceive(getVideoGameSpecsRequest);

		if (gameSpecsAreBetter(specsResponse.getVideoGameSpecs(), specsFilter)) {
			return true;
		}
		logger.debug("Cant play");
		return false;
	}

	private boolean gameSpecsAreBetter(VideoGameSpecsType videoGameSpecs,
									   GetPlayableGamesRequest.SpecsFilter specsFilter) {
		if (gameMemoryIsLargerThanFilterMemory(videoGameSpecs.getMemory(), specsFilter.getMemory())) {
			logger.debug("memory not ok");
			return false;
		}

		if (gameHddIsLargerThanFilterHdd(videoGameSpecs.getHDD(), specsFilter.getHDD())) {
			logger.debug("hdd not ok");
			return false;
		}

		if (gameOsIsBetterThanFilterOs(videoGameSpecs.getOS(), specsFilter.getOS())) {
			logger.debug("os not ok");
			return false;
		}

		if (gameProcessorIsBetterThanFilterProcessor(videoGameSpecs.getProcessor(), specsFilter.getProcessor())) {
			logger.debug("proc not ok");
			return false;
		}

		if (gameVideoCardIsBetterThanFilterVideoCard(videoGameSpecs.getVideoCard(), specsFilter.getVideoCard())) {
			logger.debug("video not ok");
			return false;
		}
			
		return true;
	}

	private boolean gameVideoCardIsBetterThanFilterVideoCard(VideoCardType gameVideoCard, VideoCardType filterVideoCard) {
		BigDecimal gameVideoCardScore = getVideoCardScore(gameVideoCard);
		BigDecimal filterVideoCardScore = getVideoCardScore(filterVideoCard);

		if (gameVideoCardScore.compareTo(filterVideoCardScore) > 0) {
			return true;
		}

		return false;
	}

	private boolean gameProcessorIsBetterThanFilterProcessor(ProcessorType gameProcessor, ProcessorType filterProcessor) {
		BigDecimal gameProcessorScore = getProcessorScore(gameProcessor);
		BigDecimal filterProcessorScore = getProcessorScore(filterProcessor);

		if (gameProcessorScore.compareTo(filterProcessorScore) > 0) {
			return true;
		}

		return false;
	}

	private boolean gameOsIsBetterThanFilterOs(OsType gameOs, OsType filterOs) {
		if (new OsComparator().compare(gameOs, filterOs) > 0) {
			return true;
		}

		return false;
	}

	private boolean gameHddIsLargerThanFilterHdd(MemoryType gameHdd, MemoryType filterHdd) {
		BigDecimal gameHddValue = getHddValue(gameHdd);
		BigDecimal filterHddValue = getHddValue(filterHdd);

		if (gameHddValue.compareTo(filterHddValue) > 0) {
			return true;
		}

		return false;
	}

	private boolean gameMemoryIsLargerThanFilterMemory(MemoryType gameMemory, MemoryType filterMemory) {
		BigDecimal gameMemoryValue = getMemoryValue(gameMemory);
		BigDecimal filterMemoryValue = getMemoryValue(filterMemory);

		if (gameMemoryValue.compareTo(filterMemoryValue) > 0) {
			return true;
		}

		return false;
	}

	private BigDecimal getVideoCardScore(VideoCardType filterVideoCard) {
		return videoCardDao.getVideoCardScore(filterVideoCard);
	}

	private BigDecimal getProcessorScore(ProcessorType filterProcessor) {
		return processorDao.getProcessorScore(filterProcessor);
	}

	private BigDecimal getHddValue(MemoryType hdd) {
		if (hdd.getSize() == null) {
			return BigDecimal.ZERO;
		}

		if (hdd.getUnit() == null) {
			hdd.setUnit(MemoryUnit.GB);
		}

		BigDecimal hddValue;
		switch (hdd.getUnit()) {
			case GB:
				hddValue = hdd.getSize().multiply(BigDecimal.valueOf(1024));
				break;
			default:
				hddValue = hdd.getSize();
		}

		return hddValue;
	}

	private BigDecimal getMemoryValue(MemoryType memory) {
		if (memory.getSize() == null) {
			return BigDecimal.ZERO;
		}

		if (memory.getUnit() == null) {
			memory.setUnit(MemoryUnit.MB);
		}

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

	private List<VideoGameType> getVideoGames() {
		GetVideoGamesRequest getVideoGamesRequest = of2.createGetVideoGamesRequest();
		GetVideoGamesResponse getVideoGamesResponse =
				(GetVideoGamesResponse) webServiceTemplate.marshalSendAndReceive(getVideoGamesRequest);
		return getVideoGamesResponse.getVideoGamesList().getVideoGame();
	}
}
