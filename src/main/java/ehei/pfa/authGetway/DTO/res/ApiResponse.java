package ehei.pfa.authGetway.DTO.res;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponse<T>{
    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(String status, String message) {
        this(status, message, null);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>("success", message);
    }

    public static <T> ApiResponse<T> success(String message,T data) {
        return new ApiResponse<>("success", message,data);
    }
}
