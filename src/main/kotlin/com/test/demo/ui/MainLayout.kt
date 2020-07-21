package com.test.demo.ui

import com.test.demo.ui.components.FlexBoxLayout
import com.test.demo.ui.components.navigation.bar.AppBar
import com.test.demo.ui.components.navigation.bar.TabBar
import com.test.demo.ui.components.navigation.drawer.NaviDrawer
import com.test.demo.ui.components.navigation.drawer.NaviItem
import com.test.demo.ui.util.UIUtils
import com.test.demo.ui.util.css.Overflow
import com.test.demo.ui.views.country.CountryList
import com.test.demo.ui.views.Home
import com.test.demo.ui.views.Payments
import com.test.demo.ui.views.product.ProductList
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.server.*
import com.vaadin.flow.theme.lumo.Lumo
import org.slf4j.LoggerFactory

@CssImport.Container(value = [  // repeatable annotations are not supported by Kotlin, please vote for https://youtrack.jetbrains.com/issue/KT-12794
    CssImport(value = "./styles/components/charts.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme"),
    CssImport(value = "./styles/components/floating-action-button.css", themeFor = "vaadin-button"),
    CssImport(value = "./styles/components/grid.css", themeFor = "vaadin-grid"),
    CssImport("./styles/lumo/border-radius.css"),
    CssImport("./styles/lumo/icon-size.css"),
    CssImport("./styles/lumo/margin.css"),
    CssImport("./styles/lumo/padding.css"),
    CssImport("./styles/lumo/shadow.css"),
    CssImport("./styles/lumo/spacing.css"),
    CssImport("./styles/lumo/typography.css"),
    CssImport("./styles/misc/box-shadow-borders.css"),
    CssImport(value = "./styles/styles.css", include = "lumo-badge")
])

@JsModule("@vaadin/vaadin-lumo-styles/badge")
@PWA(name = "My Starter Project", shortName = "My Starter Project", iconPath = "images/logo-18.png", backgroundColor = "#233348", themeColor = "#233348")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
class MainLayout : FlexBoxLayout(), RouterLayout, PageConfigurator, AfterNavigationObserver {
    private var appHeaderOuter: Div? = null
    private var row: FlexBoxLayout? = null
    var naviDrawer: NaviDrawer? = null
        private set
    private var column: FlexBoxLayout? = null
    private var appHeaderInner: Div? = null
    private var viewContainer: FlexBoxLayout? = null
    private var appFooterInner: Div? = null
    private var appFooterOuter: Div? = null
    private var tabBar: TabBar? = null
    private val navigationTabs = false
    var appBar: AppBar? = null

    /**
     * Initialise the required components and containers.
     */
    private fun initStructure() {
        naviDrawer = NaviDrawer()
        viewContainer = FlexBoxLayout()
        viewContainer!!.addClassName(CLASS_NAME + "__view-container")
        viewContainer!!.setOverflow(Overflow.HIDDEN)
        column = FlexBoxLayout(viewContainer)
        column!!.addClassName(CLASS_NAME + "__column")
        column!!.setFlexDirection(FlexDirection.COLUMN)
        column!!.setFlexGrow(1.0, viewContainer)
        column!!.setOverflow(Overflow.HIDDEN)
        row = FlexBoxLayout(naviDrawer, column)
        row!!.addClassName(CLASS_NAME + "__row")
        row!!.setFlexGrow(1.0, column)
        row!!.setOverflow(Overflow.HIDDEN)
        add(row)
        setFlexGrow(1.0, row)
    }

    /**
     * Initialise the navigation items.
     */
    private fun initNaviItems() {
        val menu = naviDrawer!!.menu
        menu.addNaviItem(VaadinIcon.HOME, "Home", Home::class.java)
//        menu.addNaviItem(VaadinIcon.CREDIT_CARD, "Payments", Payments::class.java)
        menu.addNaviItem(VaadinIcon.CREDIT_CARD, "Country", CountryList::class.java)
        menu.addNaviItem(VaadinIcon.PACKAGE, "Products", ProductList::class.java)
//        val personnel = menu.addNaviItem(VaadinIcon.USERS, "Personnel",
//                null)
    }

    /**
     * Configure the app's inner and outer headers and footers.
     */
    private fun initHeadersAndFooters() {
        // setAppHeaderOuter();
        // setAppFooterInner();
        // setAppFooterOuter();

        // Default inner header setup:
        // - When using tabbed navigation the view title, user avatar and main menu button will appear in the TabBar.
        // - When tabbed navigation is turned off they appear in the AppBar.
        appBar = AppBar("")

        // Tabbed navigation
        if (navigationTabs) {
            tabBar = TabBar()
            UIUtils.setTheme(Lumo.DARK, tabBar)

            // Shift-click to add a new tab
            for (item in naviDrawer!!.menu.naviItems) {
                item.addClickListener { e: ClickEvent<Div?> ->
                    if (e.button == 0 && e.isShiftKey) {
                        tabBar!!.selectedTab = tabBar!!.addClosableTab(item.text, item.navigationTarget)
                    }
                }
            }
            appBar!!.avatar!!.isVisible = false
            setAppHeaderInner(tabBar!!, appBar!!)

            // Default navigation
        } else {
            UIUtils.setTheme(Lumo.DARK, appBar)
            setAppHeaderInner(appBar!!)
        }
    }

    private fun setAppHeaderOuter(vararg components: Component) {
        if (appHeaderOuter == null) {
            appHeaderOuter = Div()
            appHeaderOuter!!.addClassName("app-header-outer")
            element.insertChild(0, appHeaderOuter!!.element)
        }
        appHeaderOuter!!.removeAll()
        appHeaderOuter!!.add(*components)
    }

    private fun setAppHeaderInner(vararg components: Component) {
        if (appHeaderInner == null) {
            appHeaderInner = Div()
            appHeaderInner!!.addClassName("app-header-inner")
            column!!.element.insertChild(0, appHeaderInner!!.element)
        }
        appHeaderInner!!.removeAll()
        appHeaderInner!!.add(*components)
    }

    private fun setAppFooterInner(vararg components: Component) {
        if (appFooterInner == null) {
            appFooterInner = Div()
            appFooterInner!!.addClassName("app-footer-inner")
            column!!.element.insertChild(column!!.element.childCount,
                    appFooterInner!!.element)
        }
        appFooterInner!!.removeAll()
        appFooterInner!!.add(*components)
    }

    private fun setAppFooterOuter(vararg components: Component) {
        if (appFooterOuter == null) {
            appFooterOuter = Div()
            appFooterOuter!!.addClassName("app-footer-outer")
            element.insertChild(element.childCount,
                    appFooterOuter!!.element)
        }
        appFooterOuter!!.removeAll()
        appFooterOuter!!.add(*components)
    }

    override fun configurePage(settings: InitialPageSettings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes")
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black")
        settings.addFavIcon("icon", "frontend/images/favicons/favicon.ico",
                "256x256")
    }

    override fun showRouterLayoutContent(content: HasElement) {
        viewContainer!!.element.appendChild(content.element)
    }

    override fun afterNavigation(event: AfterNavigationEvent) {
        if (navigationTabs) {
            afterNavigationWithTabs(event)
        } else {
            afterNavigationWithoutTabs(event)
        }
    }

    private fun afterNavigationWithTabs(e: AfterNavigationEvent) {
        val active = getActiveItem(e)
        if (active == null) {
            if (tabBar!!.tabCount == 0) {
                tabBar!!.addClosableTab("", Home::class.java)
            }
        } else {
            if (tabBar!!.tabCount > 0) {
                tabBar!!.updateSelectedTab(active.text,
                        active.navigationTarget)
            } else {
                tabBar!!.addClosableTab(active.text,
                        active.navigationTarget)
            }
        }
        appBar!!.menuIcon!!.isVisible = false
    }

    private fun getActiveItem(e: AfterNavigationEvent): NaviItem? {
        for (item in naviDrawer!!.menu.naviItems) {
            if (item.isHighlighted(e)) {
                return item
            }
        }
        return null
    }

    private fun afterNavigationWithoutTabs(e: AfterNavigationEvent) {
        val active = getActiveItem(e)
        if (active != null) {
//            appBar!!.title = active.text
            appBar!!.setTitle(active.text)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MainLayout::class.java)
        private const val CLASS_NAME = "root"
        fun get(): MainLayout {
            return UI.getCurrent().children
                    .filter { component: Component -> component.javaClass == MainLayout::class.java }
                    .findFirst().get() as MainLayout
        }
    }

    init {
        VaadinSession.getCurrent().errorHandler = ErrorHandler { errorEvent: ErrorEvent ->
            log.error("Uncaught UI exception",
                    errorEvent.throwable)
            Notification.show(
                    "We are sorry, but an internal error occurred")
        }
        addClassName(CLASS_NAME)
        setFlexDirection(FlexDirection.COLUMN)
        setSizeFull()

        // Initialise the UI building blocks
        initStructure()

        // Populate the navigation drawer
        initNaviItems()

        // Configure the headers and footers (optional)
        initHeadersAndFooters()
    }
}