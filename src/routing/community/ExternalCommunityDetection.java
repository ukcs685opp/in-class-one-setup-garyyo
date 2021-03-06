/*
 * Copyright (C) 2016 Michael Dougras da Silva
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
package routing.community;

import core.DTNHost;
import core.Settings;
import core.SimScenario;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Loads the communities from an external file.
 */
public class ExternalCommunityDetection extends CommunityDetection {

	/**
	 * The configuration id for the file with the list of communities.
	 * The has a format with each line describing a community of the 
	 * first number present.
	 */
    public static final String COMMUNITY_FILE_S = "communityFile";

    /**
     * Static container shared by all instances that is filled with
     * the file information about the communities.
     */
    private static Map<Integer, Set<Integer>> values;
    
    /**
     * Stores the local community.
     */
    private Set<DTNHost> localCommunity;

    
    /**
     * Constructor
     * @param s Reference to simulator settings.
     */
    public ExternalCommunityDetection(Settings s) {
        // Load communities from an external file.
        if (values == null) {
            values = new HashMap<Integer, Set<Integer>>();
            String filename = s.getSetting(COMMUNITY_FILE_S);
            FileReader file = null; 
            BufferedReader buffer = null;
            try {
            	file = new FileReader(filename);
            	buffer = new BufferedReader(file);
            	String line = buffer.readLine();
            	while (line != null) {
            		
            		String[] temp = line.split(" ");
                    int hostaddress = Integer.parseInt(temp[0]);
                    Set<Integer> hosts = new HashSet<Integer>();
                    for (int i = 1; i < temp.length; i++) {
                        int hostid = Integer.parseInt(temp[i]);
                        hosts.add(hostid);
                    }
                    values.put(hostaddress, hosts);
                    line = buffer.readLine();
            	}
            }
            catch (FileNotFoundException exc) {
            	System.out.println(String.format("File %s not found", filename));
                System.exit(1);
            }
            catch (IOException exc) {
            	System.out.println(String.format("Error reading the file %s: \n%s", filename, exc.getMessage()));
                System.exit(1);
            }
            finally {
            	try {
	            	buffer.close();
	            	file.close();
            	}
            	catch(IOException exc) {
            		// Nothing to do here
            	}
            }
        }
    }

    /**
     * Copy constructor.
     * @param prot Origin of the copy.
     */
    public ExternalCommunityDetection(ExternalCommunityDetection prot) {
        // Copy instance specific settings
    }

    @Override
    public Set<DTNHost> getCommunity() {
        if (this.localCommunity == null) {
            if (values.containsKey(this.host.getAddress())) {
                Set<Integer> temp = values.get(this.host.getAddress());
                this.localCommunity = new HashSet<DTNHost>();
                for (DTNHost host : SimScenario.getInstance().getHosts()) {
                    if (temp.contains(host.getAddress())) {
                        this.localCommunity.add(host);
                    }
                }
            } else {
                this.localCommunity = new HashSet<DTNHost>();
            }
        }
        return this.localCommunity;
    }

    @Override
    public ExternalCommunityDetection replicate() {
        return new ExternalCommunityDetection(this);
    }

    @Override
    public Set<DTNHost> getFamiliarSet() {
        return new HashSet<DTNHost>();
    }

    @Override
    public Map<DTNHost, Set<DTNHost>> getCommunityFamiliarSet() {
        return new HashMap<DTNHost, Set<DTNHost>>();
    }

    @Override
    public void startContact(DTNHost otherHost, Set<DTNHost> otherCommunity, Set<DTNHost> otherFamiliarSet, Map<DTNHost, Set<DTNHost>> otherFSOfC) {
        // Nothing to do here
    }

    @Override
    public void endContact(DTNHost otherHost, Set<DTNHost> otherFamiliarSet, Map<DTNHost, Set<DTNHost>> otherFSOfC, List<Duration> connHistory) {
        // Nothing to do here
    }
}
