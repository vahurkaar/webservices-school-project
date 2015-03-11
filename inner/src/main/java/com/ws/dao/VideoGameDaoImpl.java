package com.ws.dao;

import com.ws.model.GetVideoGamesResponse;
import com.ws.model.SystemSpecs;
import com.ws.model.VideoGameSpecsType;
import com.ws.model.VideoGameType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Repository;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Date: 23.11.13
 * Time: 20:13
 *
 * @author Vahur Kaar
 */
@Repository
public class VideoGameDaoImpl implements VideoGameDao {

	@Autowired
	private Unmarshaller marshaller;

	@Override
	public List<VideoGameType> findVideoGames(List<VideoGameType> videoGames, String searchString) throws IOException {
		Resource videoGamesResource = new ClassPathResource("videogames.xml");
		GetVideoGamesResponse videoGamesToBeSearched =
				(GetVideoGamesResponse) marshaller.unmarshal(new StreamSource(videoGamesResource.getInputStream()));

		for (VideoGameType game : videoGamesToBeSearched.getVideoGamesList().getVideoGame()) {
			if (searchString == null || game.getName().toLowerCase().contains(searchString.toLowerCase())) {
				videoGames.add(game);
			}
		}

		return videoGames;
	}

	@Override
	public VideoGameSpecsType findVideoGameSpec(String searchString) throws IOException {
		Resource specsResource = new ClassPathResource("systemSpecs.xml");
		SystemSpecs systemSpecs =
				(SystemSpecs) marshaller.unmarshal(new StreamSource(specsResource.getInputStream()));

		for (VideoGameSpecsType systemSpec : systemSpecs.getSystemSpec()) {
			if (systemSpec.getVideoGameID().equalsIgnoreCase(searchString)) {
				return systemSpec;
			}
		}
		return null;
	}
}
