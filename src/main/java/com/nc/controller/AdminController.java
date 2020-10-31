package com.nc.controller;

import com.nc.model.*;
import com.nc.service.Impl.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public class AdminController {
    private final static Logger LOGGER = Logger.getLogger(AdminController.class);
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    HardwareServiceImpl hardwareService;
    @Autowired
    PersonServiceImpl personService;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    CharacteristicServiceImpl characteristicService;

    @RequestMapping("/admin")
    public String admin(Model model) {
        LOGGER.info("Go to admin panel.");
        List<Category> categories = categoryService.findAll();
        List<Person> people = personService.findPersonsForAdmin();
        List<Hardware> hardwares = hardwareService.findAll();
        List<Order> orders = orderService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("characts", characteristicService.findAll());
        model.addAttribute("hardwareForm", new HardwareForm());
        model.addAttribute("categoryForm", new Category());
        model.addAttribute("characteristicForm", new Characteristic());
        model.addAttribute("people", people);
        model.addAttribute("hardwares", hardwares);
        model.addAttribute("orders", orders);

        return "admin";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String updatePerson(@RequestParam(name = "personId") long idPerson, Model model) {
        LOGGER.info("Go to the user data update page");
        List<Category> categories = categoryService.findAll();

        model.addAttribute("categories", categories);
        Person person = personService.findById(idPerson);
        model.addAttribute("personForm", new Person());
        model.addAttribute("person", person);
        return "registration";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updatePerson(@ModelAttribute("personForm") @Valid Person person, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            LOGGER.info("Errors occurred in input fields when updating user\n" + errorMap);
            model.addAttribute("person", person);
            return "registration";

        } else {
            Person oldPerson = personService.findById(person.getId());
            person.setPasswordPerson(oldPerson.getPassword());
            person.setActive(oldPerson.isActive());
            LOGGER.info("Update user.");
            personService.save(person);
            return "redirect:/admin";
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/delete-person", method = RequestMethod.POST)
    public String deletePerson(@RequestParam(name = "personId") long idPerson) {
        LOGGER.info("Delete user");
        personService.delete(idPerson);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/edit-hardware", method = RequestMethod.GET)
    public String editHardware(@RequestParam(name = "hardwareId") long idHardware, Model model) {
        LOGGER.info("Go to the component data update page");
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        Hardware hardware = hardwareService.findById(idHardware);
        model.addAttribute("hardwareForm", new HardwareForm());
        model.addAttribute("characts", characteristicService.findAll());
        model.addAttribute("hardware", hardware);
        return "edit_hardware";
    }

    @RequestMapping(value = "/edit-hardware", method = RequestMethod.POST)
    public String editExistHardware(@ModelAttribute("hardwareForm") @Valid HardwareForm hardwareForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);

            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            LOGGER.info("Errors occurred in input fields when updating component\n" + errorMap);
            model.mergeAttributes(errorMap);
            model.addAttribute("hardware", hardwareForm);
            return "edit_hardware";

        } else {
            LOGGER.info("Update hardware");
            hardwareService.editHardware(hardwareForm);
            return "redirect:/admin";
        }

    }


    @RequestMapping(value = "/add-hardware", method = RequestMethod.POST)
    public String createNewHardware(@ModelAttribute("hardwareForm") @Valid HardwareForm hardwareForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);

            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            LOGGER.info("Errors occurred in input fields when adding component\n" + errorMap);
            model.mergeAttributes(errorMap);
            return "redirect:/admin";

        } else {
            LOGGER.info("Add new hardware");
            hardwareService.addNewHardware(hardwareForm);
            return "redirect:/admin";
        }
    }

    @RequestMapping(value = "/delete-hardware", method = RequestMethod.POST)
    public String deleteHardware(@RequestParam(name = "hardwareId") long idHardware) {
        LOGGER.info("Delete hardware");
        hardwareService.delete(idHardware);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/add-category", method = RequestMethod.POST)
    public String createNewCategory(@ModelAttribute("categoryForm") @Valid Category category, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            LOGGER.info("Errors occurred in input fields when adding category\n" + errorMap);
            model.mergeAttributes(errorMap);
            model.addAttribute("category", category);
            return "redirect:/admin";
        } else {
            LOGGER.info("Add new category");
            categoryService.save(category);
            return "redirect:/admin";
        }

    }

    @RequestMapping(value = "/edit-category", method = RequestMethod.GET)
    public String editCategory(@RequestParam(name = "categoryId") long idCategory, Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        LOGGER.info("Go to the category data update page");
        Category category = categoryService.findById(idCategory);
        model.addAttribute("category", category);
        model.addAttribute("categoryForm", new Category());
        return "edit_charact_or_category";
    }

    @RequestMapping(value = "/edit-category", method = RequestMethod.POST)
    public String editExistCategory(@ModelAttribute("categoryForm") @Valid Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);

            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            LOGGER.info("Errors occurred in input fields when updating category\n" + errorMap);
            model.mergeAttributes(errorMap);
            model.addAttribute("category", category);
            return "edit_charact_or_category";
        } else {
            LOGGER.info("Update category");
            categoryService.update(category);
            return "redirect:/admin";
        }

    }

    @RequestMapping(value = "/delete-category", method = RequestMethod.POST)
    public String deleteCategory(@RequestParam(name = "categoryId") long idCategory) {
        LOGGER.info("Delete category");
        categoryService.delete(idCategory);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/add-characteristic", method = RequestMethod.POST)
    public String addNewCharacteristic(@ModelAttribute("characteristicForm") @Valid Characteristic characteristic, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            LOGGER.info("Errors occurred in input fields when adding characteristic\n" + errorMap);
            model.addAttribute("characteristic", characteristic);
            return "redirect:/admin";
        } else {
            LOGGER.info("Add new characteristic");
            characteristicService.save(characteristic);
            return "redirect:/admin";
        }
    }

    @RequestMapping(value = "/edit-characteristic", method = RequestMethod.GET)
    public String editCharacteristic(@RequestParam(name = "characteristicId") long idCharacteristic, Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        LOGGER.info("Go to the characteristic data update page");
        Characteristic characteristic = characteristicService.findById(idCharacteristic);
        model.addAttribute("characteristic", characteristic);
        model.addAttribute("characteristicForm", new Characteristic());
        return "edit_charact_or_category";
    }

    @RequestMapping(value = "/edit-characteristic", method = RequestMethod.POST)
    public String editExistCharacteristic(@ModelAttribute("characteristicForm") @Valid Characteristic characteristic, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);

            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            LOGGER.info("Errors occurred in input fields when updating characteristic\n" + errorMap);
            model.mergeAttributes(errorMap);
            model.addAttribute("characteristic", characteristic);
            return "edit_charact_or_category";
        } else {
            LOGGER.info("Update characteristic");
            characteristicService.update(characteristic);
            return "redirect:/admin";
        }
    }

    @RequestMapping(value = "/delete-characteristic", method = RequestMethod.POST)
    public String deleteCharacteristic(@RequestParam(name = "characteristicId") long idCharacteristic) {
        Characteristic characteristic = characteristicService.findById(idCharacteristic);
        LOGGER.info("Delete characteristic");
        characteristicService.delete(characteristic);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/delete-order", method = RequestMethod.POST)
    public String deleteOrderAdmin(@RequestParam(name = "orderId") long idOrder, Model model) {
        LOGGER.info("Delete order");
        orderService.delete(idOrder);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/edit-order", method = RequestMethod.GET)
    public String editStatusOrder(@RequestParam(name = "orderId") long idOrder) {
        orderService.changeStatusOnDelivered(idOrder);
        LOGGER.info("Update status order");
        return "redirect:/admin";
    }
}
