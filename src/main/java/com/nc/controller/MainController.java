package com.nc.controller;

import com.nc.model.Category;
import com.nc.model.Hardware;
import com.nc.service.Impl.CategoryServiceImpl;
import com.nc.service.Impl.CurrencySingleton;
import com.nc.service.Impl.HardwareServiceImpl;
import com.nc.service.Impl.OrderServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller

public class MainController {
    private final static Logger LOGGER = Logger.getLogger(MainController.class);
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    HardwareServiceImpl hardwareService;
    @Autowired
    OrderServiceImpl orderService;

    @RequestMapping("/")
    public String home(Model model) {
        LOGGER.info("Go to the home page.");
        List<Hardware> hardwares = hardwareService.findAllWhereCountNotNull();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("hardwares", hardwares);
        model.addAttribute("currency", CurrencySingleton.getDollarCurrency());
        return "home";
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','MANAGER')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String createOneOrder(@RequestParam(name = "hardwareId") long idHardware, @Param("count") int count, Model model) {

        boolean isOrderCreate = orderService.createOneOrderForAll(idHardware, count);
        if (!isOrderCreate) {
            LOGGER.info("An error occurred while adding the item to the cart.");
            model.addAttribute("message_end", "УПС! У нас нет столько товаров сколько Вы добавляете в корзину.");
            return "error";
        } else {
            LOGGER.info("Adding item to cart.");
            return "redirect:/";
        }
    }

    @RequestMapping("/login")
    public String login(Model model) {
        LOGGER.info("Go to the login page.");
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "login";
    }
}
