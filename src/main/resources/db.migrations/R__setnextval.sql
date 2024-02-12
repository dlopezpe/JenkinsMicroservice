SELECT setval(pg_get_serial_sequence('build_compilation', 'id'), (SELECT max(id)+1 FROM build_compilation));



