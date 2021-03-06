package org.snowyegret.mojo.util;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import org.snowyegret.mojo.MoJo;

public class ModelResourceLocations {

	public static ModelResourceLocation forClass(Class cls) {
		if (Item.class.isAssignableFrom(cls)) {
			return new ModelResourceLocation(MoJo.MODID + ":" + StringUtils.underscoreNameFor(cls), "inventory");
		} else if (Block.class.isAssignableFrom(cls)) {
			ModelResourceLocation mrl = new ModelResourceLocation(MoJo.MODID + ":" + StringUtils.underscoreNameFor(cls), "normal");
			//System.out.println("mrl=" + mrl);
			return mrl;
		} else {
			throw new Error("Expected either an item or a block. Got a " + cls);
		}
	}

}
