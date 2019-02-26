package se.solrike.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Path("/hello")
public class HelloWorldResource {

  @Autowired
  ResourceLoader resourceLoader;

  private String mPassword;

  @Autowired
  public HelloWorldResource(@Value("${myapi.password}") String password) {
    mPassword = password;

  }

  @GET
  @Produces("plain/text")
  public String getHello() throws IOException {
    return downloadS3Object();
  }

  public String downloadS3Object() throws IOException {
    String s3Url = "s3://c360-service-instance-resources-stage/c360admin.cepheid.com.p12";
    Resource resource = resourceLoader.getResource(s3Url);
    File downloadedS3Object = new File(resource.getFilename());

    try (InputStream inputStream = resource.getInputStream()) {
      Files.copy(inputStream, downloadedS3Object.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    
    return resource.getFilename();
  }

}
