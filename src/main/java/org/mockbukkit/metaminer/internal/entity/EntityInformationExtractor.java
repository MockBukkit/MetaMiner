package org.mockbukkit.metaminer.internal.entity;

import com.google.gson.JsonObject;
import org.bukkit.entity.EntityType;

public interface EntityInformationExtractor
{

	JsonObject extractInformation(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType);

}
