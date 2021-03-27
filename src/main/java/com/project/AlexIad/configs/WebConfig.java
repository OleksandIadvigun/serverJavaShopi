package com.project.AlexIad.configs;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String home = System.getProperty("user.home");
        String pathImage = "File:" + home + File.separator + "imagesFromServer" + File.separator;
        String path = new File("BOOT-INF/classes/static/download")
                .getAbsolutePath();
        String staticPath = "File://"+ path+ File.separator;
        System.out.println(staticPath + "   it s static path!!!!");

        registry
                .addResourceHandler("/image/**")
                .addResourceLocations(pathImage);
        registry
                .addResourceHandler("/download/**")
                .addResourceLocations(staticPath);
    }
}
