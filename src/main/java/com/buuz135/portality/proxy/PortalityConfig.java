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
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Portality.MOD_ID)
public class PortalityConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    public static PortalityConfig INSTANCE;

    @Comment(value = "The amount of energy it will consume to teleport an entity")
    public int teleportEnergyAmount = 500;
    @Comment(value = "If true players will get the wither effect if there isn't enough power to teleport")
    public boolean hurtPlayers = true;
    @Comment(value = "If true players will be launched out of the portal instead of standing still in front of it")
    public boolean launchPlayers = true;
    @Comment(value = "How long the portal structure can be")
    public int maxPortalLength = 16;
    @Comment(value = "How wide a portal can be without counting the controller(radius)")
    @ConfigEntry.BoundedDiscrete(max = 100, min = 1)
    public int maxPortalWidth = 7;
    @Comment(value = "How tall a portal can be (diameter)")
    @ConfigEntry.BoundedDiscrete(max = 200, min = 3)
    public int maxPortalHeight = 15;
    @Comment(value = "Portal energy buffer")
    public int maxPortalPower = 100000;
    @Comment(value = "Portal energy buffer insertion rate")
    public int maxPortalPowerIn = 2000;
    @Comment(value = "How much power it will consume to open the portal interdimensionally")
    public int portalPowerOpenInterdimensional = 10000;
    @Comment(value = """
            
            How much power it will consume/tick based on the portal length and if it is the caller. (portalLength*ThisValue).\r
            If it is the portal that created the link the power will be double\
            """)
    @ConfigEntry.BoundedDiscrete(max = 100, min = 1)
    public int powerPortalTick = 1;
    @Comment(value = "Max distance multiplier that a portal can be linked, based on length. PortalLength*ThisValue")
    public int distanceMultiplier = 200;


    public static void init(){
        AutoConfig.register(PortalityConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(PortalityConfig.class).getConfig();
    }
}
