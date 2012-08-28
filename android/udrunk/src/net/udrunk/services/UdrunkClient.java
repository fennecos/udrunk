package net.udrunk.services;

import net.udrunk.domain.User;
import net.udrunk.domain.dto.AllCheckinsDto;
import net.udrunk.domain.dto.AllPlacesDto;
import net.udrunk.model.Model;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Post;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest(Model.API_DOMAIN)
public interface UdrunkClient {

	@Get("/api/v1/feed/?format=json&username=valentin&api_key=valentin")
	AllCheckinsDto getFeed() throws RestClientException;

	@Get("/api/v1/place/?format=json&username=valentin&api_key=valentin")
	AllPlacesDto getPlaces() throws RestClientException;
	
	
	@Post("/api/v1/user/?username=valentin&api_key=valentin")
	User insertUser(User user) throws RestClientException;

	@Post("/api/v1/checkin/?username=valentin&api_key=valentin")
	Object insertCheckin(Object checkin) throws RestClientException;
	
	RestTemplate getRestTemplate();
	void setRestTemplate(RestTemplate restTemplate);
}
