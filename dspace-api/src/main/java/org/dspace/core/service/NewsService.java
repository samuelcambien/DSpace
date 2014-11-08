package org.dspace.core.service;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 17/11/14
 * Time: 09:12
 */
public interface NewsService {

    /**
     * Reads news from a text file.
     *
     * @param newsFile
     *        name of the news file to read in, relative to the news file path.
     */
    public String readNewsFile(String newsFile);

    /**
     * Writes news to a text file.
     *
     * @param newsFile
     *        name of the news file to read in, relative to the news file path.
     * @param news
     *            the text to be written to the file.
     */
    public String writeNewsFile(String newsFile, String news);

    /**
     * Get the path for the news files.
     *
     */
    public String getNewsFilePath();
}
