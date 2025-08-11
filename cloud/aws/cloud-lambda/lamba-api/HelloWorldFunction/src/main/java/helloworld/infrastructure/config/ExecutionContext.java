package helloworld.infrastructure.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.slf4j.Slf4jLogger;
import helloworld.infrastructure.clients.ImagePullApi;
import helloworld.infrastructure.clients.ImagePullService;
import helloworld.infrastructure.s3.ImageS3Service;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.org.webcompere.lightweightconfig.ConfigLoader;

@Data
public class ExecutionContext {
    private final static Logger LOGGER = LogManager.getLogger(ExecutionContext.class);
    private Config config;
    private ImageS3Service imageS3Service;
    private ImagePullService imagePullService;

    public ExecutionContext(){
        try {
            Injector injector = Guice.createInjector(new ServicesModule());
            this.imagePullService = injector.getInstance(ImagePullService.class);
            this.imageS3Service = injector.getInstance(ImageS3Service.class);
        } catch (Exception e) {
            LOGGER.error("Failed start up");
        }
        config = ConfigLoader.loadYmlConfigFromResource("configuration.yml", Config.class);
        imageS3Service = new ImageS3Service();

    }
}
