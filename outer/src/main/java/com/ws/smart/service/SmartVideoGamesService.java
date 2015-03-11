package com.ws.smart.service;

import com.ws.smart.model.GetPlayableGamesRequest;
import com.ws.smart.model.GetPlayableGamesResponse;

/**
 * Date: 23.11.13
 * Time: 22:04
 *
 * @author Vahur Kaar
 */
public interface SmartVideoGamesService {

	public static final String NAMESPACE = "http://www.ws.com/webservices/smart";

	public static final String GET_PLAYABLE_GAMES_REQUEST = "GetPlayableGamesRequest";

	GetPlayableGamesResponse getPlayableGames(GetPlayableGamesRequest getPlayableGamesRequest);

}
