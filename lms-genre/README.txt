
> cd /path/to/lmsgenre

Dockerfile
> mvn package
> docker build -t lmsgenre .

DockerfileWithPackaging (inclui mvn package)
> docker build -f DockerfileWithPackaging -t lmsgenre .

Running:
> docker compose -f docker-compose-rabbitmq+postgres.yml up -d
> docker exec -it postgres_in_lms_network psql -U postgres
    psql (16.3 (Debian 16.3-1.pgdg120+1))
    Type "help" for help.

    postgres=# create database genre_1;
    CREATE DATABASE
    postgres=# create database genre_2;
    CREATE DATABASE
    postgres=# \l
                                                      List of databases
       Name    |  Owner   | Encoding | Locale Provider |  Collate   |   Ctype    | ICU Locale | ICU Rules |   Access privileges
    -----------+----------+----------+-----------------+------------+------------+------------+-----------+-----------------------
     genre_1   | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           |
     genre_2   | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           |
     postgres  | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           |
     template0 | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           | =c/postgres          +
               |          |          |                 |            |            |            |           | postgres=CTc/postgres
     template1 | postgres | UTF8     | libc            | en_US.utf8 | en_US.utf8 |            |           | =c/postgres          +
    (5 rows)
    postgres=# exit
> docker compose up