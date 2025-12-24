package ehei.pfa.authGetway.database.repository;

import ehei.pfa.authGetway.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);
}
