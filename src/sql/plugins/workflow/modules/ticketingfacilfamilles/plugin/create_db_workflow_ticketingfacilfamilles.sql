/*===================================================================================*/
/* Table structure for table workflow_task_ticketing_facilfamilles_emailagent_config */
/*===================================================================================*/
DROP TABLE IF EXISTS workflow_task_ticketing_facilfamilles_emailagent_config;
CREATE TABLE workflow_task_ticketing_facilfamilles_emailagent_config
(
    id_task INT DEFAULT 0 NOT NULL,
    message_direction INT DEFAULT NULL,
    id_following_action INT DEFAULT NULL,
    PRIMARY KEY (id_task)
);

/*===================================================================================*/
/* Table struture for table workflow_task_ticketing_facilfamilles_emailagent_history */
/*===================================================================================*/
DROP TABLE IF EXISTS  workflow_task_ticketing_facilfamilles_emailagent_history;
CREATE TABLE workflow_task_ticketing_facilfamilles_emailagent_history
(
    id_task INT DEFAULT 0 NOT NULL,
    id_history INT DEFAULT 0 NOT NULL,
    id_message_agent INT DEFAULT 0 NOT NULL,
    PRIMARY KEY (id_task, id_history)
);

/*=============================================================*/
/* Table struture for table ticketing_facilfamilles_emailagent */
/*=============================================================*/
DROP TABLE IF EXISTS ticketing_facilfamilles_emailagent;
CREATE TABLE ticketing_facilfamilles_emailagent
(
    id_message_agent INT DEFAULT 0 NOT NULL,
    id_ticket INT DEFAULT 0 NOT NULL,
    email_recipients MEDIUMTEXT DEFAULT NULL,
    email_recipients_cc MEDIUMTEXT DEFAULT NULL,
    message_question LONG VARCHAR DEFAULT NULL,
    message_response LONG VARCHAR DEFAULT NULL,
    is_answered INT DEFAULT 0,
    PRIMARY KEY (id_message_agent)
);

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