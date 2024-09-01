package org.mockbukkit.metaminer.internal.entity.extractor;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.camel.Camel;
import org.bukkit.entity.EntityType;

public class BabyEntityInformationExtractor
{

	public static JsonObject process(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		double scale = getBabyScaleAsDouble(entityType, mojangEntityType);
		return ScaleEntityInformationExtractor.process(entityType, mojangEntityType, scale);
	}

	private static <T extends Entity> double getBabyScaleAsDouble(EntityType entityType, net.minecraft.world.entity.EntityType<T> mojangEntityType)
	{
		return switch (entityType)
		{
			case ARMADILLO -> Armadillo.BABY_SCALE;
			case TURTLE -> 0.30D; // Constant is not public
			case CAMEL -> Camel.BABY_SCALE;
			default -> LivingEntity.DEFAULT_BABY_SCALE;
		};
	}

	private BabyEntityInformationExtractor()
	{
		// Hide the public constructor
	}

}
