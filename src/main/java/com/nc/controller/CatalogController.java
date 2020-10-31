package com.nc.controller;

import com.nc.model.AjaxResponseBody;
import com.nc.model.Category;
import com.nc.model.Characteristic;
import com.nc.model.Hardware;
import com.nc.service.Impl.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class CatalogController {

    private final static Logger LOGGER = Logger.getLogger(CatalogController.class);
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    HardwareServiceImpl hardwareService;
    @Autowired
    CharacteristicServiceImpl characteristicService;
    @Autowired
    OrderServiceImpl orderService;

    private long catalog_id;

    @RequestMapping(value = "/catalog", method = RequestMethod.GET)
    public String oneCategory(@RequestParam(name = "categoryId") long idCatalog, Model model) {
        LOGGER.info("Go to catalog page");
        Category category = null;
        catalog_id = idCatalog;
        List<Category> categories = categoryService.findAll();
        if (idCatalog != 0) {
            List<Characteristic> characteristics = characteristicService.findDistinctByCategoryId(idCatalog);
            model.addAttribute("characts", characteristicService.characteristicsCreate(characteristics));
        } else
            model.addAttribute("characts", null);

        if (idCatalog != 0)
            category = categoryService.findById(idCatalog);
        model.addAttribute("categories", categories);
        model.addAttribute("category", category);

        return "catalog";
    }

    @RequestMapping(value = "/catalog/good", method = RequestMethod.GET)
    public String product(@RequestParam(name = "hardwareId") long idHardware, Model model) {
        LOGGER.info("Go to product page");
        List<Category> categories = categoryService.findAll();
        Hardware hardware = hardwareService.findById(idHardware);
        model.addAttribute("categories", categories);
        model.addAttribute("hardware", hardware);
        return "good";
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','MANAGER')")
    @RequestMapping(value = "/catalog/add", method = RequestMethod.POST)
    public String createOrderForCatalog(@RequestParam(name = "hardwareId") long idHardware, @Param("count") int count, Model model) {
        boolean isOrderCreate = orderService.createOneOrderForAll(idHardware, count);
        if (!isOrderCreate) {
            model.addAttribute("message_end", "УПС! У нас нет столько товаров сколько Вы добавляете в корзину.");
            LOGGER.info("An error occurred while adding the item.");
            return "error";
        } else {
            LOGGER.info("Adding item to cart");
            return "redirect:/catalog?categoryId="+catalog_id;
        }

    }


    @PostMapping("/api/search")
    public ResponseEntity<?> getSearchResultViaAjax(@Valid @RequestBody String characteristic_id, Errors errors) {

        AjaxResponseBody result = new AjaxResponseBody();
        if (errors.hasErrors()) {
            result.setMsg(errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        characteristic_id = characteristic_id.replaceAll("[^0-9]+", " ");
        List<String> characteristic_ids = Arrays.asList(characteristic_id.trim().split(" "));
        if (characteristic_id.matches(".*[0-9].*")) {
            List<Characteristic> characteristics = new ArrayList<>();

            for (int i = 0; i < characteristic_ids.size(); i++) {
                Long id = Long.parseLong(characteristic_ids.get(i));
                characteristics.add(characteristicService.findById(id));
            }
            List<Hardware> hardwares = new ArrayList<>();
            if (catalog_id != 0)
                hardwares = hardwareService.findByCharacteristicsAndCategoryId(characteristics, catalog_id);

            if (hardwares.isEmpty()) {
                result.setMsg("no user found!");
            } else {
                result.setMsg("success");
            }
            Set<Hardware> hardwareSet = new LinkedHashSet<>(hardwareService.checkOnCharacteristicHardware(hardwares, characteristics));
            result.setResult(hardwareSet);
        } else {
            List<Hardware> hardwares;
            if (catalog_id == 0)
                hardwares = hardwareService.findAllWhereCountNotNull();
            else
                hardwares = hardwareService.findByCategory(catalog_id);
            result.setMsg("success");
            Set<Hardware> hardwareSet = new LinkedHashSet<>(hardwares);
            result.setResult(hardwareSet);

        }
        result.setCurrency(result.getResult().stream().map(hardware
                -> (double) Math.round((hardware.getPriceHardware() / CurrencySingleton.getDollarCurrency()) * 100) / 100)
                .collect(Collectors.toList()));
        return ResponseEntity.ok(result);

    }
}
