package com.test.demo.ui.views.country

import com.github.mvysny.karibudsl.v10.*
import com.test.demo.CountryRepository
import com.test.demo.entity.Country
import com.test.demo.ui.MainLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@PageTitle("Country")
@Route(value = "countries", layout = MainLayout::class)
class CountryList(private val countryRepository: CountryRepository) : KComposite() {
//    private val grid = Grid<com.test.demo.entity.Country>()
    var gridData = Grid<Country>()
    val form = CountryForm(countryRepository)
    var vl: VerticalLayout? = null

    private val root = ui {
        verticalLayout {
            addClassName("list-view")
//            removeClassName("editing")

            setSizeFull()
            horizontalLayout {
                textField {
                    placeholder = "Filter By name"
                    isClearButtonVisible = true
                    valueChangeMode = ValueChangeMode.LAZY
                    addValueChangeListener {
                        updateList(it.value)
                    }
                }
                button("Add Country") {
                    addClickListener {
                        addData()
                    }
                }
            }

            div {
                addClassName("content")
                setSizeFull()

                gridData = grid<com.test.demo.entity.Country> {
                    setHeightFull()
                    addClassName("contact-grid");
                    setSizeFull()
                    // column setting
                    addColumn("COUNTRY_ID")
                    addColumn("COUNTRY_NAME")
                    addColumn("REGION_ID")

                    asSingleSelect().addValueChangeListener {
                        editData(it.value)
                    }
                }
                form.onSave = {
                    updateList()
                }
                form.onCancel = {
                    closeEditor()
                }
                add(form)
            }
        }.apply {
            vl = this
        }
    }

    fun updateList (filter: String = "") {
        if (filter.isNotEmpty()) {
            gridData.setItems(countryRepository.search(filter))
        } else {
            gridData.setItems(countryRepository.findAll())
        }
    }

    init {
        updateList()
        closeEditor()
    }

    private fun closeEditor() {
        form.setData(null)
        form.isVisible = false
        vl?.removeClassName("editing")
    }

    private fun addData() {
        gridData.asSingleSelect().clear()
        editData(Country())
    }

    private fun editData(country: Country?) {
        if (country == null) {
            closeEditor()
        } else {
            form.setData(country)
            form.setVisible(true)
            vl?.addClassName("editing")
        }
    }
}