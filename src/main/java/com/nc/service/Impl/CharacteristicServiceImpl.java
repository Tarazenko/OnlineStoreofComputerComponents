package com.nc.service.Impl;

import com.nc.model.Characteristic;
import com.nc.model.Hardware;
import com.nc.repository.CharacteristicRepository;
import com.nc.service.CharacteristicService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CharacteristicServiceImpl implements CharacteristicService {

    private final static Logger LOGGER = Logger.getLogger(CharacteristicServiceImpl.class);
    @Autowired
    CharacteristicRepository dao;
    @Autowired
    HardwareServiceImpl hardwareService;

    @Override
    public List<Characteristic> findAll() {
        LOGGER.info("Taking data from the database (All characteristics)");
        return dao.findAll();
    }

    @Override
    public Characteristic findById(long id) {
        LOGGER.info("Taking data from a database (Characteristic by ID)");
        return dao.findById(id);
    }

    @Override
    public List<Characteristic> findDistinctByCategoryId(long id) {
        LOGGER.info("Taking data from the database (Characteristics by category ID, non-repeating)");
        return dao.findDistinctByCategory_Id(id);
    }

    @Override
    public Set<List<Characteristic>> characteristicsCreate(List<Characteristic> characteristics) {
        Set<List<Characteristic>> characts = new LinkedHashSet<>();
        for (int i = 0; i < characteristics.size(); i++) {
            String nameChar = characteristics.get(i).getNameCharacteristic();
            characts.add(characteristics.stream().filter(item -> item.getNameCharacteristic()
                    .equals(nameChar)).collect(Collectors.toList()));
        }
        return characts;
    }

    @Override
    public void save(Characteristic characteristic) {
        LOGGER.info("Writing characteristic to the database\n" + characteristic.toString());
        dao.save(characteristic);
    }

    @Override
    public void update(Characteristic characteristic) {
        Characteristic oldCharacteristic = dao.findById(characteristic.getId());
        if (characteristic.getCategory().getId() == -1) {
            characteristic.setCategory(oldCharacteristic.getCategory());
            dao.save(characteristic);
        } else {
            dao.save(characteristic);
        }
        LOGGER.info("Updating characteristics data in the database\n" + characteristic.toString());
    }

    @Override
    public void delete(Characteristic characteristic) {
        List<Hardware> hardwares = hardwareService.findHardwaresByCharacteristicsIn(characteristic);
        hardwares.forEach(hardware -> hardware.getCharacteristics().removeIf(hardwareCharacteristic
                -> hardwareCharacteristic.equals(characteristic)));
        hardwareService.saveAll(hardwares);
        LOGGER.info("Delete characteristic data from the database\n" + characteristic.toString());
        dao.delete(characteristic);
    }

    @Override
    public List<Characteristic> findAllByCategoryId(long id) {
        LOGGER.info("Taking data from the database (Characteristics by category ID)");
        return dao.findAllByCategory_Id(id);
    }
}
