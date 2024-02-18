#to create table 'country'.
CREATE table country(
    country_id SERIAL,
    name text not null,
    country_code text,
    population numeric,
--     create_timestamp timestamp null default current_timestamp
);

#to select all records from table 'country'.
select * from country;

#to drop the table 'country'.
drop table country;

#to delete all records from table 'country'.
delete from country;