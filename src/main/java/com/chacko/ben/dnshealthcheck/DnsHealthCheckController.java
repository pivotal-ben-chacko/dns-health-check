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

	Logger logger = LoggerFactory.getLogger(DnsHealthCheckController.class);

	@RequestMapping(value = "/resolve/{hostname}", method = RequestMethod.GET)
	String resolveDNS(@PathVariable("hostname") String hostname) {
		DnsResolver dnsResolver = new SystemDefaultDnsResolver();
		StringBuilder builder = new StringBuilder();
		InetAddress[] addresses;
		try {
			addresses = dnsResolver.resolve(hostname);
			for (int i = 0; i < addresses.length; i++) {
				String address = addresses[i].toString();
				logger.info("Rosolved address: " + address);
				builder.append(address);
				builder.append("\n");
			}
			return builder.toString();
		} catch (UnknownHostException e) {
			logger.error("Caught UnknownHostException while trying to resolve address: " + hostname);
		}
		return builder.toString();
	}
}
