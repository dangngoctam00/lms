#!/bin/bash
# Change the path to your system folder path (Dealing with diffs on our project locations)
TAM_LMS_BACKEND_DIR="/home/thinh/sjaklfIdeaProjects/lms-backend/"
TAM_LMS_FRONTEND_DIR="/home/thinh/WebsjasdklftormProjects/lms/frontend"

TRONG_LMS_BACKEND_DIR="/home/ngoctrong102/Documents/bk/lvtn/lms-backend"
TRONG_LMS_FRONTEND_DIR="/home/ngoctrong102/Documents/bk/lvtn/lms/frontend"

THINH_LMS_BACKEND_DIR="/home/thinh/IdeaProjects/lms-backend/"
THINH_LMS_FRONTEND_DIR="/home/thinh/WebstormProjects/lms/frontend"

# LMS_BACKEND_DIR="" LMS_FRONTEND_DIR=""
if [ -d "$TRONG_LMS_FRONTEND_DIR" ] && [ -d "$TRONG_LMS_FRONTEND_DIR" ] ; then
    LMS_BACKEND_DIR="$TRONG_LMS_BACKEND_DIR"
    LMS_FRONTEND_DIR="$TRONG_LMS_FRONTEND_DIR"
fi

if [ -d "$TAM_LMS_FRONTEND_DIR" ] && [ -d "$TAM_LMS_FRONTEND_DIR" ];    then
    LMS_BACKEND_DIR="$TAM_LMS_BACKEND_DIR"
    LMS_FRONTEND_DIR="$TAM_LMS_FRONTEND_DIR"
fi

if [ -d "$THINH_LMS_FRONTEND_DIR" ]  &&  [ -d "$THINH_LMS_FRONTEND_DIR" ]; then
    LMS_BACKEND_DIR="$THINH_LMS_BACKEND_DIR"
    LMS_FRONTEND_DIR="$THINH_LMS_FRONTEND_DIR"
fi


echo $LMS_BACKEND_DIR
echo $LMS_FRONTEND_DIR

node parse.js $LMS_BACKEND_DIR $LMS_FRONTEND_DIR
