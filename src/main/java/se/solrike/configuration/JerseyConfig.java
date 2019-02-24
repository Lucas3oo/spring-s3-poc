package se.solrike.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import se.solrike.resources.ComponentMarker;

@Component
public class JerseyConfig extends ResourceConfig {
  private final static Logger logger = LoggerFactory.getLogger(JerseyConfig.class);

  @Autowired
  public JerseyConfig(@Value("${spring.jersey.applicationPath}") String basePath) {

    logger.info("Registering Jersey ...");

    String servicePackage = ComponentMarker.class.getPackage().getName();
    packages(servicePackage);

    property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    register(RolesAllowedDynamicFeature.class);

//    packages("org.glassfish.jersey.examples.multipart");
//    register(MultiPartFeature.class);

    // register(JacksonJaxbJsonProviderFactory.createProvider());

    configureSwagger(servicePackage, basePath);
    logger.info("Done Jersey Registration ...");

  }

  private BeanConfig configureSwagger(String servicePackage, String basePath) {
    // support Swagger
    register(ApiListingResource.class);
    register(SwaggerSerializers.class);
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setVersion("API " + "1.0");
    beanConfig.setSchemes(new String[] { "https" });
    beanConfig.setHost("localhost:8080");
    beanConfig.setBasePath(basePath);
    // comma separated string
    beanConfig.setResourcePackage(servicePackage);
    beanConfig.setPrettyPrint(true);
    beanConfig.setScan(true);
    beanConfig.setTitle("REST API");
    beanConfig.setDescription("The REST API is used from the JavaScript web GUI.");
    return beanConfig;
  }

}