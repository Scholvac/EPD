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
package dk.dma.epd.shore.gui.settingtabs;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import com.bbn.openmap.proj.coords.LatLonPoint;

import dk.dma.epd.common.prototype.gui.settings.BaseSettingsPanel;
import dk.dma.epd.common.prototype.gui.settings.ISettingsListener.Type;
import dk.dma.epd.shore.EPDShore;
import dk.dma.epd.shore.settings.EPDGuiSettings;
import dk.dma.epd.shore.settings.EPDMapSettings;

public class MapSettingsPanel extends BaseSettingsPanel {

    private static final long serialVersionUID = 1L;
    JSpinner defaultMapScaleSpinner;
    JSpinner maximumMapScaleSpinner;
    JSpinner latitudeSpinner;
    JSpinner longitudeSpinner;
    JTextField wmsTextField;
    JCheckBox wmsCheckBox;
    private JCheckBox chckbxWmsDrag;
    
    private EPDMapSettings mapSettings;
    private EPDGuiSettings guiSettings;
    
    public MapSettingsPanel(){
        super("Map Settings", EPDShore.res().getCachedImageIcon("images/settings/map.png"));
        
        setBackground(GuiStyler.backgroundColor);
        setBounds(10, 11, 493, 381);
        setLayout(null);

        JPanel mapSettingsPanel = new JPanel();
        mapSettingsPanel.setBackground(GuiStyler.backgroundColor);
        mapSettingsPanel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, new Color(70, 70, 70)), "Map Settings", TitledBorder.LEADING, TitledBorder.TOP, GuiStyler.defaultFont, GuiStyler.textColor));
        mapSettingsPanel.setBounds(10, 11, 473, 161);
        add(mapSettingsPanel);
        mapSettingsPanel.setLayout(null);

        defaultMapScaleSpinner = new JSpinner();
        defaultMapScaleSpinner.setBounds(10, 22, 82, 20);
        defaultMapScaleSpinner.setModel(new SpinnerNumberModel(new Integer(0), 0, null, new Integer(1000)));
        GuiStyler.styleSpinner(defaultMapScaleSpinner);



        mapSettingsPanel.add(defaultMapScaleSpinner);

        JLabel lblDefaultMapScale = new JLabel("Default map scale");
        GuiStyler.styleText(lblDefaultMapScale);
        lblDefaultMapScale.setBounds(102, 25, 105, 14);
        mapSettingsPanel.add(lblDefaultMapScale);

        maximumMapScaleSpinner = new JSpinner();
        maximumMapScaleSpinner.setBounds(10, 49, 82, 20);
        maximumMapScaleSpinner.setModel(new SpinnerNumberModel(new Integer(0), 0, null, new Integer(1000)));
        GuiStyler.styleSpinner(maximumMapScaleSpinner);
//        maximumMapScaleSpinner.setBorder(guiStyler.border);
//
//        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor)maximumMapScaleSpinner.getEditor();
//        editor.getTextField().setBackground(guiStyler.backgroundColor);
//        editor.getTextField().setForeground(guiStyler.textColor);
////        editor.getTextField().setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(83, 83, 83)));
////        editor.getTextField().setSelectionColor(Color.yellow);
////        editor.getTextField().set
//        editor.getTextField().setFont(guiStyler.defaultFont);


        mapSettingsPanel.add(maximumMapScaleSpinner);


        JLabel lblMaximumScale = new JLabel("Maximum scale");
        GuiStyler.styleText(lblMaximumScale);
        lblMaximumScale.setBounds(102, 50, 105, 14);
        mapSettingsPanel.add(lblMaximumScale);

        JLabel lblDefaultMapCenter = new JLabel("Default map center");
        GuiStyler.styleText(lblDefaultMapCenter);
        lblDefaultMapCenter.setBounds(16, 95, 153, 14);
        mapSettingsPanel.add(lblDefaultMapCenter);

        JLabel lblLatitude = new JLabel("Latitude:");
        GuiStyler.styleText(lblLatitude);
        lblLatitude.setBounds(16, 120, 46, 14);
        mapSettingsPanel.add(lblLatitude);

        latitudeSpinner = new JSpinner();
        latitudeSpinner.setBounds(72, 120, 73, 20);
        latitudeSpinner.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
        GuiStyler.styleSpinner(latitudeSpinner);

        mapSettingsPanel.add(latitudeSpinner);

        JLabel lblLongitude = new JLabel("Longitude:");
        GuiStyler.styleText(lblLongitude);
        lblLongitude.setBounds(179, 120, 67, 14);
        mapSettingsPanel.add(lblLongitude);

        longitudeSpinner = new JSpinner();
        longitudeSpinner.setBounds(239, 117, 82, 20);
        longitudeSpinner.setModel(new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
        GuiStyler.styleSpinner(longitudeSpinner);
        mapSettingsPanel.add(longitudeSpinner);

        JPanel wmsSettingsPanel = new JPanel();
        wmsSettingsPanel.setBackground(GuiStyler.backgroundColor);
        wmsSettingsPanel.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, new Color(70, 70, 70)), "WMS Settings", TitledBorder.LEADING, TitledBorder.TOP, GuiStyler.defaultFont, GuiStyler.textColor));
        wmsSettingsPanel.setBounds(10, 183, 473, 188);
        add(wmsSettingsPanel);
        wmsSettingsPanel.setLayout(null);

        wmsCheckBox = new JCheckBox("Maps start with WMS enabled");
        wmsCheckBox.setFont(GuiStyler.defaultFont);
        wmsCheckBox.setBackground(GuiStyler.backgroundColor);
        wmsCheckBox.setForeground(GuiStyler.textColor);
        wmsCheckBox.setBounds(6, 24, 200, 23);
        GuiStyler.styleCheckbox(wmsCheckBox);
        wmsSettingsPanel.add(wmsCheckBox);

        wmsTextField = new JTextField();
        GuiStyler.styleTextFields(wmsTextField);
        wmsTextField.setBounds(6, 100, 457, 20);
        wmsSettingsPanel.add(wmsTextField);
        wmsTextField.setColumns(10);

        JLabel lblNewLabel = new JLabel("WMS URL:");
        GuiStyler.styleText(lblNewLabel);
        lblNewLabel.setBounds(6, 75, 141, 14);
        wmsSettingsPanel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("<HTML>Enter the URL to the WMS service you wish to use, \r\n<BR>enter everything except BBOX and height/width options.\r\n</HTML>");
        GuiStyler.styleText(lblNewLabel_1);
        lblNewLabel_1.setBounds(6, 131, 457, 46);
        wmsSettingsPanel.add(lblNewLabel_1);
        
        chckbxWmsDrag = new JCheckBox("WMS is used when dragging (disable for performance)");
        chckbxWmsDrag.setBounds(6, 50, 364, 23);
        GuiStyler.styleCheckbox(chckbxWmsDrag);
        wmsSettingsPanel.add(chckbxWmsDrag);


        loadSettings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoadSettings() {
        mapSettings = EPDShore.getInstance().getSettings().getMapSettings();
        guiSettings = EPDShore.getInstance().getSettings().getGuiSettings();

        defaultMapScaleSpinner.setValue(mapSettings.getScale());
        maximumMapScaleSpinner.setValue(mapSettings.getMaxScale());
        Float latitude = mapSettings.getCenter().getLatitude();
        Float longitude = mapSettings.getCenter().getLongitude();
        latitudeSpinner.setValue(latitude.doubleValue());
        longitudeSpinner.setValue(longitude.doubleValue());
        
        wmsTextField.setText(mapSettings.getWmsQuery());
        wmsCheckBox.setSelected(mapSettings.isUseWms());
        chckbxWmsDrag.setSelected(mapSettings.isUseWmsDragging());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSaveSettings(){

        mapSettings.setScale((Float) defaultMapScaleSpinner.getValue());
        mapSettings.setMaxScale((Integer) maximumMapScaleSpinner.getValue());
        LatLonPoint center = new LatLonPoint.Double((Double) latitudeSpinner.getValue(), (Double) longitudeSpinner.getValue());
        mapSettings.setCenter(center);

        guiSettings.setUseWMS(wmsCheckBox.isSelected());
        guiSettings.setWmsQuery(wmsTextField.getText());
        
        mapSettings.setUseWms(wmsCheckBox.isSelected());
        mapSettings.setWmsQuery(wmsTextField.getText());
        mapSettings.setUseWmsDragging(chckbxWmsDrag.isSelected());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkSettingsChanged() {
        return
                changed(mapSettings.getScale(), defaultMapScaleSpinner.getValue()) ||
                changed(mapSettings.getMaxScale(), maximumMapScaleSpinner.getValue()) ||
                changed(mapSettings.getCenter().getLatitude(), latitudeSpinner.getValue()) ||
                changed(mapSettings.getCenter().getLongitude(), longitudeSpinner.getValue()) ||
                
                changed(mapSettings.getWmsQuery(), wmsTextField.getText()) ||
                changed(mapSettings.isUseWms(), wmsCheckBox.isSelected()) ||
                changed(mapSettings.isUseWmsDragging(), chckbxWmsDrag.isSelected());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void fireSettingsChanged() {
        fireSettingsChanged(Type.MAP);
    }    
}
