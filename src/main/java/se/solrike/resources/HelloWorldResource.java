package se.solrike.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Path("/hello")
public class HelloWorldResource {
  
  
  private String mPassword;

  @Autowired
  public HelloWorldResource(@Value("${myapi.password}") String password) {
    mPassword = password;
    
  }

  @GET
  @Produces("plain/text")
  public String getHello() {
    return "hello\n";
  }
}
