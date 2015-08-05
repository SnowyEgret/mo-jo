package org.snowyegret.mojo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.snowyegret.mojo.block.BlockPicked;
import org.snowyegret.mojo.block.BlockSaved;
import org.snowyegret.mojo.block.BlockSelected;
import org.snowyegret.mojo.util.ModelResourceLocations;

public class ClientProxy extends CommonProxy {

	private static final int META = 0;

	// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe15_item_smartitemmodel/StartupClientOnly.java
	@Override
	public void registerItemModels() {
		System.out.println("Registering Item models...");
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		for (Item i : items) {
			ModelResourceLocation mrl = ModelResourceLocations.forClass(i.getClass());
			mesher.register(i, META, mrl);
			// System.out.println(i.getUnlocalizedName() + " model=" + mesher.getModelManager().getModel(mrl));
		}

	}

	// https://github.com/TheGreyGhost/MinecraftByExample/blob/master/src/main/java/minecraftbyexample/mbe04_block_smartblockmodel1/StartupClientOnly.java
	// This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
	// or in your hand or thrown on the ground).
	// Minecraft knows to look for the item model based on the GameRegistry.registerBlock. However the registration of
	// the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
	// of any extra items you have created. Hence you have to do it manually. This will probably change in future.
	// It must be done in the init phase, not preinit, and must be done on client only.
	public void registerItemBlockModels() {
		System.out.println("Registering ItemBlock models...");
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		Item item = GameRegistry.findItem(MoJo.MODID, "block_saved");
		// System.out.println("item=" + item);
		ModelResourceLocation mrl = new ModelResourceLocation(MoJo.MODID+":block_saved", "inventory");
		mesher.register(item, META, mrl);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + item.getUnlocalizedName() + " model="
				+ mesher.getModelManager().getModel(mrl));
	}

	@Override
	public void setCustomStateMappers() {
		System.out.println("Setting custom state mappers...");
		ModelLoader.setCustomStateMapper(MoJo.blockSelected, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return ModelResourceLocations.forClass(BlockSelected.class);
			}
		});
		ModelLoader.setCustomStateMapper(MoJo.blockPicked, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return ModelResourceLocations.forClass(BlockPicked.class);
			}
		});
		ModelLoader.setCustomStateMapper(MoJo.blockSaved, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
				return ModelResourceLocations.forClass(BlockSaved.class);
			}
		});
	}
}
