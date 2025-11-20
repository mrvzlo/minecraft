package com.ave.smartminer.blockentity;

import com.ave.smartminer.sound.ModSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;

public class SmartMinerBlockEntity extends SmartMinerContainer {
    public static final int ENERGY_PER_TICK = 100;
    public static final int MAX_PROGRESS = 400;
    public static final int MAX_COOLANT = 20;
    public static final int MAX_REDSTONE = 20;
    public static final int ENERGY_PER_PROGRESS = MAX_PROGRESS * ENERGY_PER_TICK;

    private static final int FUEL_PER_COAL = ENERGY_PER_PROGRESS * 3;
    public static final int FUEL_CAPACITY = FUEL_PER_COAL * 10;
    private static final int INCREMENT = 1;
    public EnergyStorage fuel = new EnergyStorage(FUEL_CAPACITY);
    public Item type = null;
    public float progress = 0;
    public int coolant = 0;
    public int redstone = 0;
    public boolean working = false;
    public boolean invalidDepth = false;

    private float speed = 1;
    private int outputSize = 1;

    public SmartMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 5);
        if (pos != null)
            invalidDepth = pos.getY() > 20;
    }

    public void tick() {
        if (level.isClientSide || invalidDepth)
            return;

        if (progress >= MAX_PROGRESS)
            progress -= MAX_PROGRESS;

        checkNewType();
        checkResource(FUEL_SLOT, Items.COAL_BLOCK, FUEL_PER_COAL, FUEL_CAPACITY, ResourceType.FUEL);
        checkResource(REDSTONE_SLOT, Items.REDSTONE_BLOCK, 1, MAX_REDSTONE, ResourceType.REDSTONE);
        checkResource(COOLANT_SLOT, Items.LAPIS_BLOCK, 1, MAX_COOLANT, ResourceType.COOLANT);
        ItemStack slot = inventory.getStackInSlot(OUTPUT_SLOT);
        working = getWorking(slot);

        if (type == null || !working)
            return;

        progress += speed;
        fuel.extractEnergy(ENERGY_PER_TICK, false);
        playSound();

        if (progress < MAX_PROGRESS)
            return;

        coolant--;
        redstone--;
        ItemStack toAdd = new ItemStack(type);
        toAdd.setCount(slot.getCount() + outputSize);
        inventory.setStackInSlot(OUTPUT_SLOT, toAdd);
        setChanged();
    }

    private boolean getWorking(ItemStack slot) {
        if (type == null)
            return false;
        if (coolant < 1 || redstone < 1)
            return false;
        if (fuel.getEnergyStored() < ENERGY_PER_TICK)
            return false;
        if (slot.getCount() + outputSize > slot.getMaxStackSize())
            return false;
        if (slot.getCount() == 0)
            return true;
        return slot.getItem().equals(type);
    }

    private boolean checkResource(int slot, Item blockItem, int singleValue, int maxCapacity, ResourceType type) {
        ItemStack stack = inventory.getStackInSlot(slot);
        int increment = stack.getItem().equals(blockItem) ? singleValue * 9 : singleValue;

        if (stack.isEmpty() || getResourceValue(type) + increment > maxCapacity)
            return false;

        stack.shrink(1);
        inventory.setStackInSlot(slot, stack);
        addResource(type, increment);
        return true;
    }

    private void addResource(ResourceType type, int amount) {
        switch (type) {
            case FUEL -> fuel.receiveEnergy(amount, false);
            case COOLANT -> coolant += amount;
            case REDSTONE -> redstone += amount;
        }
    }

    public int soundCooldown = 0;

    private void playSound() {
        if (soundCooldown > 0) {
            soundCooldown--;
            return;
        }
        soundCooldown += 25;
        level.playSound(null, getBlockPos(), ModSounds.WORK_SOUND.get(), SoundSource.BLOCKS);
    }

    private int getResourceValue(ResourceType type) {
        return switch (type) {
            case FUEL -> fuel.getEnergyStored();
            case COOLANT -> coolant;
            case REDSTONE -> redstone;
        };
    }

    private int getOutputSize() {
        return getOutputSize(type);
    }

    public static int getOutputSize(Item item) {
        boolean isCheap = item.equals(Items.SAND) || item.equals(Items.STONE) || item.equals(Items.GRAVEL)
                || item.equals(Items.COAL_ORE) || item.equals(Items.DEEPSLATE_COAL_ORE)
                || item.equals(Items.COPPER_ORE) || item.equals(Items.DEEPSLATE_COPPER_ORE)
                || item.equals(Items.NETHER_QUARTZ_ORE);

        return isCheap ? INCREMENT + 1 : INCREMENT;
    }

    private int getSpeedMod() {
        return getSpeedMod(type);
    }

    public static int getSpeedMod(Item item) {
        ItemStack stack = new ItemStack(item);
        if (stack.is(ItemTags.DIAMOND_ORES))
            return 5;
        if (stack.is(ItemTags.EMERALD_ORES))
            return 6;
        if (item.equals(Items.ANCIENT_DEBRIS))
            return 20;

        return 1;
    }

    private void checkNewType() {
        Item newType = getCurrentFilter();
        if (type == null && newType == null || type != null && type.equals(newType))
            return;

        type = newType;
        progress = 0;
        speed = 1f / getSpeedMod();
        outputSize = getOutputSize();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        saveAll(tag);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        saveAll(tag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        type = getCurrentFilter();
        fuel = new EnergyStorage(FUEL_CAPACITY, FUEL_CAPACITY, FUEL_CAPACITY, tag.getInt("fuel"));
        progress = tag.getFloat("progress");
        coolant = tag.getInt("coolant");
        redstone = tag.getInt("redstone");
        speed = 1f / getSpeedMod();
        outputSize = getOutputSize();
    }

    private void saveAll(CompoundTag tag) {
        tag.putInt("fuel", fuel.getEnergyStored());
        tag.putFloat("progress", progress);
        tag.putInt("coolant", coolant);
        tag.putInt("redstone", redstone);
    }

    private Item getCurrentFilter() {
        ItemStack stack = inventory.getStackInSlot(TYPE_SLOT);
        return stack.isEmpty() ? null : stack.getItem();
    }

    private enum ResourceType {
        FUEL, COOLANT, REDSTONE
    }
}
