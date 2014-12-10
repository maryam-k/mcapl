// ----------------------------------------------------------------------------
// Copyright (C) 2008-2012 Louise A. Dennis, Berndt Farwer, Michael Fisher and 
// Rafael H. Bordini.
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
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// 
// To contact the authors:
// http://www.csc.liv.ac.uk/~lad
//
// This file is based on code from the Open Source software "Jason", copyright
// by Jomi F. Hubner and Rafael H. Bordini.  http://jason.sf.net
//----------------------------------------------------------------------------

package ail.syntax;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import ajpf.util.AJPFLogger;
import gov.nasa.jpf.annotation.FilterField;
    
/**
      used to group a set of vars. E.g.: when X = Y = W = Z the function map
      has X -> { X, Y, W, Z } 
          Y -> { X, Y, W, Z } 
          W -> { X, Y, W, Z } 
          Z -> { X, Y, W, Z } 
      where { X, Y, W, Z } is a VarsCluster instance.
     
      So when one var is assigned to a value, all vars gives this
      value.
      
      @author jomi
      
*/
public class VarsCluster extends DefaultTerm implements Iterable<VarTerm> {

	@FilterField
    private static int idCount = 0;

    @FilterField
    int          id = 0;
    
    // The variables in this cluster.
    Set<VarTerm> vars = null;
    
    // A Cluster of variables is associated with the unifier that identifies them all as the same.
    Unifier      u;
    
    // The VarsCluster needs a value for when it gets actually put in a term (rather than a unifier).
    Term value;
		
    // used in clone
    /**
     * Constructor.
     * @param u
     */
	private VarsCluster(Set<VarTerm> vs, Unifier u) {
		this.u = u;
		vars = new HashSet<VarTerm>();
		for (VarTerm vt : vs) {
			vars.add((VarTerm) vt.clone());
		}

	}

	/**
	 * Constructor.
	 * @param v1
	 * @param v2
	 * @param u
	 * 
	 * The Unifier could associate one of the var terms with a var cluster, so that cluster needs to expand.
	 */
	VarsCluster(VarTerm v1, VarTerm v2, Unifier u) {
		id = ++idCount;
		this.u = u;
		add(v1);
		add(v2);
		u.updateWithVarsCluster(this);
	}

	/**
	 * Add a new variable to this cluster.
	 * @param vt
	 */
	private void add(VarTerm vt) {
		Term vl = u.get(vt);
		if (vl == null) {
			// This variable isn't already in a cluster according to the unifier
			if (vars == null) {
				// This is a new cluster
				vars = new HashSet<VarTerm>();
				vars.add(vt);
			} else {
				vars.add(vt);
                for (VarTerm vtc : vars) {
                }
			}
		} else if (vl instanceof VarsCluster) {
			// This variable is already in a VarCluster according to the unifier
			if (vars == null) {
				// But this var cluster is new so we inherit all the vars from the old cluster.
				vars = ((VarsCluster) vl).vars;
			} else {
				vars.addAll(((VarsCluster) vl).vars);
			}
		} else {
			AJPFLogger.warning("ail.syntax.VarsCluster", "joining var that has value!");
		}
	}
	
	public Unifier getUnifier() {
		return u;
	}
	
	/**
	 * Is v unified by this cluster.
	 * @param v
	 * @return
	 */
	public boolean contains(VarTerm v) {
		return vars.contains(v);
	}

	/**
	 * Iterator over the variables in this cluster.
	 */
	public Iterator<VarTerm> iterator() {
		return vars.iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof VarsCluster) {
			return vars.equals(((VarsCluster) o).vars);
		}
		return false;
	} 

	/**
	 * The cluster now has a value.
	 * @return
	 */
//	boolean hasValue() {
//		return vars != null && !vars.isEmpty();
//	}

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.DefaultTerm#clone()
	 */
	public VarsCluster clone() {
		VarsCluster c = new VarsCluster(vars, u);
		return c;
	} 

	/*
	 * (non-Javadoc)
	 * @see ail.syntax.DefaultTerm#calcHashCode()
	 */
	protected int calcHashCode() {
		return vars.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "_VC" + id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Term#strip_varterm()
	 */
	public Term strip_varterm() {
		if (hasValue()) {
			return getValue().strip_varterm();
		} else {
			return this;
		}
	}
	
	public Term getValue() {
		return value;
	}
	
	public boolean hasValue() {
		return value != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Unifiable#getVarNames()
	 */
	public List<String> getVarNames() {
		ArrayList<String> varnames = new ArrayList<String>();
		for (VarTerm v: vars) {
			varnames.addAll(v.getVarNames());
		}
		return varnames;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.Unifiable#renameVar(java.lang.String, java.lang.String)
	 */
	public void renameVar(String oldname, String newname) {
		for (VarTerm v: vars) {
			v.renameVar(oldname, newname);
		}
		
		u.renameVar(oldname, newname);
	}

	@Override
	public void makeVarsAnnon() {
		for (VarTerm v: vars) {
			v.makeVarsAnnon();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see ail.syntax.DefaultTerm#apply(ail.syntax.Unifier)
	 */
	public boolean apply(Unifier un) {
		for (VarTerm v: vars) {
			Term uv = un.get(v);
			// NB.  If two vars in the cluster are unified differently by the unifier, this will set the whole cluster to the first option.
			if (uv != null) {
				if (uv instanceof VarsCluster) {
					
				} else {
					value = uv;
					for (VarTerm var: vars) {
						u.unifies(var, uv);
					}
				}
			}
		}
		
		return false;
	}
	
	
}
