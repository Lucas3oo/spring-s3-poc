package se.solrike.configuration;

import java.io.InputStream;
import java.security.KeyStore;

import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.SslStoreProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 
 * Uses Spring's resource loader to load the keystore and truststore. If
 * additional dependencies are added this provider can load resource from not
 * only classpath: and file: but other locations supported by
 * {@link org.springframework.core.io.ResourceLoader}
 * 
 *
 */
public class SpringResourceLoderSslStoreProvider implements SslStoreProvider {

  private final Ssl mSslConfiguration;
  private final ResourceLoader mResourceLoader;

  public SpringResourceLoderSslStoreProvider(ResourceLoader resourceLoader, Ssl sslConfiguration) {
    mResourceLoader = resourceLoader;
    mSslConfiguration = sslConfiguration;
  }

  @Override
  public KeyStore getKeyStore() throws Exception {
    return load(mSslConfiguration.getKeyStore(), mSslConfiguration.getKeyStoreType(),
        mSslConfiguration.getKeyStorePassword());
  }

  @Override
  public KeyStore getTrustStore() throws Exception {
    return load(mSslConfiguration.getTrustStore(), mSslConfiguration.getTrustStoreType(),
        mSslConfiguration.getTrustStorePassword());
  }

  private KeyStore load(String keyStoreLocation, String keyStoreType, String keyStorePassword) throws Exception {
    if (keyStoreLocation == null) {
      return null;
    }
    Resource resource = mResourceLoader.getResource(keyStoreLocation);
    try (InputStream inputStream = resource.getInputStream()) {
      KeyStore keyStore = KeyStore.getInstance(keyStoreType);
      char[] ps = null;
      if (keyStorePassword != null) {
        ps = keyStorePassword.toCharArray();
      }
      keyStore.load(inputStream, ps);
      return keyStore;
    }
  }

}
