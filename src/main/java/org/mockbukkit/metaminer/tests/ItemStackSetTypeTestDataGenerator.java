package org.mockbukkit.metaminer.tests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mockbukkit.metaminer.DataGenerator;
import org.mockbukkit.metaminer.util.JsonUtil;

import java.io.File;
import java.io.IOException;

public class ItemStackSetTypeTestDataGenerator implements DataGenerator
{

	private final File folder;

	public ItemStackSetTypeTestDataGenerator(File folder){
		this.folder = folder;
	}

	@Override
	public void generateData() throws IOException
	{
		JsonArray jsonArray = new JsonArray();
		for (Material material : Registry.MATERIAL)
		{
			JsonObject elementData = new JsonObject();
			elementData.add("key", new JsonPrimitive(material.key().asString()));
			JsonObject outputData = new JsonObject();
			try
			{
				ItemStack itemStack = new ItemStack(Material.DIAMOND_CHESTPLATE);
				itemStack.setType(material);
				outputData.add("material", new JsonPrimitive(itemStack.getType().key().asString()));
				if (itemStack.getItemMeta() != null)
				{
					outputData.add("meta", new JsonPrimitive(getMetaInterface(itemStack.getItemMeta().getClass()).getName()));
				}
			}
			catch (Exception e)
			{
				outputData.add("throws", new JsonPrimitive(e.getClass().getName()));
				outputData.add("throwsMsg", new JsonPrimitive(e.getMessage()));
			}
			elementData.add("result", outputData);
			jsonArray.add(elementData);
		}
		File file = new File(folder, "setType.json");
		JsonUtil.dump(jsonArray, file);
	}

	private Class<? extends ItemMeta> getMetaInterface(Class<?> aClass)
	{
		Class<?>[] interfaces = aClass.getInterfaces();
		for (Class<?> anInterface : interfaces)
		{
			if (ItemMeta.class.isAssignableFrom(anInterface))
			{
				return (Class<? extends ItemMeta>) anInterface;
			}
		}
		Class<?> superClass = aClass.getSuperclass();
		if (superClass != null)
		{
			return getMetaInterface(superClass);
		}
		throw new IllegalArgumentException("Expected a class extending the item meta interface, got: " + aClass.getName());
	}

}
