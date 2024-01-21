FROM mysql/mysql-server:5.7.27

RUN touch /var/log/mysqld
RUN chmod 777 /var/log/mysqld
RUN chown mysql:mysql /var/log/mysqld
RUN chmod 777 /var/log
RUN chown mysql:mysql /var/log

COPY ./cloudmc-init.sql /docker-entrypoint-initdb.d/cloudmc-init.sql
COPY ./my.cnf /etc/my.cnf
