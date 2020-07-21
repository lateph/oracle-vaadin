package com.test.demo.ui.components.navigation.bar

import com.test.demo.ui.MainLayout
import com.test.demo.ui.components.FlexBoxLayout
import com.test.demo.ui.components.navigation.tab.NaviTab
import com.test.demo.ui.components.navigation.tab.NaviTabs
import com.test.demo.ui.util.LumoStyles
import com.test.demo.ui.util.UIUtils
import com.test.demo.ui.views.Home
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.HasValue.ValueChangeListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.shared.Registration
import java.util.*
import java.util.function.Consumer

@CssImport("./styles/components/app-bar.css")
class AppBar(title: String, vararg tabs: NaviTab?) : FlexBoxLayout() {
    private val CLASS_NAME = "app-bar"
    private var container: FlexBoxLayout? = null

    /* === MENU ICON === */
    var menuIcon: Button? = null
        private set

    /* === CONTEXT ICON === */
    var contextIcon: Button? = null
        private set
    private var title: H4? = null
    private var actionItems: FlexBoxLayout? = null

    /* === AVATAR == */
    var avatar: Image? = null
        private set
    private var tabContainer: FlexBoxLayout? = null
    private var tabs: NaviTabs? = null
    private var tabSelectionListeners: ArrayList<Registration>? = null
    private var addTab: Button? = null
    private var search: TextField? = null
    private var searchRegistration: Registration? = null

    enum class NaviMode {
        MENU, CONTEXTUAL
    }

    fun setNaviMode(mode: NaviMode) {
        if (mode == NaviMode.MENU) {
            menuIcon!!.isVisible = true
            contextIcon!!.isVisible = false
        } else {
            menuIcon!!.isVisible = false
            contextIcon!!.isVisible = true
        }
    }

    private fun initMenuIcon() {
        menuIcon = UIUtils.createTertiaryInlineButton(VaadinIcon.MENU)
        menuIcon!!.addClassName(CLASS_NAME + "__navi-icon")
        menuIcon!!.addClickListener(ComponentEventListener { _: ClickEvent<Button?>? -> MainLayout.get().naviDrawer!!.toggle() })
        UIUtils.setAriaLabel("Menu", menuIcon)
        UIUtils.setLineHeight("1", menuIcon)
    }

    private fun initContextIcon() {
        contextIcon = UIUtils
                .createTertiaryInlineButton(VaadinIcon.ARROW_LEFT)
        contextIcon!!.addClassNames(CLASS_NAME + "__context-icon")
        contextIcon!!.setVisible(false)
        UIUtils.setAriaLabel("Back", contextIcon)
        UIUtils.setLineHeight("1", contextIcon)
    }

    private fun initTitle(title: String) {
        this.title = H4(title)
        this.title!!.className = CLASS_NAME + "__title"
    }

    private fun initSearch() {
        search = TextField()
        search!!.placeholder = "Search"
        search!!.prefixComponent = Icon(VaadinIcon.SEARCH)
        search!!.isVisible = false
    }

    private fun initAvatar() {
        avatar = Image()
        avatar!!.className = CLASS_NAME + "__avatar"
        avatar!!.src = UIUtils.IMG_PATH + "avatar.png"
        avatar!!.setAlt("User menu")
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
    }

    private fun initActionItems() {
        actionItems = FlexBoxLayout()
        actionItems!!.addClassName(CLASS_NAME + "__action-items")
        actionItems!!.isVisible = false
    }

    private fun initContainer() {
        container = FlexBoxLayout(menuIcon, contextIcon, title, search,
                actionItems, avatar)
        container!!.addClassName(CLASS_NAME + "__container")
        container!!.alignItems = FlexComponent.Alignment.CENTER
        container!!.setFlexGrow(1.0, search)
        add(container)
    }

    private fun initTabs(vararg tabs: NaviTab) {
        addTab = UIUtils.createSmallButton(VaadinIcon.PLUS)
        addTab!!.addClickListener(ComponentEventListener { e: ClickEvent<Button?>? ->
            this.tabs!!.setSelectedTab(addClosableNaviTab("New Tab", Home::class.java))
        })
        addTab!!.setVisible(false)
        this.tabs = if (tabs.size > 0) NaviTabs(*tabs) else NaviTabs()
        this.tabs!!.className = CLASS_NAME + "__tabs"
        this.tabs!!.isVisible = false
        for (tab in tabs) {
            configureTab(tab)
        }
        tabSelectionListeners = ArrayList()
        tabContainer = FlexBoxLayout(this.tabs, addTab)
        tabContainer!!.addClassName(CLASS_NAME + "__tab-container")
        tabContainer!!.alignItems = FlexComponent.Alignment.CENTER
        add(tabContainer)
    }

    fun setContextIcon(icon: Icon?) {
        contextIcon!!.icon = icon
    }

    /* === TITLE === */
    fun getTitle(): String {
        return title!!.text
    }

    fun setTitle(title: String?) {
        this.title!!.text = title
    }

    /* === ACTION ITEMS === */
    fun addActionItem(component: Component): Component {
        actionItems!!.add(component)
        updateActionItemsVisibility()
        return component
    }

    fun addActionItem(icon: VaadinIcon?): Button {
        val button = UIUtils.createButton(icon, ButtonVariant.LUMO_SMALL,
                ButtonVariant.LUMO_TERTIARY)
        addActionItem(button)
        return button
    }

    fun removeAllActionItems() {
        actionItems!!.removeAll()
        updateActionItemsVisibility()
    }

    /* === TABS === */
    fun centerTabs() {
        tabs!!.addClassName(LumoStyles.Margin.Horizontal.AUTO)
    }

    private fun configureTab(tab: Tab) {
        tab.addClassName(CLASS_NAME + "__tab")
        updateTabsVisibility()
    }

    fun addTab(text: String?): Tab {
        val tab = tabs!!.addTab(text)
        configureTab(tab)
        return tab
    }

    fun addTab(text: String?,
               navigationTarget: Class<out Component?>?): Tab {
        val tab = tabs!!.addTab(text, navigationTarget)
        configureTab(tab)
        return tab
    }

    fun addClosableNaviTab(text: String?,
                           navigationTarget: Class<out Component?>?): Tab {
        val tab = tabs!!.addClosableTab(text, navigationTarget)
        configureTab(tab)
        return tab
    }

    var selectedTab: Tab?
        get() = tabs!!.selectedTab
        set(selectedTab) {
            tabs!!.selectedTab = selectedTab
        }

    fun updateSelectedTab(text: String?,
                          navigationTarget: Class<out Component?>?) {
        tabs!!.updateSelectedTab(text, navigationTarget)
    }

    fun navigateToSelectedTab() {
        tabs!!.navigateToSelectedTab()
    }

    fun addTabSelectionListener(
            listener: (SelectedChangeEvent?) -> Unit) {
        val registration = tabs!!.addSelectedChangeListener(listener)
        tabSelectionListeners!!.add(registration)
    }

    val tabCount: Int
        get() = tabs!!.tabCount

    fun removeAllTabs() {
        tabSelectionListeners!!.forEach(Consumer { registration: Registration -> registration.remove() })
        tabSelectionListeners!!.clear()
        tabs!!.removeAll()
        updateTabsVisibility()
    }

    /* === ADD TAB BUTTON === */
    fun setAddTabVisible(visible: Boolean) {
        addTab!!.isVisible = visible
    }

    /* === SEARCH === */
    fun searchModeOn() {
        menuIcon!!.isVisible = false
        title!!.isVisible = false
        actionItems!!.isVisible = false
        tabContainer!!.isVisible = false
        contextIcon!!.icon = Icon(VaadinIcon.ARROW_BACKWARD)
        contextIcon!!.isVisible = true
        searchRegistration = contextIcon!!.addClickListener(ComponentEventListener { e: ClickEvent<Button?>? -> searchModeOff() })
        search!!.isVisible = true
        search!!.focus()
    }

    fun addSearchListener(listener: ValueChangeListener<*>) {
        search!!.addValueChangeListener(listener as ValueChangeListener<in AbstractField.ComponentValueChangeEvent<TextField, String>>?)
    }

    fun setSearchPlaceholder(placeholder: String?) {
        search!!.placeholder = placeholder
    }

    private fun searchModeOff() {
        menuIcon!!.isVisible = true
        title!!.isVisible = true
        tabContainer!!.isVisible = true
        updateActionItemsVisibility()
        updateTabsVisibility()
        contextIcon!!.isVisible = false
        searchRegistration!!.remove()
        search!!.clear()
        search!!.isVisible = false
    }

    /* === RESET === */
    fun reset() {
        title!!.text = ""
        setNaviMode(NaviMode.MENU)
        removeAllActionItems()
        removeAllTabs()
    }

    /* === UPDATE VISIBILITY === */
    private fun updateActionItemsVisibility() {
        actionItems!!.isVisible = actionItems!!.componentCount > 0
    }

    private fun updateTabsVisibility() {
        tabs!!.isVisible = tabs!!.componentCount > 0
    }

    init {
        className = CLASS_NAME
        initMenuIcon()
        initContextIcon()
        initTitle(title)
        initSearch()
        initAvatar()
        initActionItems()
        initContainer()
        initTabs(*tabs as Array<out NaviTab>)
    }
}