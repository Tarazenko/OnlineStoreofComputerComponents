package com.nc.repository;

import com.nc.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByLoginPerson(String name);

    Person findById(long id);

    Person findByActivationCode(String code);
}
