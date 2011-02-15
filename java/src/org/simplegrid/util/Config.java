/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.util;

import java.util.Properties;
import java.io.File;

public class Config {
	private String conf_file;
	private Properties conf;
	public Config() throws java.io.IOException {
		String home_dir = System.getenv("HOME"); 
		conf_file = home_dir + File.separator + "simplegrid.properties";
		loadConfig();
	}
	public Config(String conf) throws java.io.IOException {
		conf_file = conf;
		loadConfig();
	}
	private void loadConfig() throws java.io.IOException {
		File f = new File(conf_file);
		conf = null;
  		if (f.exists() && f.isFile() && f.canRead()) {
			conf = new Properties();
   			conf.load(new java.io.FileInputStream(conf_file));
   		} else {
   			throw new java.io.IOException("Error loading SimpleGrid configuration: " + conf_file);
   		}
	}
	/**
	 * This method reads a config item value
	 * @param name the key of a config item
	 * @return the value of a config item
	 */
	public String get(String name) {
		return conf.getProperty(name, null);
	}
	public String get(String name, String defaultValue) {
		return conf.getProperty(name, defaultValue);
	}
	/**
	 * This method sets/overrides a config item
	 * @param name the key of a config item
	 * @param value the value of a config item
	 */
	public void set(String name, String value) {
		conf.setProperty(name, value);
	}
	
	public void print() {
		conf.list(System.out);
	}
	/**
	 * The main() for test purpose
	 */
	public static void main(String[] args) throws Exception {
		Config myconfig = new Config();
		myconfig.print();
	}
}

