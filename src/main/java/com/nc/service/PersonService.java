package com.nc.service;


import com.nc.model.Person;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface PersonService {
    List<Person> findAll();

    Person findByLogin(String login);

    void save(Person person);

    boolean update(Person person, String confPassword, String newPassword);

    Person findAuthenticationPerson();

    List<Person> findPersonsForAdmin();

    Person findById(long id);

    void delete(long id);

    boolean addNewUser(Person person, BindingResult bindingResult, Model model, String urlAddress);

    public boolean activateUser(String code);
}
