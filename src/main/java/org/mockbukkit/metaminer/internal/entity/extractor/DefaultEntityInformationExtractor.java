package org.mockbukkit.metaminer.internal.entity.extractor;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.mockbukkit.metaminer.internal.entity.EntityInformationExtractor;
import org.mockbukkit.metaminer.internal.entity.EntityKeys;

public class DefaultEntityInformationExtractor implements EntityInformationExtractor
{
	private static final DefaultEntityInformationExtractor INSTANCE = new DefaultEntityInformationExtractor();

	public static JsonObject process(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		return INSTANCE.extractInformation(entityType, mojangEntityType);
	}

	private DefaultEntityInformationExtractor()
	{
		// Hide the public constructor
	}

	@Override
	public JsonObject extractInformation(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		double scale = 1;

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(EntityKeys.WIDTH, mojangEntityType.getWidth());
		jsonObject.addProperty(EntityKeys.HEIGHT, mojangEntityType.getHeight());
		jsonObject.addProperty(EntityKeys.SCALE, scale);
		jsonObject.addProperty(EntityKeys.EYE_HEIGHT, mojangEntityType.getDimensions().eyeHeight());

		StatesEntityInformationExtractor.process(entityType, mojangEntityType, scale).ifPresent(object -> jsonObject.add(EntityKeys.STATES, object));

		// Process the class
		Class<? extends Entity> entityClass = entityType.getEntityClass();
		Preconditions.checkState(entityClass != null, "The entity {} does not have a entity class.", entityType.name());
		if (AbstractArrow.class.isAssignableFrom(entityClass)) {
			jsonObject.addProperty(EntityKeys.BASE_DAMAGE, BaseDamageInformationExtractor.process(entityType));
		}

		return jsonObject;
	}

}
