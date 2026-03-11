package ehei.pfa.authGetway.DTO.res;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(message, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }
}