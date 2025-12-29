package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.run(args);
        logger.info("Application started successfully");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        String url = "http://localhost:8080";
        System.out.println("Application started. Opening browser: " + url);

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            }
            else {
                String os = System.getProperty("os.name").toLowerCase();
                Runtime rt = Runtime.getRuntime();

                if (os.contains("win")) {
                    // Windows
                    rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (os.contains("mac")) {
                    // Mac
                    rt.exec("open " + url);
                } else if (os.contains("nix") || os.contains("nux")) {
                    // Linux
                    String[] browsers = {"xdg-open", "google-chrome", "firefox"};
                    for (String browser : browsers) {
                        try {
                            rt.exec(new String[]{browser, url});
                            break;
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            logger.error("Unable to open browser. Please open manually: {}", url);
                        }
                    }
                }
            }
        }
        catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }
}
