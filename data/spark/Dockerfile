FROM spark:4.0.1-scala2.13-java21-python3-ubuntu
ENV SPARK_HOME="/opt/spark"

COPY conf/spark-defaults.conf "$SPARK_HOME/conf"

USER root
RUN chmod u+x /opt/spark/sbin/* && \
    chmod u+x /opt/spark/bin/*

ENV PYTHONPATH=$SPARK_HOME/python/:$PYTHONPATH

COPY entrypoint.sh .
RUN chmod u+x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
CMD [ "bash" ]