package com.nc.model;

import com.nc.annotation.CheckOnEmail;
import com.nc.annotation.CheckOnHtml;
import com.nc.annotation.CheckOnNumber;
import com.nc.enums.Role;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    @DecimalMin(value = "0.00")
    private long id;
    @Column(name = "name")
    @NotBlank(message = "Заполните поле")
    @CheckOnNumber(message = "Уберите цифры из имени")
    @Length(max = 10, message = "Слишком длинное значение")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    private String namePerson;
    @Column(name = "surname")
    @NotBlank(message = "Заполните поле")
    @Length(max = 40, message = "Слишком длинное значение")
    @CheckOnNumber(message = "Уберите цифры из фамилии")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    private String surnamePerson;
    @Column(name = "mail")
    @NotBlank(message = "Ошибка")
    @Email(message = "Неверный формат почты")
    @CheckOnEmail(message = "Неверный формат почты")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    private String mailPerson;
    @Column(name = "login")
    @NotBlank(message = "Заполните поле")
    @Length(max = 40, message = "Слишком длинное значение")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    private String loginPerson;
    @Column(name = "password")
    @NotBlank(message = "Заполните поле")
    @Length(max = 255, message = "Слишком длинное значение")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    private String passwordPerson;
    private boolean active;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "activation_code")
    private String activationCode;

    public Person() {
    }

    public Person(@NotBlank(message = "Заполните поле") @Length(max = 255, message = "Слишком длинное значение") String namePerson, @NotBlank(message = "Заполните поле") @Length(max = 255, message = "Слишком длинное значение") String surnamePerson, @NotBlank(message = "Ошибка") String mailPerson, @NotBlank(message = "Заполните поле") @Length(max = 255, message = "Слишком длинное значение") String loginPerson, @NotBlank(message = "Заполните поле") @Length(max = 255, message = "Слишком длинное значение") String passwordPerson, boolean active, Role role) {
        this.namePerson = namePerson;
        this.surnamePerson = surnamePerson;
        this.mailPerson = mailPerson;
        this.loginPerson = loginPerson;
        this.passwordPerson = passwordPerson;
        this.active = active;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }

    public String getSurnamePerson() {
        return surnamePerson;
    }

    public void setSurnamePerson(String surnamePerson) {
        this.surnamePerson = surnamePerson;
    }

    public String getMailPerson() {
        return mailPerson;
    }

    public void setMailPerson(String mailPerson) {
        this.mailPerson = mailPerson;
    }

    public String getLoginPerson() {
        return loginPerson;
    }

    public void setLoginPerson(String loginPerson) {
        this.loginPerson = loginPerson;
    }

    public String getPasswordPerson() {
        return passwordPerson;
    }

    public void setPasswordPerson(String passwordPerson) {
        this.passwordPerson = passwordPerson;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = new LinkedHashSet<>();
        roles.add(getRole());
        return roles;
    }

    @Override
    public String getPassword() {
        return passwordPerson;
    }

    @Override
    public String getUsername() {
        return loginPerson;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", namePerson='" + namePerson + '\'' +
                ", surnamePerson='" + surnamePerson + '\'' +
                ", mailPerson='" + mailPerson + '\'' +
                ", loginPerson='" + loginPerson + '\'' +
                ", passwordPerson='" + passwordPerson + '\'' +
                ", active=" + active +
                ", role=" + role.toString() +
                '}';
    }
}
