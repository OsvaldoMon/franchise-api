package com.nequi.franchise.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Entidad de dominio que representa una sucursal
 */
public class Branch {
    private String id;
    private String name;
    private List<Product> products;

    public Branch() {
        this.products = new ArrayList<>();
    }

    public Branch(String id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products != null ? products : new ArrayList<>();
    }

    public Branch(String name, List<Product> products) {
        this.name = name;
        this.products = products != null ? products : new ArrayList<>();
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products != null ? products : new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        this.products.add(product);
    }

    public void removeProduct(String productId) {
        this.products.removeIf(product -> product.getId().equals(productId));
    }

    public Optional<Product> findProductById(String productId) {
        return this.products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

    public Optional<Product> findProductWithMaxStock() {
        return this.products.stream()
                .max((p1, p2) -> Integer.compare(p1.getStock(), p2.getStock()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}
