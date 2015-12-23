package restAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.model.Bookmark;

import java.util.Collection;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{
    Collection<Bookmark> findByAccountUsername(String username);
}
