openssl genrsa -out private.pem 2048

openssl rsa -in private.pem -pubout -outform PEM -out public_key.pem

openssl pkcs8 -topk8 -inform PEM -in private.pem -out private_key.pem -nocrypt