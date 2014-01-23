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
package dk.dma.epd.shore.layers.intendedroute;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.bbn.openmap.omGraphics.OMGraphic;

import dk.dma.epd.common.prototype.ais.VesselTarget;
import dk.dma.epd.common.prototype.layers.intendedroute.IntendedRouteInfoPanel;
import dk.dma.epd.common.prototype.layers.intendedroute.IntendedRouteLayerCommon;
import dk.dma.epd.common.prototype.layers.intendedroute.IntendedRouteLegGraphic;
import dk.dma.epd.common.prototype.layers.intendedroute.IntendedRouteWpCircle;
import dk.dma.epd.shore.gui.views.JMapFrame;
import dk.dma.epd.shore.gui.views.MapMenu;
import dk.dma.epd.shore.layers.GeneralLayer;

/**
 * 
 * Layer for displaying intended routes in {@linkplain EPDShore}
 */
public class IntendedRouteLayer extends IntendedRouteLayerCommon {

    private static final long serialVersionUID = 1L;

    protected JMapFrame jMapFrame;
    
    /**
     * Returns the mouse mode service list
     * @return the mouse mode service list
     */
    @Override
    public String[] getMouseModeServiceList() {
        return GeneralLayer.getDefaultMouseModeServiceList();
    }

    /**
     * {@inheritDoc}
     */
    public void findAndInit(Object obj) {
        super.findAndInit(obj);
        
        if (obj instanceof JMapFrame && intendedRouteInfoPanel == null) {
            jMapFrame = (JMapFrame) obj;
            intendedRouteInfoPanel = new IntendedRouteInfoPanel();
            getGlassPanel().add(intendedRouteInfoPanel);
        }
    }

    /**
     * Returns a reference to the glass pane
     * @return a reference to the glass pane
     */
    protected JPanel getGlassPanel() {
        return jMapFrame.getGlassPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mouseClicked(MouseEvent e) {
        
        OMGraphic newClosest = getSelectedGraphic(
                e, 
                IntendedRouteWpCircle.class,
                IntendedRouteLegGraphic.class);

        if (e.getButton() == MouseEvent.BUTTON3 && newClosest != null) {
            
            if (newClosest instanceof IntendedRouteWpCircle) {
    
                IntendedRouteWpCircle wpCircle = (IntendedRouteWpCircle) newClosest;
                VesselTarget vesselTarget = wpCircle.getIntendedRouteGraphic().getVesselTarget();
                ((MapMenu)mapMenu).aisSuggestedRouteMenu(vesselTarget);
                mapMenu.setVisible(true);
                mapMenu.show(this, e.getX() - 2, e.getY() - 2);
                return true;
                
            } else if (newClosest instanceof IntendedRouteLegGraphic) {
    
                IntendedRouteLegGraphic wpCircle = (IntendedRouteLegGraphic) newClosest;
                VesselTarget vesselTarget = wpCircle.getIntendedRouteGraphic().getVesselTarget();
                ((MapMenu)mapMenu).aisSuggestedRouteMenu(vesselTarget);
                mapMenu.setVisible(true);
                mapMenu.show(this, e.getX() - 2, e.getY() - 2);
                return true;
            }
        }
        return false;
    }
}
