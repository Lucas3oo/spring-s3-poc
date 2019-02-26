package se.solrike.configuration;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.SslStoreProvider;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class SslConfig {
  
  @Autowired
  private ResourceLoader mResourceLoader;

  @Autowired
  private ServerProperties mServerProperties;

  @Bean
  public SslStoreProvider sslStoreProvider() {
    return new SpringResourceLoderSslStoreProvider(mResourceLoader, mServerProperties.getSsl());
  }

  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer(final SslStoreProvider sslStoreProvider) {
    return factory -> {
      factory.setSslStoreProvider(sslStoreProvider);
      factory.addConnectorCustomizers(this::customizeTomcatConnector);
    };
  }
  
  /**
   * The Spring/Tomcat integration tries to fool Tomcat by adding a new type of URL and register itself as a URL 
   * resolver. The keystore return from the SslProvider will be streamed by Spring in order to fit into the 
   * Tomcat way of reading keystores. In that process some bugs seems mess up 1) the type of keystore and 
   * 2) the passwords for both the keystore and the key.
   * 
   * @param connector
   */
  private void customizeTomcatConnector(Connector connector) {
    Ssl ssl = mServerProperties.getSsl();
    AbstractHttp11JsseProtocol<?> protocol = (AbstractHttp11JsseProtocol<?>) connector.getProtocolHandler();
    if (ssl.getKeyStoreType() != null) {
      // Seems to be bug in Spring/Tomcat integration where the type gets lost
      protocol.setKeystoreType(ssl.getKeyStoreType());
    }
    // Seems to be another bug that requires the keyPass to be same as the keystore password
    protocol.setKeyPass(ssl.getKeyStorePassword());

    if (ssl.getTrustStoreType() != null) {
      // Seems to be bug in Spring/Tomcat integration where the type gets lost
      protocol.setTruststoreType(ssl.getTrustStoreType());
    }
   
  }  
}