package com.nequi.franchise.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para Product
 */
class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("1", "Producto Test", 10);
    }

    @Test
    void testProductCreation() {
        assertNotNull(product);
        assertEquals("1", product.getId());
        assertEquals("Producto Test", product.getName());
        assertEquals(10, product.getStock());
    }

    @Test
    void testUpdateStock() {
        product.updateStock(20);
        assertEquals(20, product.getStock());
    }

    @Test
    void testUpdateStockWithNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            product.updateStock(-1);
        });
    }

    @Test
    void testEquals() {
        Product product2 = new Product("1", "Otro Producto", 5);
        assertEquals(product, product2);
    }

    @Test
    void testHashCode() {
        Product product2 = new Product("1", "Otro Producto", 5);
        assertEquals(product.hashCode(), product2.hashCode());
    }

    @Test
    void testToString() {
        String toString = product.toString();
        assertTrue(toString.contains("Product"));
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Producto Test"));
        assertTrue(toString.contains("10"));
    }
}

