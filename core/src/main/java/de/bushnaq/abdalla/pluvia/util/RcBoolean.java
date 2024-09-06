/* ---------------------------------------------------------------------------
 * BEGIN_PROJECT_HEADER
 *
 * Copyright (c) 2004-2005 by Abdalla Bushnaq.
 *
 * Author(s)  : Abdalla Bushnaq
 * Created    : 2005.Jun.30
 * Project    : Library
 * JRE        : J2SE 5.0
 *
 * END_PROJECT_HEADER
 * -------------------------------------------------------------------------*/
package de.bushnaq.abdalla.pluvia.util;

/**
 * @author kunterbunt
 *
 */
public class RcBoolean {
	private boolean booleanValue;

	public RcBoolean(boolean aValue) {
		booleanValue = aValue;
	}

	public boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean aValue) {
		booleanValue = aValue;
	}

	public void setFalse() {
		booleanValue = false;
	}

	public void setTrue() {
		booleanValue = true;
	}
}