// ----------------------------------------------------------------------------
// Copyright (C) 2012 Louise A. Dennis,  and Michael Fisher
//
// This file is part of Gwendolen
// 
// Gwendolen is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
// 
// Gwendolen is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with Gwendolen; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// 
// To contact the authors:
// http://www.csc.liv.ac.uk/~lad
//
//----------------------------------------------------------------------------

package gwendolen.uavs.simple;

import ail.mas.vehicle.VehicleEnvironment;
import ajpf.MCAPLJobber;
import ajpf.util.AJPFLogger;

import ail.mas.RoundRobinScheduler;
import ajpf.util.Choice;
import ail.syntax.Action;
import ail.syntax.Literal;
import ail.syntax.Predicate;
import ail.syntax.Unifier;
import ail.semantics.AILAgent;
import ail.util.AILexception;

public class SimpleUAVEnv extends VehicleEnvironment implements MCAPLJobber{
	String name;
	Choice<Boolean> objectSet = new Choice<Boolean>();
	public int colliding = 0;
	public boolean done = false;
	
	public SimpleUAVEnv() {
		name = "Simple UAV Environment";
		RoundRobinScheduler s = new RoundRobinScheduler();
		s.addJobber(this);
		setScheduler(s);
		addPerceptListener(s);
		objectSet.addChoice(0.1, new Boolean(true));
		objectSet.addChoice(0.9, new Boolean(false));
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public void addAgent(AILAgent a) {
		SimpleUAV uav = new SimpleUAV(a);
		addVehicle(uav);
	}
	
	public void do_job() {
		if (colliding > 1) {
			notifyListeners();
			done = true;
		} else	if (colliding == 1) {
			addPercept(new Predicate("collided"));
			AJPFLogger.info("gwendolen.uavs.simple.SimpleUAVEnv", "Collision Occurred!!" + percepts);
		} else if (objectSet.get_choice()) {
			addPercept(new Predicate("collision"));
			colliding = 1;
		}
	}
	
	public boolean done() {
		return done;
	}
	
	
	public Unifier executeAction(String agName, Action act) throws AILexception {
		if (act.getFunctor().equals("evade")) {
			removePercept(new Predicate("collision"));
			AJPFLogger.info("gwendolen.uavs.simple.SimpleUAVEnv", "UAV has successfully evaded collision!!!");
			colliding = 2;
		} 
		
		return super.executeAction(agName, act);
	}

	public int compareTo(MCAPLJobber j) {
		return j.getName().compareTo(getName());
	}
}
