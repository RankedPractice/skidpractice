module kotlin.stdlib.jdk8 {
    /* transitive */ requires kotlin.stdlib;
    requires kotlin.stdlib.jdk7;

    exports kotlin.collections.jdk8;
    exports kotlin.streams.jdk8;
    exports kotlin.text.jdk8;

    opens kotlin.internal.jdk8 to kotlin.stdlib;

}

