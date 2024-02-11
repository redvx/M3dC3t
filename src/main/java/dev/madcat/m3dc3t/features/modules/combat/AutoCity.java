package dev.madcat.m3dc3t.features.modules.combat;

import dev.madcat.m3dc3t.M3dC3t;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.BlockUtil;
import dev.madcat.m3dc3t.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class AutoCity extends Module {
    public static EntityPlayer target;
    private final Setting<Float> range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));

    public AutoCity() {
        super("AutoCity", "Automatically dig the enemy's hole.", Category.COMBAT, true, false, false);
    }


    public void onTick() {
        if (fullNullCheck())
            return;
        target = getTarget(this.range.getValue());
        if (target == null)
            return;
        BlockPos feet = new BlockPos(target.posX, target.posY, target.posZ);
        if (!detection(target)) {
            if (InstantMine.getInstance().db.getValue()){
                if (getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                    surroundMine(feet.add(0, 0, 1));
                } else {
                    if (getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                        surroundMine(feet.add(0, 0, -1));
                    } else {
                        if (getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(2, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                            surroundMine(feet.add(1, 0, 0));
                        } else {
                            if (getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-2, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                surroundMine(feet.add(-1, 0, 0));
                            } else {
                                if (getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                    surroundMine(feet.add(2, 0, 0));
                                } else {
                                    if (getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                        surroundMine(feet.add(-2, 0, 0));
                                    } else {
                                        if (getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                                            surroundMine(feet.add(0, 0, -2));
                                        } else {
                                            if (getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                                                surroundMine(feet.add(0, 0, 2));
                                            } else {
                                                if (getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                                    surroundMine(feet.add(2, 0, 0));
                                                    if (InstantMine.breakPos2 == null) {
                                                        surroundMine(feet.add(1, 0, 0));
                                                    }
                                                } else {
                                                    if (getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                                        surroundMine(feet.add(-2, 0, 0));
                                                        if (InstantMine.breakPos2 == null) {
                                                            surroundMine(feet.add(-1, 0, 0));
                                                        }
                                                    } else {
                                                        if (getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                                                            surroundMine(feet.add(0, 0, -2));
                                                            if (InstantMine.breakPos2 == null) {
                                                                surroundMine(feet.add(0, 0, -1));
                                                            }
                                                        } else {
                                                            if (getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                                                                surroundMine(feet.add(0, 0, 2));
                                                                if (InstantMine.breakPos2 == null) {
                                                                    surroundMine(feet.add(0, 0, 1));
                                                                }
                                                            } else {
                                                                if (getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                                                                    surroundMine(feet.add(0, 1, 1));
                                                                } else {
                                                                    if (getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                                                                        surroundMine(feet.add(0, 0, 1));
                                                                    } else {
                                                                        if (getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                                                                            surroundMine(feet.add(0, 0, -1));
                                                                        } else {
                                                                            if (getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                surroundMine(feet.add(1, 0, 0));
                                                                            } else {
                                                                                if (getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                    surroundMine(feet.add(-1, 0, 0));
                                                                                } else {
                                                                                    if (getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                        surroundMine(feet.add(1, 1, 0));
                                                                                    } else {
                                                                                        if (getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                            surroundMine(feet.add(-1, 1, 0));
                                                                                        } else {
                                                                                            if (getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                                                                                                surroundMine(feet.add(0, 1, -1));
                                                                                            } else {
                                                                                                if (getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                    surroundMine(feet.add(1, 1, 0));
                                                                                                    if (InstantMine.breakPos2 == null) {
                                                                                                        surroundMine(feet.add(1, 0, 0));
                                                                                                    }
                                                                                                } else {
                                                                                                    if (getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                        surroundMine(feet.add(-1, 1, 0));
                                                                                                        if (InstantMine.breakPos2 == null) {
                                                                                                            surroundMine(feet.add(-1, 0, 0));
                                                                                                        }
                                                                                                    } else {
                                                                                                        if (getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                                                                                                            surroundMine(feet.add(0, 1, -1));
                                                                                                            if (InstantMine.breakPos2 == null) {
                                                                                                                surroundMine(feet.add(0, 0, -1));
                                                                                                            }
                                                                                                        } else {
                                                                                                            if (getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                                                                                                                surroundMine(feet.add(0, 1, 1));
                                                                                                                if (InstantMine.breakPos2 == null) {
                                                                                                                    surroundMine(feet.add(0, 0, 1));
                                                                                                                }
                                                                                                            } else {
                                                                                                                if (getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-2, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                                    surroundMine(feet.add(-2, 1, 0));
                                                                                                                } else {
                                                                                                                    if (getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(2, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                                        surroundMine(feet.add(2, 1, 0));
                                                                                                                    } else {
                                                                                                                        if (getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, 2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                                                                                                                            surroundMine(feet.add(0, 1, 2));
                                                                                                                        } else {
                                                                                                                            if (getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, -2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                surroundMine(feet.add(0, 1, -2));
                                                                                                                            } else {
                                                                                                                                if (getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                    surroundMine(feet.add(-1, 2, 0));
                                                                                                                                } else {
                                                                                                                                    if (getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                        surroundMine(feet.add(1, 2, 0));
                                                                                                                                    } else {
                                                                                                                                        if (getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                            surroundMine(feet.add(0, 2, 1));
                                                                                                                                        } else {
                                                                                                                                            if (getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                                surroundMine(feet.add(0, 2, -1));
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                    surroundMine(feet.add(0, 0, 1));
                } else {
                    if (getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                        surroundMine(feet.add(0, 0, -1));
                    } else {
                        if (getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(2, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                            surroundMine(feet.add(1, 0, 0));
                        } else {
                            if (getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-2, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                surroundMine(feet.add(-1, 0, 0));
                            } else {
                                if (getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                    surroundMine(feet.add(2, 0, 0));
                                } else {
                                    if (getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                        surroundMine(feet.add(-2, 0, 0));
                                    } else {
                                        if (getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                                            surroundMine(feet.add(0, 0, -2));
                                        } else {
                                            if (getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                                                surroundMine(feet.add(0, 0, 2));
                                            } else {
                                                if (getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                                    surroundMine(feet.add(2, 0, 0));
                                                } else {
                                                    if (getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                                        surroundMine(feet.add(-2, 0, 0));
                                                    } else {
                                                        if (getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                                                            surroundMine(feet.add(0, 0, -2));
                                                        } else {
                                                            if (getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                                                                surroundMine(feet.add(0, 0, 2));
                                                            } else {
                                                                if (getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                                                                    surroundMine(feet.add(0, 1, 1));
                                                                } else {
                                                                    if (getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                                                                        surroundMine(feet.add(0, 0, 1));
                                                                    } else {
                                                                        if (getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                                                                            surroundMine(feet.add(0, 0, -1));
                                                                        } else {
                                                                            if (getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                surroundMine(feet.add(1, 0, 0));
                                                                            } else {
                                                                                if (getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                    surroundMine(feet.add(-1, 0, 0));
                                                                                } else {
                                                                                    if (getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                        surroundMine(feet.add(1, 1, 0));
                                                                                    } else {
                                                                                        if (getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                            surroundMine(feet.add(-1, 1, 0));
                                                                                        } else {
                                                                                            if (getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                                                                                                surroundMine(feet.add(0, 1, -1));
                                                                                            } else {
                                                                                                if (getBlock(feet.add(1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                    surroundMine(feet.add(1, 1, 0));
                                                                                                } else {
                                                                                                    if (getBlock(feet.add(-1, 2, 0)).getBlock() == Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                        surroundMine(feet.add(-1, 1, 0));
                                                                                                    } else {
                                                                                                        if (getBlock(feet.add(0, 2, -1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                                                                                                            surroundMine(feet.add(0, 1, -1));
                                                                                                        } else {
                                                                                                            if (getBlock(feet.add(0, 2, 1)).getBlock() == Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                                                                                                                surroundMine(feet.add(0, 1, 1));
                                                                                                            } else {
                                                                                                                if (getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-2, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                                    surroundMine(feet.add(-2, 1, 0));
                                                                                                                } else {
                                                                                                                    if (getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(2, 1, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                                        surroundMine(feet.add(2, 1, 0));
                                                                                                                    } else {
                                                                                                                        if (getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, 2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                                                                                                                            surroundMine(feet.add(0, 1, 2));
                                                                                                                        } else {
                                                                                                                            if (getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, -2)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                surroundMine(feet.add(0, 1, -2));
                                                                                                                            } else {
                                                                                                                                if (getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                    surroundMine(feet.add(-1, 2, 0));
                                                                                                                                } else {
                                                                                                                                    if (getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.AIR && getBlock(feet.add(1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                        surroundMine(feet.add(1, 2, 0));
                                                                                                                                    } else {
                                                                                                                                        if (getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 2, 1)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                            surroundMine(feet.add(0, 2, 1));
                                                                                                                                        } else {
                                                                                                                                            if (getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.AIR && getBlock(feet.add(0, 2, -1)).getBlock() != Blocks.BEDROCK) {
                                                                                                                                                surroundMine(feet.add(0, 2, -1));
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        if (target != null) {
            return target.getName();
        }
        return null;
    }



    private void surroundMine(BlockPos position) {
        if (InstantMine.breakPos2 != null) {
            if (InstantMine.breakPos2.equals(position))
                return;
        }
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(position))
                return;
            if (InstantMine.breakPos.equals(new BlockPos(target.posX, target.posY, target.posZ)) && mc.world.getBlockState(new BlockPos(target.posX, target.posY, target.posZ)).getBlock() != Blocks.AIR)
                return;
            if (InstantMine.breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)))
                return;
            if (InstantMine.breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ)))
                return;
            if (mc.player.rotationPitch <= 90 && mc.player.rotationPitch >= 80)
                return;
            if (mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB)
                return;
        }
        mc.playerController.onPlayerDamageBlock(position, BlockUtil.getRayTraceFacing(position));
    }

    private boolean detection(EntityPlayer player) {
        return mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY + 1, player.posZ)).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY + 1, player.posZ)).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 1.2)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX, player.posY + 1, player.posZ + 1.2)).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 1.2)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX, player.posY + 1, player.posZ - 1.2)).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(player.posX + 2.2, player.posY + 1, player.posZ)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX + 2.2, player.posY, player.posZ)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(player.posX - 2.2, player.posY + 1, player.posZ)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX - 2.2, player.posY, player.posZ)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(player.posX, player.posY + 1, player.posZ + 2.2)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 2.2)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 1.2)).getBlock() == Blocks.AIR || mc.world.getBlockState(new BlockPos(player.posX, player.posY + 1, player.posZ - 2.2)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 2.2)).getBlock() == Blocks.AIR & mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 1.2)).getBlock() == Blocks.AIR;
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, range) || M3dC3t.speedManager.getPlayerSpeed(player) > 10.0) continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq((Entity)player);
                continue;
            }
            if (mc.player.getDistanceSq((Entity)player) >= distance) continue;
            target = player;
            distance = mc.player.getDistanceSq((Entity)player);
        }
        return target;
    }

    private IBlockState getBlock(BlockPos block) {
        return mc.world.getBlockState(block);
    }

}


