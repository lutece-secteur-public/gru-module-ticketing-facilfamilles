/*==========================================================================*/
/* Table structure for table workflow_task_ticketing_facilfamilles_config    */
/*==========================================================================*/
DROP TABLE IF EXISTS workflow_task_ticketing_facilfamilles_config;
CREATE TABLE workflow_task_ticketing_facilfamilles_config
(
    id_task INT DEFAULT 0 NOT NULL,
    message_direction INT DEFAULT NULL,
    PRIMARY KEY (id_task)
);

/*==========================================================================*/
/* Table struture for table TODO_TABLE_TASK_SPECIFIC */
/*==========================================================================*/
DROP TABLE IF EXISTS  workflow_task_ticketing_facilfamilles_history;
CREATE TABLE workflow_task_ticketing_facilfamilles_history
(
    id_task INT DEFAULT 0 NOT NULL,
    id_history INT DEFAULT 0 NOT NULL,
    email_agent VARCHAR(255) DEFAULT NULL,
    message LONG VARCHAR DEFAULT NULL,
    PRIMARY KEY (id_task, id_history)
);