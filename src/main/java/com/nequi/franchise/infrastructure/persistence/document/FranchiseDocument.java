package com.nequi.franchise.infrastructure.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Documento MongoDB para Franchise
 */
@Document(collection = "franchises")
public class FranchiseDocument {
    @Id
    private String id;
    private String name;
    private List<BranchDocument> branches;

    public FranchiseDocument() {
        this.branches = new ArrayList<>();
    }

    public FranchiseDocument(String id, String name, List<BranchDocument> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches != null ? branches : new ArrayList<>();
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

    public List<BranchDocument> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDocument> branches) {
        this.branches = branches != null ? branches : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FranchiseDocument that = (FranchiseDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FranchiseDocument{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", branches=" + branches +
                '}';
    }
}
