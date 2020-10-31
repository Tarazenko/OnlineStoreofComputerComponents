package com.nc.service;

import com.nc.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findById(long idCategory);

    void save(Category category);

    void update(Category category);

    void delete(long id);
}
