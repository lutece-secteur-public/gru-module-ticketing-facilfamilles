UPDATE workflow_task_notify_gru_cf SET id_spring_provider = 'workflow-ticketingfacilfamilles.provider-manager.@.ticket' WHERE id_spring_provider = 'workflow-ticketingfacilfamilles.EmailAgentProviderService';

--
-- Data for table core_datastore
--
DELETE FROM core_datastore WHERE entity_key='module.workflow.ticketingfacilfamilles.site_property.fieldagent.entiteattribut';
INSERT INTO core_datastore VALUES ('module.workflow.ticketingfacilfamilles.site_property.fieldagent.entiteattribut', '');