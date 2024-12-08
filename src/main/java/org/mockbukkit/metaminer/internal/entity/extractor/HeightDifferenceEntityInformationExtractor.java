package org.mockbukkit.metaminer.internal.entity.extractor;

import com.google.gson.JsonObject;
import org.bukkit.entity.EntityType;
import org.mockbukkit.metaminer.internal.entity.EntityInformationExtractor;
import org.mockbukkit.metaminer.internal.entity.EntityKeys;
import org.mockbukkit.metaminer.util.NumberUtils;

import java.math.BigDecimal;

public class HeightDifferenceEntityInformationExtractor implements EntityInformationExtractor
{
	public static JsonObject process(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType, double difference, BigDecimal scale)
	{
		return new HeightDifferenceEntityInformationExtractor(difference, scale).extractInformation(entityType, mojangEntityType);
	}

	private final BigDecimal difference;
	private final BigDecimal scale;

	private HeightDifferenceEntityInformationExtractor(double difference, BigDecimal scale)
	{
		this.difference = BigDecimal.valueOf(difference);
		this.scale = scale;
	}

	@Override
	public JsonObject extractInformation(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		BigDecimal width = BigDecimal.valueOf(mojangEntityType.getWidth()).multiply(scale);
		BigDecimal height = BigDecimal.valueOf(mojangEntityType.getHeight()).add(difference).multiply(scale);
		BigDecimal eyeHeight = BigDecimal.valueOf(mojangEntityType.getDimensions().eyeHeight()).add(difference).multiply(scale);

		JsonObject newHeightData = new JsonObject();
		newHeightData.addProperty(EntityKeys.WIDTH, NumberUtils.toDouble(width));
		newHeightData.addProperty(EntityKeys.HEIGHT, NumberUtils.toDouble(height));
		newHeightData.addProperty(EntityKeys.EYE_HEIGHT, NumberUtils.toDouble(eyeHeight));
		return newHeightData;
	}

}
