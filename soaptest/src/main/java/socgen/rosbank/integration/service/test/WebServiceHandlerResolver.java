package socgen.rosbank.integration.service.test;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

public class WebServiceHandlerResolver implements HandlerResolver {

    private final String username;
    private final String password;

    public WebServiceHandlerResolver(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlerChain = new ArrayList<>();
        handlerChain.add(new WebServiceSecurityHandler(username, password));
        return handlerChain;
    }

}