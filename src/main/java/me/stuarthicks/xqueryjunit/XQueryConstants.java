package me.stuarthicks.xqueryjunit;

import net.sf.saxon.s9api.SequenceType;
import net.sf.saxon.s9api.XdmValue;

import static net.sf.saxon.s9api.ItemType.STRING;
import static net.sf.saxon.s9api.OccurrenceIndicator.ONE;

public final class XQueryConstants {

    public static final SequenceType[] MOCK_ARGUMENTS_NONE = new SequenceType[]{};
    public static final SequenceType RETURNS_SINGLE_STRING = SequenceType.makeSequenceType(STRING, ONE);
    public static final XdmValue[] WITH_PARAMS_NONE = new XdmValue[]{};

}
