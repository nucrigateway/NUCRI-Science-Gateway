/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.grid.data;

//ftp
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.DataChannelAuthentication;
import org.globus.ftp.GridFTPSession;
import org.globus.ftp.HostPort;
import org.globus.ftp.HostPortList;
import org.globus.ftp.exception.*;
// security
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.simplegrid.grid.security.*;

import java.io.*;

/**
 * A simple Grid data transfer API using GridFTP as transport protocol. 
 * @author liuyan
 *
 */
public class SimpleTransfer {
        String host1;
        String host2;
        int port1;
        int port2;
        GSSCredential proxy;
        /**
         * Constructor for 3rd party transfer
         * @param proxyp a valid proxy
         * @param s source host
         * @param sport source port
         * @param d dest host
         * @param dport dest port
         */
        public SimpleTransfer(GSSCredential proxyp, String s, int sport, String d, int dport) {
                host1 = s;
                port1 = sport;
                host2 = d;
                port2 = dport;
                proxy = proxyp;
        }
        /**
         * Constructor for local-remote transfer
         * @param proxyp a valid proxy
         * @param d remote host
         * @param dport remote port
         */
        public SimpleTransfer(GSSCredential proxyp, String d, int dport) {
                host1 = "localhost";
                host2 = d;
                port2 = dport;
                proxy = proxyp;
        }
        /**
         * Transfer a remote file to local
         * @param remote remote file as source
         * @param local local file as destination
         * @throws GSSException
         * @throws ServerException
         * @throws IOException
         * @throws ClientException
         */
        public void remote2local(String remote, String local) throws GSSException, ServerException, IOException, ClientException {
        	if (proxy.getRemainingLifetime() < SimpleCred.DEFAULT_MIN) {
                throw new GSSException(GSSException.CREDENTIALS_EXPIRED);
        	}

	        GridFTPClient host = new GridFTPClient(host2, port2);
	
	        host.authenticate(proxy);
	
	        //set up the parameter of the client part
	        //host.setProtectionBufferSize(16384);    //unit
	        //host.setDataChannelAuthentication(DataChannelAuthentication.SELF);
	        //host.setDataChannelProtection(GridFTPSession.PROTECTION_SAFE);
	        host.setPassive();
	        host.setLocalActive();
	        //get local file from remote file
	        File localF = new File(local);
	        host.get(remote, localF);
        }
        /**
         * Transfer a local file to remote
         * @param local local file as source
         * @param remote remote file as destination
         * @throws GSSException
         * @throws ServerException
         * @throws IOException
         * @throws ClientException
         */
        public void local2remote(String local, String remote) throws GSSException, ServerException, IOException, ClientException {
        	if (proxy.getRemainingLifetime() < SimpleCred.DEFAULT_MIN) {
        		throw new GSSException(GSSException.CREDENTIALS_EXPIRED);
        	}
	        GridFTPClient host = new GridFTPClient(host2, port2);
	
	        host.authenticate(proxy);
	
	        //set up the parameter of the client part
	        //host.setProtectionBufferSize(16384);    //unit 
	        //host.setDataChannelAuthentication(DataChannelAuthentication.SELF);
	        //host.setDataChannelProtection(GridFTPSession.PROTECTION_SAFE);
	
	        //put local file to remote file, don't append
	        File localF = new File(local);
	        host.put(localF, remote, false);
	    }
        /**
         * TODO: Currently, third-party transfer is not implemented yet. 
         */
        public void transfer() {
        	
        }
        public static void main(String[] args) throws Exception {
        	org.simplegrid.util.Config myconfig=null;
    		SimpleCred mycred=null;
    		GSSCredential proxy = null;
    		try {
    			//String password = Util.getPrivateInput("Input password for user " + args[0]);
    			myconfig = new org.simplegrid.util.Config("/home/liuyan/SimpleGrid/simplegrid/webapp/simplegrid.properties");
    			mycred = new SimpleCred(myconfig.get("grid.myproxy.server"), Integer.parseInt(myconfig.get("grid.myproxy.port")), myconfig.get("grid.myproxy.username"), "Pyu5xtum", myconfig.get("grid.myproxy.proxyfile"));
    			// assume proxy file specified is valid
    			proxy = mycred.get();
    			System.out.println(mycred.info());
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            SimpleTransfer ft = new SimpleTransfer(proxy, "dido.ncsa.uiuc.edu", 2811);
              
            ft.local2remote("/home/liuyan/velocity.log", "/home/gisolve/result3");
            //ft.remote2local("/home/gisolve/sample.uc", "/home/liuyan/gridsphere/myproxy/sample.loopback");
            //ft.local2remote("/home/liuyan/gridsphere/myproxy/sample2", "/home/gisolve/sample.uc2");
            //ft.remote2local("/home/gisolve/sample.uc2", "/home/liuyan/gridsphere/myproxy/sample.loopback2");
           
        }
}

