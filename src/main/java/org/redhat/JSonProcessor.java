package org.redhat;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("JSonProcessor")
@RegisterForReflection
public class JSonProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Object body = exchange.getIn().getBody();
		try {
			ObjectMapper mapper = new ObjectMapper();
		    JsonNode json = mapper.readTree((String)body);
		    Map<String, Object> customer = new HashMap<String, Object>();
		    customer.put("cid", json.get("cid").asInt());
		    customer.put("name", json.get("name").asText());
		    customer.put("city", json.get("city").asText());
		    exchange.getIn().setBody(customer);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
