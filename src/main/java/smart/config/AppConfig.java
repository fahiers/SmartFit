package smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
@EnableWebMvc
@ComponentScan({"smart.controllers", "smart.servicios","smart.security"})
public class AppConfig implements WebMvcConfigurer {

	   @Autowired
	   private ApplicationContext applicationContext;

	  
	   @Bean
	   public SpringResourceTemplateResolver templateResolver() {
	      SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
	      templateResolver.setApplicationContext(applicationContext);
	      templateResolver.setPrefix("/WEB-INF/templates/");
	      templateResolver.setSuffix(".html");
	      return templateResolver;
	   }
	   
	   @Bean
	   public SpringTemplateEngine templateEngine() {
	      SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	      templateEngine.setTemplateResolver(templateResolver());
	      templateEngine.setEnableSpringELCompiler(true);
	      return templateEngine;
	   }
	   
	   @Bean
	   public Firestore getDb() {
		   return FirestoreClient.getFirestore();
	   }

	   @Override
	   public void configureViewResolvers(ViewResolverRegistry registry) {
	      ThymeleafViewResolver resolver = new ThymeleafViewResolver();
	      resolver.setTemplateEngine(templateEngine());
	      registry.viewResolver(resolver);
	   }
	   @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/resource/**").addResourceLocations("WEB-INF/resources/");
	        registry.addResourceHandler("usuarios/resource/**").addResourceLocations("WEB-INF/resources/");
	        registry.addResourceHandler("sedes/resource/**").addResourceLocations("WEB-INF/resources/");
	        registry.addResourceHandler("salas/resource/**").addResourceLocations("WEB-INF/resources/");
	        registry.addResourceHandler("miSede/resource/**").addResourceLocations("WEB-INF/resources/");
	        registry.addResourceHandler("planes/resource/**").addResourceLocations("WEB-INF/resources/");
	        registry.addResourceHandler("clases/resource/**").addResourceLocations("WEB-INF/resources/");
	        registry.addResourceHandler("equipos/resource/**").addResourceLocations("WEB-INF/resources/");
	        registry.addResourceHandler("reportes/resource/**").addResourceLocations("WEB-INF/resources/");
	    }
}