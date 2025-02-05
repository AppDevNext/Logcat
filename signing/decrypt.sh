#!/usr/bin/env bash

pwd

if [[ -z "$CRYPT_PASS" ]]
then
   read -sp 'Password: ' CRYPT_PASS
   if [[ -z "$CRYPT_PASS" ]]
   then
      echo "\$CRYPT_PASS Still empty"
      exit 1
   fi
else
   echo "\$CRYPT_PASS available"
fi

openssl version

# to encrypt
# openssl aes-256-cbc -salt -pbkdf2 -k "$CRYPT_PASS" -in ./app/google-services.json -out ./app/google-services.json.enc

# Ubuntu 18.04 (openssl 1.1.0g+) needs -md md5
# https://askubuntu.com/questions/1067762/unable-to-decrypt-text-files-with-openssl-on-ubuntu-18-04/1076708
echo google-services.json
echo ====================
openssl aes-256-cbc -d -pbkdf2 -k "$CRYPT_PASS" -in ./app/google-services.json.enc -out ./app/google-services.json