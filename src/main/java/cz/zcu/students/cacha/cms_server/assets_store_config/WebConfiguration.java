package cz.zcu.students.cacha.cms_server.assets_store_config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    public static final String ARTICLES_FOLDER = "articles";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + ARTICLES_FOLDER + "/**")
                .addResourceLocations("file:" + ARTICLES_FOLDER + "/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }

    @Bean
    CommandLineRunner createUploadFolder() {
        return (args) -> {
            createNonExistingFolder(ARTICLES_FOLDER);
        };
    }

    private void createNonExistingFolder(String path) {
        File folder = new File(path);
        boolean folderExists = folder.exists() && folder.isDirectory();
        if(!folderExists) {
            folder.mkdir();
        }
    }
}
