package com.chacko.ben.dnshealthcheck;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.conn.DnsResolver;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 * DnsHealthCheckController
 * 
 * If you want to override the System defined dns resolver you must set the following envrionment variable: JAVA_OPTS
 * 
 * For example, to set dns resolver to Google dns:
 *  
 * cf set-env dnscheck JAVA_OPTS "-Dsun.net.spi.nameservice.provider.1=\"dns,sun\" -Dsun.net.spi.nameservice.nameservers=8.8.8.8"
 * 
 */
@RestController
public class DnsHealthCheckController {
	String error = "";

	Logger logger = LoggerFactory.getLogger(DnsHealthCheckController.class);

	@RequestMapping(value = "/resolve/{hostname}", method = RequestMethod.GET)
	String resolveDNS(@PathVariable("hostname") String hostname) {
		return resolveDNS(hostname, 1);
	}
	
	@RequestMapping(value = "/resolve/{hostname}/{count}", method = RequestMethod.GET)
	String resolveDNSWithRetries(@PathVariable("hostname") String hostname, @PathVariable("count") int count) {
		return resolveDNS(hostname, count);
	}

	String resolveDNS(String hostname, int count) {
		DnsResolver dnsResolver = new SystemDefaultDnsResolver();
		StringBuilder builder = new StringBuilder();
		InetAddress[] addresses;
		while (count > 0) {
			try {
				addresses = dnsResolver.resolve(hostname);
				for (int i = 0; i < addresses.length; i++) {
					String address = addresses[i].toString();
					logger.info("Rosolved address: " + address);
					builder.append("Resolved address: " + address);
					builder.append("<br/>");
				}
			} catch (UnknownHostException e) {
				logger.error("Caught UnknownHostException while trying to resolve address: " + hostname);
				return e.getMessage();
			} finally {
				count--;
			}
		}
		return builder.toString();
	}
}
