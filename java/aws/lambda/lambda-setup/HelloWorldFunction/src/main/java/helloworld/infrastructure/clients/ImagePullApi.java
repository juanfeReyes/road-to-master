package helloworld.infrastructure.clients;

import feign.RequestLine;
import helloworld.domain.ImageBody;

public interface ImagePullApi {

    @RequestLine("GET /image")
    ImageBody getImage();
}
