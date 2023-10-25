package org.Utkanowski.ultraRPC;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

/**
 * Rusherhack RPC plugin
 *
 * @author Utkanowski
 */
public class ultraRPCPlugin extends Plugin {
	
	@Override
	public void onLoad() {
		
		//logger
		this.getLogger().info(this.getName() + " loaded!");
		this.getLogger().info("Hello World!");
		
		//creating and registering a new module
		final UltraRPC ultraRPC = new UltraRPC();
		RusherHackAPI.getModuleManager().registerFeature(ultraRPC);
		
		//creating and registering a new hud element
		//final ExampleHudElement exampleHudElement = new ExampleHudElement();
		//RusherHackAPI.getHudManager().registerFeature(exampleHudElement);
		
		//creating and registering a new command
		//final ExampleCommand exampleCommand = new ExampleCommand();
		//RusherHackAPI.getCommandManager().registerFeature(exampleCommand);
	}
	
	@Override
	public void onUnload() {
		this.getLogger().info(this.getName() + " unloaded!");
	}
	
	@Override
	public String getName() {
		return "UltraRPC";
	}
	
	@Override
	public String getVersion() {
		return "v1.0";
	}
	
	@Override
	public String getDescription() {
		return "Ultra customizable RPC plugin";
	}
	
	@Override
	public String[] getAuthors() {
		return new String[]{"Ukanowski"};
	}
}
