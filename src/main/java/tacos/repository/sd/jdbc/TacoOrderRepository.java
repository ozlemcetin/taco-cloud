package tacos.repository.sd.jdbc;

import org.springframework.data.repository.CrudRepository;
import tacos.model.TacoOrder;

public interface TacoOrderRepository
        extends CrudRepository<TacoOrder, Long> {

}
