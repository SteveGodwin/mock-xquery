package me.stuarthicks.xqueryjunit;

import net.sf.saxon.s9api.*;

import static net.sf.saxon.s9api.ItemType.STRING;
import static net.sf.saxon.s9api.OccurrenceIndicator.ONE;

public final class XQueryConstants {

    public static final SequenceType[] SIGNATURE_ARGUMENTS_NONE = new SequenceType[]{};
    public static final SequenceType[] SIGNATURE_ARGUMENTS_SINGLE_STRING = new SequenceType[]{SequenceType.makeSequenceType(STRING, ONE)};

    public static final SequenceType RETURN_TYPE_SINGLE_STRING = SequenceType.makeSequenceType(STRING, ONE);
}
