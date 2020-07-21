package com.test.demo.ui.views

import com.test.demo.backend.DummyData
import com.test.demo.backend.Payment
import com.test.demo.ui.MainLayout
import com.test.demo.ui.components.Badge
import com.test.demo.ui.components.FlexBoxLayout
import com.test.demo.ui.components.ListItem
import com.test.demo.ui.components.detailsdrawer.DetailsDrawer
import com.test.demo.ui.components.detailsdrawer.DetailsDrawerHeader
import com.test.demo.ui.components.navigation.bar.AppBar
import com.test.demo.ui.layout.size.Bottom
import com.test.demo.ui.layout.size.Horizontal
import com.test.demo.ui.layout.size.Top
import com.test.demo.ui.layout.size.Vertical
import com.test.demo.ui.util.FontSize
import com.test.demo.ui.util.LumoStyles
import com.test.demo.ui.util.TextColor
import com.test.demo.ui.util.UIUtils
import com.test.demo.ui.util.css.BoxSizing
import com.test.demo.ui.util.css.WhiteSpace
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent
import com.vaadin.flow.component.tabs.TabsVariant
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.TemplateRenderer
import com.vaadin.flow.data.selection.SelectionEvent
import com.vaadin.flow.function.SerializableFunction
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@PageTitle("Payments")
@Route(value = "payments", layout = MainLayout::class)
class Payments : SplitViewFrame() {
    private var grid: Grid<Payment>? = null
    private var dataProvider: ListDataProvider<Payment>? = null
    private var detailsDrawer: DetailsDrawer? = null

    // Tabs in the drawer
    var details = Tab("Details")
    var attachments = Tab("Attachments")
    var history = Tab("History")
    var tabs = Tabs(details, attachments, history)
    override fun onAttach(attachEvent: AttachEvent) {
        super.onAttach(attachEvent)
        initAppBar()
        setViewContent(createContent())
        setViewDetails(createDetailsDrawer())
        filter()
    }

    private fun initAppBar() {
        val appBar: AppBar = MainLayout.get().appBar!!
        for (status in Payment.Status.values()) {
            appBar.addTab(status.getName())
        }
        appBar.addTabSelectionListener { e: SelectedChangeEvent? ->
            filter()
            detailsDrawer!!.hide()
        }
        appBar.centerTabs()
    }

    private fun createContent(): Component {
        val content = FlexBoxLayout(createGrid())
        content.setBoxSizing(BoxSizing.BORDER_BOX)
        content.setHeightFull()
        content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X)
        return content
    }

    private fun createGrid(): Grid<*> {
        dataProvider = DataProvider.ofCollection(DummyData.getPayments())
        grid = Grid()
        grid!!.addSelectionListener { event: SelectionEvent<Grid<Payment>?, Payment> -> event.firstSelectedItem.ifPresent { payment: Payment -> showDetails(payment) } }
        grid!!.setDataProvider(dataProvider)
        grid!!.setHeightFull()
        val badgeRenderer = ComponentRenderer(
                SerializableFunction { payment: Payment ->
                    val status = payment.status
                    val badge = Badge(status.getName(), status.theme)
                    UIUtils.setTooltip(status.desc, badge)
                    badge
                }
        )
        grid!!.addColumn(badgeRenderer)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setHeader("Status")
        grid!!.addColumn(ComponentRenderer(SerializableFunction { payment: Payment -> createFromInfo(payment) }))
                .setHeader("From").width = "200px"
        grid!!.addColumn(ComponentRenderer(SerializableFunction { payment: Payment -> createToInfo(payment) }))
                .setHeader("To").width = "200px"
        grid!!.addColumn(ComponentRenderer(SerializableFunction { payment: Payment -> createAmount(payment) }))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setHeader("Amount ($)").textAlign = ColumnTextAlign.END
        grid!!.addColumn(TemplateRenderer.of<Payment>("[[item.date]]")
                .withProperty("date") { payment: Payment -> UIUtils.formatDate(payment.date) })
                .setAutoWidth(true)
                .setComparator { obj: Payment -> obj.date }
                .setFlexGrow(0)
                .setHeader("Due Date")
        return grid!!
    }

    private fun createFromInfo(payment: Payment): Component {
        val item = ListItem(payment.from, payment.fromIBAN)
        item.setPadding(Vertical.XS)
        return item
    }

    private fun createToInfo(payment: Payment): Component {
        val item = ListItem(payment.to, payment.toIBAN)
        item.setPadding(Vertical.XS)
        return item
    }

    private fun createAmount(payment: Payment): Component {
        val amount = payment.amount
        return UIUtils.createAmountLabel(amount)
    }

    private fun createDetailsDrawer(): DetailsDrawer {
        detailsDrawer = DetailsDrawer(DetailsDrawer.Position.RIGHT)
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS)
        tabs.addSelectedChangeListener { event: SelectedChangeEvent? -> configureTabs() }
        val detailsDrawerHeader = DetailsDrawerHeader("Payment Details", tabs)
        detailsDrawerHeader.addCloseListener { buttonClickEvent: ClickEvent<Button?>? -> detailsDrawer!!.hide() }
        detailsDrawer!!.setHeader(detailsDrawerHeader)
        return detailsDrawer!!
    }

    private fun showDetails(payment: Payment) {
        configureTabs()
        detailsDrawer!!.show()
    }

    private fun createDetails(payment: Payment): Component {
        val status = ListItem(payment.status.icon,
                payment.status.getName(), "Status")
        status.content.alignItems = FlexComponent.Alignment.BASELINE
        status.content.setSpacing(Bottom.XS)
        UIUtils.setTheme(payment.status.theme.themeName,
                status.primary)
        UIUtils.setTooltip(payment.status.desc, status)
        val from = ListItem(
                UIUtils.createTertiaryIcon(VaadinIcon.UPLOAD_ALT),
                """
                    ${payment.from}
                    ${payment.fromIBAN}
                    """.trimIndent(), "Sender")
        val to = ListItem(
                UIUtils.createTertiaryIcon(VaadinIcon.DOWNLOAD_ALT),
                """
                    ${payment.to}
                    ${payment.toIBAN}
                    """.trimIndent(), "Receiver")
        val amount = ListItem(
                UIUtils.createTertiaryIcon(VaadinIcon.DOLLAR),
                UIUtils.formatAmount(payment.amount), "Amount")
        val date = ListItem(
                UIUtils.createTertiaryIcon(VaadinIcon.CALENDAR),
                UIUtils.formatDate(payment.date), "Date")
        for (item in arrayOf(status, from, to, amount,
                date)) {
            item.setReverse(true)
            item.setWhiteSpace(WhiteSpace.PRE_LINE)
        }
        val details = Div(status, from, to, amount, date)
        details.addClassName(LumoStyles.Padding.Vertical.S)
        return details
    }

    private fun createAttachments(): Component {
        val message = UIUtils.createLabel(FontSize.S, TextColor.SECONDARY, "Not implemented yet.")
        message.addClassNames(LumoStyles.Padding.Responsive.Horizontal.L, LumoStyles.Padding.Vertical.L)
        return message
    }

    private fun createHistory(): Component {
        val message = UIUtils.createLabel(FontSize.S, TextColor.SECONDARY, "Not implemented yet.")
        message.addClassNames(LumoStyles.Padding.Responsive.Horizontal.L, LumoStyles.Padding.Vertical.L)
        return message
    }

    private fun filter() {
        val selectedTab: Tab = MainLayout.get().appBar!!.selectedTab!!
        if (selectedTab != null) dataProvider!!.setFilterByValue({ obj: Payment -> obj.status }, Payment.Status
                .valueOf(selectedTab.label.toUpperCase()))
    }

    private fun configureTabs() {
        val selectedTab = tabs.selectedTab
        if (selectedTab == details) {
            detailsDrawer!!.setContent(
                    createDetails(grid!!.selectionModel.firstSelectedItem.get()))
        } else if (selectedTab == attachments) {
            detailsDrawer!!.setContent(createAttachments())
        } else if (selectedTab == history) {
            detailsDrawer!!.setContent(createHistory())
        }
    }
}