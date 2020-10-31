package com.nc.repository;

import com.nc.model.Characteristic;
import com.nc.model.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {
    Hardware findById(long id);

    List<Hardware> findByCategory_Id(long id);

    List<Hardware> findByCharacteristicsInAndCategory_Id(List<Characteristic> characteristics, long id);

    List<Hardware> findAllByCharacteristics(Characteristic characteristic);

}
