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
package dk.dma.epd.common.prototype.enavcloud;

import static java.util.Objects.requireNonNull;

import java.awt.Color;
import java.io.Serializable;
import java.util.Date;

import net.maritimecloud.net.service.spi.ServiceInitiationPoint;
import net.maritimecloud.net.service.spi.ServiceMessage;
import dk.dma.enav.model.voyage.Route;
import dk.dma.epd.common.prototype.service.EnavServiceHandlerCommon.CloudMessageStatus;

/**
 * Maritime cloud service for exchanging routes suggestions from 
 * a sea traffic control center (STCC) to ship.
 * <p>
 * Defines the service initiation point along with the following classes:
 * <ul>
 *   <li>{@linkplain RouteSuggestionStatus} status of the route suggestion.</li>
 *   <li>{@linkplain RouteSuggestionMessage} Used for sending a suggested route from STCC to a ship
 *        and a status back from the ship to the STCC.</li>
 *   <li>{@linkplain RouteSuggestionReply} Used to acknowledge the message.</li>
 * </ul>
 */
public class RouteSuggestionService {
    
    /** An initiation point */
    public static final ServiceInitiationPoint<RouteSuggestionMessage> INIT = new ServiceInitiationPoint<>(
            RouteSuggestionMessage.class);
    
    /**
     * Status of the route suggestion
     */
    public enum RouteSuggestionStatus {

        PENDING(Color.YELLOW),
        ACCEPTED(new Color(130, 165, 80)),
        REJECTED(new Color(165, 80, 80)),
        NOTED(new Color(130, 130, 200));
        
        Color color;        
        private RouteSuggestionStatus(Color color) {
            this.color = color;
        }
        
        public Color getColor()  { return color; }        
    }
    
    /**
     * Used for sending a suggested route from STCC to a ship
     * and a status back from the ship to the STCC
     */
    public static class RouteSuggestionMessage extends ServiceMessage<RouteSuggestionReply>
        implements Serializable {
        
        private static final long serialVersionUID = 2190397388847226293L;
        
        private Route route;
        private Date sentDate;
        private String message;
        private long id;
        private RouteSuggestionStatus status;
        
        // Not sent along
        private transient CloudMessageStatus cloudMessageStatus;
        
        /**
         * No-arg constructor
         */
        public RouteSuggestionMessage() {
        }

        /**
         * Constructor - used for messages
         * 
         * @param route the suggested route
         * @param message an additional message
         */
        public RouteSuggestionMessage(Route route, String message, RouteSuggestionStatus status) {
            this.route = requireNonNull(route);
            this.message = requireNonNull(message);
            this.status = requireNonNull(status);
            this.id = System.currentTimeMillis();
            this.sentDate = new Date();
        }

        /**
         * Constructor - used for replys
         * 
         * @param route the suggested route
         * @param sender the sender
         * @param message an additional message
         */
        public RouteSuggestionMessage(long id, String message, RouteSuggestionStatus status) {
            this.id = id;
            this.message = message;
            this.status = requireNonNull(status);
            this.sentDate = new Date();
        }
       
        /********* Getters and setters ***********/
        
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Route getRoute() {
            return route;
        }
        
        public void setRoute(Route route) {
            this.route = route;
        }
        
        public Date getSentDate() {
            return sentDate;
        }

        public void setSentDate(Date sentDate) {
            this.sentDate = sentDate;
        }
        
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public RouteSuggestionStatus getStatus() {
            return status;
        }

        public void setStatus(RouteSuggestionStatus status) {
            this.status = status;
        }        
        
        public CloudMessageStatus getCloudMessageStatus() {
            return cloudMessageStatus;
        }

        public void setCloudMessageStatus(CloudMessageStatus cloudMessageStatus) {
            this.cloudMessageStatus = cloudMessageStatus;
        }
        
        /**
         * Some of the cloud status updates may arrive out of order. Use the 
         * {@code CloudMessageStatus.combine()} method to make sure the order
         * is maintained
         * 
         * @param cloudMessageStatus the new cloud message status
         */
        public synchronized void updateCloudMessageStatus(CloudMessageStatus cloudMessageStatus) {
            if (cloudMessageStatus != null) {
                this.cloudMessageStatus = cloudMessageStatus.combine(this.cloudMessageStatus);
            }
        }
    }

    /**
     * Used to acknowledge the message
     */
    public static class RouteSuggestionReply extends ServiceMessage<Void> {

        private long id;
        private Date receivedDate;
  
        /**
         * Constructor
         */
        public RouteSuggestionReply() {
        }

        /**
         * Constructor
         * 
         * @param id id of the original suggestion
         */
        public RouteSuggestionReply(long id) {
            this.id = id;
            this.receivedDate = new Date();
        }

        /********* Getters and setters ***********/
        
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Date getReceivedDate() {
            return receivedDate;
        }

        public void setReceivedDate(Date receivedDate) {
            this.receivedDate = receivedDate;
        }
    }
}
