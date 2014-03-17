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
package dk.dma.epd.common.prototype.settings.layers;

import java.util.Properties;

import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.util.PropUtils;

import dk.dma.epd.common.prototype.settings.ObservedSettings;

/**
 * <p>
 * A base class for maintaining settings that apply to an individual layer. I.e.
 * this class should be used as an abstract base class when writing classes that
 * store settings that are specifically targeted at a given type of layer.
 * </p>
 * <p>
 * If you discover a setting that is relevant to <b>all</b> layer types, you
 * should place that setting in this class.
 * </p>
 * <p>
 * This class inherits from {@link ObservedSettings} which allows clients to
 * register for notifications of changes to any setting maintained by this
 * class.
 * </p>
 * 
 * @param <OBSERVER>
 *            The type of the observers observing the {@code LayerSettings} for
 *            changes.
 * @author Janus Varmarken
 */
public abstract class LayerSettings<OBSERVER extends ILayerSettingsObserver>
        extends ObservedSettings<OBSERVER> {
    /*
     * Add settings that are relevant to all layer types here.
     */

    /**
     * The key in the properties file for the setting that specifies if the
     * layer should be visible.
     */
    private static final String KEY_VISIBLE = "layerVisible";

    /**
     * The key in the properties file for the setting that specifies the maximum
     * distance between the mouse cursor and a graphic element for the graphic
     * element to be interactable.
     */
    private static final String KEY_GRAPHIC_INTERACT_TOLERANCE = "graphicInteractTolerance";

    /**
     * Specifies if the layer should be displayed.
     */
    private boolean visible = true;

    /**
     * Specifies the radius of an invisible circle surrounding the mouse cursor
     * for which any overlapping {@link OMGraphic} is considered interactable
     * (i.e. can be clicked, hovered etc.). Increasing this value will make the
     * layer more tolerant to imprecise mouse selection/pointing.
     */
    private float graphicInteractTolerance = 5.0f;

    /**
     * Get if the layer should be displayed.
     * 
     * @return {@code true} if the layer should be displayed, {@code false} if
     *         it should be hidden.
     */
    public boolean isVisible() {
        try {
            this.settingLock.readLock().lock();
            return this.visible;
        } finally {
            this.settingLock.readLock().unlock();
        }
    }

    /**
     * Set if the layer should be displayed.
     * 
     * @param visible
     *            {@code true} to display the layer, {@code false} to hide the
     *            layer.
     */
    public void setVisible(boolean visible) {
        try {
            this.settingLock.writeLock().lock();
            if (this.visible == visible) {
                // No change, no need to notify observers.
                return;
            }
            this.visible = visible;
            for (OBSERVER obs : this.observers) {
                obs.isVisibleChanged(this.visible);
            }
        } finally {
            this.settingLock.writeLock().unlock();
        }
    }

    /**
     * Get the value that specifies the radius (in pixels) of an invisible
     * circle surrounding the mouse cursor for which any overlapping
     * {@link OMGraphic} is considered interactable (i.e. can be clicked,
     * hovered etc.).
     * 
     * @return The interaction radius in pixels.
     */
    public float getGraphicInteractTolerance() {
        try {
            this.settingLock.readLock().lock();
            return this.graphicInteractTolerance;
        } finally {
            this.settingLock.readLock().unlock();
        }
    }

    /**
     * Set the value that specifies the radius (in pixels) of an invisible
     * circle surrounding the mouse cursor for which any overlapping
     * {@link OMGraphic} is considered interactable (i.e. can be clicked,
     * hovered etc.). Increasing this value will make the layer more tolerant to
     * imprecise mouse selection/pointing.
     * 
     * @param graphicInteractTolerance
     *            The new interaction radius in pixels.
     */
    public void setGraphicInteractTolerance(float graphicInteractTolerance) {
        try {
            this.settingLock.writeLock().lock();
            if (this.graphicInteractTolerance == graphicInteractTolerance) {
                // No change, no need to notify observers.
                return;
            }
            this.graphicInteractTolerance = graphicInteractTolerance;
            for (OBSERVER obs : this.observers) {
                obs.graphicInteractToleranceChanged(this.graphicInteractTolerance);
            }
        } finally {
            this.settingLock.writeLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>NOTE: This is a concrete implementation. Any subclass should make sure
     * to invoke the super implementation.</b>
     * </p>
     */
    @Override
    protected void onLoadSuccess(Properties settings) {
        this.settingLock.writeLock().lock();
        this.setVisible(PropUtils.booleanFromProperties(settings, KEY_VISIBLE,
                this.visible));
        this.setGraphicInteractTolerance(PropUtils.floatFromProperties(
                settings, KEY_GRAPHIC_INTERACT_TOLERANCE,
                this.graphicInteractTolerance));
        this.settingLock.writeLock().unlock();
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>NOTE: This is a concrete implementation. Any subclass should make sure
     * to invoke the super implementation, add its settings to the
     * {@link Properties} instance returned by the super call and finally return that
     * instance.</b>
     * </p>
     */
    @Override
    protected Properties onSaveSettings() {
        this.settingLock.readLock().lock();
        Properties toSave = new Properties();
        toSave.setProperty(KEY_VISIBLE, Boolean.toString(this.visible));
        toSave.setProperty(KEY_VISIBLE,
                Float.toString(this.graphicInteractTolerance));
        this.settingLock.readLock().unlock();
        return toSave;
    }
}
