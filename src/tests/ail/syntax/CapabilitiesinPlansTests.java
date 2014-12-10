// ----------------------------------------------------------------------------
// Copyright (C) 2014 Louise A. Dennis, and  Michael Fisher 
//
// This file is part of the Agent Infrastructure Layer (AIL)
// 
// The AIL is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
// 
// The AIL is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with the AIL; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// 
// To contact the authors:
// http://www.csc.liv.ac.uk/~lad
//
//----------------------------------------------------------------------------

package ail.syntax;

import eass.parser.EASSLexer;
import eass.parser.EASSParser;
import junit.framework.Assert;

import mcaplantlr.runtime.ANTLRStringStream;
import mcaplantlr.runtime.CommonTokenStream;

import org.junit.Test;

import ail.semantics.AILAgent;

import java.util.Iterator;

/**
 * Regression tests associated with reasoning about capabilities/actions appearing
 * in plans.
 * @author louiseadennis
 *
 */
public class CapabilitiesinPlansTests {
	@Test public void getPlansContainingCapTest() {
		EASSLexer plan1_lexer = new EASSLexer(new ANTLRStringStream("+!correct_angle(A) [perform] : {True} <- .query(calculate_angle(A)), perf(turn(A)), *turned, remove_shared(turned);"));		
		CommonTokenStream plan1_tokens = new CommonTokenStream(plan1_lexer);
		EASSParser plan1_parser = new EASSParser(plan1_tokens);
		EASSLexer plan2_lexer = new EASSLexer(new ANTLRStringStream("+! move(D) [perform] : {True} <- .query(calculate_distance(D)), perf(forward(D)), *moved, remove_shared(moved), +!evaluate_success(pos(X, Y), forward(D), True) [perform];"));
		CommonTokenStream plan2_tokens = new CommonTokenStream(plan2_lexer);
		EASSParser plan2_parser = new EASSParser(plan2_tokens);
		
		try {
			Plan plan1 = (plan1_parser.plan()).toMCAPL();
			Plan plan2 = (plan2_parser.plan()).toMCAPL();

			AILAgent a = new AILAgent("ag");
			a.addPlan(plan1);
			a.addPlan(plan2);
			
			Predicate capname = new Predicate("forward");
			capname.addTerm(new VarTerm("F"));
			
			Iterator<Plan> ip = a.getPL().getPlansContainingCap(capname);
			
			Plan p = ip.next();
			Assert.assertTrue(p != null);
			Assert.assertTrue(p.getID() == plan2.getID());
			Assert.assertFalse(ip.hasNext());
			
			
		} catch (Exception e) {
			
		}
	}
	
	@Test public void findEquivalentCapabilityTest() {
		EASSLexer cap1_lexer = new EASSLexer(new ANTLRStringStream("{pos(X, Y), angle(Theta)} forward(D) {newX(X, D, Theta, NX), newY(Y, D, Theta, NY), pos(NX, NY), angle(Theta)}"));		
		CommonTokenStream cap1_tokens = new CommonTokenStream(cap1_lexer);
		EASSParser cap1_parser = new EASSParser(cap1_tokens);
		EASSLexer cap2_lexer = new EASSLexer(new ANTLRStringStream("{} feedback(X, Y) {pos(X, Y)}"));
		CommonTokenStream cap2_tokens = new CommonTokenStream(cap2_lexer);
		EASSParser cap2_parser = new EASSParser(cap2_tokens);
		
		try {
			Capability cap1 = (cap1_parser.capability()).toMCAPL();
			Capability cap2 = (cap2_parser.capability()).toMCAPL();
			
			AILAgent a = new AILAgent("ag");
			a.addCap(cap1);
			a.addCap(cap2);
			
			Predicate post = new Predicate("pos");
			post.addTerm(new VarTerm("X"));
			post.addTerm(new VarTerm("Y"));
			
			Capability c = a.getCL().findEquivalent(cap1.getCap(), Predicate.PTrue, post, a.getRuleBase());
			Assert.assertTrue(c.getCap().unifies(cap2.getCap(), new Unifier()));
		} catch (Exception e) {
			
		}
		
	}

}
