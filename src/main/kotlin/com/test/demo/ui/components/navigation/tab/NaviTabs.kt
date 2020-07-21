package com.test.demo.ui.components.navigation.tab

import com.test.demo.ui.util.UIUtils
import com.test.demo.ui.util.css.Overflow
import com.test.demo.ui.views.Home
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs

/**
 * NaviTabs supports tabs that can be closed, and that can navigate to a
 * specific target when clicked.
 */
class NaviTabs() : Tabs() {
    private val listener = ComponentEventListener { selectedChangeEvent: SelectedChangeEvent? -> navigateToSelectedTab() } as ComponentEventListener<SelectedChangeEvent?>

    /**
     * When adding the first tab, the selection change event is triggered. This
     * will cause the app to navigate to that tab's navigation target (if any).
     * This constructor allows you to add the tabs before the event listener is
     * set.
     */
    constructor(vararg naviTabs: NaviTab?) : this() {
        add(*naviTabs)
    }

    /**
     * Creates a regular tab without any click listeners.
     */
    fun addTab(text: String?): Tab {
        val tab = Tab(text)
        add(tab)
        return tab
    }

    /**
     * Creates a tab that when clicked navigates to the specified target.
     */
    fun addTab(text: String?,
               navigationTarget: Class<out Component?>?): Tab {
        val tab: Tab = NaviTab(text, navigationTarget)
        add(tab)
        return tab
    }

    /**
     * Creates a (closable) tab that when clicked navigates to the specified
     * target.
     */
    fun addClosableTab(text: String?,
                       navigationTarget: Class<out Component?>?): Tab {
        val tab = ClosableNaviTab(text, navigationTarget)
        add(tab)
        tab.closeButton.addClickListener { event: ClickEvent<Button?>? ->
            remove(tab)
            navigateToSelectedTab()
        }
        return tab
    }

    /**
     * Navigates to the selected tab's navigation target if available.
     */
    fun navigateToSelectedTab() {
        if (selectedTab is NaviTab) {
            try {
                UI.getCurrent().navigate(
                        (selectedTab as NaviTab).navigationTarget)
            } catch (e: Exception) {
                // @todo this is an code flow by exception anti-pattern. Either
                // handle the case without the exception, or
                // @todo at least document meticulously why this can't be done
                // any other way and what kind of exceptions are we catching
                // @todo and when they can occur.
                // @todo this block consumes all exceptions, even
                // backend-originated, and may result in exceptions disappearing
                // mysteriously.

                // If the right-most tab is closed, the Tabs component does not
                // auto-select tabs on the left.
                if (tabCount > 0) {
                    selectedIndex = tabCount - 1
                } else {
                    UI.getCurrent().navigate(Home::class.java)
                }
            }
        }
    }

    /**
     * Updates the current tab's name and navigation target.
     */
    fun updateSelectedTab(text: String?,
                          navigationTarget: Class<out Component?>?) {
        val tab = selectedTab
        tab.label = text
        if (tab is NaviTab) {
            tab.navigationTarget = navigationTarget
        }
        (tab as? ClosableNaviTab)?.add(tab.closeButton)
        navigateToSelectedTab()
    }

    /**
     * Returns the number of tabs.
     */
    val tabCount: Int
        get() = Math.toIntExact(children
                .filter { component: Component? -> component is Tab }.count())

    init {
        addSelectedChangeListener(listener)
        element.setAttribute("overflow", "end")
        UIUtils.setOverflow(Overflow.HIDDEN, this)
    }
}