package org.mockbukkit.metaminer.internal.entity.extractor;

import com.google.gson.JsonObject;
import org.bukkit.entity.EntityType;

public class BigEntityInformationExtractor
{

	public static JsonObject process(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		return ScaleEntityInformationExtractor.process(entityType, mojangEntityType, 4);
	}

	private BigEntityInformationExtractor()
	{
		// Hide the public constructor
	}

}
