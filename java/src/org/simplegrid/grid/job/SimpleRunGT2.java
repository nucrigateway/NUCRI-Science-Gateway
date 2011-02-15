/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.grid.job;
//Grid security
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSCredential;
import org.simplegrid.grid.security.*;

import org.gridforum.jgss.ExtendedGSSManager;
import org.gridforum.jgss.ExtendedGSSCredential;

import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.gssapi.GlobusGSSManagerImpl;
import org.globus.gsi.gssapi.GlobusGSSException;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;

// Gram
import org.globus.gram.Gram;
import org.globus.gram.GramJob;
import org.globus.gram.GramException;


import java.io.*;
//import java.util.Properties;

/**
 * GRAM implementation of SimpleRun. GRAM was released in Globus Toolkit 2.x.
 */
public class SimpleRunGT2 implements SimpleRun {
	protected GSSCredential proxy;
	/**
	 * Initialization with a valid Grid proxy
	 * @param cred proxy object
	 */
	public SimpleRunGT2(GSSCredential cred) {
		proxy = cred;
	}
	/**
	 * Ping remote GRAM server
	 * @return success or failure
	 */
	private boolean ping(String contact) {
		try {
			Gram.ping(proxy, contact);
		} catch(Exception e) {
			System.err.println("Error - SimpleRunGT2::ping():");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Submit a job to remote GRAM server. 
	 * Note: Gram request doens't recognize DUROC RSL, so single RSL is required
	 */
	public String execute(String contact, String rsl) {
		GramJob job = null;
		String id;
		try {
			if (proxy.getRemainingLifetime() < SimpleCred.DEFAULT_MIN)
				return null;
			// ping remote Gram server
			boolean b = ping(contact);
			if (!b) return null;
			// submit job
			job = new GramJob(proxy, rsl);
			System.out.println(rsl);
			Gram.request(contact, job, true);
			id = job.getIDAsString();
		} catch (GramException e) {
			System.err.println("GramException - SimpleRunGT2::execute():\n"+e.toString());
			return null;
		} catch (GSSException e) {
			System.err.println("GSSException - SimpleRunGT2::execute():\n"+e.toString());
			return null;
		}
		//System.out.println("globus-job-run: SUCCESS, ghandle: "+id);
		return id;
	}

	/**
	 * Probe job status
	 * @param jobId job handle
	 * @return a GramJob object for job status retrieval
	 */
	private GramJob probeJob(String jobId) {
		GramJob job = null;
		try {
			job = new GramJob("probe rsl");
			job.setCredentials(proxy);
			job.setID(jobId);
			Gram.jobStatus(job);
		} catch (Exception e) {
			return null;
		}
		return job;
	}
	/**
	 * Retrieve job status
	 * @param jobId Job handle
	 * @return Job status as String
	 */
	public String getStatus(String jobId) {
		GramJob rJob = this.probeJob(jobId);
		// Note: if status is "Error fetching status", it means job is dequed,
		//       either finished or failed
		return (rJob==null)?"Error fetching status":GramJob.getStatusAsString(rJob.getStatus());
	}
/*
	// test main
  public static void main(String[] args) {
    try {
    	org.simplegrid.util.Config myconfig=new org.simplegrid.util.Config();
    	String rsl = SimpleRSL.getRSL_GT2(myconfig, "UC", "500 20 mydataset", "myoutput");
		SimpleCred mycred=null;
		GSSCredential proxy = null;
		myconfig = new org.simplegrid.util.Config();
		mycred = new SimpleCred(myconfig.get("grid.myproxy.server"), Integer.parseInt(myconfig.get("grid.myproxy.port")), myconfig.get("grid.myproxy.username"), "", myconfig.get("grid.myproxy.proxyfile"));
		// assume proxy file specified is valid
		proxy = mycred.get();

	  SimpleRunGT2 sr = new SimpleRunGT2(proxy);
      String jobId = sr.execute(myconfig.get("grid.gram."+"UC"), rsl);
      System.out.println("Job submitted. ID: " + jobId);
      String status;
      do {
          Thread.sleep(4000);
          status = sr.getStatus(jobId);
          System.out.println("Job Status: " + status);
      } while (!status.equalsIgnoreCase("Error fetching status"));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  */
}

