// ----------------------------------------------------------------------------
// Copyright (C) 2008-2012 Louise A. Dennis, Berndt Farwer, Michael Fisher and 
// Rafael H. Bordini.
// 
// This file is part of Agent JPF (AJPF)
//
// AJPF is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
// 
// AJPF is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with AJPF; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// 
// To contact the authors:
// http://www.csc.liv.ac.uk/~lad
//
// This file is based on code from the Open Source software "Jason", copyright
// by Jomi F. Hubner and Rafael H. Bordini.  http://jason.sf.net
//----------------------------------------------------------------------------

package ajpf.psl;

import java.util.List;

/**
 * An Interface for formulas as understood by the agent programming language.
 * NOT an interface for properties in the MCAPL Property Specification Language.
 * Do NOT get confused!!!
 * 
 * @author louiseadennis
 */
public interface MCAPLTerm extends Comparable<MCAPLTerm> {
	
	/**
	 * Clones the formula.
	 * 
	 * @return a new, identical, formula.
	 */
	public Object clone();
	
	/**
	 * Is this a numeric term.
	 * @return
	 */
    public boolean isNumeric();

    /**
     * Is this a list term.
     * @return
     */
    public boolean isList();

    /**
     * Is this a string term.
     * @return
     */
    public boolean isString();
    
    /**
     * return the functor of this term.  NB. may return null if this makes no sense.
     * @return
     */
    public String getFunctor();
    
    /**
     * Return the arguments to this term.  NB. may return null if this makes no sense.
     * @return
     */
    public List<? extends MCAPLTerm> getTerms();
    
    /**
     * How many arguments does this term have.
     * @return
     */
    public int getTermsSize();
    
    /**
     * We allow _only_ unnamed variables in MCAPL PSL expressions.
     * @return
     */
    public boolean isUnnamedVar();
   
}
