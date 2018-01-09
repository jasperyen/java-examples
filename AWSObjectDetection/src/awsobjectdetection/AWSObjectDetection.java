package awsobjectdetection;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;

/**
 *
 * @author Jasper
 */
public class AWSObjectDetection {


    static private final AWSCredentials credentials;
    static private final AmazonRekognition rekognitionClient;
    
    static {
        
        credentials = new ProfileCredentialsProvider("default").getCredentials();
        
        rekognitionClient = AmazonRekognitionClientBuilder
                                .standard()
                                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                .build();
    }
    
    public static void main(String[] args) throws Exception {
    	String photo="Koala.jpg";
        
        ByteBuffer imageBytes;
        try (InputStream inputStream = new FileInputStream(new File(photo))) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        }
        
        Image image = new Image(). withBytes(imageBytes);
        
        DetectLabelsRequest request = new DetectLabelsRequest().withImage(image);

        System.out.println("Start detect . . . " + photo);
        
        DetectLabelsResult result = rekognitionClient.detectLabels(request);
        List<Label> labels = result.getLabels();

        System.out.println("Detected labels for " + photo);
        
        labels.forEach( label -> {
            System.out.println(label.getName() + ": " + label.getConfidence());
        });
    }
    
}
