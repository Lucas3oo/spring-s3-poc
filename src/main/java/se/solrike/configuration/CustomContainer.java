package se.solrike.configuration;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.SslStoreProvider;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomContainer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

  private SslStoreProvider mSslStoreProvider;
  private ServerProperties mServerProperties;

  @Autowired
  public CustomContainer(SslStoreProvider sslStoreProvider, ServerProperties serverProperties) {
    mSslStoreProvider = sslStoreProvider;
    mServerProperties = serverProperties;
  }

  @Override
  public void customize(TomcatServletWebServerFactory factory) {
    factory.setSslStoreProvider(mSslStoreProvider);
    factory.addConnectorCustomizers(this::customizeTomcatConnector);
  }
  
  private void customizeTomcatConnector(Connector connector) {
    Ssl ssl = mServerProperties.getSsl();
    AbstractHttp11JsseProtocol<?> protocol = (AbstractHttp11JsseProtocol<?>) connector.getProtocolHandler();

    protocol.setKeystoreType(ssl.getKeyStoreType());
    
//    protocol.setTruststorePass(ssl.getTrustStorePassword());
//    protocol.setTruststoreType("JKS");
  }  
  
}
