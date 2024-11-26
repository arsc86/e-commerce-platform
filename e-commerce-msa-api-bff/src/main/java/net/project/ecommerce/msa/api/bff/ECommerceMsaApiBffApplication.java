package net.project.ecommerce.msa.api.bff;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@ComponentScan("net.project")
public class ECommerceMsaApiBffApplication {
	
	@Value("${path.webservice.base}")
	private String pathWebServiceBase;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceMsaApiBffApplication.class, args);
	}

    @Bean
    ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet dispatcherServlet) 
    {
        ServletRegistrationBean<DispatcherServlet> bean = 
        		new ServletRegistrationBean<>(dispatcherServlet,"/" + pathWebServiceBase + "/*");
        bean.setAsyncSupported(true);
        bean.setName(pathWebServiceBase);
        bean.setLoadOnStartup(1);
        return bean;
    }


}
