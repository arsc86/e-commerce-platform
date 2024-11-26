package net.project.ecommerce.msa.api.bff.config;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;


@Configuration
public class MultiPartConfig {
 
	@Value("${nfs.multipart.max.upload.byte:1000000}")
	private int maxFileSize;

    @Bean
    TomcatServletWebServerFactory tomcatFactory()
    {
        return new TomcatServletWebServerFactory()
        {
            @Override
            protected void postProcessContext(Context context)
            {
                context.setAllowCasualMultipartParsing(true);
            }
        };
    }


    @Bean
    WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {


        return factory -> factory.addConnectorCustomizers(connector -> {
            connector.setMaxPostSize(maxFileSize);
        });
    }

    @Bean
    ObjectMapper objectMapper()
    {
        JsonFactory jsonFactory = JsonFactory.builder().streamReadConstraints(
                StreamReadConstraints.builder()
                        .maxStringLength(Integer.MAX_VALUE)
                        .build()
        ).build();
        return JsonMapper.builder(jsonFactory)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

                . build();
    }

}
