-- Newborns
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (1, 'RSV', 0, 'Respiratory Syncytial Virus', 'Vaccine for RSV') ON CONFLICT DO NOTHING;

-- 2 months
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (2, 'D, T, aP, Hib, IPV, Hep B', 2,
        'Diphtheria, Tetanus, Pertussis, Haemophilus influenzae type b, Inactivated Polio Virus, Hepatitis B',
        'Combination vaccine for DTPaHibIPVHepB') ON CONFLICT DO NOTHING;
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (3, 'Rotavirus (1st dose) ', 2, 'Rotavirus', 'Vaccine for Rotavirus') ON CONFLICT DO NOTHING;
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (4, 'Pneumocoques (1st dose) ', 2, 'Pneumococcal', 'Vaccine for Pneumococcus') ON CONFLICT DO NOTHING;

-- 3 months
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (5, 'Rotavirus (2nd dose)', 3, 'Rotavirus', 'Vaccine for Rotavirus') ON CONFLICT DO NOTHING;
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (6, 'Meningococcal B (1st dose)', 3, 'Meningococcal B', 'Vaccine for Meningococcus B') ON CONFLICT DO NOTHING;

-- 4 months
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (7, 'D, T, aP, Hib, IPV, Hep B (2nd dose)', 4,
        'Diphtheria, Tetanus, Pertussis, Haemophilus influenzae type b, Inactivated Polio Virus, Hepatitis B',
        'Combination vaccine for DTPaHibIPVHepB') ON CONFLICT DO NOTHING;
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (8, 'Pneumocoques (2nd dose) ', 4, 'Pneumococcal', 'Vaccine for Pneumococcus') ON CONFLICT DO NOTHING;
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (9, 'Rotavirus (3rd dose)', 4, 'Rotavirus', 'Vaccine for Rotavirus') ON CONFLICT DO NOTHING;

-- 5 months
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (10, 'Meningococcal B (2nd dose)', 5, 'Meningococcal B', 'Vaccine for Meningococcus B') ON CONFLICT DO NOTHING;

-- 6 months
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (11, 'RSV', 6, 'Respiratory Syncytial Virus', 'Vaccine for RSV') ON CONFLICT DO NOTHING;

-- 11 months
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (12, 'D, T, aP, Hib, IPV, Hep B (3rd dose)', 11,
        'Diphtheria, Tetanus, Pertussis, Haemophilus influenzae type b, Inactivated Polio Virus, Hepatitis B',
        'Combination vaccine for DTPaHibIPVHepB') ON CONFLICT DO NOTHING;
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (13, 'Pneumocoques (3rd dose) ', 11, 'Pneumococcal', 'Vaccine for Pneumococcus') ON CONFLICT DO NOTHING;

-- 12 months
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (14, 'RORV (1st dose)', 12, 'Measles, Mumps, Rubella, Varicella',
        'Vaccine for Measles, Mumps, Rubella, Varicella') ON CONFLICT DO NOTHING;

-- 13 months
INSERT INTO vaccines (ID, NAME, MATURITY_MONTH, DESCRIPTION, GOALS)
VALUES (15, 'Meningococcal ACWY (1st dose) ', 13, 'Meningococcal ACWY',
        'Vaccine for Meningococcus ACWY') ON CONFLICT DO NOTHING;
