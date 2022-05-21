/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.setting.repository;

import cc.fyre.potpvp.setting.Setting;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface SettingRepository {
    public Map<Setting, Boolean> loadSettings(UUID var1) throws IOException;

    public void saveSettings(UUID var1, Map<Setting, Boolean> var2) throws IOException;
}

