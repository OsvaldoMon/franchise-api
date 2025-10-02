package com.nequi.franchise.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import java.util.List;

/**
 * DTO para Branch
 */
public class BranchDto {
    
    private String id;
    
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String name;
    
    @Valid
    private List<ProductDto> products;

    public BranchDto() {}

    public BranchDto(String id, String name, List<ProductDto> products) {
        this.id = id;
        this.name = name;
        this.products = products;
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

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "BranchDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}
