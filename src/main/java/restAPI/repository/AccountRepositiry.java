package restAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.model.Account;
import restAPI.model.Bookmark;

import java.util.Optional;

public interface AccountRepositiry extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername (String username);
}
