package com.test.demo.entity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "PRODUCT")
data class Product(
        @Id
        var KODE: String="",
        var NAME: String="",
        var DESCRIPTION: String="",
        var IMAGE: String="",
        var RATING: Int? = 0,
        var TOTAL: Int? = 0
)