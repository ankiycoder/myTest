package socgen.rosbank.integration.service.test;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceSecurityHandler implements SOAPHandler<SOAPMessageContext> {

	private final Logger log = LoggerFactory.getLogger(WebServiceSecurityHandler.class);
	private final String username;
	private final String password;

	public WebServiceSecurityHandler(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public Set<QName> getHeaders() {
		final QName securityHeader = new QName(
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security",
				"wsse");
		return Collections.singleton(securityHeader);
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		SOAPMessage message = context.getMessage();
		if (outboundProperty) {
			try {
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
				SOAPHeader header = envelope.getHeader();
				SOAPElement securityElement = header.addChildElement("Security", "wsse",
						"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
				SOAPElement usernameTokenElement = securityElement.addChildElement("UsernameToken", "wsse");
				SOAPElement usernameElement = usernameTokenElement.addChildElement("Username", "wsse");
				usernameElement.addTextNode(username);
				SOAPElement passwordElement = usernameTokenElement.addChildElement("Password", "wsse");
				passwordElement.setAttribute("Type", "PasswordText");
				passwordElement.addTextNode(password);
				message.saveChanges();
			} catch (SOAPException e) {
				log.error("Error on adding security attributes to SOAP message header", e);
			}
			String formattedSOAPMessage = SOAPHelper.getFormattedSOAPMessage(message);
			log.debug("Soap request: {}\n", formattedSOAPMessage);

		} else {
			String formattedSOAPMessage = SOAPHelper.getFormattedSOAPMessage(message);
			log.debug("Soap response: {}\n", formattedSOAPMessage);

		}
		return outboundProperty;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {

	}

}