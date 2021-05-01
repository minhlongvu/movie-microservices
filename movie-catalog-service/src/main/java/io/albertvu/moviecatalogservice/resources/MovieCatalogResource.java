package io.albertvu.moviecatalogservice.resources;

// accepts requests for "give me the catalog with this id" and return list of movies with everything bundled together

import io.albertvu.moviecatalogservice.models.CatalogItem;
import io.albertvu.moviecatalogservice.models.Movie;
import io.albertvu.moviecatalogservice.models.Rating;
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

	@Autowired
	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId) {

		// get all rated movie IDs
		List<Rating> ratings = Arrays.asList(
				new Rating("1234", 4),
				new Rating("5678", 3)
		);

		// we can make API call with REST template using for loop or map
		return ratings.stream().map(rating -> {
			// get the payload at the URL and unmarshal into the class
			//Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

			Movie movie = webClientBuilder.build()
					.get()
					.uri("http://localhost:8082/movies/" + rating.getMovieId())
					.retrieve()
					.bodyToMono(Movie.class)
					.block();

			return new CatalogItem(movie.getName(), "Test description", rating.getRating());
		})
		// ToList collector can be used for collecting all Stream elements into a List instance
		.collect(Collectors.toList());
	}
}
