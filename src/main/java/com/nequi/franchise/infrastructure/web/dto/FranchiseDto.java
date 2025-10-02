package com.nequi.franchise.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import java.util.List;

/**
 * DTO para Franchise
 */
public class FranchiseDto {
    
    private String id;
    
    @NotBlank(message = "El nombre de la franquicia es obligatorio")
    private String name;
    
    @Valid
    private List<BranchDto> branches;

    public FranchiseDto() {}

    public FranchiseDto(String id, String name, List<BranchDto> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
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

    public List<BranchDto> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDto> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return "FranchiseDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", branches=" + branches +
                '}';
    }
}
