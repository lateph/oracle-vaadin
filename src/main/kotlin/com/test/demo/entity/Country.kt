package com.test.demo.entity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "COUNTRIES")
data class Country(
        @Id
        var COUNTRY_ID: String="",
        var COUNTRY_NAME: String="",
        var REGION_ID: Int? = 0
)