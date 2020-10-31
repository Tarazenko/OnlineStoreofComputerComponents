package com.nc.controller;

import com.nc.model.Category;
import com.nc.model.Person;
import com.nc.service.Impl.CategoryServiceImpl;
import com.nc.service.Impl.PersonServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','USER','MANAGER')")
public class ProfileController {
    private final static Logger LOGGER = Logger.getLogger(ProfileController.class);
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    PersonServiceImpl personService;


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model) {
        LOGGER.info("Go to profile page.");
        List<Category> categories = categoryService.findAll();
        Person person = personService.findAuthenticationPerson();
        model.addAttribute("categories", categories);
        model.addAttribute("person", person);
        model.addAttribute("personForm", new Person());
        return "profile";
    }

    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    public String updatePerson(@ModelAttribute("personForm") @Valid Person person, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            LOGGER.info("Errors occurred in input fields when updating user\n" + errorMap);
            model.mergeAttributes(errorMap);
            model.addAttribute("person", person);
            return "redirect:/profile";
        } else {
            LOGGER.info("Update user.");
            personService.update(person,  null, null);
            return "redirect:/profile";
        }
    }

    @RequestMapping(value = "/profile/update-password", method = RequestMethod.POST)
    public String updatePerson(@Param("newPassword") @Valid String newPassword,
                               @Param("confPassword") @Valid String confPassword, Model model) {
        if (newPassword != null && newPassword.length() <= 255
                && confPassword != null && confPassword.length() <= 255) {
            Person person = personService.findAuthenticationPerson();
            boolean isValid = personService.update(person, confPassword, newPassword);
            if (!isValid) {
                LOGGER.info("Errors occurred in input fields when updating user.");
                model.addAttribute("incorrect_password", "Не получилось изменить.");
                return "error";
            }
            LOGGER.info("Update user.");
            return "redirect:/profile";
        }
        LOGGER.info("Errors occurred in input fields when updating user");
        model.addAttribute("incorrect_password", "Не корректно введены данные для изменения пароля.");
        return "error";
    }
}
