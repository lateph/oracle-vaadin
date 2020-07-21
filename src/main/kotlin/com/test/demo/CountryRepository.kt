package com.test.demo

import com.test.demo.entity.Country
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CountryRepository : JpaRepository<Country, Long> {
    @Query("SELECT c FROM Country c WHERE COUNTRY_NAME Like concat('%', :searchTerm, '%')")
    fun search(@Param("searchTerm") searchTerm: String): List<Country>

}