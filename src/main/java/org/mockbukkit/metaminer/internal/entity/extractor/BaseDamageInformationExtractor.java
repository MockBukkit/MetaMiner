package org.mockbukkit.metaminer.internal.entity.extractor;

import org.bukkit.entity.EntityType;

public class BaseDamageInformationExtractor
{
	private BaseDamageInformationExtractor()
	{
		// Hide public constructor
	}

	public static double process(EntityType entityType)
	{
		if (EntityType.ARROW.equals(entityType) || EntityType.SPECTRAL_ARROW.equals(entityType))
		{
			return 2.0D;
		}
		if (EntityType.TRIDENT.equals(entityType))
		{
			return 8.0D;
		}

		String className = (entityType.getEntityClass() != null ? entityType.getEntityClass().getName() : "null");
		throw new UnsupportedOperationException(String.format("Unknown base damage for entity %s (%s)", entityType.name(), className));
	}

}
