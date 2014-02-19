/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.epd.shore.gui.route.strategic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import dk.dma.epd.common.graphics.Resources;
import dk.dma.epd.common.prototype.ais.VesselStaticData;
import dk.dma.epd.common.prototype.ais.VesselTarget;
import dk.dma.epd.common.prototype.model.route.Route;
import dk.dma.epd.shore.EPDShore;
import dk.dma.epd.shore.ais.AisHandler;
import dk.dma.epd.shore.gui.route.RoutePropertiesDialog;
import dk.dma.epd.shore.gui.settingtabs.GuiStyler;
import dk.dma.epd.shore.gui.views.NotificationCenter;
import dk.dma.epd.shore.service.StrategicRouteHandler;
import dk.dma.epd.shore.service.StrategicRouteNegotiationData;
import dk.dma.epd.shore.voyage.Voyage;
import dk.dma.epd.shore.voyage.VoyageManager;

public class StrategicRouteNotificationPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    Border paddingLeft = BorderFactory.createMatteBorder(0, 8, 0, 0, new Color(
            65, 65, 65));
    Border paddingBottom = BorderFactory.createMatteBorder(0, 0, 5, 0,
            new Color(83, 83, 83));
    Border notificationPadding = BorderFactory.createCompoundBorder(
            paddingBottom, paddingLeft);
    Border notificationsIndicatorImportant = BorderFactory.createMatteBorder(0,
            0, 0, 10, new Color(206, 120, 120));
    Border paddingLeftPressed = BorderFactory.createMatteBorder(0, 8, 0, 0,
            new Color(45, 45, 45));
    Border notificationPaddingPressed = BorderFactory.createCompoundBorder(
            paddingBottom, paddingLeftPressed);

    private JTable routeTable;

    private JPanel pane_3;
    private JScrollPane scrollPane_1;

    private Color backgroundColor = new Color(83, 83, 83);
    // private JTextPane area = new JTextPane();
    private StrategicRouteNotificationInternalPanel area = new StrategicRouteNotificationInternalPanel();

    private JLabel route_details;
    private JLabel optimize_btn;
    private JLabel handle_request;

    private JPanel masterPanel;

    private DefaultListSelectionModel values;
    private JPanel headerPanel;

    private JScrollPane leftScrollPane;

    private StrategicRouteTableModel routeTableModel;

    private int currentSelection = -1;

    private JPanel rightPanel;
    private JPanel leftPanel;
    // private EnavServiceHandler enavServiceHandler;
    private AisHandler aisHandler;
    private VoyageManager voyageManager;
    private NotificationCenter notCenter;
    private StrategicRouteHandler strategicRouteHandler;

    public StrategicRouteNotificationPanel(NotificationCenter notCenter) {
        this.notCenter = notCenter;
        GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
        gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_2.gridx = 1;
        gbc_scrollPane_2.gridy = 1;
        gbc_scrollPane_2.gridheight = 1;
        gbc_scrollPane_2.insets = new Insets(-10, 0, 0, 0);
        String[] colHeadings = { "ID", "Title" };
        DefaultTableModel model = new DefaultTableModel(30, colHeadings.length);
        model.setColumnIdentifiers(colHeadings);

        // Right
        GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
        gbc_scrollPane_3.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_3.gridx = 2;
        gbc_scrollPane_3.gridy = 0;
        gbc_scrollPane_3.insets = new Insets(10, 0, 0, 0);

        GridBagConstraints gbc_scrollPane_1_1 = new GridBagConstraints();
        gbc_scrollPane_1_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1_1.gridx = 2;
        gbc_scrollPane_1_1.gridy = 1;
        setLayout(null);

        masterPanel = new JPanel();
        masterPanel.setBounds(0, 0, 800, 600);
        masterPanel.setBackground(backgroundColor);

        // masterPanel.add(mapPanel, BorderLayout.NORTH);

        masterPanel.setBorder(BorderFactory.createEtchedBorder(
                EtchedBorder.LOWERED, new Color(30, 30, 30), new Color(45, 45,
                        45)));

        masterPanel.setLayout(null);
        this.add(masterPanel);

        leftPanel = new JPanel();
        leftPanel.setBounds(0, 0, 345, 600);
        leftPanel.setBackground(backgroundColor);
        masterPanel.add(leftPanel);
        leftPanel.setLayout(null);

        leftScrollPane = new JScrollPane();
        leftScrollPane.setBounds(0, 20, 345, 560);
        leftPanel.add(leftScrollPane);
        leftScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        leftScrollPane
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftScrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        // Center
        // MARKER GOES HERE
        // headerPanel = new JPanel();
        // headerPanel.setBounds(0, 0, 300, 10);
        // leftPanel.add(headerPanel);
        // headerPanel.setBackground(new Color(39, 39, 39));
        // headerPanel.setPreferredSize(new Dimension(300, 10));
        // headerPanel.setLayout(null);
        //

        headerPanel = new JPanel(new FlowLayout(0));
        headerPanel.setBackground(new Color(39, 39, 39));
        // headerPanel.setPreferredSize(new Dimension(400, 15));
        headerPanel.setBounds(0, 0, 400, 20);
        // headerPanel.setSize(new Dimension(300, 10));
        ((FlowLayout) headerPanel.getLayout()).setHgap(0);
        leftPanel.add(headerPanel);

        routeTable = new JTable();

        routeTable = new JTable(model) {
            private static final long serialVersionUID = 1L;

            public Component prepareRenderer(TableCellRenderer renderer,
                    int Index_row, int Index_col) {
                Component comp = super.prepareRenderer(renderer, Index_row,
                        Index_col);
                if (Index_row % 2 == 0) {
                    comp.setBackground(new Color(49, 49, 49));
                } else {
                    comp.setBackground(new Color(65, 65, 65));
                }

    
                // Paint based on awk
                if (routeTableModel != null) {
                    if (routeTableModel.isAwk(Index_row) && Index_col == 0) {
                        comp.setForeground(new Color(130, 165, 80));
                    } else if (!routeTableModel.isAwk(Index_row)
                            && Index_col == 0) {
                        comp.setForeground(new Color(165, 80, 80));
                    }

                }
                
                if (isCellSelected(Index_row, Index_col)) {
                    comp.setForeground(Color.white);
                    comp.setBackground(new Color(85, 85, 85));
                }


                return comp;
            }

        };

        routeTable.setTableHeader(null);
        routeTable.setBorder(new EmptyBorder(0, 0, 0, 0));
        routeTable.setIntercellSpacing(new Dimension(0, 0));
        routeTable.setBackground(new Color(49, 49, 49));
        routeTable.setShowVerticalLines(false);
        routeTable.setShowHorizontalLines(false);
        routeTable.setShowGrid(false);
        routeTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        routeTable.setForeground(Color.white);
        routeTable.setSelectionForeground(Color.gray);
        routeTable.setRowHeight(20);
        routeTable.setFocusable(false);
        routeTable.setAutoResizeMode(0);

        leftScrollPane.getViewport().setBackground(backgroundColor);

        leftScrollPane.setViewportView(routeTable);
        // scrollPane_2.setViewportView(routeTable);

        leftScrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
                new Color(30, 30, 30)));
        // ((FlowLayout) headerPanel.getLayout()).setHgap(0);
        GridBagConstraints gbc_test = new GridBagConstraints();
        gbc_test.fill = GridBagConstraints.HORIZONTAL;
        gbc_test.gridx = 1;
        gbc_test.gridy = 0;
        gbc_test.gridheight = 1;
        gbc_test.insets = new Insets(0, 0, 0, 5);

        rightPanel = new JPanel();
        rightPanel.setBounds(345, 0, 410, 600);
        rightPanel.setBackground(backgroundColor);
        masterPanel.add(rightPanel);
        rightPanel.setLayout(null);
        pane_3 = new JPanel();
        pane_3.setBounds(35, 11, 345, 30);
        rightPanel.add(pane_3);
        pane_3.setBackground(backgroundColor);
        pane_3.setLayout(new FlowLayout());
        pane_3.setVisible(true);

        Resources res = EPDShore.res().folder("images/notificationcenter");
        route_details = new JLabel("Route Details", res.getCachedImageIcon("routes.png"),
                SwingConstants.CENTER);
        GuiStyler.styleButton(route_details);
        route_details.setPreferredSize(new Dimension(110, 20));
        pane_3.add(route_details);
        route_details.setEnabled(false);

        handle_request = new JLabel("Handle Request", res.getCachedImageIcon("arrow-circle-315.png"),
                SwingConstants.CENTER);
        GuiStyler.styleButton(handle_request);
        handle_request.setPreferredSize(new Dimension(110, 20));
        pane_3.add(handle_request);
        handle_request.setEnabled(false);

        optimize_btn = new JLabel("Optimize", res.getCachedImageIcon("gear--arrow.png"),
                SwingConstants.CENTER);
        optimize_btn.setEnabled(false);
        GuiStyler.styleButton(optimize_btn);
        optimize_btn.setPreferredSize(new Dimension(110, 20));
//        pane_3.add(optimize_btn);
//        optimize_btn.setEnabled(false);

        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(0, 41, 408, 541);
        rightPanel.add(scrollPane_1);
        scrollPane_1.setViewportView(area);

        // area.setEditable(false);
        // area.setContentType("text/html");
        // area.setPreferredSize(new Dimension(2000, 1000));
        area.setLayout(null);
        area.setBackground(backgroundColor);
        // area.setMargin(new Insets(10, 10, 10, 10));
        scrollPane_1.setVisible(true);

        // area.setText("");

        addMouseListeners();

        scrollPane_1.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                new Color(30, 30, 30)));

    }

    public void initTable() {
        routeTableModel = (StrategicRouteTableModel) routeTable
                .getModel();
        routeTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        routeTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        routeTable.getColumnModel().getColumn(2).setPreferredWidth(90);
        routeTable.getColumnModel().getColumn(3).setPreferredWidth(85);
        // routeTable.getColumnModel().getColumn(4).setPreferredWidth(65);

        routeTable.getSelectionModel().addListSelectionListener(
                new RouteExchangeRowListener());

        headerPanel.add(createHeaderColumn(routeTableModel.getColumnName(0),
                100));
        headerPanel
                .add(createHeaderColumn(routeTableModel.getColumnName(1), 70));
        headerPanel
                .add(createHeaderColumn(routeTableModel.getColumnName(2), 90));
        headerPanel
                .add(createHeaderColumn(routeTableModel.getColumnName(3), 85));
        // headerPanel
        // .add(createHeaderColumn(routeTableModel.getColumnName(4), 65));

        routeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        routeTable.setRowSelectionAllowed(true);
        routeTable.setColumnSelectionAllowed(false);

        if (routeTable.getRowCount() > 0) {
            routeTable.setRowSelectionInterval(0, 0);
            readMessage(0);
        }

    }

    public void readMessage(int selection, int msgindex) {
        routeTable.setRowSelectionInterval(selection, selection);
        readMessage(selection);
    }

    private JPanel createHeaderColumn(String name, int width) {
        JPanel container = new JPanel();
        container.setSize(width, 10);
        container.setPreferredSize(new Dimension(width, 10));
        container.setBackground(new Color(39, 39, 39));
        container.setBounds(0, 0, width, 15);
        ((FlowLayout) container.getLayout()).setVgap(0);
        ((FlowLayout) container.getLayout()).setHgap(0);

        JLabel col = new JLabel(name);
        col.setSize(width, 10);
        col.setPreferredSize(new Dimension(width, 10));
        col.setForeground(Color.white);
        col.setFont(new Font("Arial", Font.BOLD, 9));
        if ("ID".equals(name)) {
            col.setHorizontalAlignment(0);
        }
        container.add(col);
        return container;
    }

    public void readMessage(int selectedRow) {

        if (routeTableModel.isAwk(selectedRow)) {
            handle_request.setEnabled(false);
        } else {
            handle_request.setEnabled(true);
        }
        optimize_btn.setEnabled(true);
        route_details.setEnabled(true);

        // Update area

        StrategicRouteNegotiationData message = ((StrategicRouteTableModel) routeTable.getModel()).getMessages().get(selectedRow);

        VesselTarget aisData = aisHandler.getVesselTarget(message.getRouteMessage().get(0).getMmsi());        
        
        
        if (message != null) {

            if (selectedRow == -1) {
                System.out.println("bad select row");
            } else {
                
                if (aisData.getStaticData() != null && message != null){
                    area.updateLabels(message, aisData);    
                }else{
                    area.updateLabels(message);
                }
                
                
            }
        }

    }

    public void addMouseListeners() {

        route_details.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {

                if (route_details.isEnabled()) {
                    StrategicRouteNegotiationData message = routeTableModel
                            .getMessages().get(currentSelection);

                    RoutePropertiesDialog routePropertiesDialog = new RoutePropertiesDialog(
                            EPDShore.getInstance().getMainFrame(), 
                            EPDShore.getInstance().getMainFrame().getActiveMapWindow().getChartPanel(),
                            new Route(message.getRouteMessage().get(message.getRouteMessage().size()-1).getRoute()));
                    
                    routePropertiesDialog.setVisible(true);

                    // route_details.setEnabled(false);
                }
            }
        });

        handle_request.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (handle_request.isEnabled()) {
                    StrategicRouteNegotiationData message = routeTableModel
                            .getMessages().get(currentSelection);

                    String shipName = "" + message.getMmsi();

                    VesselStaticData staticData = aisHandler.getVesselTarget(message.getMmsi()).getStaticData();
                    if (staticData != null) {
                        shipName = staticData.getName();
                    }

                    // Get latest route
                    Route route = new Route(message.getRouteMessage()
                            .get(message.getRouteMessage().size() - 1)
                            .getRoute());

                    Voyage voyage = new Voyage(message.getMmsi(), route,
                            message.getId());

                    Route originalRoute = new Route(message.getRouteMessage().get(0).getRoute());
                    
                    EPDShore.getInstance().getMainFrame().addStrategicRouteExchangeHandlingWindow(originalRoute,
                            shipName, voyage, false);

                    notCenter.setVisible(false);

                }
            }
        });

        optimize_btn.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (optimize_btn.isEnabled()) {
                    // TODO
                }
            }
        });

    }

    public void updateTable() {
        routeTableModel.updateMessages();
        routeTable.updateUI();

    }

    public JTable getRouteTable() {
        return routeTable;
    }

    public void setAisHandler(AisHandler aisHandler) {
        this.aisHandler = aisHandler;
    }

    public VoyageManager getVoyageManager() {
        return voyageManager;
    }

    public void setVoyageManager(VoyageManager voyageManager) {
        this.voyageManager = voyageManager;
    }

    public StrategicRouteHandler getStrategicRouteHandler() {
        return strategicRouteHandler;
    }

    public void setStrategicRouteHandler(StrategicRouteHandler strategicRouteHandler) {
        this.strategicRouteHandler = strategicRouteHandler;
    }

    private class RouteExchangeRowListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {

            if (event.getValueIsAdjusting()) {
                return;
            }

            values = (DefaultListSelectionModel) event.getSource();

            currentSelection = values.getAnchorSelectionIndex();

            // Enable buttons?

            if (values.getAnchorSelectionIndex() == -1) {
                // removeArea();
                return;
            } else {
                readMessage(values.getAnchorSelectionIndex());
            }

        }
    }

}