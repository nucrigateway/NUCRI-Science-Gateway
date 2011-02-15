/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.grid.job;

// Grid security
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSCredential;

// required by WS GRAM client
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.security.cert.X509Certificate;
import javax.xml.rpc.Stub;
import javax.xml.soap.SOAPElement;
import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.delegation.DelegationUtil;
import org.globus.exec.generated.CreateManagedJobInputType;
import org.globus.exec.generated.CreateManagedJobOutputType;
import org.globus.exec.generated.JobDescriptionType;
import org.globus.exec.generated.ManagedJobFactoryPortType;
import org.globus.exec.generated.ManagedJobPortType;
import org.globus.exec.generated.ReleaseInputType;
import org.globus.exec.utils.ManagedJobConstants;
import org.globus.exec.utils.ManagedJobFactoryConstants;
import org.globus.exec.utils.client.ManagedJobClientHelper;
import org.globus.exec.utils.client.ManagedJobFactoryClientHelper;
import org.globus.exec.utils.rsl.RSLHelper;
import org.globus.wsrf.NotificationConsumerManager;
import org.globus.wsrf.WSNConstants;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.impl.security.authentication.Constants;
import org.globus.wsrf.impl.security.authorization.Authorization;
import org.globus.wsrf.impl.security.authorization.HostAuthorization;
import org.globus.wsrf.impl.security.authorization.IdentityAuthorization;
import org.globus.wsrf.impl.security.authorization.SelfAuthorization;
import org.globus.wsrf.impl.security.descriptor.ClientSecurityDescriptor;
import org.globus.wsrf.impl.security.descriptor.GSISecureMsgAuthMethod;
import org.globus.wsrf.impl.security.descriptor.GSITransportAuthMethod;
import org.globus.wsrf.impl.security.descriptor.ResourceSecurityDescriptor;

import org.oasis.wsn.Subscribe;
import org.oasis.wsn.SubscribeResponse;
import org.oasis.wsn.SubscriptionManager;
import org.oasis.wsn.TopicExpressionType;
import org.oasis.wsn.WSBaseNotificationServiceAddressingLocator;
import org.oasis.wsrf.lifetime.Destroy;
import org.oasis.wsrf.properties.GetMultipleResourceProperties_Element;
import org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import org.simplegrid.grid.security.*;


/**
 * WS-GRAM implementation of SimpleRun. WS-GRAM was released in Globus Toolkit 4.x.
 */
public class SimpleRunGT4 implements SimpleRun {
	protected GSSCredential proxy;
	/**
	 * Initialization with a valid Grid proxy
	 * @param cred proxy object
	 */
	public SimpleRunGT4(GSSCredential cred) {
		proxy = cred;
	}
	/**
	 * Submit a single job to remote WS-GRAM service. 
	 * Note: argument "contact" is not used, just to comply with SimpleRun interface
	 * TODO: support multijob submission --> support job delegation
	 */
	public String execute(String contact, String rsl) {
		org.globus.exec.client.GramJob job = null;
		try {
			//JobDescriptionType jobDescription = RSLHelper.readRSL(rsl);
			//System.err.println("Job's endpoint: "+jobDescription.getFactoryEndpoint().toString());
			if (proxy.getRemainingLifetime() < SimpleCred.DEFAULT_MIN)
				throw new Exception("proxy doesn't have enough lifetime to submit job");
            //System.out.println("remaining time: "+cred.getRemainingLifetime());
			job = new org.globus.exec.client.GramJob(rsl);
			job.setCredentials(proxy);
			// Since Endpoint has been specified in RSL, we don't care the 1st argument here
			job.submit(null, true); // batch mode
			System.out.println("Job submitted. RSL:\n"+rsl);
			if (job.isRequested()) {
				return job.getHandle();
			} else {
				throw new Exception("job submission failed");
			}
		} catch (Exception e) {
            System.err.println(" General exception - SimpleRunGT4::execute():\n"+e.toString());
            System.err.println(rsl);
            e.printStackTrace();
            return null;
        }
	}
	public String execute2(String contact, String rsl) {
		org.globus.exec.client.GramJob job = null;
		try {
			//JobDescriptionType jobDescription = RSLHelper.readRSL(rsl);
			//System.err.println("Job's endpoint: "+jobDescription.getFactoryEndpoint().toString());
			URL factoryUrl = ManagedJobFactoryClientHelper.getServiceURL(
				    contact).getURL();
			String factoryType
				    = ManagedJobFactoryConstants.FACTORY_TYPE.PBS;
			EndpointReferenceType factoryEndpoint
				    = ManagedJobFactoryClientHelper.getFactoryEndpoint(factoryUrl, factoryType);
			ManagedJobFactoryPortType factoryPort
				    = ManagedJobFactoryClientHelper.getPort(factoryEndpoint);
			
			
			if (proxy.getRemainingLifetime() < SimpleCred.DEFAULT_MIN)
				return null;
            //System.out.println("remaining time: "+cred.getRemainingLifetime());
			job = new org.globus.exec.client.GramJob(rsl);
			job.setCredentials(proxy);
			// Since Endpoint has been specified in RSL, we don't care the 1st argument here
			job.submit(factoryEndpoint, true); // batch mode

			if (job.isRequested()) {
				return job.getHandle();
			} else {
				return null; 
			}
		} catch (Exception e) {
            System.err.println(" General exception - SimpleRunGT4::execute():\n"+e.toString());
            System.err.println(rsl);
            e.printStackTrace();
            return null;
        }
	}
	/**
	 * Retrieve job status
	 * @param handle Job handle
	 * @return Job status as String
	 */
    public String getStatus(String handle) {
     	org.globus.exec.client.GramJob job = null;
        try {
            job = new org.globus.exec.client.GramJob("<job><executable>probeJob</executable></job>");
            job.setCredentials(proxy);
            job.setHandle(handle);
            job.refreshStatus();
            return job.getState().getValue();
        } catch (Exception e) {
            return null;
        }
    }
    /*
	// test main
    public static void main(String[] args) {
      try {
      	org.simplegrid.util.Config myconfig=new org.simplegrid.util.Config();
      	String rsl = SimpleRSL.getRSL_GT4(myconfig, "UC", "500 20 mydataset", "myoutput");
  		SimpleCred mycred=null;
  		GSSCredential proxy = null;
  		myconfig = new org.simplegrid.util.Config();
  		mycred = new SimpleCred(myconfig.get("grid.myproxy.server"), Integer.parseInt(myconfig.get("grid.myproxy.port")), myconfig.get("grid.myproxy.username"), "", myconfig.get("grid.myproxy.proxyfile"));
  		// assume proxy file specified is valid
  		proxy = mycred.get();

  	  SimpleRunGT4 sr = new SimpleRunGT4(proxy);
        String jobId = sr.execute2(myconfig.get("grid.wsgram."+"UC"), rsl);
        System.out.println("Job submitted. ID: " + jobId);
        String status;
        do {
            Thread.sleep(4000);
            status = sr.getStatus(jobId);
            System.out.println("Job Status: " + status);
        } while (!status.equalsIgnoreCase("Error fetching status") && status.equalsIgnoreCase("Done"));
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    */
}

