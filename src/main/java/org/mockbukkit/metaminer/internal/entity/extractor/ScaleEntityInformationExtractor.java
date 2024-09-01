package org.mockbukkit.metaminer.internal.entity.extractor;

import com.google.gson.JsonObject;
import org.bukkit.entity.EntityType;
import org.mockbukkit.metaminer.internal.entity.EntityInformationExtractor;
import org.mockbukkit.metaminer.internal.entity.EntityKeys;
import org.mockbukkit.metaminer.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScaleEntityInformationExtractor implements EntityInformationExtractor
{
	public static JsonObject process(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType, double scale)
	{
		return new ScaleEntityInformationExtractor(scale).extractInformation(entityType, mojangEntityType);
	}

	private final BigDecimal scale;

	private ScaleEntityInformationExtractor(double scale)
	{
		this.scale = BigDecimal.valueOf(scale);
	}

	@Override
	public JsonObject extractInformation(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		BigDecimal normalWidth = BigDecimal.valueOf(mojangEntityType.getWidth());
		BigDecimal normalHeight = BigDecimal.valueOf(mojangEntityType.getHeight());
		BigDecimal normalEyeHeight = BigDecimal.valueOf(mojangEntityType.getDimensions().eyeHeight());

		BigDecimal babyWidth = normalWidth.multiply(scale);
		BigDecimal babyHeight = normalHeight.multiply(scale);
		BigDecimal babyEyeHeight = BigDecimal.ZERO;
		if (BigDecimal.ZERO.compareTo(normalHeight) != 0)
		{
			babyEyeHeight = babyHeight.multiply(normalEyeHeight).divide(normalHeight, RoundingMode.HALF_UP);
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(EntityKeys.WIDTH, NumberUtils.toDouble(babyWidth));
		jsonObject.addProperty(EntityKeys.HEIGHT, NumberUtils.toDouble(babyHeight));
		jsonObject.addProperty(EntityKeys.SCALE, NumberUtils.toDouble(scale));
		jsonObject.addProperty(EntityKeys.EYE_HEIGHT, NumberUtils.toDouble(babyEyeHeight));

		StatesEntityInformationExtractor.process(entityType, mojangEntityType, scale).ifPresent(object -> jsonObject.add(EntityKeys.STATES, object));

		return jsonObject;
	}

}