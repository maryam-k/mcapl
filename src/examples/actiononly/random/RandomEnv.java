// ----------------------------------------------------------------------------
// Copyright (C) 2014 Louise A. Dennis, and Michael Fisher 
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

package actiononly.random;

import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

import actiononly.mas.ActionOnlyEnv;
import ail.mas.DefaultEnvironment;
import ail.syntax.Action;
import ail.syntax.Capability;

/**
 * A Very simple environment that chooses randomly among agent capabilities.
 * @author lad
 *
 */
public class RandomEnv extends DefaultEnvironment implements ActionOnlyEnv {
	Random r = new Random();

	@Override
	public Action selectAction(Iterator<Capability> ic) {
		ArrayList<Capability> cs = new ArrayList<Capability>();
		
		while (ic.hasNext()) {
			cs.add(ic.next());
		}
		
		int length = cs.size();
		
		int index = r.nextInt(length);
		return cs.get(index).getCap();
	}

}