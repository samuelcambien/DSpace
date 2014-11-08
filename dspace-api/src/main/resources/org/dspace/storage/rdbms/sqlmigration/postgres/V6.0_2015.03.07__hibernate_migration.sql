DROP VIEW community2item;

CREATE TABLE dspaceobject
(
    uuid            uuid NOT NULL  PRIMARY KEY
);

CREATE TABLE site
(
    uuid            uuid NOT NULL PRIMARY KEY REFERENCES dspaceobject(uuid)

);

ALTER TABLE eperson ADD COLUMN uuid UUID DEFAULT gen_random_uuid() UNIQUE;
INSERT INTO dspaceobject  (uuid) SELECT uuid FROM eperson;
ALTER TABLE eperson ADD FOREIGN KEY (uuid) REFERENCES dspaceobject;

ALTER TABLE epersongroup ADD COLUMN uuid UUID DEFAULT gen_random_uuid() UNIQUE;
INSERT INTO dspaceobject  (uuid) SELECT uuid FROM epersongroup;
ALTER TABLE epersongroup ADD FOREIGN KEY (uuid) REFERENCES dspaceobject;

ALTER TABLE item ADD COLUMN uuid UUID DEFAULT gen_random_uuid() UNIQUE;
INSERT INTO dspaceobject  (uuid) SELECT uuid FROM item;
ALTER TABLE item ADD FOREIGN KEY (uuid) REFERENCES dspaceobject;

ALTER TABLE community ADD COLUMN uuid UUID DEFAULT gen_random_uuid() UNIQUE;
INSERT INTO dspaceobject  (uuid) SELECT uuid FROM community;
ALTER TABLE community ADD FOREIGN KEY (uuid) REFERENCES dspaceobject;

ALTER TABLE collection ADD COLUMN uuid UUID DEFAULT gen_random_uuid() UNIQUE;
INSERT INTO dspaceobject  (uuid) SELECT uuid FROM collection;
ALTER TABLE collection ADD FOREIGN KEY (uuid) REFERENCES dspaceobject;

ALTER TABLE bundle ADD COLUMN uuid UUID DEFAULT gen_random_uuid() UNIQUE;
INSERT INTO dspaceobject  (uuid) SELECT uuid FROM bundle;
ALTER TABLE bundle ADD FOREIGN KEY (uuid) REFERENCES dspaceobject;

ALTER TABLE bitstream ADD COLUMN uuid UUID DEFAULT gen_random_uuid() UNIQUE;
INSERT INTO dspaceobject  (uuid) SELECT uuid FROM bitstream;
ALTER TABLE bitstream ADD FOREIGN KEY (uuid) REFERENCES dspaceobject;

-- Migrate EPersonGroup2EPerson table
ALTER TABLE EPersonGroup2EPerson RENAME COLUMN eperson_group_id to eperson_group_legacy_id;
ALTER TABLE EPersonGroup2EPerson RENAME COLUMN eperson_id to eperson_legacy_id;
ALTER TABLE EPersonGroup2EPerson ADD COLUMN eperson_group_id UUID REFERENCES EpersonGroup(uuid) NOT NULL;
ALTER TABLE EPersonGroup2EPerson ADD COLUMN eperson_id UUID REFERENCES Eperson(uuid) NOT NULL;
UPDATE EPersonGroup2EPerson SET eperson_group_id = EPersonGroup.uuid FROM EpersonGroup WHERE EPersonGroup2EPerson.eperson_group_legacy_id = EPersonGroup.eperson_group_id;
UPDATE EPersonGroup2EPerson SET eperson_id = eperson.uuid FROM eperson WHERE EPersonGroup2EPerson.eperson_legacy_id = eperson.eperson_id;
ALTER TABLE EPersonGroup2EPerson DROP COLUMN eperson_group_legacy_id;
ALTER TABLE EPersonGroup2EPerson DROP COLUMN eperson_legacy_id;
ALTER TABLE epersongroup2eperson DROP COLUMN id;
ALTER TABLE EPersonGroup2EPerson add primary key (eperson_group_id,eperson_id);

-- Migrate GROUP2GROUP table
ALTER TABLE Group2Group RENAME COLUMN parent_id to parent_legacy_id;
ALTER TABLE Group2Group RENAME COLUMN child_id to child_legacy_id;
ALTER TABLE Group2Group ADD COLUMN parent_id UUID REFERENCES EpersonGroup(uuid) NOT NULL;
ALTER TABLE Group2Group ADD COLUMN child_id UUID REFERENCES EpersonGroup(uuid) NOT NULL;
UPDATE Group2Group SET parent_id = EPersonGroup.uuid FROM EpersonGroup WHERE Group2Group.parent_legacy_id = EPersonGroup.eperson_group_id;
UPDATE Group2Group SET child_id = EpersonGroup.uuid FROM EpersonGroup WHERE Group2Group.child_legacy_id = EpersonGroup.eperson_group_id;
ALTER TABLE Group2Group DROP COLUMN parent_legacy_id;
ALTER TABLE Group2Group DROP COLUMN child_legacy_id;
ALTER TABLE Group2Group DROP COLUMN id;
ALTER TABLE Group2Group add primary key (parent_id,child_id);

-- Migrate collection2item
ALTER TABLE Collection2Item RENAME COLUMN collection_id to collection_legacy_id;
ALTER TABLE Collection2Item RENAME COLUMN item_id to item_legacy_id;
ALTER TABLE Collection2Item ADD COLUMN collection_id UUID REFERENCES Collection(uuid) NOT NULL;
ALTER TABLE Collection2Item ADD COLUMN item_id UUID REFERENCES Item(uuid) NOT NULL;
UPDATE Collection2Item SET collection_id = Collection.uuid FROM Collection WHERE Collection2Item.collection_legacy_id = Collection.collection_id;
UPDATE Collection2Item SET item_id = Item.uuid FROM Item WHERE Collection2Item.item_legacy_id = Item.item_id;
ALTER TABLE Collection2Item DROP COLUMN collection_legacy_id;
ALTER TABLE Collection2Item DROP COLUMN item_legacy_id;
ALTER TABLE Collection2Item DROP COLUMN id;
ALTER TABLE Collection2Item add primary key (collection_id,item_id);

-- Migrate community2collection
ALTER TABLE community2collection RENAME COLUMN collection_id to collection_legacy_id;
ALTER TABLE community2collection RENAME COLUMN community_id to community_legacy_id;
ALTER TABLE community2collection ADD COLUMN collection_id UUID REFERENCES Collection(uuid) NOT NULL;
ALTER TABLE community2collection ADD COLUMN community_id UUID REFERENCES Community(uuid) NOT NULL;
UPDATE community2collection SET collection_id = Collection.uuid FROM Collection WHERE community2collection.collection_legacy_id = Collection.collection_id;
UPDATE community2collection SET community_id = Community.uuid FROM Community WHERE community2collection.community_legacy_id = Community.community_id;
ALTER TABLE community2collection DROP COLUMN collection_legacy_id;
ALTER TABLE community2collection DROP COLUMN community_legacy_id;
ALTER TABLE community2collection DROP COLUMN id;
ALTER TABLE community2collection add primary key (collection_id,community_id);


-- Migrate Group2GroupCache table
ALTER TABLE Group2GroupCache RENAME COLUMN parent_id to parent_legacy_id;
ALTER TABLE Group2GroupCache RENAME COLUMN child_id to child_legacy_id;
ALTER TABLE Group2GroupCache ADD COLUMN parent_id UUID REFERENCES EpersonGroup(uuid) NOT NULL;
ALTER TABLE Group2GroupCache ADD COLUMN child_id UUID REFERENCES EpersonGroup(uuid) NOT NULL;
UPDATE Group2GroupCache SET parent_id = EPersonGroup.uuid FROM EpersonGroup WHERE Group2GroupCache.parent_legacy_id = EPersonGroup.eperson_group_id;
UPDATE Group2GroupCache SET child_id = EpersonGroup.uuid FROM EpersonGroup WHERE Group2GroupCache.child_legacy_id = EpersonGroup.eperson_group_id;
ALTER TABLE Group2GroupCache DROP COLUMN parent_legacy_id;
ALTER TABLE Group2GroupCache DROP COLUMN child_legacy_id;
ALTER TABLE Group2GroupCache DROP COLUMN id;
ALTER TABLE Group2GroupCache add primary key (parent_id,child_id);


-- Migrate item
ALTER TABLE item RENAME COLUMN submitter_id to submitter_id_legacy_id;
ALTER TABLE item ADD COLUMN submitter_id UUID REFERENCES EPerson(uuid);
UPDATE item SET submitter_id = eperson.uuid FROM eperson WHERE item.submitter_id_legacy_id = eperson.eperson_id;
ALTER TABLE item DROP COLUMN submitter_id_legacy_id;

ALTER TABLE item RENAME COLUMN owning_collection to owning_collection_legacy;
ALTER TABLE item ADD COLUMN owning_collection UUID REFERENCES Collection(uuid);
UPDATE item SET owning_collection = Collection.uuid FROM Collection WHERE item.owning_collection_legacy = collection.collection_id;
ALTER TABLE item DROP COLUMN owning_collection_legacy;


-- Migrate community references
ALTER TABLE Community RENAME COLUMN admin to admin_legacy;
ALTER TABLE Community ADD COLUMN admin UUID REFERENCES EPersonGroup(uuid);
UPDATE Community SET admin = EPersonGroup.uuid FROM EPersonGroup WHERE Community.admin_legacy = EPersonGroup.eperson_group_id;
ALTER TABLE Community DROP COLUMN admin_legacy;

--Migrate Collection references
ALTER TABLE Collection RENAME COLUMN workflow_step_1 to workflow_step_1_legacy;
ALTER TABLE Collection RENAME COLUMN workflow_step_2 to workflow_step_2_legacy;
ALTER TABLE Collection RENAME COLUMN workflow_step_3 to workflow_step_3_legacy;
ALTER TABLE Collection RENAME COLUMN submitter to submitter_legacy;
ALTER TABLE Collection RENAME COLUMN admin to admin_legacy;
ALTER TABLE Collection ADD COLUMN workflow_step_1 UUID REFERENCES EPersonGroup(uuid);
ALTER TABLE Collection ADD COLUMN workflow_step_2 UUID REFERENCES EPersonGroup(uuid);
ALTER TABLE Collection ADD COLUMN workflow_step_3 UUID REFERENCES EPersonGroup(uuid);
ALTER TABLE Collection ADD COLUMN submitter UUID REFERENCES EPersonGroup(uuid);
ALTER TABLE Collection ADD COLUMN admin UUID REFERENCES EPersonGroup(uuid);
UPDATE Collection SET workflow_step_1 = EPersonGroup.uuid FROM EPersonGroup WHERE Collection.workflow_step_1_legacy = EPersonGroup.eperson_group_id;
UPDATE Collection SET workflow_step_2 = EPersonGroup.uuid FROM EPersonGroup WHERE Collection.workflow_step_2_legacy = EPersonGroup.eperson_group_id;
UPDATE Collection SET workflow_step_3 = EPersonGroup.uuid FROM EPersonGroup WHERE Collection.workflow_step_3_legacy = EPersonGroup.eperson_group_id;
UPDATE Collection SET submitter = EPersonGroup.uuid FROM EPersonGroup WHERE Collection.submitter_legacy = EPersonGroup.eperson_group_id;
UPDATE Collection SET admin = EPersonGroup.uuid FROM EPersonGroup WHERE Collection.admin_legacy = EPersonGroup.eperson_group_id;
ALTER TABLE Collection DROP COLUMN workflow_step_1_legacy;
ALTER TABLE Collection DROP COLUMN workflow_step_2_legacy;
ALTER TABLE Collection DROP COLUMN workflow_step_3_legacy;
ALTER TABLE Collection DROP COLUMN submitter_legacy;
ALTER TABLE Collection DROP COLUMN admin_legacy;


-- Migrate resource policy references
ALTER TABLE ResourcePolicy RENAME COLUMN eperson_id to eperson_id_legacy_id;
ALTER TABLE ResourcePolicy ADD COLUMN eperson_id UUID REFERENCES EPerson(uuid);
UPDATE ResourcePolicy SET eperson_id = eperson.uuid FROM eperson WHERE ResourcePolicy.eperson_id_legacy_id = eperson.eperson_id;
ALTER TABLE ResourcePolicy DROP COLUMN eperson_id_legacy_id;
ALTER TABLE ResourcePolicy RENAME COLUMN epersongroup_id to epersongroup_id_legacy_id;
ALTER TABLE ResourcePolicy ADD COLUMN epersongroup_id UUID REFERENCES EPersonGroup(uuid);
UPDATE ResourcePolicy SET epersongroup_id = epersongroup.uuid FROM epersongroup WHERE ResourcePolicy.epersongroup_id_legacy_id = epersongroup.eperson_group_id;
ALTER TABLE ResourcePolicy DROP COLUMN epersongroup_id_legacy_id;

-- Migrate Subscription
ALTER TABLE Subscription RENAME COLUMN eperson_id to eperson_legacy_id;
ALTER TABLE Subscription ADD COLUMN eperson_id UUID REFERENCES EPerson(uuid);
UPDATE Subscription SET eperson_id = eperson.uuid FROM eperson WHERE Subscription.eperson_legacy_id = eperson.eperson_id;
ALTER TABLE Subscription DROP COLUMN eperson_legacy_id;

-- Migrate versionitem
ALTER TABLE versionitem RENAME COLUMN eperson_id to eperson_legacy_id;
ALTER TABLE versionitem ADD COLUMN eperson_id UUID REFERENCES EPerson(uuid);
UPDATE versionitem SET eperson_id = eperson.uuid FROM eperson WHERE versionitem.eperson_legacy_id = eperson.eperson_id;
ALTER TABLE versionitem DROP COLUMN eperson_legacy_id;

-- Migrate handle table
ALTER TABLE handle RENAME COLUMN resource_id to resource_legacy_id;
ALTER TABLE handle ADD COLUMN resource_id UUID REFERENCES dspaceobject(uuid);
UPDATE handle SET resource_id = community.uuid FROM community WHERE handle.resource_legacy_id = community.community_id AND handle.resource_type_id = 4;
UPDATE handle SET resource_id = collection.uuid FROM collection WHERE handle.resource_legacy_id = collection.collection_id AND handle.resource_type_id = 3;
UPDATE handle SET resource_id = item.uuid FROM item WHERE handle.resource_legacy_id = item.item_id AND handle.resource_type_id = 2;
