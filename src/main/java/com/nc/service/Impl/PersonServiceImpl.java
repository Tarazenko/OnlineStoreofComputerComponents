package com.nc.service.Impl;

import com.nc.config.SpringSecurityConfig;
import com.nc.enums.Role;
import com.nc.model.Order;
import com.nc.model.Person;
import com.nc.repository.PersonRepository;
import com.nc.service.PersonService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

@Service
public class PersonServiceImpl implements PersonService {
    private final static Logger LOGGER = Logger.getLogger(PersonServiceImpl.class);
    @Autowired
    PersonRepository dao;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    SpringSecurityConfig springSecurityConfig;
    @Autowired
    MailSender mailSender;

    @Override
    public List<Person> findAll() {
        LOGGER.info("Taking data from the database (All users)");
        return dao.findAll();
    }

    @Override
    public Person findByLogin(String login) {
        LOGGER.info("Taking data from a database (User by a specific login)");
        return dao.findByLoginPerson(login);
    }

    @Override
    public void save(Person person) {
        LOGGER.info("Writing user data to the database");
        dao.save(person);
    }

    @Override
    public boolean update(Person person, String confPassword, String newPassword) {
        Person oldPerson = dao.findById(person.getId());
        if (confPassword != null) {
            if (!newPassword.equals(confPassword))
                return false;
        }
        if (newPassword == null)
            person.setPasswordPerson(oldPerson.getPassword());
        else {
            PasswordEncoder passwordEncoder = springSecurityConfig.getPasswordEncoder();
            String correctLine = escapeHtml4(newPassword);
            if (correctLine.equals(newPassword))
                person.setPasswordPerson(passwordEncoder.encode(newPassword));
            else
                return false;
        }
        person.setActive(oldPerson.isActive());
        person.setRole(oldPerson.getRole());
        LOGGER.info("Updating user data in the database\n" + person.toString());
        dao.save(person);
        LOGGER.info("User reauthorization on the site");
        Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext().getAuthentication().getAuthorities();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(person.getLoginPerson(), person.getPassword(), nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    @Override
    public Person findAuthenticationPerson() {
        LOGGER.info("Finding an authorized user.");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return dao.findByLoginPerson(auth.getName());
    }

    @Override
    public List<Person> findPersonsForAdmin() {
        List<Person> people = dao.findAll().stream()
                .filter(person -> person.getRole().equals(Role.ADMIN))
                .collect(Collectors.toList());
        LOGGER.info("Taking data from the database (All users except administrators).");
        return people;
    }

    @Override
    public Person findById(long id) {
        LOGGER.info("Taking data from the database (User by ID).");
        return dao.findById(id);
    }

    @Override
    public void delete(long id) {
        List<Order> orders = orderService.findByPerson_Id(id);
        orders.forEach(order -> order.setHardware(null));
        LOGGER.info("Removing user data from the database.");
        if (orders.size() != 0)
            orderService.deleteAllByPersonId(id);
        else
            dao.deleteById(id);
    }

    @Override
    public boolean addNewUser(Person person, BindingResult bindingResult, Model model, String urlAddress) {
        Person userFromDb = findByLogin(person.getLoginPerson());
        PasswordEncoder passwordEncoder = springSecurityConfig.getPasswordEncoder();
        boolean result = true;
        if (userFromDb != null) {
            model.addAttribute("exist_error", "Пользователь существует.");
            result = false;
        } else if (bindingResult.hasErrors()) {
            result = false;

        } else {
            person.setPasswordPerson(passwordEncoder.encode(person.getPassword()));
            person.setRole(Role.USER);
            person.setActive(false);
            person.setActivationCode(UUID.randomUUID().toString());

            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to localhost. Please, visit next link: http://localhost:8080/activate/%s",
                    person.getLoginPerson(),
                    person.getActivationCode()
            );
            mailSender.send(person.getMailPerson(), "Activation code", message);
            LOGGER.info("Writing user data to the database.\n" + person.toString());
            dao.save(person);
        }
        return result;
    }

    @Override
    public boolean activateUser(String code) {
        Person person = dao.findByActivationCode(code);

        if (person == null) {
            return false;
        }
        person.setActive(true);
        LOGGER.info("Updating user data in the database (Activation).\n" + person.toString());
        dao.save(person);

        return true;
    }
}
