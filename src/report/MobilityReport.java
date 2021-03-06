/*
 * Copyright (C) 2016 micha
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package report;

import core.DTNHost;
import core.SimClock;
import core.SimScenario;
import core.UpdateListener;
import java.util.List;
import routing.MessageRouter;
import routing.mobility.ReportMobility;

/**
 *
 * @author micha
 */
public class MobilityReport extends Report implements UpdateListener {
    
    private int simulationDuration; 
    
    /**
     * Constructor.
     */
    public MobilityReport() {
        init();
        simulationDuration = (int)Math.floor(SimScenario.getInstance().getEndTime());
    }

    @Override
    public void updated(List<DTNHost> hosts) {
        int curTime = SimClock.getIntTime();
        // If it is the end of the simulation it is time to write the communities
        if (curTime % simulationDuration == 0) {
            for (DTNHost host : hosts) {
                MessageRouter router = host.getRouter();
                if (router instanceof ReportMobility) {
                    ReportMobility report = (ReportMobility) router;
                    this.write("" + host.getAddress() + " " 
                            + report.getMobilityLevel());
                } 
            }
        }
    }
}