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
package dk.dma.epd.shore.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.util.Iterator;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import dk.dma.epd.common.graphics.Resources;
import dk.dma.epd.common.prototype.gui.InternalComponentFrame;
import dk.dma.epd.common.prototype.gui.IMapFrame;
import dk.dma.epd.common.prototype.layers.EPDLayerCommon;
import dk.dma.epd.common.prototype.settings.gui.MapCommonSettings;
import dk.dma.epd.common.prototype.settings.observers.MapCommonSettingsListener;
import dk.dma.epd.shore.EPDShore;
import dk.dma.epd.shore.event.ToolbarMoveMouseListener;

/**
 * Class for setting up a map frame
 * 
 * @author Steffen D. Sommer (steffendsommer@gmail.com), David A. Camre (davidcamre@gmail.com)
 */
public class JMapFrame extends InternalComponentFrame implements IMapFrame {

    private static final long serialVersionUID = 1L;
    protected ChartPanel chartPanel;
    boolean locked;
    boolean alwaysInFront;
    MouseMotionListener[] actions;
    protected int id;
    protected final MainFrame mainFrame;
    protected JLabel moveHandler;
    protected JPanel mapPanel;
    protected JPanel masterPanel;
    protected JLabel maximize;
    protected MapMenu mapMenu;

    protected static int moveHandlerHeight = 18;
    protected boolean maximized;
    public int width;
    public int height;
    protected static int chartPanelOffset = 12;
    JInternalFrame mapFrame;
    protected JPanel glassPanel;

    MapFrameType type = MapFrameType.standard;

    LayerTogglingPanel layerTogglingPanel = new LayerTogglingPanel();

    /**
     * Map settings for this frame.
     */
    private final MapCommonSettings<MapCommonSettingsListener> mapSettings;
    
    /**
     * Constructor for setting up the map frame
     * 
     * @param id
     *            id number for this map frame
     * @param mainFrame
     *            reference to the mainframe
     * @param frameMapSettings
     *            The map settings that this frame should use.
     */
    public JMapFrame(int id, MainFrame mainFrame, final MapFrameType type, MapCommonSettings<MapCommonSettingsListener> frameMapSettings) {
        super("New Window " + id, true, true, true, true);
        this.mapSettings = Objects.requireNonNull(frameMapSettings);
        this.mainFrame = mainFrame;
        this.id = id;
        this.type = type;

        // Initialize the glass pane
        initGlassPane();

        chartPanel = new ChartPanel(mainFrame, this);
        this.setContentPane(chartPanel);

        setContentPane(chartPanel);

        new Thread(new Runnable() {

            @Override
            public void run() {
                chartPanel.initChart(type);
            }
        }).run();

        initGUI();

        layerTogglingPanel.setChartPanel(chartPanel);

        this.setVisible(true);

        setVisible(true);

    }

    /**
     * Get a reference to the {@link MapCommonSettings} used by this frame.
     * @return The {@link MapCommonSettings} used by this frame.
     */
    public MapCommonSettings<MapCommonSettingsListener> getMapSettings() {
        return this.mapSettings;
    }
    
    /**
     * Overloaded constructor for setting up the map frame
     * 
     * @param id
     *            id number for this map frame
     * @param mainFrame
     *            reference to the map frame
     * @param center
     *            where to center map
     * @param scale
     *            map zoom level
     * @param frameMapSettings
     *            The map settings that this frame should use.
     */
    public JMapFrame(int id, MainFrame mainFrame, Point2D center, float scale, MapCommonSettings<MapCommonSettingsListener> frameMapSettings) {

        super("New Window " + id, true, true, true, true);

        this.mainFrame = mainFrame;
        this.id = id;
        this.mapSettings = Objects.requireNonNull(frameMapSettings);
        // Initialize the glass pane
        initGlassPane();

        chartPanel = new ChartPanel(mainFrame, this);
        this.setContentPane(chartPanel);
        this.setVisible(true);

        chartPanel.initChart(center, scale);
        initGUI();

        layerTogglingPanel.setChartPanel(chartPanel);
    }

    /**
     * Function for initializing the glasspane
     */
    private void initGlassPane() {
        glassPanel = (JPanel) getGlassPane();
        glassPanel.setLayout(null);
        glassPanel.setVisible(false);

        layerTogglingPanel.setParent(this);
        // layerTogglingPanel.setBounds(0, 20, 208, 300);

        glassPanel.add(layerTogglingPanel);
        glassPanel.setVisible(true);
        layerTogglingPanel.setVisible(true);

    }

    /**
     * Function for getting the glassPanel of the map frame
     * 
     * @return glassPanel the glassPanel of the map frame
     */
    @Override
    public JPanel getGlassPanel() {
        return glassPanel;
    }

    /**
     * Returns a reference to the map frame cast as a component
     * 
     * @return a reference to the map frame cast as a component
     */
    @Override
    public Component asComponent() {
        return this;
    }

    /**
     * Function for setting the map frame always on top
     */
    public void alwaysFront() {

        if (alwaysInFront) {
            alwaysInFront = false;
        } else {
            alwaysInFront = true;
        }

        mainFrame.getDesktop().getManager().addToFront(id, this);
    }

    /**
     * Function for getting the chartpanel(map) of the map frame
     * 
     * @return
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
     * Function for getting the id of the map frame
     * 
     * @return id id of the map frame
     */
    public int getId() {
        return id;
    }

    /**
     * Function for setting up custom GUI for the map frame
     */
    public void initGUI() {
        makeKeyBindings();

        mapFrame = this;

        // Listen for resize
        mapFrame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaintMapWindow();
            }
        });

        // Strip off
        setRootPaneCheckingEnabled(false);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // Map tools
        mapPanel = new JPanel(new GridLayout(1, 3));
        mapPanel.setPreferredSize(new Dimension(500, moveHandlerHeight));
        mapPanel.setOpaque(true);
        mapPanel.setBackground(Color.DARK_GRAY);
        mapPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(30, 30, 30)));

        ToolbarMoveMouseListener mml = new ToolbarMoveMouseListener(this, mainFrame);
        mapPanel.addMouseListener(mml);
        mapPanel.addMouseMotionListener(mml);

        // Placeholder - for now
        mapPanel.add(new JLabel());

        // Movehandler/Title dragable)
        moveHandler = new JLabel("New Window " + id, SwingConstants.CENTER);
        moveHandler.setFont(new Font("Arial", Font.BOLD, 9));
        moveHandler.setForeground(new Color(200, 200, 200));
        moveHandler.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getClickCount() == 2) {
                    rename();
                }
            }
        });
        moveHandler.addMouseListener(mml);
        moveHandler.addMouseMotionListener(mml);
        actions = moveHandler.getListeners(MouseMotionListener.class);
        mapPanel.add(moveHandler);

        // The tools (minimize, maximize and close)
        JPanel mapToolsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        mapToolsPanel.setOpaque(false);
        mapToolsPanel.setPreferredSize(new Dimension(60, 50));

        final Resources windowRes = EPDShore.res().folder("images/window");
        JLabel minimize = new JLabel(windowRes.getCachedImageIcon("minimize.png"));
        minimize.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                try {
                    mapFrame.setIcon(true);
                } catch (PropertyVetoException e1) {
                    e1.printStackTrace();
                }
            }

        });
        minimize.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 3));
        mapToolsPanel.add(minimize);

        maximize = new JLabel(windowRes.getCachedImageIcon("maximize.png"));
        maximize.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                try {
                    if (maximized) {
                        mapFrame.setMaximum(false);
                        maximized = false;
                        maximize.setIcon(windowRes.getCachedImageIcon("maximize.png"));
                    } else {
                        mapFrame.setMaximum(true);
                        maximized = true;
                        maximize.setIcon(windowRes.getCachedImageIcon("restore.png"));
                    }
                } catch (PropertyVetoException e1) {
                    e1.printStackTrace();
                }
            }

        });
        maximize.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        mapToolsPanel.add(maximize);

        JLabel close = new JLabel(windowRes.getCachedImageIcon("close.png"));
        close.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {

                try {
                    mapFrame.setClosed(true);
                } catch (PropertyVetoException e1) {
                    e1.printStackTrace();
                }
            }

        });
        close.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 2));
        mapToolsPanel.add(close);
        mapPanel.add(mapToolsPanel);

        // Create the masterpanel for aligning
        masterPanel = new JPanel(new BorderLayout());
        masterPanel.add(mapPanel, BorderLayout.NORTH);
        masterPanel.add(chartPanel, BorderLayout.SOUTH);
        masterPanel.setBackground(new Color(45, 45, 45));
        masterPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(30, 30, 30), new Color(45, 45, 45)));

        this.setContentPane(masterPanel);
        ImageIcon minimizedIcon = EPDShore.res().getCachedImageIcon("images/settings/map.png");
        this.setFrameIcon(minimizedIcon);
        this.iconable = false;
        repaintMapWindow();

        // Init the map right click menu
        setMapMenu(new MapMenu());
        chartPanel.getMapHandler().add(getMapMenu());

    }

    public void setMaximizedIcon() {
        maximized = true;
        maximize.setIcon(EPDShore.res().getCachedImageIcon("images/window/restore.png"));
    }

    /**
     * Function for getting the status of map frame in terms of in front
     * 
     * @return
     */
    public boolean isInFront() {
        return alwaysInFront;
    }

    /**
     * @return the type
     */
    public MapFrameType getType() {
        return type;
    }

    /**
     * Function for getting the status of map frame in terms of locked/unlocked
     * 
     * @return
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Function for locking/unlocking the map frame
     */
    public void lockUnlockWindow() {

        if (locked) {

            this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            masterPanel.add(mapPanel, BorderLayout.NORTH);
            masterPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(30, 30, 30), new Color(45, 45,
                    45)));
            locked = false;
            mapFrame.setResizable(true);

        } else {
            setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(30, 30, 30), new Color(45, 45, 45)));
            masterPanel.setBorder(null);
            masterPanel.remove(mapPanel);
            locked = true;
            mapFrame.setResizable(false);

        }

        repaintMapWindow();
    }

    /**
     * Function for setting the key bindings for the map frame
     */
    protected void makeKeyBindings() {

        JPanel content = (JPanel) getContentPane();
        InputMap inputMap = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        @SuppressWarnings("serial")
        Action zoomIn = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                chartPanel.doZoom(0.5f);
            }
        };

        @SuppressWarnings("serial")
        Action zoomOut = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                chartPanel.doZoom(2f);
            }
        };

        @SuppressWarnings("serial")
        Action panUp = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                chartPanel.pan(1);
            }
        };
        @SuppressWarnings("serial")
        Action panDown = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                chartPanel.pan(2);
            }
        };

        @SuppressWarnings("serial")
        Action panLeft = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                chartPanel.pan(3);
            }
        };
        @SuppressWarnings("serial")
        Action panRight = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                chartPanel.pan(4);
            }
        };

        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, 0), "ZoomIn");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PLUS, 0), "ZoomIn");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, 0), "ZoomOut");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, 0), "ZoomOut");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0), "panUp");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0), "panDown");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, 0), "panLeft");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, 0), "panRight");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_KP_UP, 0), "panUp");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_KP_DOWN, 0), "panDown");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_KP_LEFT, 0), "panLeft");
        inputMap.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_KP_RIGHT, 0), "panRight");

        content.getActionMap().put("ZoomOut", zoomOut);
        content.getActionMap().put("ZoomIn", zoomIn);
        content.getActionMap().put("panUp", panUp);
        content.getActionMap().put("panDown", panDown);
        content.getActionMap().put("panLeft", panLeft);
        content.getActionMap().put("panRight", panRight);

    }

    /**
     * Function for renaming the map frame
     */
    public void rename() {

        String title = JOptionPane.showInputDialog(this, "Enter a new title:", this.getTitle());

        if (title != null) {

            this.setTitle(title);
            mainFrame.renameMapWindow(this);
            moveHandler.setText(title);
        }
    }

    /**
     * Function for repainting the mapframe after e.g. resize
     */
    public void repaintMapWindow() {

        width = mapFrame.getSize().width;
        int innerHeight = mapFrame.getSize().height - moveHandlerHeight - chartPanelOffset;
        height = mapFrame.getSize().height;

        if (locked) {
            innerHeight = mapFrame.getSize().height - 4; // 4 for border
        }

        // And finally set the size and repaint it
        chartPanel.setSize(width, innerHeight);
        chartPanel.setPreferredSize(new Dimension(width, innerHeight));

        this.setSize(width, height);

        layerTogglingPanel.checkPosition();
        this.revalidate();
        this.repaint();

    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        moveHandler.setText(title);
    }

    public MapMenu getMapMenu() {
        return mapMenu;
    }

    public void setMapMenu(MapMenu mapMenu) {
        this.mapMenu = mapMenu;
    }

    /**
     * Find and init bean function used in initializing other classes
     */
    public void findAndInit(Iterator<?> it) {
        while (it.hasNext()) {
            Object object = it.next();
            findAndInit(object);

            if (object instanceof EPDLayerCommon) {
                layerTogglingPanel.addLayerFunctionality((EPDLayerCommon) object);
            }
            // NB: The ENC layer is added when layerTogglingPanel.setChartPanel() is called
        }
    }

    public LayerTogglingPanel getLayerTogglingPanel() {
        return layerTogglingPanel;
    }

}
