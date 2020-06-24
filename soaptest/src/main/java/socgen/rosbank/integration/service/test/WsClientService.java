package socgen.rosbank.integration.service.test;

import javax.xml.ws.BindingProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import socgen.rosbank.integration.wsdl.glws.DSBranchFindListAllReq;
import socgen.rosbank.integration.wsdl.glws.DSBranchFindListAllRes;
import socgen.rosbank.integration.wsdl.glws.WSPORTTYPE;
import socgen.rosbank.integration.wsdl.glws.WSSERVICE;

@Service
public class WsClientService {

	@Value("${wsdlUrl}")
	private String wsdlUrl;

	public void go() {

		WSSERVICE ss = new WSSERVICE();
		ss.setHandlerResolver(new WebServiceHandlerResolver("suv_loader", "123456"));
		WSPORTTYPE port = ss.getWSPORT();

		System.out.println("Call service by URL [" + wsdlUrl + "]");

		setServiceProperties((BindingProvider) port, wsdlUrl);
		{

			try {
				DSBranchFindListAllReq request = new DSBranchFindListAllReq();
				DSBranchFindListAllRes resp = port.dsBranchFindListAll(request);

			} catch (Exception e) {
				System.out.println("Expected exception: DSCALLFAULT has occurred.");
				e.printStackTrace();
			}
		}
	}

	private void setServiceProperties(BindingProvider bindingProvider, String urlString) {
		bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);
	}
}
