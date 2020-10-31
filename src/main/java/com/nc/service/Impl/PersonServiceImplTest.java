package com.nc.service.Impl;

import com.nc.config.SpringSecurityConfig;
import com.nc.enums.Role;
import com.nc.model.Person;
import com.nc.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SpringSecurityConfig springSecurityConfig;

    @Mock
    private MailSender mailSender;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;
    private Person admin;

    @Before
    public void init() {
        person = new Person();
        person.setActive(false);
        person.setId(1);
        person.setNamePerson("Ilya");
        person.setSurnamePerson("Tarasenko");
        person.setLoginPerson("ilysha");
        person.setRole(Role.USER);

        admin = new Person();
        admin.setActive(true);
        admin.setId(2);
        admin.setNamePerson("Ivan");
        admin.setSurnamePerson("Ivanich");
        admin.setLoginPerson("adminushka");
        admin.setRole(Role.ADMIN);
    }

    @Test
    public void addNewUserAddingAlreadyExistUser() {
        doReturn(person).when(personRepository).findByLoginPerson("ilysha");
        doReturn(passwordEncoder).when(springSecurityConfig).getPasswordEncoder();
        boolean expectResult = false;
        boolean actualResult = personService.addNewUser(person, bindingResult, model, "test");
        assertEquals(expectResult, actualResult);
    }

    @Test
    public void addNewUserAddingNewUser() {
        doReturn(null).when(personRepository).findByLoginPerson("ilysha");
        doReturn(passwordEncoder).when(springSecurityConfig).getPasswordEncoder();
        boolean expectResult = true;
        boolean actualResult = personService.addNewUser(person, bindingResult, model, "test");
        verify(personRepository).save(any());
        assertEquals(expectResult, actualResult);
    }

    @Test
    public void findPersonsForAdminTest() {
        doReturn(Arrays.asList(admin, person)).when(personRepository).findAll();
        List<Person> expectAdmins = Arrays.asList(admin);
        List<Person> actualAdmins = personService.findPersonsForAdmin();
        assertEquals(expectAdmins, actualAdmins);
    }

    @Test
    public void activateUserUserWithCodeExist() {
        doReturn(person).when(personRepository).findByActivationCode(any());
        boolean expectResult = true;
        boolean actualResult = personService.activateUser("123");
        verify(personRepository).save(any());
        assertEquals(person.isActive(), true);
        assertEquals(expectResult, actualResult);
    }

    @Test
    public void activateUserNoUserWithCode() {
        doReturn(null).when(personRepository).findByActivationCode(any());
        boolean expectResult = false;
        boolean actualResult = personService.activateUser("123");
        verify(personRepository, never()).save(any());
        assertEquals(person.isActive(), false);
        assertEquals(expectResult, actualResult);
    }
}