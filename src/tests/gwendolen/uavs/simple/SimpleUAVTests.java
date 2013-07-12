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

import org.junit.Test;

import ajpf.MCAPLcontroller;
import ajpf.MCAPLProbListener;
import gov.nasa.jpf.util.test.TestJPF;
import gov.nasa.jpf.jvm.Verify;

import java.text.DecimalFormat;


/**
 * regression test for programming-by-contract annotations
 */
public class SimpleUAVTests extends TestJPF {
	MCAPLProbListener l;
	double probability;

 static final String[] PICKUP_ARGS = { "-show", 
	 "+listener=ajpf.MCAPLProbListener,.listener.ExecTracker",
     "+et.print_insn=false",
     "+vm.max_transition_length = MAX"
};
//	static final String[] PICKUP_ARGS = {"src/examples/gwendolen/simple/PickUpAgent.jpf",
//										"+target=gwendolen.simple.SimpleTests"};



  //--- driver to execute single test methods
  public static void main(String[] args) {
	  runTestsOfThisClass(args);
  }
  

  //--- test methods

 
  @Test //----------------------------------------------------------------------
  public void testProbablyEvades () {
    if (verifyNoPropertyViolation(PICKUP_ARGS)){
    	String filename = "/src/examples/gwendolen/uavs/simple/collision_agent.gwen";
    	String abs_filename = MCAPLcontroller.getAbsFilename(filename);
    	String[] program_args = new String[2];
    	program_args[0] = abs_filename;
    	program_args[1] = "0";
    	SimpleUAVMAS.runexample(program_args);
    } else {
    	 
     }
    assertTrue(testProbability(0.01));
   }
  
  public boolean testProbability(double d) {
	  if (!Verify.isRunningInJPF()) {
		  double mcapl_prob = MCAPLProbListener.overall_prob;
		  DecimalFormat fourDform = new DecimalFormat("#.####");
		  Double mp = Double.valueOf(fourDform.format(mcapl_prob));
		  Double ip = Double.valueOf(fourDform.format(d));
		  if (mp.equals(ip)) {
			  return true;
		  } else {
			  System.err.println(mp + " != " + ip);
			  return false;
		  }
	  } else {
		  return true;
	  }
	  
  };


}
