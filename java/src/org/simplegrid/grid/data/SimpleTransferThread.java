/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.grid.data;

import org.ietf.jgss.GSSCredential;
import org.simplegrid.grid.data.SimpleTransfer;

/**
 * Threaded version of SimpleTransfer. Useful in web application development.
 * The call returns immediately to give quick response to web client.
 * SimpleTransferThread supports the transfer of an array of files.
 * @author liuyan
 *
 */
public class SimpleTransferThread extends Thread {
	public static final int LOCALTOREMOTE = 0;
	public static final int REMOTETOLOCAL = 1;
	String local[]; 
	String remote[];
	String status, ownerid; // id is used to identify the owner of this thread
	int mode;
	SimpleTransfer myxfer;
	/**
	 * Initialization with source and destination file sets, and a valid proxy
	 * @param mmode
	 * @param pproxy
	 * @param remoteHost
	 * @param localFile
	 * @param remoteFile
	 * @param jid
	 */
	public SimpleTransferThread(int mmode, GSSCredential pproxy, String remoteHost, String localFile[], String remoteFile[], String jid) {
		status = "";
		mode = mmode;
		remote = remoteFile;
		local = localFile;
		myxfer = new SimpleTransfer(pproxy, remoteHost, 2811);
		ownerid = jid;
	}
	/**
	 * Check if transfer is undergoing, successful, or failed
	 * @return status as String
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * Identify this transfer. Usually, a job handle or application id is
	 * an appropriate candidate for owner id.
	 * @return transfer id
	 */
	public String getOwnerId() {
		return ownerid;
	}
	/**
	 * Start thread
	 */
	public void run() {
		try {
			for (int i=0; i<local.length; i++) {
		
				if (mode == SimpleTransferThread.LOCALTOREMOTE) {
					myxfer.local2remote(local[i], remote[i]);
				} else {
					myxfer.remote2local(remote[i], local[i]);
				}
			}
			status = "SUCCESS";
		} catch (Exception e) {
			// just stop
			status = "error: "+e.toString();
			e.printStackTrace();
		}
	}
}

