package ehei.pfa.authGetway.database.repository;

import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.enums.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    void updateRole(@Param("id") String id, @Param("role") UserRole role);
}
