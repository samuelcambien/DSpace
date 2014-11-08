package org.dspace.core.service;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 17/11/14
 * Time: 09:03
 */
public interface LicenseService {

    /**
     * Writes license to a text file.
     *
     * @param licenseFile
     *            name for the file into which license will be written,
     *            relative to the current directory.
     */
    public void writeLicenseFile(String licenseFile,
            String newLicense);

    /**
     * Get the License
     *
     * @param
     *         licenseFile   file name
     *
     *  @return
     *         license text
     *
     */
    public String getLicenseText(String licenseFile);

    /**
     * Get the site-wide default license that submitters need to grant
     *
     * @return the default license
     */
    public String getDefaultSubmissionLicense();
}
