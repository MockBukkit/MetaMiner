package org.mockbukkit.metaminer.internal.entity.extractor;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.mockbukkit.metaminer.internal.entity.EntityInformationExtractor;
import org.mockbukkit.metaminer.internal.entity.EntityKeys;
import org.mockbukkit.metaminer.util.NumberUtils;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Optional;

public class StatesEntityInformationExtractor implements EntityInformationExtractor
{
	public static Optional<JsonObject> process(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType, BigDecimal scale)
	{
		JsonObject jsonObject = new StatesEntityInformationExtractor(scale).extractInformation(entityType, mojangEntityType);
		return jsonObject.isEmpty() ? Optional.empty() : Optional.of(jsonObject);
	}

	public static Optional<JsonObject> process(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType, double scale)
	{
		return process(entityType, mojangEntityType, BigDecimal.valueOf(scale));
	}

	private final BigDecimal scale;

	private StatesEntityInformationExtractor(@Nonnull BigDecimal scale)
	{
		Preconditions.checkNotNull(scale, "The scale cannot be null.");
		this.scale = scale;
	}

	@Override
	public JsonObject extractInformation(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		Class<? extends org.bukkit.entity.Entity> entityClass = entityType.getEntityClass();
		assert entityClass != null;

		JsonObject jsonObject = new JsonObject();

		if (org.bukkit.entity.Camel.class.isAssignableFrom(entityClass))
		{
			double sittingHeightDiff = 1.43D;
			BigDecimal width = BigDecimal.valueOf(mojangEntityType.getWidth()).multiply(scale);
			BigDecimal height = BigDecimal.valueOf(mojangEntityType.getHeight()).subtract(BigDecimal.valueOf(sittingHeightDiff)).multiply(scale);
			BigDecimal eyeHeight = BigDecimal.valueOf(mojangEntityType.getDimensions().eyeHeight()).subtract(BigDecimal.valueOf(sittingHeightDiff)).multiply(scale);

			JsonObject camelSitting = new JsonObject();
			camelSitting.addProperty(EntityKeys.WIDTH, NumberUtils.toDouble(width));
			camelSitting.addProperty(EntityKeys.HEIGHT, NumberUtils.toDouble(height));
			camelSitting.addProperty(EntityKeys.EYE_HEIGHT, NumberUtils.toDouble(eyeHeight));
			jsonObject.add("sitting", camelSitting);
		}

		if (Enderman.class.isAssignableFrom(entityClass))
		{
			BigDecimal angryHeightDiff = BigDecimal.valueOf(0.35);
			BigDecimal width = BigDecimal.valueOf(mojangEntityType.getWidth()).multiply(scale);
			BigDecimal height = BigDecimal.valueOf(mojangEntityType.getHeight()).add(angryHeightDiff).multiply(scale);
			BigDecimal eyeHeight = BigDecimal.valueOf(mojangEntityType.getDimensions().eyeHeight()).add(angryHeightDiff).multiply(scale);

			JsonObject camelSitting = new JsonObject();
			camelSitting.addProperty(EntityKeys.WIDTH, NumberUtils.toDouble(width));
			camelSitting.addProperty(EntityKeys.HEIGHT, NumberUtils.toDouble(height));
			camelSitting.addProperty(EntityKeys.EYE_HEIGHT, NumberUtils.toDouble(eyeHeight));
			jsonObject.add("angry", camelSitting);
		}

		// TODO: Player - Sneaking, Gliding, Swimming, Sleeping

		return jsonObject;
	}

}
