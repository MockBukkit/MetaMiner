package org.mockbukkit.metaminer.internal.entity;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.kyori.adventure.key.Key;
import net.minecraft.world.entity.Entity;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.mockbukkit.metaminer.DataGenerator;
import org.mockbukkit.metaminer.internal.entity.extractor.BabyEntityInformationExtractor;
import org.mockbukkit.metaminer.internal.entity.extractor.BigEntityInformationExtractor;
import org.mockbukkit.metaminer.internal.entity.extractor.DefaultEntityInformationExtractor;
import org.mockbukkit.metaminer.internal.entity.extractor.MediumEntityInformationExtractor;
import org.mockbukkit.metaminer.internal.entity.extractor.SmallEntityInformationExtractor;
import org.mockbukkit.metaminer.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EntityTypeGenerator implements DataGenerator
{
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityTypeGenerator.class);

	private final File workDirectory;

	public EntityTypeGenerator(File workDirectory)
	{
		Preconditions.checkNotNull(workDirectory, "The work directory can't be null!");
		this.workDirectory = workDirectory;
	}

	@Override
	public void generateData() throws IOException
	{
		File entitiesDirectory = new File(workDirectory, "entities");
		Map<String, net.minecraft.world.entity.EntityType<?>> entities = getEntities();

		int entityCounter = 0;
		int entityTotal = entities.size();

		for (Map.Entry<String, net.minecraft.world.entity.EntityType<? extends Entity>> entry : entities.entrySet()) {

			String name = entry.getKey();
			net.minecraft.world.entity.EntityType<? extends Entity> mojangEntityType = entry.getValue();
			if (mojangEntityType == null)
			{
				throw new IllegalArgumentException(String.format("Entity with name %s was not found in minecraft entity. Possible values are: %s",
						name, entities.keySet()));
			}

			EntityType entityType = EntityType.fromName(name);
			if (entityType == null)
			{
				throw new IllegalArgumentException(String.format("Entity with name %s was not found in bukkit entity. Possible values are: %s",
						name, Set.of(EntityType.values()).stream()
								.map(EntityType::getKey)
								.map(NamespacedKey::getKey)
								.collect(Collectors.toSet())));
			}

			if (LOGGER.isInfoEnabled())
			{
				LOGGER.info("[{}/{}] Working on entity {}...", String.format("%3d", ++entityCounter), entityTotal, name);
			}

			JsonObject root = extractInformation(entityType, mojangEntityType);

			String fileName = String.format("%s.json", name);
			File file = new File(entitiesDirectory, fileName);
			JsonUtil.dump(root, file);
		}
	}

	private JsonObject extractInformation(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		JsonObject root = new JsonObject();

		Set<EntitySubType> possibleStates = getPossibleStatesForEntity(entityType, mojangEntityType);
		for (EntitySubType state : possibleStates) {
			switch (state)
			{
			case DEFAULT -> root.add(EntitySubType.DEFAULT.getName(), DefaultEntityInformationExtractor.process(entityType, mojangEntityType));
			case BABY -> root.add(EntitySubType.BABY.getName(), BabyEntityInformationExtractor.process(entityType, mojangEntityType));
			case SMALL -> root.add(EntitySubType.SMALL.getName(), SmallEntityInformationExtractor.process(entityType, mojangEntityType));
			case MEDIUM -> root.add(EntitySubType.MEDIUM.getName(), MediumEntityInformationExtractor.process(entityType, mojangEntityType));
			case BIG -> root.add(EntitySubType.BIG.getName(), BigEntityInformationExtractor.process(entityType, mojangEntityType));
			default -> throw new UnsupportedOperationException("Unknown entity state: " + state);
			}
		}

		return root;
	}

	private static Set<EntitySubType> getPossibleStatesForEntity(EntityType entityType, net.minecraft.world.entity.EntityType<?> mojangEntityType)
	{
		Class<? extends org.bukkit.entity.Entity> entityClass = entityType.getEntityClass();
		if (entityClass == null)
		{
			throw new IllegalArgumentException(String.format("Entity type %s does not have a entity class", entityType.name()));
		}

		Set<EntitySubType> possibleStates = new HashSet<>();

		// Add the default
		possibleStates.add(EntitySubType.DEFAULT);

		// Add the baby state
		if (Ageable.class.isAssignableFrom(entityClass))
		{
			possibleStates.add(EntitySubType.BABY);
		}

		// Add the armor stand
		if (ArmorStand.class.isAssignableFrom(entityClass))
		{
			possibleStates.add(EntitySubType.SMALL);
		}

		// Add the Slime/Magma cube
		if (Slime.class.isAssignableFrom(entityClass))
		{
			possibleStates.add(EntitySubType.BIG);
			possibleStates.add(EntitySubType.MEDIUM);
			possibleStates.add(EntitySubType.SMALL);

			possibleStates.remove(EntitySubType.DEFAULT);
		}

		return possibleStates;
	}

	public static Map<String, net.minecraft.world.entity.EntityType<? extends Entity>> getEntities() {

		Map<String, net.minecraft.world.entity.EntityType<?>> entityTypes = new TreeMap<>();

		for (EntityType entityType : EntityType.values()) {

			if (EntityType.UNKNOWN.equals(entityType))
			{
				// Unknown entity should be ignored
				continue;
			}

			NamespacedKey key = entityType.getKey();
			if (!Key.MINECRAFT_NAMESPACE.equalsIgnoreCase(key.getNamespace()))
			{
				throw new IllegalArgumentException(String.format("Entity type %s has namespace %s while the expected value would be %s",
						key.getNamespace(), key, Key.MINECRAFT_NAMESPACE));
			}

			String entityName =key.getKey();
			Optional<net.minecraft.world.entity.EntityType<?>> minecraftEntityType = net.minecraft.world.entity.EntityType.byString(entityName);
			if (minecraftEntityType.isEmpty())
			{
				throw new IllegalArgumentException(String.format("Entity %s was not found in minecraft registry.", entityName));
			}
			entityTypes.put(entityName, minecraftEntityType.get());
		}

		return entityTypes;
	}

}
