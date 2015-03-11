package com.ws.dao;

import com.ws.model.VideoGameSpecsType;
import com.ws.model.VideoGameType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Date: 23.11.13
 * Time: 19:09
 *
 * @author Vahur Kaar
 */
public interface VideoGameDao {

	List<VideoGameType> findVideoGames(List<VideoGameType> videoGames, String searchString) throws IOException;

	VideoGameSpecsType findVideoGameSpec(String searchString) throws IOException;

}
