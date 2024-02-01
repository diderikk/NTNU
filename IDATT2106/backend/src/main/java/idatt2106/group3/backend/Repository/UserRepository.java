package idatt2106.group3.backend.Repository;

import idatt2106.group3.backend.Model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findUserByEmail(String email);
}
