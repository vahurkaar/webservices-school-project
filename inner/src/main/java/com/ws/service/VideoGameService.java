package com.ws.service;

import com.ws.model.GetVideoGameSpecsRequest;
import com.ws.model.GetVideoGameSpecsResponse;
import com.ws.model.GetVideoGamesRequest;
import com.ws.model.GetVideoGamesResponse;

/**
 * Date: 19.11.13
 * Time: 19:15
 *
 * @author Vahur Kaar
 */
public interface VideoGameService {

	public static final String NAMESPACE = "http://www.ws.com/webservices";

	public static final String GET_VIDEO_GAMES_REQUEST = "GetVideoGamesRequest";

	public static final String GET_VIDEO_GAME_SPECS_REQUEST = "GetVideoGameSpecsRequest";

	GetVideoGamesResponse getVideoGames(GetVideoGamesRequest getVideoGamesRequest);

	GetVideoGameSpecsResponse getVideoGameSpecs(GetVideoGameSpecsRequest getVideoGameSpecsRequest);

}
