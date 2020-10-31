package com.nc.repository;

import com.nc.model.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
    Characteristic findById(long id);

    List<Characteristic> findDistinctByCategory_Id(long id);

    List<Characteristic> findAllByCategory_Id(long id);
}
