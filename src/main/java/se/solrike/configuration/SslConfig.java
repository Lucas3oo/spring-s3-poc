package se.solrike.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.SslStoreProvider;
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
    return new S3SslStoreProvider(mResourceLoader, mServerProperties.getSsl());
  }

//  @Bean
//  public EmbeddedServletContainerCustomizer servletContainerCustomizer(SslStoreProvider sslStoreProvider) {
//    Ssl ssl = serverProperties.getSsl();
//    TomcatURLStreamHandlerFactory.getInstance().addUserFactory(new FixedSslStoreProviderUrlStreamHandlerFactory(
//        sslStoreProvider, ssl.getKeyStorePassword(), ssl.getTrustStorePassword()));
//
//    return container -> {
//      container.setSslStoreProvider(sslStoreProvider);
//      ((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers(this::customizeTomcatConnector);
//    };
//  }
//
//  private void customizeTomcatConnector(Connector connector) {
//    Ssl ssl = serverProperties.getSsl();
//    AbstractHttp11JsseProtocol<?> protocol = (AbstractHttp11JsseProtocol<?>) connector.getProtocolHandler();
//    protocol.setTruststorePass(ssl.getTrustStorePassword());
//    protocol.setTruststoreType(ssl.getTrustStoreType());
//  }
}