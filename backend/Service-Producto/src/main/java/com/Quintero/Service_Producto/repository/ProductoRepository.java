package com.Quintero.Service_Producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Quintero.Service_Producto.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsBySku(String sku);
}
