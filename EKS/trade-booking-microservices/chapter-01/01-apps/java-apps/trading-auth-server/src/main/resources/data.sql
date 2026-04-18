
INSERT INTO oauth2_registered_client (
id,
client_id,
client_id_issued_at,
client_name,
client_authentication_methods,
authorization_grant_types,
redirect_uris,
scopes,
client_settings,
token_settings
)
VALUES (
'angular-client',
'angular-client',
CURRENT_TIMESTAMP,
'Angular SPA',
'none',
'authorization_code,refresh_token',
'https://localhost:4200/login/callback',
'openid,profile',
'{"requireProofKey":true,"requireAuthorizationConsent":false}',
'{"accessTokenTimeToLive":"PT10M"}'
);




INSERT INTO jwt_key_config (ID, ACTIVE_KID) VALUES (1, 'auth-server-01');

INSERT INTO users VALUES
('user-12874','sandeep','$2a$10$kRRsT2h7RETwa52w.aHNge7HLxCnr/LotMlpav6r4wm4DfsLCK1Ju','sandeep.yadav@nse.com','nse-prod',true);

INSERT INTO roles VALUES
(1,'TRADER'),
(2,'ADMIN'),
(3,'VIEWER');

INSERT INTO user_roles VALUES
('user-12874',2);


INSERT INTO scopes (id, name) VALUES
(1, 'trade.read'),
(2, 'trade.write'),
(3, 'trade.cancel');


INSERT INTO audiences(id, name) VALUES
(1, 'trading-api-gateway'),
(2, 'trading-auth-server'),
(3, 'exchange'),
(4, 'refdata-provider'),
(5, 'trade-processor'),
(6, 'trade-enricher');


INSERT INTO role_scopes VALUES
(1,1),
(1,2),
(2,1),
(2,2),
(2,3),
(3,1);



INSERT INTO user_audience VALUES
('user-12874',1),
('user-12874',2),
('user-12874',3),
('user-12874',4),
('user-12874',5),
('user-12874',6);
