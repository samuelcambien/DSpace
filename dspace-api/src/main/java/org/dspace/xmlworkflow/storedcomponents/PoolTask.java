/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.xmlworkflow.storedcomponents;

import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;

import javax.persistence.*;
import java.sql.SQLException;

/**
 * Pool task representing the database representation of a pool task for a step and an eperson
 *
 * @author Bram De Schouwer (bram.deschouwer at dot com)
 * @author Kevin Van de Velde (kevin at atmire dot com)
 * @author Ben Bosman (ben at atmire dot com)
 * @author Mark Diggory (markd at atmire dot com)
 */
@Entity
@Table(name="cwf_pooltask", schema = "public")
public class PoolTask {

    @Id
    @Column(name="pooltask_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="cwf_pooltask_seq")
    @SequenceGenerator(name="cwf_pooltask_seq", sequenceName="cwf_pooltask_seq", allocationSize = 1)
    private int id;

    @OneToOne
    @JoinColumn(name = "workflowitem_id")
    private XmlWorkflowItem workflowItem;

    @Column(name = "workflow_id")
    @Lob
    private String workflowId;

    @Column(name = "step_id")
    @Lob
    private String stepId;

    @Column(name = "action_id")
    @Lob
    private String actionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name="eperson_id")
    private EPerson ePerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "group_id")
    private Group group;



    public void setEperson(EPerson eperson){
        this.ePerson = eperson;
    }

    public EPerson getEperson(){
        return ePerson;
    }

    public void setGroup(Group group){
        this.group = group;
    }

    public Group getGroup(){
        return this.group;
    }

    public void setWorkflowID(String id){
        this.workflowId = id;
    }

    public String getWorkflowID(){
        return workflowId;
    }

    public void setWorkflowItem(XmlWorkflowItem xmlWorkflowItem){
        this.workflowItem = xmlWorkflowItem;
    }

    public XmlWorkflowItem getWorkflowItem(){
        return this.workflowItem;
    }

    public void setStepID(String stepID){
        this.stepId = stepID;
    }

    public String getStepID() throws SQLException {
        return stepId;
    }

    public void setActionID(String actionID){
        this.actionId = actionID;
    }

    public String getActionID(){
        return this.actionId;
    }

}
