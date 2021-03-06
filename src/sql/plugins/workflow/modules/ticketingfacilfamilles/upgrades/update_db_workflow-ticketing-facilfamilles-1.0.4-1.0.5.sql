--
-- Data for table core_datastore
--
DELETE FROM core_datastore WHERE entity_key='module.workflow.ticketingfacilfamilles.site_property.fieldagent.entiteattribut';

--
-- Drop all tables which has been moved to another module (module-workflow-ticketing)
--
DROP TABLE IF EXISTS workflow_task_ticketing_facilfamilles_emailagent_history;
DROP TABLE IF EXISTS ticketing_facilfamilles_emailagent;
DROP TABLE IF EXISTS workflow_task_ticketing_facilfamilles_emailagent_config;
DROP TABLE IF EXISTS workflow_task_ticketing_facilfamilles_emailagent_recipient;
DROP TABLE IF EXISTS workflow_task_ticketing_facilfamilles_emailagent_cc;
