/**
 * 
 */
package co.com.bancolombia.jerseyclient.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.Response.Status;

import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Actions;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.EnumLoggerLevel;
import co.com.bancolombia.model.propertyoflogger.PropertyOfLogger.Services;
import co.com.bancolombia.model.propertyoflogger.gateways.IPropertyOfLoggerRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author linkott
 *
 */
@RequiredArgsConstructor
@SuppressWarnings("java:S4830")
public class TrustManagerConf implements X509TrustManager {

	private final IPropertyOfLoggerRepository loggerApp;

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		
		loggerApp.logger(Actions.CJ_DRIVEN_ADAPTER_UTIL_CHECKCLIENT, Status.PRECONDITION_REQUIRED.toString(),
				EnumLoggerLevel.INFO, null);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	
		loggerApp.logger(Actions.CJ_DRIVEN_ADAPTER_UTIL_CHECKSERVER, Status.PRECONDITION_REQUIRED.toString(),
				EnumLoggerLevel.INFO, null);
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		AtomicInteger counter = new AtomicInteger();
		return new X509Certificate[counter.incrementAndGet()];
	}

}
