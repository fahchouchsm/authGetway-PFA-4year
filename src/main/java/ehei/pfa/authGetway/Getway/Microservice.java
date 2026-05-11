package ehei.pfa.authGetway.Getway;

import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Getter
public class Microservice {
    private String id;
    private String ipAddress;
    private String path;

    public Microservice() {}

    public Microservice(String id, String ipAddress, String path) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.path = path;
    }

    public static List<Microservice> getMicroservices() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("microservices.json");
            JsonNode root = objectMapper.readTree(resource.getInputStream());
            return Arrays.asList(objectMapper.treeToValue(root.get("microservices"), Microservice[].class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load microservices.json", e);
        }
    }
}