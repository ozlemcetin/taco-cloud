-- If there’s a file named schema.sql in the root of the application’s classpath, then
-- the SQL in that file will be executed against the database when the application starts.
-- Therefore, you should place the contents of listing 3.8 in your project as a file named
-- schema.sql in the src/main/resources folder.

-- You also need to preload the database with some ingredient data. Fortunately,
-- Spring Boot will also execute a file named data.sql from the root of the classpath when
-- the application starts. Therefore, you can load the database with ingredient data using
-- the insert statements in the next listing, placed in src/main/resources/data.sql.

create table if not exists Taco_Order (
    id identity,
    delivery_Name varchar(50) not null,
    delivery_Street varchar(50) not null,
    delivery_City varchar(50) not null,
    delivery_State varchar(2) not null,
    delivery_Zip varchar(10) not null,
    cc_number varchar(16) not null,
    cc_expiration varchar(5) not null,
    cc_cvv varchar(3) not null,
    placed_at timestamp not null
    );

create table if not exists Taco (
    id identity,
    name varchar(50) not null,
    taco_order bigint not null,
    taco_order_key bigint not null,
    created_at timestamp not null
    );

create table if not exists Ingredient (
    id varchar(4) not null,
    name varchar(25) not null,
    type varchar(10) not null,
    CONSTRAINT PK_Ingredient PRIMARY KEY (ID)
    );

create table if not exists Ingredient_Ref (
    ingredient varchar(4) not null,
    taco bigint not null,
    taco_key bigint not null
    );

alter table Taco
    add foreign key (taco_order) references Taco_Order(id);


alter table Ingredient_Ref
    add foreign key (ingredient) references Ingredient(id);

