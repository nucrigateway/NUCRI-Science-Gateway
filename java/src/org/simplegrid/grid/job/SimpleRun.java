/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.grid.job;

/**
 * SimpleRun interface is a simple wrapping of Grid job execution and monitoring.
 * @author liuyan
 *
 */
public interface SimpleRun {
	/**
	 * Submit a Grid job, denoted by its RSL, to remote site by contact
	 * @param contact GRAM or WS-GRAM URL of remote Globus resource management service
	 * @param rsl The RSL description of job
	 * @return The unique job handle as a String
	 */
	public String execute(String contact, String rsl);
	/**
	 * Retrieve job status
	 * @param jobId The unique job handle
	 * @return job status as String
	 */
	public String getStatus(String jobId);
}
