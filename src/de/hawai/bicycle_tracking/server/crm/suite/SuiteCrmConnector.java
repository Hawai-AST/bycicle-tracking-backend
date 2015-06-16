package de.hawai.bicycle_tracking.server.crm.suite;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hawai.bicycle_tracking.server.crm.suite.token.Token;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.GetEntryListToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.GetRelationshipsToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.LoginToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.SetEntryToken;

@Component
public class SuiteCrmConnector  {

	@Value("${suite.url}")
	private String CRM_URI = "http://141.22.29.121/suitecrm/service/v4_1/rest.php";
	
	@Value("${suite.user}")
	private String suiteUser;
	@Value("${suite.password}")
	private String plainPWD;

	private ObjectMapper mapper;

	private SuiteSession suiteSession;

	private static final String METHOD = "method";

	public SuiteCrmConnector() {
		mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}

	public SuiteCrmConnector(LoginToken loginToken) {
		super();
		postLogin(loginToken);
	}

	private Object postToCrm(MultiValueMap<String, String> request, Class<?> responseType) {

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new FormHttpMessageConverter());
		list.add(new MappingJackson2HttpMessageConverter());
		list.add(new StringHttpMessageConverter());
		restTemplate.setMessageConverters(list);

		request.add("input_type", "JSON");
		request.add("response_type", "JSON");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(request, headers);

		URI uri = null;
		try {
			uri = new URI(CRM_URI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Object postForObject = null;
		try {
			postForObject = restTemplate.postForObject(uri, entity, responseType);
		} catch (HttpMessageNotReadableException e) {
			e.printStackTrace();
			return null;
		}
		return postForObject;

	}

	private void assureLogin() {
		if (null == suiteSession) {
			postLogin(new LoginToken(suiteUser, plainPWD));
		}
	}

	public SuiteSession postLogin(LoginToken login) {
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add(METHOD, SuiteCrmMethods.LOGIN);
		addTokenToRequest(login, request);
		suiteSession = (SuiteSession) postToCrm(request, SuiteSession.class);
		return suiteSession;
	}

	public Object postGetEntryList(GetEntryListToken getEntryList, Class<?> responseType) {
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add(METHOD, SuiteCrmMethods.GET_ENTRY_LIST);
		addTokenToRequest(getEntryList, request);
		return postToCrm(request, responseType);
	}

	public Object postGetEntryList(GetEntryListToken getEntryList) {
		return postGetEntryList(getEntryList, Object.class);
	}
	
    public Object postSetEntry(SetEntryToken setEntryToken, Class<?> responseType) throws IOException {
    	MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add(METHOD, SuiteCrmMethods.SET_ENTRY);
		addTokenToRequest(setEntryToken, request);
        return postToCrm(request, responseType);
    }
    
    public Object postGetRelationships(GetRelationshipsToken getRelationshipsToken, Class<?> responseType) {
    	MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add(METHOD, SuiteCrmMethods.GET_RELATIONSHIPS);
		addTokenToRequest(getRelationshipsToken, request);
		return postToCrm(request, responseType);
    }

	private void addTokenToRequest(Token token, MultiValueMap<String, String> request) {
		try {
			request.add("rest_data", mapper.writeValueAsString(token));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public String getSessionId() {
		assureLogin();
		return suiteSession.getSessionId();
	}

}
