package org.snowyegret.mojo.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockHighlightSmartModel implements ISmartBlockModel {

	private IBakedModel model;
	private int color;
	private static final int COLOR = new Color(200, 200, 255).getRGB();

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		IBlockState prevState = ((IExtendedBlockState) state).getValue(BlockHighlight.PROP_STATE);
		color = ((IExtendedBlockState) state).getValue(BlockHighlight.PROP_COLOR);
		// Fix for Crash with infinite loop at BlockSelected/PickedModel.isAmbientOcclusion #172
		// When selecting blocks left in world with a selection spell, sometimes s was BlockSelected instead of null
		if (prevState != null && prevState.getBlock() instanceof BlockHighlight) {
			// TODO Can I get its extended state again?
			System.out.println("State is BlockSelected. Setting prevState to null to avoid infinite loop");
			prevState = null;
		}
		if (prevState == null) {
			System.out.println("prevState=" + prevState);
			System.out.println(">>>>>>>>>>>>>Setting prevState to default");
			prevState = Blocks.clay.getDefaultState();
		}
		model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(prevState);
		return this;
	}

	@Override
	public List getFaceQuads(EnumFacing face) {
		List<BakedQuad> quads = new ArrayList<>();
		List<BakedQuad> faceQuads = model.getFaceQuads(face);
		for (BakedQuad q : faceQuads) {
			quads.add(new BakedQuad(tint(q.getVertexData()), 0, face));
		}
		return quads;
	}

	@Override
	public List getGeneralQuads() {
		List<BakedQuad> quads = new ArrayList<>();
		List<BakedQuad> generalQuads = model.getGeneralQuads();
		for (BakedQuad q : generalQuads) {
			quads.add(new BakedQuad(tint(q.getVertexData()), 0, q.getFace()));
		}
		return quads;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return model.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return model.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return model.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model.getItemCameraTransforms();
	}

	// Private-----------------------------------------------------

	private int[] tint(int[] vertexData) {
		int[] vd = new int[vertexData.length];
		System.arraycopy(vertexData, 0, vd, 0, vertexData.length);
//		vd[3] = COLOR;
//		vd[10] = COLOR;
//		vd[17] = COLOR;
//		vd[24] = COLOR;
		vd[3] = color;
		vd[10] = color;
		vd[17] = color;
		vd[24] = color;
		return vd;
	}

}
