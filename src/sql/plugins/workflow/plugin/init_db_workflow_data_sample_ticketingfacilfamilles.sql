-- delete first due to foreign keys
DELETE FROM workflow_task WHERE id_task >= 1300 and id_task <= 1305; 
DELETE FROM workflow_action WHERE id_action >= 1200 and id_action <= 1202;
DELETE FROM workflow_state WHERE id_state >= 1100 and id_state <= 1103;
DELETE FROM workflow_workflow WHERE id_workflow = 1000;

-- ajout workflow
INSERT INTO workflow_workflow (id_workflow, name, description, creation_date, is_enabled, workgroup_key) 
	VALUES	(1000,'Workflow ActeurTerrain','Workflow ActeurTerrain','2016-10-01 00:00:00',1,'all');

-- ajout state
INSERT INTO workflow_state (id_state, name, description, id_workflow, is_initial_state, is_required_workgroup_assigned, id_icon, display_order) 
	VALUES	(1100,'Nouveau','Nouveau',1000,1,0,NULL,1),
			(1101,'A traiter','A traiter',1000,0,0,NULL,2),
			(1102,'Envoyé','Envoyé',1000,0,0,NULL,3),
			(1103,'Traité','Traité',1000,0,0,NULL,4);
			
-- ajout action
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action) 
	VALUES 	(1200,'Initialisation','Initialisation',1000,1100,1101,1,1,0,1,0),
			(1201,'Sollicitation Acteur terrain','Sollicitation acteur terrain',1000,1101,1102,1,0,0,2,0),
			(1202,'Réponse Acteur terrain','Réponse acteur terrain',1000,1102,1103,1,0,0,3,0)
;

-- ajout task 
INSERT INTO workflow_task (id_task, task_type_key, id_action, display_order) 
	VALUES  (1300,'taskTicketingFacilFamilles',1201,1), -- Sollicitation Agent Terrain
			(1301,'taskTypeUpload',1201,2),
			(1302,'taskNotifyGru',1201,3),
			(1303,'taskTicketingFacilFamilles',1202,1),  -- Reponse Agent Terrain
			(1304,'taskTypeUpload',1202,2)
;

-- config workflow_task_upload_config
DELETE FROM workflow_task_upload_config WHERE id_task >= 1300 and id_task <= 1305;
INSERT INTO workflow_task_upload_config (id_task, title, max_file, max_size_file, is_mandatory) VALUES
	(1301, 'Pièce(s) jointe(s)', 5, 10, 0),
	(1304, 'Pièce(s) jointe(s)', 5, 10, 0)
;

-- config workflow_task_upload_config
DELETE FROM workflow_task_notify_gru_cf WHERE id_task >= 1300 and id_task <= 1305;
INSERT INTO workflow_task_notify_gru_cf (id_task, id_spring_provider, is_active_onglet_guichet, is_active_onglet_agent, is_active_onglet_email, is_active_onglet_sms, id_mailing_list_broadcast, email_broadcast, sender_name_broadcast, subject_broadcast, message_broadcast, recipients_cc_broadcast, recipients_cci_broadcast, is_active_onglet_broadcast) VALUES
	(1302, 'notifygru-ticketing-facilfamilles.ProviderService', 0, 0, 0, 0, -1, "${agent_email}", "Mairie de Paris", "[GRU] La sollicitation ${reference} requiert une action de votre part", "Bonjour,<br>Vous avez reçu un message du service Facil'Famille<br>${message}<br>Vous pouvez répondre au message via le lien suivant ${ticketing_ticket_link}", '', '', 1)
;

-- table specific
DELETE FROM workflow_task_ticketing_facilfamilles_config where id_task > 0;
INSERT INTO workflow_task_ticketing_facilfamilles_config (id_task, message_direction) 
    VALUES  (1300, 1),
    		(1303, 0)
;

-- conf core
DELETE FROM core_datastore WHERE entity_key LIKE 'ticketing.configuration.%';
INSERT INTO core_datastore (entity_key, entity_value) VALUES
('ticketing.configuration.workflow.id', '1000'),
('ticketing.configuration.states.selected', '1100'),
('ticketing.configuration.states.selected.for.role.gru_level_3', '1100'),
('ticketing.configuration.state.id.closed', '1102'),
('ticketing.configuration.actions.filtered.when.assigned.to.me', ''),
('ticketing.configuration.adminUser.id.front', '5'),
('ticketing.configuration.channel.id.front', '99')
;
