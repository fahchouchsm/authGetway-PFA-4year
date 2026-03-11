package ehei.pfa.authGetway.controller;

import ehei.pfa.authGetway.DTO.RoleDTO;
import ehei.pfa.authGetway.DTO.res.ApiResponse;
import ehei.pfa.authGetway.DTO.res.UserResDTO;
import ehei.pfa.authGetway.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/user/role/{id}")
    public ResponseEntity<ApiResponse<UserResDTO>> changeRole(@PathVariable String id, @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(ApiResponse.success("User role updated.", adminService.changeRole(id, roleDTO.getUserRole())));
    }
}