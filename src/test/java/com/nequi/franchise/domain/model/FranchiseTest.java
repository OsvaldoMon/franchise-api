package com.nequi.franchise.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

/**
 * Pruebas unitarias para Franchise
 */
class FranchiseTest {

    private Franchise franchise;
    private Branch branch1;
    private Branch branch2;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        franchise = new Franchise("1", "Franquicia Test", List.of());
        branch1 = new Branch("1", "Sucursal 1", List.of());
        branch2 = new Branch("2", "Sucursal 2", List.of());
        product1 = new Product("1", "Producto 1", 10);
        product2 = new Product("2", "Producto 2", 20);
    }

    @Test
    void testFranchiseCreation() {
        assertNotNull(franchise);
        assertEquals("1", franchise.getId());
        assertEquals("Franquicia Test", franchise.getName());
        assertNotNull(franchise.getBranches());
    }

    @Test
    void testAddBranch() {
        franchise.addBranch(branch1);
        assertEquals(1, franchise.getBranches().size());
        assertTrue(franchise.getBranches().contains(branch1));
    }

    @Test
    void testAddNullBranch() {
        assertThrows(IllegalArgumentException.class, () -> {
            franchise.addBranch(null);
        });
    }

    @Test
    void testRemoveBranch() {
        franchise.addBranch(branch1);
        franchise.addBranch(branch2);
        assertEquals(2, franchise.getBranches().size());
        
        franchise.removeBranch("1");
        assertEquals(1, franchise.getBranches().size());
        assertFalse(franchise.getBranches().contains(branch1));
    }

    @Test
    void testFindBranchById() {
        franchise.addBranch(branch1);
        franchise.addBranch(branch2);
        
        Optional<Branch> found = franchise.findBranchById("1");
        assertTrue(found.isPresent());
        assertEquals(branch1, found.get());
        
        Optional<Branch> notFound = franchise.findBranchById("3");
        assertFalse(notFound.isPresent());
    }

    @Test
    void testGetProductsWithMaxStockByBranch() {
        branch1.addProduct(product1);
        branch1.addProduct(product2);
        branch2.addProduct(new Product("3", "Producto 3", 5));
        
        franchise.addBranch(branch1);
        franchise.addBranch(branch2);
        
        List<Franchise.ProductWithBranch> maxStockProducts = franchise.getProductsWithMaxStockByBranch();
        assertEquals(2, maxStockProducts.size());
        
        // Verificar que se encontró el producto con mayor stock en cada sucursal
        assertTrue(maxStockProducts.stream().anyMatch(pwb -> pwb.getProduct().equals(product2)));
    }

    @Test
    void testEquals() {
        Franchise franchise2 = new Franchise("1", "Otra Franquicia", List.of());
        assertEquals(franchise, franchise2);
    }

    @Test
    void testHashCode() {
        Franchise franchise2 = new Franchise("1", "Otra Franquicia", List.of());
        assertEquals(franchise.hashCode(), franchise2.hashCode());
    }

    @Test
    void testProductWithBranch() {
        Franchise.ProductWithBranch pwb = new Franchise.ProductWithBranch(product1, "Sucursal Test");
        assertEquals(product1, pwb.getProduct());
        assertEquals("Sucursal Test", pwb.getBranchName());
    }
}
