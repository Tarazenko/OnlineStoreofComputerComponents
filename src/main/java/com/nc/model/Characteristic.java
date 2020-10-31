package com.nc.model;

import com.nc.annotation.CheckOnHtml;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "characteristic")
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DecimalMin(value = "0.00")
    @Column(name = "characteristic_id")
    private long id;
    @NotBlank(message = "Заполните поле")
    @Length(max = 255, message = "Слишком длинное значение")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    @Column(name = "name")
    private String nameCharacteristic;
    @NotBlank(message = "Заполните поле")
    @Length(max = 255, message = "Слишком длинное значение")
    @CheckOnHtml(message = "Некорректный ввод. Присутсвуют теги html.")
    @Column(name = "value")
    private String valueCharacteristic;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public Characteristic() {
    }

    public Characteristic(String nameCharacteristic, String valueCharacteristic, Category category) {
        this.nameCharacteristic = nameCharacteristic;
        this.valueCharacteristic = valueCharacteristic;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameCharacteristic() {
        return nameCharacteristic;
    }

    public void setNameCharacteristic(String nameCharacteristic) {
        this.nameCharacteristic = nameCharacteristic;
    }

    public String getValueCharacteristic() {
        return valueCharacteristic;
    }

    public void setValueCharacteristic(String valueCharacteristic) {
        this.valueCharacteristic = valueCharacteristic;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Characteristic{" +
                "id=" + id +
                ", nameCharacteristic='" + nameCharacteristic + '\'' +
                ", valueCharacteristic='" + valueCharacteristic + '\'' +
                ", category=" + category.getNameCategory() +
                '}';
    }
}
