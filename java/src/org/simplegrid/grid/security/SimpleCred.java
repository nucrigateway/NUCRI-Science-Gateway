/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.grid.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import org.globus.gsi.CertUtil;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.gsi.gssapi.auth.IdentityAuthorization;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.util.Util;
import org.globus.common.CoGProperties;
import org.globus.common.Version;
import org.globus.myproxy.CredentialInfo;
import org.globus.myproxy.ChangePasswordParams;
import org.globus.myproxy.DestroyParams;
import org.globus.myproxy.InitParams;
import org.globus.myproxy.GetParams;
import org.globus.myproxy.InfoParams;
import org.globus.myproxy.StoreParams;
import org.globus.myproxy.MyProxyException;

import org.gridforum.jgss.ExtendedGSSManager;
import org.gridforum.jgss.ExtendedGSSCredential;

import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSException;

/**
 * A simple grid proxy credential manager that wraps Java COG and MyProxy API 
 * and provide an easy-to-use interface for grid portal developers. Once initialized,
 * user just needs to call the get() method which fetches a proxy from either a local
 * file or MyProxy server, maintains the proxy, and automatically renew it when the
 * current proxy is expired.
 * 
 * @author liuyan
 *
 */
public class SimpleCred {
	public static final int DEFAULT_LIFETIME = 12 * 3600;
	public static final int DEFAULT_MIN = 3 * 3600;
	String proxy_file; // this is a valid proxy file that exists already
	String host;
	int port;
	int requested_lifetime;
	String username;
	String password;
	GSSCredential proxy;
	org.globus.myproxy.MyProxy myproxy;
	boolean logon_save = true; // save as proxy file after myproxy logon

	/**
	 * SimpleCred constructor
	 * @param server myproxy server address
	 * @param p myproxy server port
	 * @param user myproxy user name
	 * @param passwd myproxy user password
	 * @param fname local proxy file name
	 * @throws Exception
	 */
	public SimpleCred(String server, int p, String user, String passwd, String fname) throws Exception {
		host = server;
		port = p;
		proxy = null;
		myproxy = null; 
		username = user;
		password = passwd;
		proxy_file = fname;
		requested_lifetime = SimpleCred.DEFAULT_LIFETIME;
	}
	/**
	 * Load proxy from a valid proxy file. It is implemented by calling Java COG API
	 * @param certFile local proxy file
	 */
	public void load(String certFile) {
		proxy = null;
		try{
			// commented code below is not recommended by cog manual. listed here for illustration purpose
			//GlobusCredential cred = new GlobusCredential(proxyFile);	        
        	//GSSCredential gssCred = new GlobusGSSCredentialImpl(cred, GSSCredential.INITIATE_AND_ACCEPT);
			//proxy = gssCred;
        //} catch (GlobusCredentialException e1){
       	// 	e1.printStackTrace();
			
			File proxyFile = new File(certFile);
			if (proxyFile.exists()) {
				byte [] credData = new byte[(int)proxyFile.length()];
				FileInputStream in = new FileInputStream(proxyFile);
				in.read(credData);
				in.close();
				// create credential by loading from proxy file
				ExtendedGSSManager manager = (ExtendedGSSManager)ExtendedGSSManager.getInstance();
				proxy = manager.createCredential(credData,
		  		                         ExtendedGSSCredential.IMPEXP_OPAQUE,
		                                 GSSCredential.DEFAULT_LIFETIME,
		                                 null, // use default mechanism - GSI
		                                 GSSCredential.INITIATE_AND_ACCEPT);
			}
        } catch (GSSException e2) {
        	e2.printStackTrace();           
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        }
        if (!this.isValid())
        	proxy = null;
        System.out.println(this.info());
	}
	/**
	 * MyProxy logon with default lifetime request
	 * @param user user name on myproxy server
	 * @param passwd password
	 * @throws GSSException
	 */
	public void logon(String user, String passwd) throws GSSException {
		this.logon(user, passwd, this.requested_lifetime);
	}
	/**
	 * Fetch a proxy from MyProxy server by calling MyProxy API
	 * @param user user name on myproxy server
	 * @param passwd password
	 * @param ltseconds lifetime to request in seconds
	 * @throws GSSException
	 */
	public void logon(String user, String passwd, int ltseconds) throws GSSException {
		CertUtil.init();
		username = user;
		password = passwd;
		requested_lifetime = ltseconds;
		GetParams getRequest = new GetParams();
		getRequest.setUserName(user);
		getRequest.setPassphrase(passwd);
		getRequest.setCredentialName(null); // anonymous logon
		getRequest.setLifetime(requested_lifetime);
		    
		try {
			if (myproxy == null) {
				myproxy = new org.globus.myproxy.MyProxy(this.host, this.port);
			}
		    proxy = myproxy.get(null, getRequest);
		    if (!this.isValid()) 
		    	proxy = null;
			//System.out.println(this.info());
		} catch(MyProxyException e) {
		    proxy = null;
		    e.printStackTrace();
		}
	}
	/**
	 * Save proxy into a proxy file
	 * @param proxy_location the proxy file to be written
	 * @return success or failure
	 */
	public boolean export(String proxy_location) {
		try {
			if (proxy_location != null) {
			    // create a file
			    File f = new File(proxy_location);
			    if (!f.exists() || f.canWrite()) {
				    OutputStream out = new FileOutputStream(proxy_location);
					// set read only permissions
					Util.setOwnerAccessOnly(proxy_location);
					// write the contents
					byte [] data = ((ExtendedGSSCredential)proxy).export(ExtendedGSSCredential.IMPEXP_OPAQUE);
					out.write(data);
				    out.close();
			    }
			}
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		} catch(GSSException e) {
		    e.printStackTrace();
		    return false;
		}
	}
	/**
	 * default get() method to fetch a valid proxy
	 * @return a valid grid proxy or null
	 */
	public GSSCredential get() {
		return this.get(false);
	}
	/**
	 * Get a valid proxy.
	 * If there is a valid proxy, return it;
	 * If a local proxy file contains a valid proxy, load and return it;
	 * Otherwise, logon to myproxy server and fetch a valid proxy.
	 * User does not need to call <code>logon()</code> or <code>load()</code>
	 * explicitly. <code>get()</code> is sufficient for general use
	 * @param forceRetrieve destroy current proxy and get again
	 * @return a valid grid proxy or null
	 */
	public GSSCredential get(boolean forceRetrieve) {
		// destry current credential first
		if (forceRetrieve && proxy != null) {
			try {
				proxy.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
			proxy = null;
		}
		if (this.isValid())
			return proxy;
		try {
			this.load(proxy_file);
			if (proxy != null)
				return proxy;
			this.logon(username, password);
			if (proxy != null) {
				if (logon_save) {
					this.export(proxy_file);
				}
				return proxy;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Print grid proxy information
	 * @return proxy information as a String
	 */
	public String info() {
		if (!this.isValid())
			return "";
		String s = "";
		s += "Subject: " + this.getDN() + "\n";
		int t = this.getRemainingTime();
		s += "Remaining lifetime: " + (t / 3600) + ":" + ((t % 3600) / 60) + ":" + (t % 60) + " ("+ t + " seconds)\n";
		s += "MyProxy server: " + this.host + ":" + this.port + "\n";
		s += "MyProxy user: " +this.username +"\n";
		
		return s;
	}
	/**
	 * Get subject (DN) information of a proxy
	 * @return Subject of the proxy as a String
	 */
	public String getDN() {
		try {
			return (this.isValid()?(proxy.getName().toString()):"");
		} catch (GSSException e) {
			return "";
		}
	}
	/**
	 * Test if current proxy is valid
	 * @return true or false
	 */
	public boolean isValid() {
		if (proxy == null) return false;
		try {
			// the min requirement for a globus job submission
			return proxy.getRemainingLifetime() > SimpleCred.DEFAULT_MIN;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * Get remaining time in seconds
	 * @return the remaining time of a proxy in seconds
	 */
	public int getRemainingTime() {
		if (proxy == null) return 0;
		try {
			return proxy.getRemainingLifetime();
		} catch (GSSException e) {
			return 0;
		}
	}
	public int getRequestedLifeTime() {
		return this.requested_lifetime;
	}
	//TODO
	public void destroy() {
		
	}

	// bean set/get methods
	public String getUsername() { return this.username; }
	public String getPassword() { return this.password; }
	public String getHost() { return this.host; }
	public int getPort() { return this.port; }
	public String getProxyFile() { return this.proxy_file; }
	public void setUsername(String uname) { this.username = uname; }
	public void setPassword(String pass) { this.password = pass; }
	public void setHost(String h) { this.host = h; }
	public void setPort(int p) { this.port = p; }
	public void setProxyFile(String pf) { this.proxy_file = pf; }
	public void setRemainingTime(int ltseconds) { this.requested_lifetime = ltseconds; }
	
	public static void main(String[] args) {
		org.simplegrid.util.Config myconfig=null;
		SimpleCred mycred=null;
		try {
			//String password = Util.getPrivateInput("Input password for user " + args[0]);
			myconfig = new org.simplegrid.util.Config();
			mycred = new SimpleCred(myconfig.get("grid.myproxy.server"), Integer.parseInt(myconfig.get("grid.myproxy.port")), myconfig.get("grid.myproxy.username"), "", myconfig.get("grid.myproxy.proxyfile"));
		
			GSSCredential proxy = mycred.get();
			System.out.println(mycred.info());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mycred.logon("gisolve", ""); // feed correct password
			System.out.println(mycred.info());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

