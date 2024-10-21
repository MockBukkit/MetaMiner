package org.mockbukkit.metaminer.internal.entity.extractor;

import com.google.gson.JsonObject;
import org.bukkit.entity.EntityType;

public class SmallEntityInformationExtractor
{

	public static JsonObject process(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		if (EntityType.ARMOR_STAND.equals(entityType))
		{
			return ScaleEntityInformationExtractor.process(entityType, mojangEntityType, 0.5);
		}

		return ScaleEntityInformationExtractor.process(entityType, mojangEntityType, 1);
	}

	private SmallEntityInformationExtractor()
	{
		// Hide the public constructor
	}

}
