package com.test.demo

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import com.github.mvysny.karibudsl.v10.*

class MainView: KComposite() {
    private val root = ui {
        verticalLayout {
            h1("Hello, Vaadin-on-Kotlin!")
        }
    }
}