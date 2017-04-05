ALTER TABLE ticketing_facilfamilles_emailagent CHANGE email_agent email_recipients MEDIUMTEXT DEFAULT NULL;
ALTER TABLE ticketing_facilfamilles_emailagent ADD COLUMN email_recipients_cc MEDIUMTEXT DEFAULT NULL;

UPDATE workflow_task_notify_gru_cf SET email_broadcast = '${email_recipients}', recipients_cc_broadcast = '${email_recipients_cc}' WHERE id_spring_provider = 'workflow-ticketingfacilfamilles.provider-manager.@.ticket';
