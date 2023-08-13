FROM golang:1.20-alpine

MAINTAINER ngtam

USER root

WORKDIR /go/lms

COPY . .

RUN go mod tidy

RUN go build -o .

RUN chmod +x .

EXPOSE 5001

ENTRYPOINT ["./lms-class"]