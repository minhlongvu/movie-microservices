package io.albertvu.moviecatalogservice.resources;

// accepts requests for "give me the catalog with this id" and return list of movies with everything bundled together

import io.albertvu.moviecatalogservice.models.CatalogItem;
import io.albertvu.moviecatalogservice.models.Movie;
import io.albertvu.moviecatalogservice.models.Rating;
import io.albertvu.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;



	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId) {

		// grabs list of movieId and rating
		UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);

		// we can make API call with REST template using for loop or map
		return ratings.getUserRating().stream().map(rating -> {
			// get the payload at the URL and unmarshal into the class
			// For each movie ID, call movie info service and get details
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

			// Put them all together
			return new CatalogItem(movie.getName(), "Test description", rating.getRating());
		})
		// ToList collector can be used for collecting all Stream elements into a List instance
		.collect(Collectors.toList());
	}
}
