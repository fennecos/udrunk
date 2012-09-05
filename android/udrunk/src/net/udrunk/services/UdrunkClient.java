package net.udrunk.services;

import net.udrunk.domain.User;
import net.udrunk.domain.dto.AllCheckinsDto;
import net.udrunk.domain.dto.AllLoginDto;
import net.udrunk.domain.dto.AllPlacesDto;
import net.udrunk.model.Model;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Post;
import com.googlecode.androidannotations.annotations.rest.Rest;

@Rest(Model.API_DOMAIN)
public interface UdrunkClient {

	@Get("/api/v1/feed/?format=json&username={username}&api_key={apiKey}")
	AllCheckinsDto getFeed(String username, String apiKey) throws RestClientException;

	@Get("/api/v1/place/?format=json&username={username}&api_key={apiKey}&near={lng},{lat}")
	AllPlacesDto getPlaces(double lat, double lng, String username, String apiKey) throws RestClientException;
	
	
	@Get("/api/v1/login/?format=json&login={login}&pass={pass}")
	AllLoginDto login(String login, String pass) throws RestClientException;
	
	@Post("/api/v1/user/")
	User insertUser(User user) throws RestClientException;

	@Post("/api/v1/checkin/?username={username}&api_key={apiKey}")
	Object insertCheckin(Object checkin, String username, String apiKey) throws RestClientException;
	
	RestTemplate getRestTemplate();
	void setRestTemplate(RestTemplate restTemplate);
}
