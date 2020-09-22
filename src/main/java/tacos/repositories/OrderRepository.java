package tacos.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import tacos.entities.Order;
import tacos.entities.User;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    /*
        The OrderBy portion specifies a property by which the results will be ordered
     */

    /*
        Pageable is Spring Data’s way of selecting some subset of the results by a page number and page size.
     */
    List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);

    /*
    When generating the repository implementation, Spring Data examines any methods
    in the repository interface, parses the method name, and attempts to understand the
    method’s purpose in the context of the persisted object (an Order, in this case).

    In essence, Spring Data defines a sort of miniature domain-specific language (DSL)
    where persistence details are expressed in repository method signatures

    Spring Data knows that this method is intended to find Orders, because you’ve
    parameterized CrudRepository with Order.

    The method name, findByDeliveryZip(), makes it clear that this method should
    find all Order entities by matching their deliveryZip property
    with the value passed in as a parameter to the method

    Repository methods are composed of
        a verb,
        an optional subject, the word By,
        and a predicate
     */
    List<Order> findByDeliveryZip(String deliveryZip);

    /*
        Spring Data understand read, get, find.
        Orders, the subject of the method is optional
        By, signifies the start of properties to match
        DeliveryZip And PlacedAt
        Between, the value fall between the given values
    */
    List<Order> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);

    /*
    IsAfter, After, IsGreaterThan, GreaterThan
    IsGreaterThanEqual, GreaterThanEqual
    IsBefore, Before, IsLessThan, LessThan
    IsLessThanEqual, LessThanEqual
    IsBetween, Between
    IsNull, Null
    IsNotNull, NotNull
    IsIn, In
    IsNotIn, NotIn
    IsStartingWith, StartingWith, StartsWith
    IsEndingWith, EndingWith, EndsWith
    IsContaining, Containing, Contains
    IsLike, Like
    IsNotLike, NotLike
    IsTrue, True
    IsFalse, False
    Is, Equals
    IsNot, Not
    IgnoringCase, IgnoresCase
     */

    /*
    As alternatives for IgnoringCase and IgnoresCase, you can place either
    AllIgnoringCase or AllIgnoresCase
    on the method to ignore case for all String comparisons
     */

    List<Order> findByDeliveryStreetAndDeliveryCityAllIgnoringCase(String deliveryStreet, String deliveryCity);

    /*
    Finally, you can also place OrderBy at the end of the method name to sort the results
    by a specified column.
     */
    List<Order> findByDeliveryCityAllIgnoringCaseOrderByDeliveryStreet(String deliveryCity);

    /*
    To name the method anything you want and
    annotate it with @Query to explicitly specify the query to be performed when the
    method is called,
     */

    //@Query("Taco_Order o where o.deliveryCity='Seattle'")
    //List<Order> readOrdersDeliveredInSeattle();

}
