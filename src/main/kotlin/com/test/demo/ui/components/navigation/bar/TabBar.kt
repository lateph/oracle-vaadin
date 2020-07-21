package com.test.demo.ui.components.navigation.bar

import com.test.demo.ui.MainLayout
import com.test.demo.ui.components.FlexBoxLayout
import com.test.demo.ui.components.navigation.tab.NaviTabs
import com.test.demo.ui.util.LumoStyles
import com.test.demo.ui.util.UIUtils
import com.test.demo.ui.views.Home
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent

@CssImport("./styles/components/tab-bar.css")
class TabBar : FlexBoxLayout() {
    private val CLASS_NAME = "tab-bar"

    /* === MENU ICON === */
    val menuIcon: Button
    private val tabs: NaviTabs
    private val addTab: Button
    private val avatar: Image

    /* === TABS === */
    fun centerTabs() {
        tabs.addClassName(LumoStyles.Margin.Horizontal.AUTO)
    }

    private fun configureTab(tab: Tab) {
        tab.addClassName(CLASS_NAME + "__tab")
    }

    fun addTab(text: String?): Tab {
        val tab = tabs.addTab(text)
        configureTab(tab)
        return tab
    }

    fun addTab(text: String?,
               navigationTarget: Class<out Component?>?): Tab {
        val tab = tabs.addTab(text, navigationTarget)
        configureTab(tab)
        return tab
    }

    fun addClosableTab(text: String?,
                       navigationTarget: Class<out Component?>?): Tab {
        val tab = tabs.addClosableTab(text, navigationTarget)
        configureTab(tab)
        return tab
    }

    var selectedTab: Tab?
        get() = tabs.selectedTab
        set(selectedTab) {
            tabs.selectedTab = selectedTab
        }

    fun updateSelectedTab(text: String?,
                          navigationTarget: Class<out Component?>?) {
        tabs.updateSelectedTab(text, navigationTarget)
    }

    fun addTabSelectionListener(
            listener: ComponentEventListener<SelectedChangeEvent?>?) {
        tabs.addSelectedChangeListener(listener)
    }

    val tabCount: Int
        get() = tabs.tabCount

    fun removeAllTabs() {
        tabs.removeAll()
    }

    /* === ADD TAB BUTTON === */
    fun setAddTabVisible(visible: Boolean) {
        addTab.isVisible = visible
    }

    init {
        className = CLASS_NAME
        menuIcon = UIUtils.createTertiaryInlineButton(VaadinIcon.MENU)
        menuIcon.addClassName(CLASS_NAME + "__navi-icon")
        menuIcon.addClickListener { e: ClickEvent<Button?>? -> MainLayout.get().naviDrawer!!.toggle() }
        avatar = Image()
        avatar.className = CLASS_NAME + "__avatar"
        avatar.src = UIUtils.IMG_PATH + "avatar.png"
        val contextMenu = ContextMenu(avatar)
        contextMenu.isOpenOnClick = true
        contextMenu.addItem("Settings"
        ) { e: ClickEvent<MenuItem?>? ->
            Notification.show("Not implemented yet.", 3000,
                    Notification.Position.BOTTOM_CENTER)
        }
        contextMenu.addItem("Log Out"
        ) { e: ClickEvent<MenuItem?>? ->
            Notification.show("Not implemented yet.", 3000,
                    Notification.Position.BOTTOM_CENTER)
        }
        addTab = UIUtils.createSmallButton(VaadinIcon.PLUS)
        tabs = NaviTabs()

        addTab.addClickListener { e: ClickEvent<Button?>? ->
            tabs.selectedTab = addClosableTab("New Tab", Home::class.java)
        }
        addTab.className = CLASS_NAME + "__add-tab"
        tabs.className = CLASS_NAME + "__tabs"
        add(menuIcon, tabs, addTab, avatar)
    }
}