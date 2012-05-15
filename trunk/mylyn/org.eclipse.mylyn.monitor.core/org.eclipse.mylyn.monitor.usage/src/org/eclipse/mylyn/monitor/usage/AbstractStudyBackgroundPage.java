/*******************************************************************************
 * Copyright (c) 2004, 2008 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Leah Findlater - initial API and implementation
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.monitor.usage;

import org.eclipse.jface.wizard.IWizardPage;

import java.io.File;

/**
 * Extend to provide a custom background page.
 *
 * @author Leah Findlater
 * @author Mik Kersten
 * @since 3.7
 */
public abstract class AbstractStudyBackgroundPage implements IWizardPage {

	public abstract File createFeedbackFile();
}
