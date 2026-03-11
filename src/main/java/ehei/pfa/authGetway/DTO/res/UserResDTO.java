package ehei.pfa.authGetway.DTO.res;

import ehei.pfa.authGetway.enums.UserRole;

public class UserResDTO {
    private String id;

    private String name;

    private String lastName;

    private String email;

    private boolean emailVerified;

    private UserRole role;

    private String website;

    public UserResDTO(String id, String name, String lastName, String email, boolean emailVerified, UserRole role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.role = role;
    }

    public UserResDTO(String id, String name, String lastName, String email, boolean emailVerified, UserRole role, String website) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.role = role;
        this.website = website;
    }
}
