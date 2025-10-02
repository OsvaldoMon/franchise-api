package com.nequi.franchise.infrastructure.web.dto;

/**
 * DTO para ProductWithBranch
 */
public class ProductWithBranchDto {
    
    private ProductDto product;
    private String branchName;

    public ProductWithBranchDto() {}

    public ProductWithBranchDto(ProductDto product, String branchName) {
        this.product = product;
        this.branchName = branchName;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
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
        return "ProductWithBranchDto{" +
                "product=" + product +
                ", branchName='" + branchName + '\'' +
                '}';
    }
}
