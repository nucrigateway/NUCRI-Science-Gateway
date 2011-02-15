/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.grid.job;

import org.simplegrid.util.Config;

import java.io.File;

/**
 * A simple globus RSL composer for the tutorial application
 * @author liuyan
 *
 */
public class SimpleRSL {
	public static String getRSL_GT2(Config myconfig, String site, String exec, String workdir, String outdir, String arguments, String jobid) {
		String project = myconfig.get("grid.projectname");

		
		// GT2 rsl doesn't need to include resource manager element
		//return "& (resourceManagerContact=\"tg-login1.sdsc.teragrid.org:2120/jobmanager-pbs\")" + "\n"
        return "&  (jobType=single)" + "\n"
        + "        (count=1)" + "\n"
        + "        (host_count=\"1"+(site.equals("UC")?":ia64-compute":"")+"\")" + "\n"
        + (project==null?"":"        (project=\""+project+"\")" + "\n")
        + "        (executable=\""+exec+"\")" + "\n"
        + "        (arguments="+wrapArgs(arguments)+")" + "\n"
        + "        (directory=\""+workdir+"\")" + "\n"
        + "        (stdout=\""+outdir+"/stdout."+jobid+"\")" + "\n"
        + "        (stderr=\""+outdir+"/stderr."+jobid+"\")" + "\n"
        + "" + "\n";
	}
	private static String wrapArgs(String arguments) {
		String[] s = arguments.split("\\s+");
		String args = "";
		for (int i=0; i<s.length; i++) {
			args += "\""+s[i]+"\" ";
		}
		return args;
	}
	public static String getRSL_GT4(Config myconfig, String site, String exec, String workdir, String outdir, String arguments, String jobid) {
		return SimpleRSL.getRSL_GT4(true, myconfig, site, exec, workdir, outdir, arguments, jobid);
	}
	public static String getRSL_GT4(boolean header, Config myconfig, String site, String exec, String workdir, String outdir, String arguments, String jobid) {
		String project = myconfig.get("grid.projectname");
		String remoteurl = myconfig.get("grid.wsgram."+site);
		String remotescheduler = myconfig.get("grid.wsgram.rm."+site);
		String argxml = "";
		String[] args = arguments.split("\\s+");
		for (int i=0; i<args.length; i++) {
			argxml += "    <argument>"+args[i]+"</argument>" + "\n";
		}
		return "<job>" + "\n"
		+	(header?(
		  "    <factoryEndpoint" + "\n"
        + "          xmlns:gram=\"http://www.globus.org/namespaces/2004/10/gram/job\"" + "\n"
        + "          xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/03/addressing\">" + "\n"
        + "        <wsa:Address>"+remoteurl+"</wsa:Address>" + "\n"
        + "        <wsa:ReferenceProperties>" + "\n"
        + "            <gram:ResourceID>"+remotescheduler+"</gram:ResourceID>" + "\n"
        + "        </wsa:ReferenceProperties>" + "\n"
        + "    </factoryEndpoint>" + "\n"
        	):"")
/* optional        	
        + "    <jobType>single</jobType>" + "\n"
        + "    <count>1</count>" + "\n"
        + "    <hostCount>1</hostCount>" + "\n"
*/
        + (project==null?"":"    <project>"+project+"</project>" + "\n")
        + "    <executable>"+exec+"</executable>" + "\n"
        + "    <directory>"+workdir+"</directory>" + "\n"
        + argxml
        + "    <stdout>"+outdir+"/stdout."+jobid+"</stdout>" + "\n"
        + "    <stderr>"+outdir+"/stderr."+jobid+"</stderr>" + "\n"
        + "</job>" + "\n";
	}
}

