package me.stuarthicks.xqueryjunit;

import net.sf.saxon.value.SequenceType;

public final class XQueryConstants {

    /**
     * These constants exist for convenience and as reference. They will never be an exhaustive list.
     */

    // Method signatures you might want to stub
    public static final SequenceType[] ARGUMENTS_NONE = new SequenceType[]{};
    public static final SequenceType[] ARGUMENTS_SINGLE_STRING = new SequenceType[]{SequenceType.SINGLE_STRING};
    public static final SequenceType[] ARGUMENTS_SINGLE_INT = new SequenceType[]{SequenceType.SINGLE_INTEGER};
    public static final SequenceType[] ARGUMENTS_SINGLE_FLOAT = new SequenceType[]{SequenceType.SINGLE_FLOAT};

    // Types your stubs might want to return
    public static final SequenceType RETURNS_SINGLE_STRING = SequenceType.SINGLE_STRING;
    public static final SequenceType RETURNS_SINGLE_INT = SequenceType.SINGLE_INTEGER;
    public static final SequenceType RETURNS_SINGLE_FLOAT = SequenceType.SINGLE_FLOAT;
}
