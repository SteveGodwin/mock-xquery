package me.stuarthicks.xqueryjunit;

import net.sf.saxon.value.SequenceType;

public final class XQueryConstants {

    public static final SequenceType[] ARGUMENTS_NONE = new SequenceType[]{};
    public static final SequenceType[] ARGUMENTS_SINGLE_STRING = new SequenceType[]{SequenceType.SINGLE_STRING};
    public static final SequenceType[] ARGUMENTS_SINGLE_INT = new SequenceType[]{SequenceType.SINGLE_INTEGER};
    public static final SequenceType[] ARGUMENTS_SINGLE_FLOAT = new SequenceType[]{SequenceType.SINGLE_FLOAT};

    public static final SequenceType RETURNS_SINGLE_STRING = SequenceType.SINGLE_STRING;
    public static final SequenceType RETURNS_SINGLE_INT = SequenceType.SINGLE_INTEGER;
    public static final SequenceType RETURNS_SINGLE_FLOAT = SequenceType.SINGLE_FLOAT;
}
