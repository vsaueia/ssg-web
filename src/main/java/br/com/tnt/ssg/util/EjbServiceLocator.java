/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.tnt.ssg.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * 
 * @author christiano
 */
public class EjbServiceLocator {

	private InitialContext ic;

	public EjbServiceLocator() {
		try {
			ic = new InitialContext();
		} catch (NamingException ne) {
			throw new RuntimeException(ne);
		}
	}

	/**
	 * 
	 * @param jndiName
	 * @return
	 * @throws NamingException
	 */
	public Object lookup(String jndiName) throws NamingException {
		return ic.lookup(jndiName);
	}
}
