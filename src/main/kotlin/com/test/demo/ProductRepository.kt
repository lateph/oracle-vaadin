package com.test.demo

import com.test.demo.entity.Country
import com.test.demo.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProductRepository : JpaRepository<Product, Long> {
    @Query("SELECT c FROM Product c WHERE NAME Like concat('%', :searchTerm, '%')")
    fun search(@Param("searchTerm") searchTerm: String): List<Product>

}