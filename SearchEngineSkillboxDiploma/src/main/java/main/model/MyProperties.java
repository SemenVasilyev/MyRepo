package main.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Data
public class MyProperties {
    @Getter
    private static String host;
    @Getter
    private static String port;
    @Getter
    private static String bdName;
    @Getter
    private static String username;
    @Getter
    private static String password;
    @Getter
    private static String tablePrefix;
    @Getter
    private static List<SiteFromPropertys> siteFromPropertys;
    @Getter
    private static String userAgent;
    @Getter
    private static String webAccess;

    private MyProperties() {}

    static {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        PropertiesYAML propertiesYAML = null;
        try {
            propertiesYAML = mapper.readValue(new File("application.yaml"), PropertiesYAML.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        host = propertiesYAML.getHost();
        port = propertiesYAML.getPort() == null ? "3306" : propertiesYAML.getPort();
        bdName = propertiesYAML.getBdName();
        username = propertiesYAML.getUsername();
        password = propertiesYAML.getPassword();
        tablePrefix = propertiesYAML.getTablePrefix()== null ? "" :  propertiesYAML.getTablePrefix();
        siteFromPropertys = propertiesYAML.getSiteFromPropertys();
        userAgent = propertiesYAML.getUserAgent();
        webAccess = propertiesYAML.getWebAccess();

    }

    @Data
    private static class PropertiesYAML {
        private String host;
        private String port;
        private String bdName;
        private String username;
        private String password;
        private String tablePrefix;
        private List<SiteFromPropertys> siteFromPropertys;
        private String userAgent;
        private String webAccess;
    }
}