package org.mockbukkit.metaminer.internal.entity.extractor;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mockbukkit.metaminer.internal.entity.EntityInformationExtractor;

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

		if (Camel.class.isAssignableFrom(entityClass))
		{
			JsonObject camelSitting = HeightDifferenceEntityInformationExtractor.process(entityType, mojangEntityType, -1.43D, scale);
			jsonObject.add("sitting", camelSitting);
		}

		if (Enderman.class.isAssignableFrom(entityClass))
		{
			JsonObject enderManAngry = HeightDifferenceEntityInformationExtractor.process(entityType, mojangEntityType, 0.35D, scale);
			jsonObject.add("angry", enderManAngry);
		}

		if (Player.class.isAssignableFrom(entityClass))
		{
			// Sneaking
			JsonObject camelSitting = HeightDifferenceEntityInformationExtractor.process(entityType, mojangEntityType, -0.3D, scale);
			jsonObject.add("sneaking", camelSitting);

			// Gliding / Swimming / Crawling
			JsonObject gliding = HeightDifferenceEntityInformationExtractor.process(entityType, mojangEntityType, -1.2D, scale);
			jsonObject.add("gliding", gliding);
			jsonObject.add("swimming", gliding);
			jsonObject.add("crawling", gliding);

			// Sneaking
			JsonObject sleeping = HeightDifferenceEntityInformationExtractor.process(entityType, mojangEntityType, -1.6D, scale);
			jsonObject.add("sleeping", sleeping);
		}

		return jsonObject;
	}

}
