/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.music;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dspace.content.Item;
import org.dspace.core.ReloadableEntity;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class is the DB Entity representation of the Process object to be stored in the Database
 */
@Entity
@Table(name = "album")
public class Album implements ReloadableEntity<UUID> {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false)
    private UUID id;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "release_date")
    @Temporal(TIMESTAMP)
    private Date releaseDate;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Override
    public UUID getID() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Return <code>true</code> if <code>other</code> is the same Process
     * as this object, <code>false</code> otherwise
     *
     * @param other object to compare to
     * @return <code>true</code> if object passed in represents the same
     * collection as this object
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Album &&
                new EqualsBuilder().append(this.getID(), ((Album) other).getID())
                        .append(this.getArtist(), ((Album) other).getArtist())
                        .append(this.getTitle(), ((Album) other).getTitle())
                        .append(this.getReleaseDate(), ((Album) other).getReleaseDate())
                        .isEquals());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.getID())
                .append(this.getArtist())
                .append(this.getTitle())
                .append(this.getReleaseDate())
                .toHashCode();
    }
}
