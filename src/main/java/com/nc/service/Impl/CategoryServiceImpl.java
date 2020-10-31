package com.nc.service.Impl;

import com.nc.model.Category;
import com.nc.model.Characteristic;
import com.nc.model.Hardware;
import com.nc.repository.CategoryRepository;
import com.nc.service.CategoryService;
import com.nc.service.HardwareService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static Logger LOGGER = Logger.getLogger(CategoryServiceImpl.class);
    @Autowired
    CategoryRepository dao;
    @Autowired
    HardwareService hardwareService;
    @Autowired
    CharacteristicServiceImpl characteristicService;

    @Override
    public List<Category> findAll() {
        LOGGER.info("Taking data from a database (All categories)");
        return dao.findAll();
    }

    @Override
    public Category findById(long idCategory) {
        LOGGER.info("Taking data from the database (Category by ID)");
        return dao.findById(idCategory);
    }

    @Override
    public void save(Category category) {
        LOGGER.info("Writing category data to the database\n" + category.toString());
        dao.save(category);
    }

    @Override
    public void update(Category category) {
        LOGGER.info("Updating category data in the database\n" + category.toString());
        save(category);
    }

    @Override
    public void delete(long id) {
        List<Hardware> hardwares = hardwareService.findByCategory(id);
        hardwares.forEach(hardware -> hardware.setCategory(null));
        hardwareService.saveAll(hardwares);
        List<Characteristic> characteristics = characteristicService.findAllByCategoryId(id);
        characteristics.forEach(characteristic -> characteristic.setCategory(null));
        LOGGER.info("Delete category from the database");
        dao.deleteById(id);
    }
}
