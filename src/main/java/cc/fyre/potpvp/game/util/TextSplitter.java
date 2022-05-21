/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.game.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J.\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005H\u0007J4\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005H\u0007\u00a8\u0006\f"}, d2={"Lcc/fyre/potpvp/game/util/TextSplitter;", "", "()V", "split", "", "", "length", "", "text", "linePrefix", "wordSuffix", "lines", "potpvp-si"})
public final class TextSplitter {
    @NotNull
    public static final TextSplitter INSTANCE = new TextSplitter();

    private TextSplitter() {
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @NotNull
    public static final List<String> split(int length, @NotNull List<String> lines, @NotNull String linePrefix, @NotNull String wordSuffix) {
        Intrinsics.checkNotNullParameter(lines, "lines");
        Intrinsics.checkNotNullParameter(linePrefix, "linePrefix");
        Intrinsics.checkNotNullParameter(wordSuffix, "wordSuffix");
        StringBuilder builder = new StringBuilder();
        Object object = lines.iterator();
        while (object.hasNext()) {
            void $this$trim$iv;
            String line;
            String string = line = object.next();
            StringBuilder stringBuilder = builder;
            boolean $i$f$trim = false;
            CharSequence $this$trim$iv$iv = (CharSequence)$this$trim$iv;
            boolean $i$f$trim2 = false;
            int startIndex$iv$iv = 0;
            int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
            boolean startFound$iv$iv = false;
            while (startIndex$iv$iv <= endIndex$iv$iv) {
                boolean match$iv$iv;
                int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
                char it = $this$trim$iv$iv.charAt(index$iv$iv);
                boolean bl = false;
                boolean bl2 = match$iv$iv = Intrinsics.compare(it, 32) <= 0;
                if (!startFound$iv$iv) {
                    if (!match$iv$iv) {
                        startFound$iv$iv = true;
                        continue;
                    }
                    ++startIndex$iv$iv;
                    continue;
                }
                if (!match$iv$iv) break;
                --endIndex$iv$iv;
            }
            String string2 = ((Object)$this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1)).toString();
            stringBuilder.append(string2);
            builder.append(" ");
        }
        object = builder.substring(0, builder.length() - 1);
        Intrinsics.checkNotNullExpressionValue(object, "builder.substring(0, builder.length - 1)");
        return TextSplitter.split(length, (String)object, linePrefix, wordSuffix);
    }

    /*
     * WARNING - void declaration
     */
    @JvmStatic
    @NotNull
    public static final List<String> split(int length, @NotNull String text, @NotNull String linePrefix, @NotNull String wordSuffix) {
        void $this$toTypedArray$iv;
        List list;
        Collection $this$dropLastWhile$iv;
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(linePrefix, "linePrefix");
        Intrinsics.checkNotNullParameter(wordSuffix, "wordSuffix");
        if (text.length() <= length) {
            String[] stringArray = new String[]{Intrinsics.stringPlus(linePrefix, text)};
            return CollectionsKt.arrayListOf(stringArray);
        }
        ArrayList<String> lines = new ArrayList<String>();
        Object object = text;
        Object object2 = " ";
        int n = 0;
        object2 = new Regex((String)object2);
        n = 0;
        boolean bl = false;
        object = ((Regex)object2).split((CharSequence)object, n);
        boolean $i$f$dropLastWhile = false;
        if (!$this$dropLastWhile$iv.isEmpty()) {
            ListIterator iterator$iv = $this$dropLastWhile$iv.listIterator($this$dropLastWhile$iv.size());
            while (iterator$iv.hasPrevious()) {
                String it = (String)iterator$iv.previous();
                boolean bl2 = false;
                CharSequence charSequence = it;
                boolean bl3 = false;
                if (charSequence.length() == 0) continue;
                list = CollectionsKt.take($this$dropLastWhile$iv, iterator$iv.nextIndex() + 1);
                break;
            }
        } else {
            list = CollectionsKt.emptyList();
        }
        $this$dropLastWhile$iv = list;
        boolean $i$f$toTypedArray = false;
        void thisCollection$iv = $this$toTypedArray$iv;
        String[] stringArray = thisCollection$iv.toArray(new String[0]);
        if (stringArray == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        String[] split = stringArray;
        StringBuilder builder = new StringBuilder(linePrefix);
        int n2 = 0;
        int n3 = split.length + -1;
        if (n2 <= n3) {
            do {
                int i = n2++;
                if (builder.length() + split[i].length() >= length) {
                    lines.add(builder.toString());
                    builder = new StringBuilder(linePrefix);
                }
                builder.append(split[i]);
                builder.append(wordSuffix);
                if (i != split.length - 1) continue;
                builder.replace(builder.length() - wordSuffix.length(), builder.length(), "");
            } while (n2 <= n3);
        }
        CharSequence charSequence = builder;
        n3 = 0;
        if (charSequence.length() > 0) {
            lines.add(builder.toString());
        }
        return lines;
    }
}

