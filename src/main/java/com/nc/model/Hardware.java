package com.nc.model;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Entity
@Table(name = "hardware")
public class Hardware {
    private final static Logger LOGGER = Logger.getLogger(Hardware.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hardware_id")
    private long id;
    @Column(name = "name")
    private String nameHardware;
    @Column(name = "description")
    private String descriptionHardware;
    @Column(name = "price")
    private double priceHardware;
    @Column(name = "count")
    private int countHardware;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "characteristic_id")
    private List<Characteristic> characteristics;
    @Lob
    @Column(name = "image", length = 10000000)
    private Blob imageHardware;

    public Hardware() {
    }

    public Hardware(String nameHardware, String descriptionHardware, double priceHardware, int countHardware, Category category, List<Characteristic> characteristics, Blob imageHardware) {
        this.nameHardware = nameHardware;
        this.descriptionHardware = descriptionHardware;
        this.priceHardware = priceHardware;
        this.countHardware = countHardware;
        this.category = category;
        this.characteristics = characteristics;
        this.imageHardware = imageHardware;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameHardware() {
        return nameHardware;
    }

    public void setName(String nameHardware) {
        this.nameHardware = nameHardware;
    }

    public String getDescriptionHardware() {
        return descriptionHardware;
    }

    public void setDescriptionHardware(String descriptionHardware) {
        this.descriptionHardware = descriptionHardware;
    }

    public double getPriceHardware() {
        return priceHardware;
    }

    public void setPriceHardware(double priceHardware) {
        this.priceHardware = priceHardware;
    }

    public int getCountHardware() {
        return countHardware;
    }

    public void setCountHardware(int countHardware) {
        this.countHardware = countHardware;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public byte[] getImageHardware() {
        byte[] bytes = new byte[0];
        try {
            if (imageHardware != null) {
                LOGGER.info("Convert images from Blob to byte.");
                bytes = imageHardware.getBytes(1, (int) imageHardware.length());
                imageHardware.free();
            }
        } catch (SQLException e) {
            LOGGER.error("Error converting image from blob to byte:\n" + ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }

        return bytes;
    }

    public String generateBase64Image() {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(this.getImageHardware());
    }

    public void setImageHardware(Blob imageHardware) {
        this.imageHardware = imageHardware;
    }

    @Override
    public String toString() {
        return "Hardware{" +
                "id=" + id +
                ", nameHardware='" + nameHardware + '\'' +
                ", descriptionHardware='" + descriptionHardware + '\'' +
                ", priceHardware=" + priceHardware +
                ", countHardware=" + countHardware +
                ", category=" + ((category == null)?"":category.getNameCategory()) +
                '}';
    }
}
