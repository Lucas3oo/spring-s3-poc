package se.solrike.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.SslStoreProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class S3SslStoreProvider implements SslStoreProvider {

  private final Ssl mSslConfiguration;
  private final ResourceLoader mResourceLoader;

  public S3SslStoreProvider(ResourceLoader resourceLoader, Ssl sslConfiguration) {
    mResourceLoader = resourceLoader;
    mSslConfiguration = sslConfiguration;
    
    //mSslConfiguration.setTrustStoreType("JKS");
  }

  @Override
  public KeyStore getKeyStore() throws Exception {
    Resource resource = mResourceLoader.getResource(mSslConfiguration.getKeyStore());
    InputStream inputStream = resource.getInputStream();
    KeyStore keyStore = KeyStore.getInstance(mSslConfiguration.getKeyStoreType());
    keyStore.load(inputStream, mSslConfiguration.getKeyStorePassword().toCharArray());
    return keyStore;
  }
  
  
  @Override
  public KeyStore getTrustStore() throws Exception {
    return null;
  }

  public KeyStore getTrustStoreXXX() throws Exception {
    byte[] bytes = Files.readAllBytes(
        Paths.get("/Library/Java/JavaVirtualMachines/jdk1.8.0_111.jdk/Contents/Home/jre/lib/security/cacerts"));
    return load(bytes, mSslConfiguration.getTrustStoreType(), mSslConfiguration.getTrustStorePassword());
  }

  private KeyStore load(byte[] bytes, String type, String password)
      throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
    try (InputStream stream = new ByteArrayInputStream(bytes)) {
      KeyStore keyStore = KeyStore.getInstance(type);
      
      if (password != null) {
        keyStore.load(stream, password.toCharArray());
      }
      else {
        keyStore.load(stream, null);
      }

      return keyStore;
    }
  }
}
