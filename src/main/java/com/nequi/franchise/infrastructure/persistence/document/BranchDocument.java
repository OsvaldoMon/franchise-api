package com.nequi.franchise.infrastructure.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Documento MongoDB para Branch
 */
@Document(collection = "branches")
public class BranchDocument {
    @Id
    private String id;
    private String name;
    @Field("franchise_id")
    private String franchiseId;
    private List<ProductDocument> products;

    public BranchDocument() {
        this.products = new ArrayList<>();
    }

    public BranchDocument(String id, String name, String franchiseId, List<ProductDocument> products) {
        this.id = id;
        this.name = name;
        this.franchiseId = franchiseId;
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

    public String getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(String franchiseId) {
        this.franchiseId = franchiseId;
    }

    public List<ProductDocument> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDocument> products) {
        this.products = products != null ? products : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchDocument that = (BranchDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BranchDocument{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", franchiseId='" + franchiseId + '\'' +
                ", products=" + products +
                '}';
    }
}
