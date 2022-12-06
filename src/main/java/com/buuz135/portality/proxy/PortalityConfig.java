/**
 * MIT License
 *
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buuz135.portality.proxy;

import com.buuz135.portality.Portality;
import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = Portality.MOD_ID)
public class PortalityConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    public static PortalityConfig INSTANCE;


    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(max = 10000000, min = 1)
    @ConfigVal(comment = "The amount of energy it will be consumed to teleport an entity")
    public int TELEPORT_ENERGY_AMOUNT = 500;

    @ConfigEntry.Gui.PrefixText
    @ConfigVal(comment = "If true players will get the wither effect if there isn't enough power to teleport")
    public boolean HURT_PLAYERS = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigVal(comment = "If true players will be launched out of the portal instead of standing still in front of it")
    public boolean LAUNCH_PLAYERS = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigVal(comment = "How long the portal structure it can be")
    public int MAX_PORTAL_LENGTH = 16;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(max = 100, min = 1)
    @ConfigVal(comment = "How wide a portal can be without counting the controller(radius)")
    public int MAX_PORTAL_WIDTH = 7;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(max = 200, min = 3)
    @ConfigVal(comment = "How tall a portal can be (diameter)")
    public int MAX_PORTAL_HEIGHT = 15;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(max = Long.MAX_VALUE, min = 1)
    @ConfigVal(comment = "Portal energy buffer")
    public int MAX_PORTAL_POWER = 100000;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(max = 100000, min = 1)
    @ConfigVal(comment = "Portal energy buffer insertion rate")
    public int MAX_PORTAL_POWER_IN = 2000;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(max = 100000000, min = 1)
    @ConfigVal(comment = "How much power it will be consumed to open the portal interdimensionally")
    public int PORTAL_POWER_OPEN_INTERDIMENSIONAL = 10000;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(max = 100, min = 1)
    @ConfigVal(comment = "How much power it will be consumed/tick based on the portal length and if it is the caller. (portalLength*ThisValue). If it is the portal the created the link the power will be double")
    public int POWER_PORTAL_TICK = 1;
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.BoundedDiscrete(max = 1000, min = 1)
    @ConfigVal(comment = "Max distance multiplier that a portal can be linked, based on length. PortalLength*ThisValue")
    public int DISTANCE_MULTIPLIER = 200;


    public static void init(){
        AutoConfig.register(PortalityConfig.class, Toml4jConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(PortalityConfig.class).getConfig();
    }
}
