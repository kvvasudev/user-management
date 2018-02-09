ALTER TABLE organisation
ADD CONSTRAINT uniqueOrgName UNIQUE (name);

ALTER TABLE groupdata
ADD CONSTRAINT uniqueGroupName UNIQUE (name);