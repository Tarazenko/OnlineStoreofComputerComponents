package com.nc.service;

import com.nc.model.Characteristic;

import java.util.List;
import java.util.Set;

public interface CharacteristicService {
    List<Characteristic> findAll();

    Characteristic findById(long id);

    List<Characteristic> findDistinctByCategoryId(long id);

    Set<List<Characteristic>> characteristicsCreate(List<Characteristic> characteristics);

    void save(Characteristic characteristic);

    void update(Characteristic characteristic);

    void delete(Characteristic characteristic);

    List<Characteristic> findAllByCategoryId(long id);
}
