package tacos.repositories;

import org.springframework.data.repository.CrudRepository;
import tacos.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    /*
    Spring Data JPA will automatically generate the implementation
    of this interface at runtime
     */
    User findByUsername(String username);
}
