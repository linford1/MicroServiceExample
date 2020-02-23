package com.LinHub.moviecatalogueservice.resoures;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.LinHub.moviecatalogueservice.models.CatalogueItem;
import com.LinHub.moviecatalogueservice.models.Movie;
import com.LinHub.moviecatalogueservice.models.Rating;
import com.LinHub.moviecatalogueservice.models.UserRating;

@RestController
@RequestMapping("/catalogue")
public class MovieCatalogueResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
			
	@RequestMapping("/{userId}")
	public List<CatalogueItem> getCatalogue(@PathVariable("userId")String userId)
	{
		UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+ userId, UserRating.class);
		
		
		return ratings.getUserRating().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
			
			return new CatalogueItem(movie.getName(),"Desc", rating.getRating());
			}).collect(Collectors.toList());
		
	}

	//restTemplate will be depricated, webclient is the future
	/*Movie movie = webClientBuilder.build()
	.get()
	.uri("http://localhost:8081/movies/" + rating.getMovieId())
	.retrieve()
	.bodyToMono(Movie.class)
	.block();*/
}
