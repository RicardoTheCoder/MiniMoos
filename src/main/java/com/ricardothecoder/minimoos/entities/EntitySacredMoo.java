package com.ricardothecoder.minimoos.entities;

import com.iamshift.interfaces.IMobChanger;
import com.ricardothecoder.minimoos.Config;
import com.ricardothecoder.minimoos.References;
import com.ricardothecoder.minimoos.loot.LootManager;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.PotionItems;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "com.iamshift.interfaces.IMobChanger", modid = "mineaddons")
public class EntitySacredMoo extends EntityMiniMoo implements IMobChanger
{
	public EntitySacredMoo(World worldIn)
	{
		super(worldIn);
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
		if (isFool() && damageSrc.getEntity() instanceof EntityPlayer)
		{
			foolPlayer((EntityPlayer)damageSrc.getEntity());
			return;
		}
		
		super.damageEntity(damageSrc, damageAmount);
		if (damageSrc.getEntity() instanceof EntityPlayer)
			((EntityPlayer)damageSrc.getEntity()).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60, 0, false, false));
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

	// ALTERS FALL DAMAGE
	@Override
	public int getMaxFallHeight()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public void fall(float distance, float damageMultiplier)
	{
		super.fall(distance, 0);
	}

	// MISC STUFF
	@Override
	protected ResourceLocation getLootTable()
	{
		return LootManager.SACRED_COW;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return new ResourceLocation(References.MODID, "textures/entity/sacredcow.png");
	}
	
	@Override
	public boolean getCanSpawnHere()
	{  
        return this.worldObj.provider instanceof WorldProviderSurface && this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox()) && this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.containsAnyLiquid(this.getEntityBoundingBox());
	}
	
	// MINE ADDONS
	@Override
	@Optional.Method(modid = "mineaddons")
	public void cursedWaterEffect()
	{
		if (!Config.allowEntityConversion)
			return;
		
		EntityDemonMoo moo = new EntityDemonMoo(worldObj);
		moo.setPositionAndUpdate(posX, posY, posZ);
		worldObj.spawnEntityInWorld(moo);
		
		setDead();
	}

	@Override
	@Optional.Method(modid = "mineaddons")
	public void sacredWaterEffect()
	{
	}
}