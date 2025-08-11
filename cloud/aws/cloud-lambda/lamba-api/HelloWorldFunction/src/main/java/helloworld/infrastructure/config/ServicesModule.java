package helloworld.infrastructure.config;

import com.google.inject.AbstractModule;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import helloworld.infrastructure.clients.ImagePullApi;
import helloworld.infrastructure.s3.ImageS3Service;
import uk.org.webcompere.lightweightconfig.ConfigLoader;

public class ServicesModule extends AbstractModule {

    protected void configure() {
        Config config = ConfigLoader.loadYmlConfigFromResource("configuration.yml", Config.class);

        ImagePullApi imagePullApi = Feign.builder()
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger())
                .target(ImagePullApi.class, config.getImagePullEndpoint());

        bind(Config.class).toInstance(config);
        bind(ImagePullApi.class).toInstance(imagePullApi);
        bind(ImageS3Service.class).to(ImageS3Service.class);
    }
}
