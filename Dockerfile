FROM ubuntu:latest
LABEL authors="marca"

ENTRYPOINT ["top", "-b"]