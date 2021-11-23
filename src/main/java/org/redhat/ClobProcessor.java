package org.redhat;

import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import io.quarkus.runtime.annotations.RegisterForReflection;
import oracle.sql.CLOB;

@ApplicationScoped
@Named("ClobProcessor")
@RegisterForReflection
public class ClobProcessor implements Processor {

	private static final Logger LOG = Logger.getLogger(ClobProcessor.class.getName());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Object body = exchange.getIn().getBody();
		if (body instanceof LinkedHashMap) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) body;
			if (map.containsKey("result_clob")) {
				LOG.info("Processing clob...");
				Object clob = map.get("result_clob");
				if (clob != null) {
					Reader r = ((CLOB)clob).getCharacterStream();
					StringBuffer buffer = new StringBuffer();
					int ch;
					while ((ch = r.read())!=-1) {
						buffer.append(""+(char)ch);
					}
					exchange.getIn().setBody(buffer.toString());
				} else {
					LOG.info("Emply clob.");
					exchange.getIn().setBody("[]");
				}
			} else {
				LOG.info("Result clob missing in " + map.keySet());
			}
		} else {
			LOG.info("Invalid content type: " + body.getClass().getName());
		}
	}

}
