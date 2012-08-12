package net.udrunk.services;

import net.udrunk.UdrunkApplication;
import net.udrunk.domain.dto.AllCheckinsDto;
import net.udrunk.domain.dto.AllPlacesDto;

import org.springframework.web.client.RestTemplate;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest(UdrunkApplication.API_DOMAIN)
public interface UdrunkClient {

	@Get("/api/v1/feed/?format=json")
	AllCheckinsDto getFeed();

	@Get("/api/v1/place/?format=json")
	AllPlacesDto getPlaces();
	
	RestTemplate getRestTemplate();
	void setRestTemplate(RestTemplate restTemplate);
}
