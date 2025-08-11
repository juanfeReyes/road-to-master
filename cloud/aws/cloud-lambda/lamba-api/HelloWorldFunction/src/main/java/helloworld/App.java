package helloworld;

import java.io.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import helloworld.infrastructure.config.ExecutionContext;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestStreamHandler {

    ExecutionContext executionContext = new ExecutionContext();

    private String environmentName = System.getenv("ENV_NAME");
    public void handleRequest(InputStream inputStream,
                              OutputStream outputStream,
                              final Context context) {
        context.getLogger().log("Staring App :)\n");
        context.getLogger().log("Environment: "+environmentName+"\n");
        context.getLogger().log("Environment from config yml: "+ executionContext.getConfig().getEnvironmentName() +"\n");
        executionContext.getImageS3Service().execute();

    }


}
