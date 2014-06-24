package me.stuarthicks.xqueryjunit;

import net.sf.saxon.value.SequenceType;

public final class XQueryConstants {

    public static final SequenceType[] SIGNATURE_ARGUMENTS_NONE = new SequenceType[]{};
    public static final SequenceType[] SIGNATURE_ARGUMENTS_SINGLE_STRING = new SequenceType[]{SequenceType.SINGLE_STRING};

    public static final SequenceType RETURN_TYPE_SINGLE_STRING = SequenceType.SINGLE_STRING;
}
