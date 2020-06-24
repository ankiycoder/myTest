package socgen.rosbank.integration.service.test;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOAPHelper {

	private static final Logger logger = LoggerFactory.getLogger(SOAPHelper.class);
	private static final Pattern patternPassword = Pattern.compile("Password.*?>.+?</.*?Password");
	private static Transformer tf = null;

	static {
		try {
			TransformerFactory tff = TransformerFactory.newInstance();
			tf = tff.newTransformer();
			tf.setOutputProperty("indent", "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
		} catch (Exception var1) {
			logger.info("Can't initiate javax.xml.transform.Transformer for SOAPHelper");
			logger.debug(var1.getMessage(), var1);
		}

	}

	public static String getFormattedSOAPMessage(SOAPMessage soapMessage) {
		if (soapMessage == null) {
			return "";
		} else if (tf == null) {
			return getSOAPMessage(soapMessage);
		} else {
			try {
				Source sc = soapMessage.getSOAPPart().getContent();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				StreamResult result = new StreamResult(out);
				tf.transform(sc, result);
				return out.toString("UTF8");
			} catch (Exception var4) {
				logger.error("Can't convert SOAP message to formatted string");
				logger.debug(var4.getMessage(), var4);
				return getSOAPMessage(soapMessage);
			}
		}
	}

	public static String getSOAPMessage(SOAPMessage soapMessage) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapMessage.writeTo(out);
			return out.toString("UTF8");
		} catch (Exception var2) {
			logger.error("Can't convert SOAP message to string");
			logger.debug(var2.getMessage(), var2);
			return "";
		}
	}


}
