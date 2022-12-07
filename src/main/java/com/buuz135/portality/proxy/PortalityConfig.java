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

@Config(name = Portality.MOD_ID)
public class PortalityConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    public static PortalityConfig INSTANCE;

    public int TELEPORT_ENERGY_AMOUNT = 500;
    public boolean HURT_PLAYERS = true;
    public boolean LAUNCH_PLAYERS = true;
    public int MAX_PORTAL_LENGTH = 16;
    @ConfigEntry.BoundedDiscrete(max = 100, min = 1)
    public int MAX_PORTAL_WIDTH = 7;
    @ConfigEntry.BoundedDiscrete(max = 200, min = 3)
    public int MAX_PORTAL_HEIGHT = 15;
    public int MAX_PORTAL_POWER = 100000;
    public int MAX_PORTAL_POWER_IN = 2000;
    public int PORTAL_POWER_OPEN_INTERDIMENSIONAL = 10000;
    @ConfigEntry.BoundedDiscrete(max = 100, min = 1)
    public int POWER_PORTAL_TICK = 1;
    public int DISTANCE_MULTIPLIER = 200;


    public static void init(){
        AutoConfig.register(PortalityConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(PortalityConfig.class).getConfig();
    }
}
