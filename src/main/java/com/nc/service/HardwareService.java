package com.nc.service;

import com.nc.model.Characteristic;
import com.nc.model.Hardware;
import com.nc.model.HardwareForm;

import java.util.List;

public interface HardwareService {
    List<Hardware> findAllWhereCountNotNull();

    List<Hardware> findAll();

    Hardware findById(long id);

    List<Hardware> findByCategory(long id);

    void update(Hardware hardware);

    void save(Hardware hardware);

    void addNewHardware(HardwareForm hardwareForm);

    void editHardware(HardwareForm hardwareForm);

    void delete(long id);

    List<Hardware> findByCharacteristicsAndCategoryId(List<Characteristic> characteristics, long id);

    List<Hardware> checkOnCharacteristicHardware(List<Hardware> hardwares, List<Characteristic> characteristics);

    List<Hardware> findHardwaresByCharacteristicsIn(Characteristic characteristic);

    void saveAll(List<Hardware> hardwares);

}
