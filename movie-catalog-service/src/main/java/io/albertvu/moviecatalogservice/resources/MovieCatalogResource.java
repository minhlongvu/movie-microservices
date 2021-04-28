package io.albertvu.moviecatalogservice.resources;

// accepts requests for "give me the catalog with this id" and return list of movies with everything bundled together

import io.albertvu.moviecatalogservice.models.CatalogItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId) {
		return Collections.singletonList(
				new CatalogItem("Transformers", "Test description", 4)
		);
	}
}
