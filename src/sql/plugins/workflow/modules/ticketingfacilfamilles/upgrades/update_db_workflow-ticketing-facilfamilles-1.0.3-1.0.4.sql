/*=============================================================*/
/* Table struture for table workflow_task_ticketing_facilfamilles_emailagent_recipient */
/*=============================================================*/
DROP TABLE IF EXISTS workflow_task_ticketing_facilfamilles_emailagent_recipient;
CREATE TABLE workflow_task_ticketing_facilfamilles_emailagent_recipient
(
	id_recipient INT DEFAULT 0 NOT NULL,
	id_task INT DEFAULT 0 NOT NULL,
	id_history INT DEFAULT 0 NOT NULL,
	email VARCHAR(255) NOT NULL,
	field VARCHAR(255) DEFAULT NULL,
	name VARCHAR(255) DEFAULT NULL,
	firstname VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (id_recipient)
);

/*=============================================================*/
/* Table struture for table workflow_task_ticketing_facilfamilles_emailagent_cc */
/*=============================================================*/
DROP TABLE IF EXISTS workflow_task_ticketing_facilfamilles_emailagent_cc;
CREATE TABLE workflow_task_ticketing_facilfamilles_emailagent_cc
(
	id_cc INT DEFAULT 0 NOT NULL,
	id_task INT DEFAULT 0 NOT NULL,
	id_history INT DEFAULT 0 NOT NULL,
	email VARCHAR(255) NOT NULL,
	PRIMARY KEY (id_cc)
);

ALTER TABLE ticketing_facilfamilles_emailagent CHANGE email_agent email_recipients MEDIUMTEXT DEFAULT NULL;
ALTER TABLE ticketing_facilfamilles_emailagent ADD COLUMN email_recipients_cc MEDIUMTEXT DEFAULT NULL;

UPDATE workflow_task_notify_gru_cf SET email_broadcast = '${email_recipients}', recipients_cc_broadcast = '${email_recipients_cc}' WHERE id_spring_provider = 'workflow-ticketingfacilfamilles.provider-manager.@.ticket';
