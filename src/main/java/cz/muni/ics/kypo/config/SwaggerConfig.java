package cz.muni.ics.kypo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Set;

/**
 * Common configuration of Swagger for all projects that import this project.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerConfig.class);
    private static final String NAME_OF_SECURITY_SCHEME = "KYPO";
    @Value("${swagger.enabled:true}")
    private boolean swaggerEnabled;

    /**
     * The Docket bean is configured to give more control over the API documentation generation process.
     *
     * @return the docket
     */
    @Bean
    public Docket api() {
        LOG.debug("SwaggerConfig -> api()");
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnabled)
                .groupName("public-api")
                .apiInfo(apiInfo()).useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        LOG.debug("SwaggerConfig -> apiInfo()");
        return new ApiInfoBuilder()
                .title("REST API documentation")
                .description("Developed by CSIRT team")
                .termsOfServiceUrl("Licensed by CSIRT team")
                .build();
    }
}
