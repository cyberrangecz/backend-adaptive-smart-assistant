package cz.muni.ics.kypo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * The type Validation messages config.
 */
@Configuration
public class ValidationMessagesConfig {

    /**
     * Message source validation message source.
     *
     * @return the message source
     */
    @Bean
    public MessageSource messageSourceValidation() {
        final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:locale/ValidationMessages");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(0);
        return source;
    }

    /**
     * Gets validator.
     *
     * @return the validator
     */
    @Bean
    @Primary
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSourceValidation());
        return bean;
    }
}
