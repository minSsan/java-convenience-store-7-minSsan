package store;

import store.infrastructure.config.AppConfig;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        appConfig.controller().run();
    }
}
