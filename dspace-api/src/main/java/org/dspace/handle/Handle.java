package org.dspace.handle;

import org.dspace.content.DSpaceObject;

import javax.persistence.*;

/**
 * User: kevin (kevin at atmire.com)
 * Date: 24/02/14
 * Time: 13:28
 */
@Entity
@Table(name="handle", schema = "public")
public class Handle {

    @Id
    @Column(name="handle_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="handle_seq")
    @SequenceGenerator(name="handle_seq", sequenceName="handle_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "handle", unique = true)
    private String handle;

    @OneToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "resource_id")
    private DSpaceObject dso;

    @Column(name = "resource_type_id")
    private Integer resourceTypeId;

    public Integer getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setDSpaceObject(DSpaceObject dso) {
        this.dso = dso;
    }

    public DSpaceObject getDSpaceObject() {
        return dso;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public Integer getResourceTypeId() {
        return resourceTypeId;
    }
}
