package org.snowyegret.mojo.network;

import org.snowyegret.mojo.MoJo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PickMessageHandler implements IMessageHandler<PickMessage, IMessage> {

	@Override
	public IMessage onMessage(final PickMessage message, MessageContext ctx) {
		Minecraft minecraft = Minecraft.getMinecraft();
		final WorldClient worldClient = minecraft.theWorld;
		minecraft.addScheduledTask(new Runnable() {
			public void run() {
				processMessage(worldClient, message);
			}

		});

		return null;
	}

	// Private------------------------------------------------------------

	private void processMessage(WorldClient worldClient, PickMessage message) {
		MoJo.instance.setPickInfo(message.getPickInfo()); 
	}

}