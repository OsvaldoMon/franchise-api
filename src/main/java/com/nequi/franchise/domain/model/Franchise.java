package com.nequi.franchise.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Entidad de dominio que representa una franquicia
 */
public class Franchise {
    private String id;
    private String name;
    private List<Branch> branches;

    public Franchise() {
        this.branches = new ArrayList<>();
    }

    public Franchise(String id, String name, List<Branch> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches != null ? branches : new ArrayList<>();
    }

    public Franchise(String name, List<Branch> branches) {
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

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches != null ? branches : new ArrayList<>();
    }

    public void addBranch(Branch branch) {
        if (branch == null) {
            throw new IllegalArgumentException("La sucursal no puede ser nula");
        }
        this.branches.add(branch);
    }

    public void removeBranch(String branchId) {
        this.branches.removeIf(branch -> branch.getId().equals(branchId));
    }

    public Optional<Branch> findBranchById(String branchId) {
        return this.branches.stream()
                .filter(branch -> branch.getId().equals(branchId))
                .findFirst();
    }

    public List<ProductWithBranch> getProductsWithMaxStockByBranch() {
        List<ProductWithBranch> result = new ArrayList<>();
        for (Branch branch : this.branches) {
            branch.findProductWithMaxStock()
                    .ifPresent(product -> result.add(new ProductWithBranch(product, branch.getName())));
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Franchise franchise = (Franchise) o;
        return Objects.equals(id, franchise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Franchise{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", branches=" + branches +
                '}';
    }

    /**
     * Clase interna para representar un producto con su sucursal
     */
    public static class ProductWithBranch {
        private Product product;
        private String branchName;

        public ProductWithBranch(Product product, String branchName) {
            this.product = product;
            this.branchName = branchName;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        @Override
        public String toString() {
            return "ProductWithBranch{" +
                    "product=" + product +
                    ", branchName='" + branchName + '\'' +
                    '}';
        }
    }
}
