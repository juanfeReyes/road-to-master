package helloworld.infrastructure.s3;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageS3Service {

    private static final Logger LOGGER = LogManager.getLogger(ImageS3Service.class);

    public void execute(){
        LOGGER.info("Staring image s3 service handling");
    }
}
