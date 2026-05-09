package com.netflix.contentservice.model;

// tracks video processing lifecycle
// pending -> movie uploaded -> encoding -> ready or failed
public enum VideoStatus {
    PENDING, // MOVIE ADDED BUT NOT UPLOADED YET
    UPLOADED, // raw video reached s3
    ENCODING, // ffmpeg is encoding video
    ENCODED, // encoding completed
    READY, // HLS playlist ready and can be streamed
    FAILED // encoding failed
}
