package dev.madcat.m3dc3t.features.modules.render;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.event.events.Render3DEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.modules.combat.AntiCity;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.EntityUtil;
import dev.madcat.m3dc3t.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class CityESP
        extends Module {
    public EntityPlayer target;
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(7.0f), Float.valueOf(1.0f), Float.valueOf(12.0f)));


    public CityESP() {
        super("CityESP", "Show enemy's hole flaws.", Module.Category.RENDER, true, false, false);
    }
    @Override
    public void onRender3D(Render3DEvent event) {
        if (CityESP.fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue().floatValue());
        this.surroundRender1();
    }


    private void surroundRender1() {
        if (this.target == null) return;
        Vec3d a = this.target.getPositionVector();
        BlockPos people = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);


        if (this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, -1, 0, 0, true);
            } else {
                if (this.getBlock(people.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundRender1(a, -1, 0, 0, false);
                }
            }
        }
        if (this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 1, 0, 0, true);
            } else {
                if (this.getBlock(people.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundRender1(a, 1, 0, 0, false);
                }
            }
        }
        if (this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                if (this.getBlock(people.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    if (this.getBlock(people.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                        this.surroundRender1(a, -2, 0, 0, true);
                    } else {
                        this.surroundRender1(a, -2, 0, 0, false);
                    }
                }
            }
        }
        if (this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                if (this.getBlock(people.add(-2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    if (this.getBlock(people.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-2, 0, 0)).getBlock() == Blocks.AIR) {
                        this.surroundRender1(a, -2, 1, 0, true);
                    } else {
                        this.surroundRender1(a, -2, 1, 0, false);
                    }
                }
            }
        }
        if (this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                if (this.getBlock(people.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    if (this.getBlock(people.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                        this.surroundRender1(a, 2, 0, 0, true);
                    } else {
                        this.surroundRender1(a, 2, 0, 0, false);
                    }
                }
            }
        }
        if (this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                if (this.getBlock(people.add(2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    if (this.getBlock(people.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(2, 0, 0)).getBlock() == Blocks.AIR) {
                        this.surroundRender1(a, 2, 1, 0, true);
                    } else {
                        this.surroundRender1(a, 2, 1, 0, false);
                    }
                }
            }
        }
        if (this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 0, 0, 1, true);
            } else {
                if (this.getBlock(people.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundRender1(a, 0, 0, 1, false);
                }
            }
        }
        if (this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(a, 0, 0, -1, true);
            } else {
                if (this.getBlock(people.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(people.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundRender1(a, 0, 0, -1, false);
                }
            }
        }
        if (this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                if (this.getBlock(people.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                    if (this.getBlock(people.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                        this.surroundRender1(a, 0, 0, 2, true);
                    } else {
                        this.surroundRender1(a, 0, 0, 2, false);
                    }
                }
            }
        }
        if (this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                if (this.getBlock(people.add(0, 1, 2)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                    if (this.getBlock(people.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, 2)).getBlock() == Blocks.AIR) {
                        this.surroundRender1(a, 0, 1, 2, true);
                    } else {
                        this.surroundRender1(a, 0, 1, 2, false);
                    }
                }
            }
        }
        if (this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                if (this.getBlock(people.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                    if (this.getBlock(people.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                        this.surroundRender1(a, 0, 0, -2, true);
                    } else {
                        this.surroundRender1(a, 0, 0, -2, false);
                    }
                }
            }
        }
        if (this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(people.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                if (this.getBlock(people.add(0, 1, -2)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                    if (this.getBlock(people.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, -2)).getBlock() == Blocks.AIR) {
                        this.surroundRender1(a, 0, 1, -2, true);
                    } else {
                        this.surroundRender1(a, 0, 1, -2, false);
                    }
                }
            }
        }
            if (this.getBlock(people.add(0, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, 0, 0)).getBlock() != Blocks.BEDROCK) {
                RenderUtil.drawBoxESP(new BlockPos(a), new Color(255, 255, 0), false, new Color(255, 255, 0), 1.0f, false, true, 42, true);
            }
        }

    private void surroundRender1(Vec3d pos, double x, double y, double z, boolean red) {
        BlockPos position = new BlockPos(pos).add(x, y, z);
        if (CityESP.mc.world.getBlockState(position).getBlock() == Blocks.AIR) return;
        if (CityESP.mc.world.getBlockState(position).getBlock() == Blocks.FIRE) {
            return;
        }
        if (red) {
            RenderUtil.drawBoxESP(position, new Color(255, 147, 147), false, new Color(255, 147, 147), 1.0f, false, true, 80, true);
            return;
        }
        RenderUtil.drawBoxESP(position, new Color(118, 118, 255), false, new Color(118, 118, 255), 1.0f, false, true, 40, true);
    }
    private IBlockState getBlock(BlockPos block) {
        return AntiCity.mc.world.getBlockState(block);
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, range) || M3dC3t.friendManager.isFriend(player.getName()) || mc.player.posY - player.posY >= 5.0) continue;
            if (target == null) {
                target = player;
                distance = EntityUtil.mc.player.getDistanceSq((Entity)player);
                continue;
            }
            if (EntityUtil.mc.player.getDistanceSq((Entity)player) >= distance) continue;
            target = player;
            distance = EntityUtil.mc.player.getDistanceSq((Entity)player);
        }
        return target;
    }
}

