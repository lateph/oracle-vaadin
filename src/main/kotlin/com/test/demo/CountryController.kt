package com.test.demo

import com.test.demo.entity.Product
import com.test.demo.ui.views.product.ProductForm.Companion.getUploadFolder
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@RestController
@RequestMapping("/api")
class CountryController(private val repository: ProductRepository) {

    @GetMapping("/products")
    fun getAllCountry(): List<Product> =
            repository.findAll()

    @RequestMapping("/image/{id}")
    fun getFile(@PathVariable("id") filename: String): ResponseEntity<Resource>{
        val file = File(getUploadFolder(), filename)
        val headers = HttpHeaders()
        headers.setContentType(MediaType.IMAGE_PNG)
        val path: Path = Paths.get(file.absolutePath)
        val resource = ByteArrayResource(Files.readAllBytes(path)) as Resource
        return ResponseEntity.ok()
                .headers(headers)
//                .contentLength(file.length())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource)
    }

}