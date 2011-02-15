/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial Information Laboratory (CIGI),
 *  University of Illinois at Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.app.dms;

import org.gisolve.viz.raster.DirectImage;

import org.ietf.jgss.GSSCredential;
import org.simplegrid.grid.data.*;
import org.simplegrid.grid.job.*;
import org.simplegrid.grid.security.*;
import org.simplegrid.util.*;

public class DMS {

	public String submit(String id, int resolutionx, int resolutiony, int k, String inputData) {
		try {
			// get configuration
			org.simplegrid.util.Config myconfig = this.getConfig();
			// 1. get a proxy
			SimpleCred mycred = new SimpleCred(myconfig.get("grid.myproxy.server"), Integer.parseInt(myconfig.get("grid.myproxy.port")), myconfig.get("grid.myproxy.username"), myconfig.get("grid.myproxy.password"), myconfig.get("grid.myproxy.proxyfile"));
			GSSCredential proxy = mycred.get();
			System.out.println(mycred.info());
			System.out.println();
			
			// get TeraGrid site
			String site = this.getTGSite(myconfig);
			String siteGram = this.getTGSiteGram(myconfig);
			System.out.println("Job "+id+" will be submitted to "+site+" using "+siteGram);
			System.out.println();
			
			// 2. transfer the sample dataset to one of the teragrid site
			SimpleTransfer ft = new SimpleTransfer(proxy, myconfig.get("grid.gridftp."+site), 2811);
			ft.local2remote(inputData, myconfig.get("grid.sites.dmsdir."+site)+"/datasets/"+id+".dat");
			System.out.println("Dataset "+inputData+" has been transferred to "+myconfig.get("grid.gridftp."+site));
			System.out.println();
			
			// 3. submit job : GT4/GT2 method
			String jobId="";
			String appdir = this.getRemoteAppDir(myconfig, site);
			
			String workdir = appdir+"/release";
			String exec = workdir+"/process.pl";
			String outdir = appdir + "/results";
			String arguments = resolutionx+" "+resolutiony+" "+k+" "
				+appdir+"/datasets/"+id+".dat"+" "
				+appdir+"/results/"+id+".dat";
			if (siteGram.equalsIgnoreCase("GT4")) {
				String rsl = SimpleRSL.getRSL_GT4(myconfig, site, exec, workdir, outdir, arguments, id);
				SimpleRunGT4 sr = new SimpleRunGT4(proxy);
				jobId = sr.execute(myconfig.get("grid.wsgram."+site), rsl);
			} else {
                String rsl = SimpleRSL.getRSL_GT2(myconfig, site, exec, workdir, outdir, arguments, id);
                System.out.println("RSL:\n"+rsl);
                SimpleRunGT2 sr = new SimpleRunGT2(proxy);
                jobId = sr.execute(myconfig.get("grid.gram."+site), rsl);
			}
			System.out.println("Job "+id+" is submitted: TeraGrid job handle is "+jobId);
			return jobId;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	public String getStatus(String jobId) {
		try {
			String status;
			// get configuration
			org.simplegrid.util.Config myconfig = this.getConfig();
			// get TeraGrid site
			String siteGram = this.getTGSiteGram(myconfig);
			// 1. get a proxy
			SimpleCred mycred = new SimpleCred(myconfig.get("grid.myproxy.server"), Integer.parseInt(myconfig.get("grid.myproxy.port")), myconfig.get("grid.myproxy.username"), myconfig.get("grid.myproxy.password"), myconfig.get("grid.myproxy.proxyfile"));
			GSSCredential proxy = mycred.get();
	
			// 2. get status
			if (siteGram.equalsIgnoreCase("GT4")) {
				SimpleRunGT4 sr = new SimpleRunGT4(proxy);
				status = sr.getStatus(jobId);
			} else {
				SimpleRunGT2 sr = new SimpleRunGT2(proxy);
				status = sr.getStatus(jobId);
			}
			if (status.equalsIgnoreCase("Error fetching status") || status.equalsIgnoreCase("DONE"))
				status="finished";
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	public String getResult(String id, String jobId, String filePath) {
		try {
			// get configuration
			org.simplegrid.util.Config myconfig = this.getConfig();
			// 1. get a proxy
			SimpleCred mycred = new SimpleCred(myconfig.get("grid.myproxy.server"), Integer.parseInt(myconfig.get("grid.myproxy.port")), myconfig.get("grid.myproxy.username"), myconfig.get("grid.myproxy.password"), myconfig.get("grid.myproxy.proxyfile"));
			GSSCredential proxy = mycred.get();
	
			// 4. transfer results back
			String site = this.getTGSite(myconfig);
			SimpleTransfer ft = new SimpleTransfer(proxy, myconfig.get("grid.gridftp."+site), 2811);
			String remotefile = myconfig.get("grid.sites.dmsdir."+site)+"/results/"+id+".dat";
			ft.remote2local(remotefile, filePath);
			System.out.println("Result of job "+id+" has been transferred back to "+filePath);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	public String visualize(String filePath, String bndPath, String imagePath) {
		try {
			DirectImage di = new DirectImage();
            di.loadDataset(filePath, bndPath);
            di.createImage(1600, imagePath);
            System.out.println("Visualization image has been created as "+imagePath);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	private Config getConfig() {
		try {
			String homePath = this.getHomePath();
			String configPath = homePath + "/etc/simplegrid.properties";
			org.simplegrid.util.Config myconfig= new org.simplegrid.util.Config(configPath);
			return myconfig;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private String getHomePath() {
		String homePath = System.getenv("SIMPLEGRID_HOME");
		if (homePath == null || homePath.equals(""))
			homePath = "/opt/simplegrid2";
		return homePath;
	}
	private String getTGSite(Config myconfig) {
		return (myconfig==null)?"":myconfig.get("grid.defaultsite");
	}
	private String getTGSiteGram(Config myconfig) {
		return (myconfig==null)?"":myconfig.get("grid.defaultsite.gram");
	}
	private String getRemoteAppDir(Config myconfig, String site) {
		return (myconfig==null)?"":myconfig.get("grid.sites.dmsdir."+site);
	}

}
