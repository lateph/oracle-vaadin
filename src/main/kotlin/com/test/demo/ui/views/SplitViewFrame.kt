package com.test.demo.ui.views

import com.test.demo.ui.MainLayout
import com.test.demo.ui.components.FlexBoxLayout
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection

/**
 * A view frame that establishes app design guidelines. It consists of four
 * parts:
 *
 *  * Topmost [header][.setViewHeader]
 *  * Center [content][.setViewContent]
 *  * Center [details][.setViewDetails]
 *  * Bottom [footer][.setViewFooter]
 *
 */
@CssImport("./styles/components/view-frame.css")
open class SplitViewFrame : Composite<Div?>(), HasStyle {
    private val CLASS_NAME = "view-frame"
    private val header: Div
    private val wrapper: FlexBoxLayout
    private val content: Div
    private val details: Div
    private val footer: Div

    enum class Position {
        RIGHT, BOTTOM
    }

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
     * Sets the detail slot's components.
     */
    fun setViewDetails(vararg components: Component?) {
        details.removeAll()
        details.add(*components)
    }

    fun setViewDetailsPosition(position: Position) {
        if (position == Position.RIGHT) {
            wrapper.setFlexDirection(FlexDirection.ROW)
        } else if (position == Position.BOTTOM) {
            wrapper.setFlexDirection(FlexDirection.COLUMN)
        }
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
        wrapper = FlexBoxLayout()
        wrapper.className = CLASS_NAME + "__wrapper"
        content = Div()
        content.className = CLASS_NAME + "__content"
        details = Div()
        details.className = CLASS_NAME + "__details"
        footer = Div()
        footer.className = CLASS_NAME + "__footer"
        wrapper.add(content, details)
        getContent()!!.add(header, wrapper, footer)
    }
}