package net.darpserj.simplestmobfarm;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;



public class UniversalMobCatcher extends Item {
    public UniversalMobCatcher(Settings settings) {
        super(settings);
    }

    public boolean hasValidNbt(ItemStack stack){
        return  stack.getNbt() != null
                && stack.getNbt().getString(SimpleMobFarm.MOD_ID + ":entity_type") != null
                && !stack.getNbt().getString(SimpleMobFarm.MOD_ID + ":entity_type").isEmpty();
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(user.isPlayer() && !entity.isPlayer() ){

            ItemStack stackInHand = user.getStackInHand(hand);
            NbtCompound nbt = stackInHand.getOrCreateNbt();
            nbt.putString(SimpleMobFarm.MOD_ID + ":entity_type", entity.getType().getUntranslatedName());
            stackInHand.setNbt(nbt);
            entity.discard();
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return hasValidNbt(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);

        if(this.hasValidNbt(stack)){

            if (world.isClient) {
                return TypedActionResult.pass(user.getStackInHand(hand));
            }

            NbtCompound nbt = stack.getNbt();
            String entityType = nbt.getString(SimpleMobFarm.MOD_ID + ":entity_type");
            BlockPos pos = user.getBlockPos();
            ServerWorld serverWorld = (ServerWorld) world;
            // Get the entity type from the string
            EntityType<?> entity = Registries.ENTITY_TYPE.get(new Identifier(entityType));
            entity.spawn(serverWorld, pos, SpawnReason.EVENT);
            stack.setNbt(new NbtCompound());

            return TypedActionResult.success(user.getStackInHand(hand));

        } else {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

    }
}

