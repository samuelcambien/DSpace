package org.dspace.content.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.core.Context;
import org.dspace.service.DSpaceCRUDService;

import java.sql.SQLException;
import java.util.List;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 27/10/14
 * Time: 10:02
 */
public interface BitstreamFormatService extends DSpaceCRUDService<BitstreamFormat> {



    /**
     * Find a bitstream format by its (unique) MIME type.
     * If more than one bitstream format has the same MIME type, the
     * one returned is unpredictable.
     *
     * @param context
     *            DSpace context object
     * @param mimeType
     *            MIME type value
     *
     * @return the corresponding bitstream format, or <code>null</code> if
     *         there's no bitstream format with the given MIMEtype.
     * @throws java.sql.SQLException
     */
    public BitstreamFormat findByMIMEType(Context context, String mimeType) throws SQLException;

    /**
     * Find a bitstream format by its (unique) short description
     *
     * @param context
     *            DSpace context object
     * @param desc
     *            the short description
     *
     * @return the corresponding bitstream format, or <code>null</code> if
     *         there's no bitstream format with the given short description
     * @throws SQLException
     */
    public BitstreamFormat findByShortDescription(Context context, String desc) throws SQLException;

    /**
     * Get the generic "unknown" bitstream format.
     *
     * @param context
     *            DSpace context object
     *
     * @return the "unknown" bitstream format.
     * @throws SQLException
     *
     * @throws IllegalStateException
     *             if the "unknown" bitstream format couldn't be found
     */
    public BitstreamFormat findUnknown(Context context) throws SQLException;

    /**
     * Retrieve all bitstream formats from the registry, ordered by ID
     *
     * @param context
     *            DSpace context object
     *
     * @return the bitstream formats.
     * @throws SQLException
     */
    public List<BitstreamFormat> findAll(Context context) throws SQLException;

    /**
     * Retrieve all non-internal bitstream formats from the registry. The
     * "unknown" format is not included, and the formats are ordered by support
     * level (highest first) first then short description.
     *
     * @param context
     *            DSpace context object
     *
     * @return the bitstream formats.
     * @throws SQLException
     */
    public List<BitstreamFormat> findNonInternal(Context context) throws SQLException;

    /**
     * Set the short description of the bitstream format
     *
     * @param shortDescription
     *            the new short description
     */
    public void setShortDescription(Context context, BitstreamFormat bitstreamFormat, String shortDescription) throws SQLException;

    /**
     * Get the support level text for this bitstream format - one of
     * <code>UNKNOWN</code>,<code>KNOWN</code> or <code>SUPPORTED</code>.
     *
     * @return the support level
     */
    public String getSupportLevelText(BitstreamFormat bitstreamFormat);

    /**
     * Set the support level for this bitstream format - one of
     * <code>UNKNOWN</code>,<code>KNOWN</code> or <code>SUPPORTED</code>.
     *
     * @param supportLevel
     *            the new support level
     */
    public void setSupportLevel(BitstreamFormat bitstreamFormat, int supportLevel);

    /**
     * If you know the support level string, look up the corresponding type ID
     * constant.
     *
     * @param supportLevel
     *            String with the name of the action (must be exact match)
     *
     * @return the corresponding action ID, or <code>-1</code> if the action
     *         string is unknown
     */
    public int getSupportLevelID(String supportLevel);

    /**
     * Attempt to identify the format of a particular bitstream. If the format
     * is unknown, null is returned.
     *
     * @param bitstream
     *            the bitstream to identify the format of
     *
     * @return a format from the bitstream format registry, or null
     */
    public BitstreamFormat guessFormat(Context context, Bitstream bitstream) throws SQLException;
}
