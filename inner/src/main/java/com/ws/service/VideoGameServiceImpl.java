package com.ws.service;

import com.ws.dao.VideoGameDao;
import com.ws.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;

/**
 * Date: 23.11.13
 * Time: 18:37
 *
 * @author Vahur Kaar
 */
@Endpoint
public class VideoGameServiceImpl implements VideoGameService {

	private Logger logger = LoggerFactory.getLogger(VideoGameServiceImpl.class);

	private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

	@Autowired
	private VideoGameDao videoGameDao;

	@PayloadRoot(namespace = NAMESPACE, localPart = GET_VIDEO_GAMES_REQUEST)
	@ResponsePayload
	@Override
	public GetVideoGamesResponse getVideoGames(@RequestPayload GetVideoGamesRequest getVideoGamesRequest) {
		logger.debug("Searching for videogames... (" + getVideoGamesRequest.getVideoGameName() + ")");
		String searchString = getVideoGamesRequest.getVideoGameName();

		GetVideoGamesResponse response = OBJECT_FACTORY.createGetVideoGamesResponse();
		VideoGamesList videoGamesList = OBJECT_FACTORY.createVideoGamesList();
		response.setVideoGamesList(videoGamesList);

		try {
			videoGameDao.findVideoGames(videoGamesList.getVideoGame(), searchString);
		} catch (Exception e) {
			logger.error("Fail", e);
		}

		return response;
	}

	@PayloadRoot(namespace = NAMESPACE, localPart = GET_VIDEO_GAME_SPECS_REQUEST)
	@ResponsePayload
	@Override
	public GetVideoGameSpecsResponse getVideoGameSpecs(@RequestPayload GetVideoGameSpecsRequest getVideoGameSpecsRequest) {
		logger.debug("Searching for specs... (" + getVideoGameSpecsRequest.getVideoGameID() + ")");
		String searchString = getVideoGameSpecsRequest.getVideoGameID();

		GetVideoGameSpecsResponse response = OBJECT_FACTORY.createGetVideoGameSpecsResponse();

		try {
			response.setVideoGameSpecs(videoGameDao.findVideoGameSpec(searchString));
		} catch (IOException e) {
			logger.error("Fail...", e);
		}

		return response;
	}
}
