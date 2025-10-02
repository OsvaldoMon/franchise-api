package com.nequi.franchise.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

/**
 * Pruebas unitarias para Branch
 */
class BranchTest {

    private Branch branch;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        branch = new Branch("1", "Sucursal Test", List.of());
        product1 = new Product("1", "Producto 1", 10);
        product2 = new Product("2", "Producto 2", 20);
    }

    @Test
    void testBranchCreation() {
        assertNotNull(branch);
        assertEquals("1", branch.getId());
        assertEquals("Sucursal Test", branch.getName());
        assertNotNull(branch.getProducts());
    }

    @Test
    void testAddProduct() {
        branch.addProduct(product1);
        assertEquals(1, branch.getProducts().size());
        assertTrue(branch.getProducts().contains(product1));
    }

    @Test
    void testAddNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            branch.addProduct(null);
        });
    }

    @Test
    void testRemoveProduct() {
        branch.addProduct(product1);
        branch.addProduct(product2);
        assertEquals(2, branch.getProducts().size());
        
        branch.removeProduct("1");
        assertEquals(1, branch.getProducts().size());
        assertFalse(branch.getProducts().contains(product1));
    }

    @Test
    void testFindProductById() {
        branch.addProduct(product1);
        branch.addProduct(product2);
        
        Optional<Product> found = branch.findProductById("1");
        assertTrue(found.isPresent());
        assertEquals(product1, found.get());
        
        Optional<Product> notFound = branch.findProductById("3");
        assertFalse(notFound.isPresent());
    }

    @Test
    void testFindProductWithMaxStock() {
        branch.addProduct(product1);
        branch.addProduct(product2);
        
        Optional<Product> maxStock = branch.findProductWithMaxStock();
        assertTrue(maxStock.isPresent());
        assertEquals(product2, maxStock.get());
    }

    @Test
    void testFindProductWithMaxStockEmpty() {
        Optional<Product> maxStock = branch.findProductWithMaxStock();
        assertFalse(maxStock.isPresent());
    }

    @Test
    void testEquals() {
        Branch branch2 = new Branch("1", "Otra Sucursal", List.of());
        assertEquals(branch, branch2);
    }

    @Test
    void testHashCode() {
        Branch branch2 = new Branch("1", "Otra Sucursal", List.of());
        assertEquals(branch.hashCode(), branch2.hashCode());
    }
}

