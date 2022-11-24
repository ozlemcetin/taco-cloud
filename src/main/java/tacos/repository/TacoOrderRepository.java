package tacos.repository;

import tacos.model.TacoOrder;

public interface TacoOrderRepository {

    /*
        In our design, TacoOrder and Taco are part of an aggregate in which TacoOrder is
        the aggregate root. In other words, Taco objects don’t exist outside of the context of a
        TacoOrder. So, for now, we only need to define a repository to persist TacoOrder
        objects and, in turn, Taco objects along with them. Such a repository can be defined
        in a OrderRepository interface like this:
     */

    TacoOrder save(TacoOrder tacoOrder);
}
