package com.nequi.franchise.domain.model;

import java.util.Objects;

/**
 * Entidad de dominio que representa un producto
 */
public class Product {
    private String id;
    private String name;
    private Integer stock;

    public Product() {}

    public Product(String id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public Product(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void updateStock(Integer newStock) {
        if (newStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = newStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                '}';
    }
}
