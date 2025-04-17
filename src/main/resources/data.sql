-- Newborns
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (1, 'RSV', 0, 'Respiratory Syncytial Virus', 'Vaccine for RSV') ON CONFLICT DO NOTHING;
