package helloworld.infrastructure.clients;

import com.google.inject.Inject;
import helloworld.domain.ImageBody;

public class ImagePullService {

    private final ImagePullApi imagePullApi;

    @Inject
    public ImagePullService(ImagePullApi imagePullApi){
        this.imagePullApi = imagePullApi;
    }

    public ImageBody getImage (){
        return imagePullApi.getImage();
    }

}
