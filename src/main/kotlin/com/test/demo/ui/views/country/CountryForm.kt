package com.test.demo.ui.views.country

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.formLayout
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.test.demo.CountryRepository
import com.test.demo.entity.Country
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.PropertyId
import com.vaadin.flow.data.binder.Setter
import com.vaadin.flow.data.converter.StringToIntegerConverter
import com.vaadin.flow.function.ValueProvider
import java.lang.Exception
import kotlin.reflect.KMutableProperty


class CountryForm(val repository: CountryRepository) : KComposite() {
    //    private val grid = Grid<com.test.demo.entity.Country>()
    var onSave: (()->Unit)? = null
    var onCancel: (()->Unit)? = null

    var countryId = TextField("Country ID")
    var countryName = TextField("Country Name")
    var regionId = TextField("Region ID")

    var binder: Binder<Country> = BeanValidationBinder(Country::class.java)
    private var country: Country? = null

    private val root = ui {
        formLayout {
            addClassName("contact-form")
            binder.forField(regionId)
                    .withConverter(
                            StringToIntegerConverter("Must enter a number"))
                    .bind(Country::REGION_ID)
            binder.forField(countryId).bind(Country::COUNTRY_ID)
            binder.forField(countryName).bind(Country::COUNTRY_NAME)

            add(countryId)
            add(countryName)
            add(regionId)
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
                }
                button("Cancel") {
                    addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                    addClickShortcut(Key.ESCAPE)

                    onCancel?.invoke()
                }
            }
        }
    }

    private fun save() {
        binder.writeBean(country)
        country?.let {
            try {
                repository.save(it)
                onSave?.invoke()
            } catch (e: Exception) {
                throw e
            }
        }
    }


    fun setData(country: Country?) {
        this.country = country
        binder.readBean(country)
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