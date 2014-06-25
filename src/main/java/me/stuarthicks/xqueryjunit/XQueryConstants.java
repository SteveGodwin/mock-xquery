package me.stuarthicks.xqueryjunit;

import net.sf.saxon.value.SequenceType;

public final class XQueryConstants {

    public static final SequenceType[] ARGUMENTS_NONE = new SequenceType[]{};
    public static final SequenceType[] ARGUMENTS_SINGLE_STRING = new SequenceType[]{SequenceType.SINGLE_STRING};

    public static final SequenceType RETURNS_SINGLE_STRING = SequenceType.SINGLE_STRING;
}
