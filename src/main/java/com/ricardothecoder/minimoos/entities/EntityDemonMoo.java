package com.ricardothecoder.minimoos.entities;

import com.iamshift.interfaces.IMobChanger;
import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.loot.LootManager;
import com.ricardothecoder.yac.util.GameruleManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "com.iamshift.interfaces.IMobChanger", modid = "mineaddons")
public class EntityDemonMoo extends EntityMiniMoo implements IMobChanger
{
	public EntityDemonMoo(World worldIn)
	{
		super(worldIn);
		isImmuneToFire = true;
	}

	// ENTITY CONTROL
	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * 1.5D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
	{
		if (isFool() && damageSrc.getEntity() instanceof EntityPlayer && !(damageSrc.getEntity() instanceof FakePlayer))
		{
			foolPlayer((EntityPlayer)damageSrc.getEntity());
			return;
		}
		
		super.damageEntity(damageSrc, damageAmount);
		if (damageSrc.getEntity() instanceof EntityPlayer && !(damageSrc.getEntity() instanceof FakePlayer))
			damageSrc.getEntity().setFire(7);
	}
	
	// BREEDING
	@Override
	public boolean canMateWith(EntityAnimal otherAnimal)
	{
		return false;
	}

	@Override
	public EntityCow createChild(EntityAgeable ageable)
	{
		return null;
	}

	// ALTERS FIRE
	@Override
	protected void dealFireDamage(int amount)
	{
	}

	@Override
	public void setFire(int seconds)
	{
	}

	@Override
	protected void setOnFireFromLava()
	{
	}

	// MISC STUFF
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootManager.DEMON_COW;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return new ResourceLocation(References.MODID, "textures/entity/demoncow.png");
	}
	
	@Override
	public boolean getCanSpawnHere()
	{  
        return this.worldObj.provider instanceof WorldProviderHell && this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox()) && this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.containsAnyLiquid(this.getEntityBoundingBox());
	}

	// MINE ADDONS
	@Override
	@Optional.Method(modid = "mineaddons")
	public void cursedWaterEffect()
	{
	}

	@Override
	@Optional.Method(modid = "mineaddons")
	public void sacredWaterEffect()
	{
		if (!Config.allowEntityConversion)
			return;
		
		EntitySacredMoo moo = new EntitySacredMoo(worldObj);
		moo.setPositionAndUpdate(posX, posY, posZ);
		worldObj.spawnEntityInWorld(moo);
		
		setDead();
	}
}