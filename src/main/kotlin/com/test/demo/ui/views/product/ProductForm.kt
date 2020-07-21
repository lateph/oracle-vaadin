package com.test.demo.ui.views.product

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.formLayout
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.test.demo.ProductRepository
import com.test.demo.entity.Product
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.MultiFileReceiver
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.converter.StringToIntegerConverter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.reflect.KMutableProperty


class ProductForm(val repository: ProductRepository) : KComposite() {
    //    private val grid = Grid<com.test.demo.entity.Country>()
    var onSave: (()->Unit)? = null
    var onCancel: (()->Unit)? = null

    var kode = TextField("Kode")
    var name = TextField("Name")
    var rating = TextField("Rating")
    var total = TextField("Total")
    var description = TextField("Description")
    var image = TextField("Image")

    var binder: Binder<Product> = BeanValidationBinder(Product::class.java)
    private var data: Product? = null
    var fileReceiver = MultiFileReceiver{
        filename, mimeType ->
        val file = File(getUploadFolder(), filename)
        binder.writeBean(data)
        data?.IMAGE = filename
        binder.readBean(data)
        try {
            FileOutputStream(file)
        } catch (e1: FileNotFoundException) {
            e1.printStackTrace()
            null
        }
    }
    var upload = Upload(fileReceiver).apply {
        this.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif")
        setMaxFiles(1)
    }

    private val root = ui {
        formLayout {
            addClassName("contact-form")
            binder.forField(total)
                    .withConverter(
                            StringToIntegerConverter("Must enter a number"))
                    .bind(Product::TOTAL)
            binder.forField(rating)
                    .withConverter(
                            StringToIntegerConverter("Must enter a number"))
                    .bind(Product::RATING)
            binder.forField(description).bind(Product::DESCRIPTION)
            binder.forField(kode).bind(Product::KODE)
            binder.forField(name).bind(Product::NAME)
            binder.forField(image).bind(Product::IMAGE)


            add(kode)
            add(name)
            add(description)
            add(image)
            add(upload)
            add(rating)
            add(total)
            horizontalLayout {
                button("Save") {
                    addThemeVariants(ButtonVariant.LUMO_PRIMARY)
                    addClickShortcut(Key.ENTER)

                    addClickListener {
                        save()
                    }
                }
                button("Delete") {
                    addThemeVariants(ButtonVariant.LUMO_ERROR)

                    addClickListener {
                        data?.let { it1 -> repository.delete(it1) }
                    }
                }
                button("Cancel") {
                    addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                    addClickShortcut(Key.ESCAPE)

                    addClickListener {
                        onCancel?.invoke()
                    }
                }
            }
        }
    }

    private fun save() {
        binder.writeBean(data)
        data?.let {
            try {
                repository.save(it)
                onSave?.invoke()
            } catch (e: Exception) {
                throw e
            }
        }
    }


    fun setData(data: Product?) {
        this.data = data
        binder.readBean(data)
    }

    companion object {
        fun getUploadFolder(): File? {
            val folder = File("uploaded-files")
            if (!folder.exists()) {
                folder.mkdirs()
            }
            return folder
        }
    }

    fun <BEAN, T> Binder.BindingBuilder<BEAN, T>.bind(prop: KMutableProperty<T>) {
        println("bind")
        println(prop)
        this.bind(
                { bean: BEAN -> prop.getter.call(bean) },
                { bean: BEAN, v: T ->
                    System.out.print("seharuse update")
                    println(bean)
                    System.out.println(v)
                    prop.setter.call(bean, v)
                })
    }
}