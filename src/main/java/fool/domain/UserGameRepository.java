package fool.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserGameRepository extends CrudRepository<UserGame, Long>{
    List<UserGame> findAll();
    List<UserGame> findByStage(String stage);
}