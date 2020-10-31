package com.nc.model;

import com.nc.annotation.CheckOnHtml;
import com.nc.annotation.CheckPrice;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class HardwareForm {

    @DecimalMin(value = "0.00")
    private long id;

    @NotBlank(message = "Заполните поле")
    @Length(max = 255, message = "Слишком длинное значение")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    private String nameHardware;

    @Length(max = 255, message = "Слишком длинное значение")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    private String descriptionHardware;

    @NotBlank(message = "Заполните поле")
    @CheckPrice(message = "Цена не может быть отрицательной")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    private String priceHardware;

    @DecimalMin(value = "0.00", message = "Значение должно быть 0 или больше нуля.")
    private int countHardware;

    private Category category;

    private List<Characteristic> characteristics;

    private MultipartFile imageHardware;

    public HardwareForm() {
    }

    public HardwareForm(@NotBlank(message = "Заполните поле") @Length(max = 255, message = "Слишком длинное значение") String nameHardware, @NotBlank(message = "Заполните поле") @Length(max = 255, message = "Слишком длинное значение") String descriptionHardware, @NotBlank(message = "Заполните поле") String priceHardware, @NotBlank(message = "Заполните поле") int countHardware, Category category, List<Characteristic> characteristics, MultipartFile imageHardware) {
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

    public void setNameHardware(String nameHardware) {
        this.nameHardware = nameHardware;
    }

    public String getDescriptionHardware() {
        return descriptionHardware;
    }

    public void setDescriptionHardware(String descriptionHardware) {
        this.descriptionHardware = descriptionHardware;
    }

    public String getPriceHardware() {
        return priceHardware;
    }

    public void setPriceHardware(String priceHardware) {
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

    public MultipartFile getImageHardware() {
        return imageHardware;
    }

    public void setImageHardware(MultipartFile imageHardware) {
        this.imageHardware = imageHardware;
    }
}
