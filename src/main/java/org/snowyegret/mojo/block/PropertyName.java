package org.snowyegret.mojo.block;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyName implements IUnlistedProperty<String> {

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isValid(String value) {
		return true;
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public String valueToString(String value) {
		return value;
	}

}
