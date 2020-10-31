package com.nc.service.Impl;

import com.nc.model.Characteristic;
import com.nc.model.Hardware;
import com.nc.model.HardwareForm;
import com.nc.repository.HardwareRepository;
import com.nc.service.HardwareService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.List;

@Service
public class HardwareServiceImpl implements HardwareService {
    private final static Logger LOGGER = Logger.getLogger(HardwareServiceImpl.class);
    @Autowired
    HardwareRepository dao;

    @Override
    public List<Hardware> findAllWhereCountNotNull() {
        LOGGER.info("Taking data from the database (All components for which CountHardware! = 0)");
        return deleteHardwareWhereCountEqualsNull(dao.findAll());
    }

    @Override
    public List<Hardware> findAll() {
        LOGGER.info("Taking data from the database (All components)");
        return dao.findAll();
    }

    @Override
    public Hardware findById(long id) {
        Hardware hardware = dao.findById(id);
        LOGGER.info("Taking data from the database (Component by ID)\n" + hardware.toString());
        return dao.findById(id);
    }

    @Override
    public List<Hardware> findByCategory(long id) {
        LOGGER.info("Taking data from the database (Accessories by category ID)");
        return deleteHardwareWhereCountEqualsNull(dao.findByCategory_Id(id));
    }

    @Override
    public void update(Hardware hardware) {
        LOGGER.info("Updating component data in the database\n" + hardware.toString());
        save(hardware);
    }

    @Override
    public void save(Hardware hardware) {
        LOGGER.info("Writing component data to the database\n" + hardware.toString());
        dao.save(hardware);
    }

    @Override
    public void addNewHardware(HardwareForm hardwareForm) {
        Blob blob = null;
        try {
            blob = new SerialBlob(hardwareForm.getImageHardware().getBytes());
        } catch (Exception e) {
            LOGGER.error("An error occurred in " + hardwareForm.getNameHardware() + " when converting images from byte to blob :\n" + ExceptionUtils.getStackTrace(e));
        }
        Hardware hardware = new Hardware();
        hardware.setName(hardwareForm.getNameHardware());
        hardware.setCategory(hardwareForm.getCategory());
        hardware.setDescriptionHardware(hardwareForm.getDescriptionHardware());
        hardware.setCharacteristics(hardwareForm.getCharacteristics());
        hardware.setPriceHardware(Double.parseDouble(hardwareForm.getPriceHardware()));
        hardware.setCountHardware(hardwareForm.getCountHardware());
        hardware.setImageHardware(blob);
        save(hardware);
    }

    @Override
    public void editHardware(HardwareForm hardwareForm) {
        Hardware oldHardware = dao.findById(hardwareForm.getId());
        Hardware newHardware = new Hardware();
        newHardware.setId(hardwareForm.getId());
        try {
            if (hardwareForm.getImageHardware().getSize() != 0)
                newHardware.setImageHardware(new SerialBlob(hardwareForm.getImageHardware().getBytes()));
            else
                newHardware.setImageHardware(new SerialBlob(oldHardware.getImageHardware()));

        } catch (Exception e) {
            LOGGER.error("An error occurred in " + hardwareForm.getNameHardware() + " when converting images from byte to blob :\n" + ExceptionUtils.getStackTrace(e));
        }
        newHardware.setName(hardwareForm.getNameHardware());
        newHardware.setDescriptionHardware(hardwareForm.getDescriptionHardware());
        if (hardwareForm.getCategory().getId() == -1)
            newHardware.setCategory(oldHardware.getCategory());
        else
            newHardware.setCategory(hardwareForm.getCategory());

        if (hardwareForm.getCharacteristics() == null)
            newHardware.setCharacteristics(oldHardware.getCharacteristics());
        else
            newHardware.setCharacteristics(hardwareForm.getCharacteristics());

        newHardware.setPriceHardware(Double.parseDouble(hardwareForm.getPriceHardware()));
        newHardware.setCountHardware(hardwareForm.getCountHardware());
        save(newHardware);
    }

    @Override
    public void delete(long id) {
        Hardware hardware = dao.findById(id);
        hardware.setCharacteristics(null);
        dao.save(hardware);
        LOGGER.info("Removing component data from the database\n" + hardware.toString());
        dao.deleteById(id);
    }

    @Override
    public List<Hardware> findByCharacteristicsAndCategoryId(List<Characteristic> characteristics, long id) {
        LOGGER.info("Taking data from the database (Accessories by category ID and characteristics)");
        return dao.findByCharacteristicsInAndCategory_Id(characteristics, id);
    }

    @Override
    public List<Hardware> checkOnCharacteristicHardware(List<Hardware> hardwares, List<Characteristic> characteristics) {
        LOGGER.info("Checking and removing hardwares if the number of characteristics of components does not match the number requested.");
        hardwares.removeIf(hardware -> hardware.getCharacteristics().size() < characteristics.size());
        return hardwares;
    }

    @Override
    public List<Hardware> findHardwaresByCharacteristicsIn(Characteristic characteristic) {
        LOGGER.info("Taking data from the database (Accessories by characteristics)");
        return dao.findAllByCharacteristics(characteristic);
    }

    @Override
    public void saveAll(List<Hardware> hardwares) {
        LOGGER.info("Writing component data in the database");
        dao.saveAll(hardwares);
    }

    private List<Hardware> deleteHardwareWhereCountEqualsNull(List<Hardware> hardwares) {
        hardwares.removeIf(hardware -> hardware.getCountHardware() == 0);
        return hardwares;
    }


}
