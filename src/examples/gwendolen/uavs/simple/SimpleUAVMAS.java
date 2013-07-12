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

import ajpf.MCAPLcontroller;
import ail.mas.MAS;
import gwendolen.GwendolenMASBuilder;
import ajpf.MCAPLLanguageAgent;
import ajpf.psl.ast.Property_AST;
import ail.semantics.AILAgent;


/**
 * 
 * 
 * @author louiseadennis
 *
 */
public class SimpleUAVMAS {
	static double prob = 0;

	public static void main(String[] args) {
		runexample(args);
	}
		
	public static void runexample(String[] args) {
		String abs_filename = args[0];

		String propertystring = getProperty(Integer.parseInt(args[1]));
   		Property_AST prop_ast = new Property_AST();
		prop_ast.parse(propertystring);
		
		
		MAS mas = (new GwendolenMASBuilder(abs_filename, true)).getMAS();
		SimpleUAVEnv environment = new SimpleUAVEnv();
		mas.setEnv(environment);
		for (MCAPLLanguageAgent a: mas.getMCAPLAgents()) {
			SimpleUAV uav = new SimpleUAV((AILAgent) a);
			environment.addVehicle(uav);
		}
			
			// Set up a MCAPL controller and specification.
		MCAPLcontroller mccontrol = new MCAPLcontroller(mas, propertystring, 1);
			// Start the system.
		mccontrol.begin(); 
		prob = getListenerProbability();
//		System.err.println("what?");
//        System.err.println(prob);
 
	}
	
	public static double getListenerProbability() {
		return 0;
	}
	
	public static String getProperty(int i) {
		String propertystring;
		switch (i) {
		case 0:
			propertystring = "A(~P(collided))";
			break;
		default:
			propertystring = "A(~P(collided))";				
		}
		return propertystring;
	}

}
