package com.nc.repository;

import com.nc.enums.Status;
import com.nc.model.Hardware;
import com.nc.model.Order;
import com.nc.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPerson_Id(long id);

    Order findById(long id);

    List<Order> findByPerson(Person person);

    List<Order> findByPersonAndStatus(Person person, Status status);

    Order findByHardwareAndPersonAndStatus(Hardware hardware, Person person, Status status);

    void deleteAllByPersonId(long id);
}
