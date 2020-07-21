package com.test.demo.ui.views

import com.test.demo.ui.MainLayout
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div

/**
 * A view frame that establishes app design guidelines. It consists of three
 * parts:
 *
 *  * Topmost [header][.setViewHeader]
 *  * Center [content][.setViewContent]
 *  * Bottom [footer][.setViewFooter]
 *
 */
@CssImport("./styles/components/view-frame.css")
open class ViewFrame : Composite<Div?>(), HasStyle {
    private val CLASS_NAME = "view-frame"
    private val header: Div
    private val content: Div
    private val footer: Div

    /**
     * Sets the header slot's components.
     */
    fun setViewHeader(vararg components: Component?) {
        header.removeAll()
        header.add(*components)
    }

    /**
     * Sets the content slot's components.
     */
    fun setViewContent(vararg components: Component?) {
        content.removeAll()
        content.add(*components)
    }

    /**
     * Sets the footer slot's components.
     */
    fun setViewFooter(vararg components: Component?) {
        footer.removeAll()
        footer.add(*components)
    }

    override fun onAttach(attachEvent: AttachEvent) {
        super.onAttach(attachEvent)
        MainLayout.get().appBar!!.reset()
    }

    init {
        className = CLASS_NAME
        header = Div()
        header.className = CLASS_NAME + "__header"
        content = Div()
        content.className = CLASS_NAME + "__content"
        footer = Div()
        footer.className = CLASS_NAME + "__footer"
        getContent()!!.add(header, content, footer)
    }
}