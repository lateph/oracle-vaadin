package com.test.demo.ui.views.product

import com.github.mvysny.karibudsl.v10.*
import com.test.demo.ProductRepository
import com.test.demo.backend.Person
import com.test.demo.entity.Product
import com.test.demo.ui.MainLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ClickableRenderer.ItemClickListener
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.NativeButtonRenderer
import com.vaadin.flow.data.renderer.TemplateRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.function.ValueProvider
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route


@PageTitle("Product")
@Route(value = "products", layout = MainLayout::class)
class ProductList(private val repository: ProductRepository) : KComposite() {
//    private val grid = Grid<com.test.demo.entity.Country>()
    var gridData = Grid<Product>()
    val form = ProductForm(repository)
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
                button("New Record") {
                    addClickListener {
                        addData()
                    }
                }
            }

            div {
                addClassName("content")
                setSizeFull()

                gridData = grid<Product> {
                    setHeightFull()
                    addClassName("contact-grid");
                    setSizeFull()
                    // column setting
                    addColumn("KODE")
                    addColumn("NAME")
                    addColumn("DESCRIPTION")
                    addColumn("RATING")
                    addColumn("TOTAL")
//                    addColumn(NativeButtonRenderer(
//                            ValueProvider { item: Product -> "Remove $item" },
//                            ItemClickListener { clickedItem: Product? -> })
//                    )

                    addColumn(TemplateRenderer
                            .of<Product>("<img src=\"[[item.name]]\" class=\"img-test\" />")
                            .withProperty("name") { obj: Product -> "/api/image/" +obj.IMAGE }
                    ).setHeader("Image")

//                    addColumn(ComponentRenderer())
//                    val t: TemplateRenderer<Product> = TemplateRenderer("asdasd")
//                    addColumn(TemplateRenderer
//                            .of<Person>("<b>[[item.name]]</b>")
//                            .withProperty("name") { obj: Person -> obj.name }
//                    ).setHeader("Name")
//
//                    addColumn { p ->
//                        val image = Image("/api/image" + p.IMAGE, p.KODE)
//                        image
//                    }.setHeader("Image")
//                    addColumn({ "Show" }, ButtonRenderer<Product>(
//                            { event -> PersonView.navigateTo(event.item) }))

                    asSingleSelect().addValueChangeListener {
                        editData(it.value)
                    }
                }
                form.onSave = {
                    updateList()
                    closeEditor()
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
            gridData.setItems(repository.search(filter))
        } else {
            gridData.setItems(repository.findAll())
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
        editData(Product())
    }

    private fun editData(data: Product?) {
        if (data == null) {
            closeEditor()
        } else {
            form.setData(data)
            form.setVisible(true)
            vl?.addClassName("editing")
        }
    }
}