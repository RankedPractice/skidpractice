/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.configuration.MapOfCodecsProvider;
import org.bson.codecs.configuration.ProvidersCodecRegistry;

public final class CodecRegistries {
    public static CodecRegistry fromCodecs(Codec<?> ... codecs) {
        return CodecRegistries.fromCodecs(Arrays.asList(codecs));
    }

    public static CodecRegistry fromCodecs(List<? extends Codec<?>> codecs) {
        return CodecRegistries.fromProviders(new MapOfCodecsProvider(codecs));
    }

    public static CodecRegistry fromProviders(CodecProvider ... providers) {
        return CodecRegistries.fromProviders(Arrays.asList(providers));
    }

    public static CodecRegistry fromProviders(List<? extends CodecProvider> providers) {
        return new ProvidersCodecRegistry(providers);
    }

    public static CodecRegistry fromRegistries(CodecRegistry ... registries) {
        return CodecRegistries.fromRegistries(Arrays.asList(registries));
    }

    public static CodecRegistry fromRegistries(List<? extends CodecRegistry> registries) {
        ArrayList<CodecProvider> providers = new ArrayList<CodecProvider>();
        for (CodecRegistry codecRegistry : registries) {
            providers.add(CodecRegistries.providerFromRegistry(codecRegistry));
        }
        return new ProvidersCodecRegistry(providers);
    }

    private static CodecProvider providerFromRegistry(final CodecRegistry innerRegistry) {
        if (innerRegistry instanceof CodecProvider) {
            return (CodecProvider)((Object)innerRegistry);
        }
        return new CodecProvider(){

            @Override
            public <T> Codec<T> get(Class<T> clazz, CodecRegistry outerRregistry) {
                try {
                    return innerRegistry.get(clazz);
                }
                catch (CodecConfigurationException e) {
                    return null;
                }
            }
        };
    }

    private CodecRegistries() {
    }
}

